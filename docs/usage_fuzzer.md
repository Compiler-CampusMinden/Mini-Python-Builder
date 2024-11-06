# Fuzzers
## Verwendung
Der Fuzzer kann mit den folgenden Befehlen gesteuert werden:
Dieser Befehl löscht den Ordner aus ggf. vorherigen Durchläufen und erstellt einen Neuen.
```bash
rm -rf build/compilerOutput/Fuzzing && mkdir build/compilerOutput/Fuzzing
```
Dieser Befehl kompiliert den Fuzzer inkl. Generator neu, was nach jeder Änderung getan werden muss.
```bash
mvn test-compile
```
Der Befehl startet den Fuzzer. Die Kommandozeilenparameter können angepasst werden. compile ist Optional und entscheidet ob die generierten Projekte kompiliert werden.
```bash
mvn jqf:fuzz -Dclass=CBuilder.CompilerFuzzTest -Dmethod=testWithGenerator -Dcompile -Dtime=5m -e
```
Der Befehlt reproduziert einen Fehler. Im input-target-Parameter muss die ID angepasst werden.
```bash
mvn jqf:repro -Dclass=CBuilder.CompilerFuzzTest -Dmethod=testwithGenerator -Dinput-target=target/fuzz-results/CBuilder/CompilerFuzzTest/testwithGenerator/failures/id_X
```

## Erweiterung

Falls der Fuzzer erweitert werden soll, kann das je nach Erweiterung an drei Stellen passieren.

Soll ein neuer Generator verwendet werden, muss dieser in CompilerFuzzTest.java angegeben werden, damit er von JQF erkannt wird.

Sollen dem bestehenden Generator neue Funktionen die nicht Rekursion verwenden hinzugefügt werden, muss das im CBuildGenerator.java passieren. In der dortigen generateStatement-Funktion existiert ein if-Statement. Im if-Statement werden die Funktion, in der selben Art wie die bisherigen hinzugefügt.

Sollen dem bestehenden Generator neue Funktionen mit Rekursion hinzugefügt werden. Dabei müssen diese im else-Statement, der generateStatement-Funktion, hinzugefügt werden. Dies sollte in der selben Art stattfinden wie bei den Funktionen ohne Rekursion. Bei der Implementierung der Rekursions-Funktionen muss berücksichtigt werden, dass der expressiondepth-Parameter inkrementiert wird.
