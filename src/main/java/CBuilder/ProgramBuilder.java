package CBuilder;

import CBuilder.objects.MPyClass;
import CBuilder.objects.functions.Function;
import CBuilder.objects.functions.ReturnStatement;
import CBuilder.variables.VariableDeclaration;
import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Entrypoint to creating C code from a MiniPython AST.
 */
public class ProgramBuilder {

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
            "type-hierarchy/object",
            // custom objects
            "type-hierarchy/type",
    };

    private final List<Statement> statements;
    private final List<VariableDeclaration> globalVariables;

    private final List<Function> globalFunctions;

    private final List<MPyClass> classes;

    /**
     * Allows to manually specify options regarding C code generation.
     */
    public ProgramBuilder() {
        statements = new LinkedList<>();
        globalVariables = new java.util.LinkedList<>();
        globalFunctions = new LinkedList<>();
        classes = new LinkedList<>();
    }

    /**
     * Add a new statement in the global scope.
     *
     * The resulting statement list more or less represents the program's main method.
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

    public void addVariable(VariableDeclaration variable) {
        globalVariables.add(variable);
    }

    public void addFunction(Function function) {
        globalFunctions.add(function);
    }

    public void addClass(MPyClass mPyClass) {
        classes.add(mPyClass);
    }

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
            program.append(f.buildCFunction());
            program.append(f.buildFuncObjectDeclaration());
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

    public void writeProgram(Path directory) {
        try {
            if (getClass().getResource("/c-runtime").toURI().getScheme().equals("file")) {
                Path p = Path.of(ProgramBuilder.class.getResource("/c-runtime").toURI());
                FileUtils.copyDirectory(p.toFile(), directory.toFile());
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
                    Files.copy(file, targetLocation.resolve(jarArchive.relativize(file).toString()), StandardCopyOption.REPLACE_EXISTING);
                    return FileVisitResult.CONTINUE;
                }

            });
        }
    }

}
