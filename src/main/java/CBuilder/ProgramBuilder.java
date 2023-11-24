package CBuilder;

import CBuilder.objects.MPyClass;
import CBuilder.objects.functions.Function;
import CBuilder.objects.functions.ReturnStatement;
import CBuilder.variables.VariableDeclaration;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/**
 * Entrypoint to creating C code from a MiniPython AST.
 */
public class ProgramBuilder {

    /**
     * Definition of the c-runtime header files.
     */
    private static final String[] headers = {
            // allow usage of assertions
            "assert",
            // aliases mapping python names to the internal c names
            "mpy_aliases",
            // ref-counting
            "mpy_obj",
            // initialisation
            "builtins-setup",
            // building function args
            "function-args",
            "literals/tuple",
            "literals/int",
            "literals/boolean",
            "literals/str",
            "type-hierarchy/object",
            // custom objects
            "type-hierarchy/type",
    };

    /**
     * A list of all statements in the global scope.
     */
    private final List<Statement> statements;

    /**
     * A list of all defined variables in the global scope.
     */
    private final List<VariableDeclaration> globalVariables;

    /**
     * A list of all defined functions in the global scope.
     */
    private final List<Function> globalFunctions;

    /**
     * A list of all defined classes in th global scope.
     */
    private final List<MPyClass> classes;

    /**
     * Create a new program with empty global scope.
     * Allows to manually specify options regarding c-code generation.
     */
    public ProgramBuilder() {
        statements = new LinkedList<>();
        globalVariables = new java.util.LinkedList<>();
        globalFunctions = new LinkedList<>();
        classes = new LinkedList<>();
    }

    /**
     * Add a new statement to the global scope.
     * The resulting statement list more or less represents the program's main method.
     *
     * @param statement The statement to add.
     */
    public void addStatement(Statement statement) {
        if (statement instanceof ReturnStatement) {
            // I'd prefer to have this type safe (i. e. different statement interfaces for global/non-global scope,
            // but this is just a prototype after all and shouldn't happen too often anyway)
            throw new IllegalArgumentException("Cannot return from global scope");
        }
        statements.add(statement);
    }

    /**
     * Add a new variable to the global scope.
     *
     * @param variable The variable to add.
     */
    public void addVariable(VariableDeclaration variable) {
        globalVariables.add(variable);
    }

    /**
     * Add a new function definition to the global scope.
     *
     * @param function The function to add.
     */
    public void addFunction(Function function) {
        globalFunctions.add(function);
    }

    /**
     * Add a new class definition to the global scope.
     *
     * @param mPyClass The class to add.
     */
    public void addClass(MPyClass mPyClass) {
        classes.add(mPyClass);
    }

    /**
     * Generate a c-program of the containing elements.
     *
     * @return A string which containing the generated c-program.
     */
    public String buildProgram() {
        StringBuilder program = new StringBuilder();

        program.append("#include <stddef.h>\n");
        program.append('\n');

        for (String headerName : headers) {
            program.append("#include \"" + headerName + ".h\"\n");
        }
        program.append('\n');

        for (VariableDeclaration v : globalVariables) {
            program.append(v.build(false));
        }
        program.append('\n');

        for (Function f : globalFunctions) {
            program.append(f.buildFuncObjectDeclaration());
            program.append(f.buildCFunction());
        }
        program.append('\n');

        for (MPyClass c : classes) {
            program.append(c.buildDeclaration());
        }
        program.append('\n');

        program.append("int main() {\n");

        StringBuilder body = new StringBuilder();

        // setup builtins written in C
        body.append("__mpy_builtins_setup();\n");

        // first init global variables
        // cannot be done above due to c's notion of 'const' variables/initializers
        for (VariableDeclaration v : globalVariables) {
            // but does not init the global var
            body.append(v.buildInitialisation());
        }
        body.append('\n');

        // then init the global functions
        for (Function f : globalFunctions) {
            body.append(f.buildInitialisation());
        }
        body.append('\n');

        for (MPyClass c : classes) {
            body.append(c.buildInitialisation());
        }
        body.append('\n');

        // then the statements
        for (Statement s : statements) {
            body.append(s.buildStatement());
        }
        body.append('\n');

        // finally decrement refcount of all global variables
        for (VariableDeclaration v : globalVariables) {
            body.append(v.buildRefDec());
        }
        body.append('\n');

        for (Function f : globalFunctions) {
            body.append(f.buildRefDec());
        }
        body.append('\n');

        for (MPyClass c : classes) {
            body.append(c.buildRefDec());
        }
        body.append('\n');

        // and cleanup behind builtin setup
        body.append("__mpy_builtins_cleanup();\n");

        body.append("return 0;\n");

        program.append(body.toString().lines().map(string -> "\t" + string + "\n").collect(
                Collectors.joining()));
        program.append("}\n");

        return program.toString();
    }

    /**
     * Write the c-program into the output file.
     * Additionally copies the c-runtime into output path.
     *
     * @param directory The directory which containing the c-runtime.
     */
    public void writeProgram(Path directory) {
        try {
            // make sure to create the target folder
            if (!directory.toFile().exists()) directory.toFile().mkdirs();
            // copy all files to target folder
            if (getClass().getResource("/c-runtime").toURI().getScheme().equals("file")) {
                Path p = Path.of(ProgramBuilder.class.getResource("/c-runtime").toURI());
                copyFolder(p, directory);
            } else {
                copyFolderFromJar("/c-runtime", directory);
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException("Failed to get resource URI for template folder", e);
        } catch (IOException e) {
            throw new RuntimeException("Failed to copy template folder to output directory", e);
        }

        try {
            Path output = Path.of(directory.toString(), "src/program.c");
            java.nio.file.Files.writeString(output, this.buildProgram(), java.nio.file.StandardOpenOption.CREATE, java.nio.file.StandardOpenOption.WRITE, java.nio.file.StandardOpenOption.TRUNCATE_EXISTING);
        } catch (java.io.IOException e) {
            throw new RuntimeException("Failed to write 'program.c'!", e);
        }
    }

    /**
     * Copy the c-runtime folder into the target location.
     *
     * @param srcDir The Path to the jar archive.
     * @param destDir The output directory.
     * @throws IOException Will be thrown if a needed file or folder is not accessible.
     */
    public void copyFolder(Path srcDir, Path destDir) throws IOException {
        try (Stream<Path> stream = Files.walk(srcDir)) {
            stream.forEach(source -> {
                try {
                    Files.copy(source, destDir.resolve(srcDir.relativize(source)), REPLACE_EXISTING);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to copy template folder to output directory", e);
                }
            });
        }
    }

    /**
     * Copy the c-runtime folder from jar into the target location.
     *
     * @param resourcePath The Path to the jar archive.
     * @param targetLocation The output directory.
     * @throws URISyntaxException Will be thrown if the c-runtime can not be found in the application resources.
     * @throws IOException Will be thrown if a needed file or folder is not accessible.
     */
    public void copyFolderFromJar(String resourcePath, final Path targetLocation) throws URISyntaxException, IOException {
        URI resource = getClass().getResource("/c-runtime").toURI();

        try (FileSystem fileSystem = FileSystems.newFileSystem(resource, Map.of())) {
            final Path jarArchive = fileSystem.getPath(resourcePath);
            Files.walkFileTree(jarArchive, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    Files.createDirectories(targetLocation.resolve(jarArchive.relativize(dir).toString()));
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.copy(file, targetLocation.resolve(jarArchive.relativize(file).toString()), REPLACE_EXISTING);
                    return FileVisitResult.CONTINUE;
                }

            });
        }
    }

}
