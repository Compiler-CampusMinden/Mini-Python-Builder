# Verwendung des generierten C-Codes im Mini-Python-CBuilder

Der CBuilder bringt eine [C-Runtime](../c-runtime/) mit, um das durch den CBuilder erzeugte
C-Programm übersetzen zu können.

Sie müssen beim Generieren des C-Codes selbst dafür Sorge tragen, dass dieser Code im
Verzeichnis der C-Runtime abgelegt wird. Dies erreichen Sie beim Aufruf von
`ProgramBuilder#writeProgram(outputFolder)`, wo Sie über den Parameter `outputFolder` den
Speicherort der durch den CBuilder erzeugten C-Datei `program.c` angeben können.

Im C-Runtime-Ordner steht ein Makefile bereit, mit dem Sie das generierte Programm in ein
ausführbares Programm übersetzen können. Dafür müssen auf Ihrem System sowohl `Make` als ein
passender C-Compiler verfügbar sein.

## Notwendige Software

Sie benötigen **[make](https://www.gnu.org/software/make/)** und einen **C compiler** (z. B.
[gcc](https://gcc.gnu.org/) oder [clang](https://clang.llvm.org/)).

Das Projekt wurde mit den folgenden Versionen der genannten Tools getestet (Sie brauchen
natürlich nur _einen_ der genannten C-Compiler):

| Name                                       | Version |
|:-------------------------------------------|:-------:|
| [make](https://www.gnu.org/software/make/) |  3.81   |
| [gcc](https://gcc.gnu.org/)                |    8    |
| [clang](https://clang.llvm.org/)           |   10    |

Verstehen Sie die angegebenen Versionen bitte als untere Schranke, d.h. neuere Versionen sollten
auch problemlos funktionieren.

### Linux

Die notwendige Software sollte über Ihre Paketverwaltung erhältlich sein.

### Windows

Da der generierte C-Code den in der C-Runtime Funktionen aus dem
[`POSIX.1-2008`-Standard](https://en.wikipedia.org/wiki/POSIX#POSIX.1-2008_(with_two_TCs))
benötigt, kann dieser nicht direkt unter Windows verwendet werden.

Sie können sich hier beispielsweise mit [MSYS2](https://www.msys2.org/) behelfen.
Installieren Sie die Anwendung, wie auf der Webseite beschrieben. Öffnen Sie die
installierte MSYS2-Shell und installieren Sie `make` und den `gcc`:

``` shell
pacman -S make
pacman -S gcc
```

Jetzt sollte Ihnen die notwendige Software in der MSYS2-Shell zur Verfügung stehen.

Alternativ können Sie aber auch andere Tools einsetzen, beispielsweise:

- [Linux Subsystem](https://learn.microsoft.com/en-us/windows/wsl/about)
- [Docker](https://www.docker.com/)
- Virtuelle Maschine

### MacOS

Sie können Make und clang (und einige weitere Kommandozeilen-Tools) mittels
`xcode-select --install` installieren. Alternativen können Sie auch
[Homebrew](https://brew.sh/) oder andere Alternativen nutzen.

## Makefile

Das mitgelieferte Makefile bietet Ihnen folgende Targets an, um den generierten C-Code
übersetzen zu können:

|  Name   | Beschreibung                                                             |
|:-------:|:-------------------------------------------------------------------------|
|  `all`  | Compiliert den Code und erzeugt unter `./bin/` ein ausführbares Program. |
|  `run`  | Startet die compilierte Anwendung.                                       |
| `clean` | Entfernt alle erzeugten Artefakte.                                       |
