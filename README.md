# Mini-Python Builder (Backend)

## Overview

This repository provides

1.  the language definition for **[Mini Python]**, and
2.  a **Java-based [builder]**, and
3.  a compatible **[C runtime environment]**.

The builder emits C code via API calls (analogous to LLVM): To generate C code for a Mini
Python program, you call the builder functions while traversing the AST. This generated
code can be compiled into an executable application using a standard C compiler and the
C runtime environment provided here.

For details please refer to the [documentation].

[Mini Python]: docs/syntax_definition.md
[builder]: src/main/java/CBuilder/
[C runtime environment]: c-runtime/
[documentation]: docs/readme.md


## Required software

To work with the builder, you need **Java**. The actual release does not really matter
(Java 9 and greater should work), however [JDK 17 LTS] is recommended in order to be
supported by Gradle.

To compile the generated C code, you need **[make]** and a **C compiler** (e.g. [gcc] or
[clang]).

You also need a POSIX-compatible operating system. For details see
_[Verwendung des generierten C-Codes im Mini-Python-CBuilder]_.

[JDK 17 LTS]: https://openjdk.org/projects/jdk/17/
[make]: https://www.gnu.org/software/make/
[gcc]: https://gcc.gnu.org/
[clang]: https://clang.llvm.org/
[Verwendung des generierten C-Codes im Mini-Python-CBuilder]: docs/usage_generated_code.md


## Contributing

Questions, bug reports and feature requests are very welcome.
Please be sure to read the [contributor guidelines](CONTRIBUTING.md) before
opening a new issue.


---

## License

This [work](https://github.com/Compiler-CampusMinden/Mini-Python-Builder) by
[Florian Warzecha](https://github.com/liketechnik),
[Sebastian Steinmeyer](https://github.com/CrappyAlgorithm),
[Michael Peters](https://github.com/mpeters4),
[BC George](https://github.com/bcg7),
[Carsten Gips](https://github.com/cagix), and
[contributors](https://github.com/Compiler-CampusMinden/Mini-Python-Builder/graphs/contributors)
is licensed under [MIT](LICENSE.md).
