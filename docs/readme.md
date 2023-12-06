# Überblick

Dieses Repository stellt

1.  die Sprachdefinition für **[Mini-Python](syntax_definition.md)**, und
2.  einen **Java-basierten [CBuilder](../src/main/java/CBuilder/)**, und
3.  eine kompatible **[C-Laufzeitumgebung](../c-runtime/)** bereit.

Der Builder erzeugt C-Code über API-Aufrufe (analog zu LLVM): Zur Generierung von C-Code für ein Mini
Python-Programm rufen Sie während der AST-Traversierung die jeweiligen Builder-Funktionen auf. Dieser
generierte Code kann dann mit einem Standard-C-Compiler und der mitgelieferten C-Laufzeitumgebung zu
einer ausführbaren Anwendung kompiliert werden.

Sie finden hier die Dokumentation zum [syntaktischen](syntax_definition.md) und
[semantischen](semantic_definition.md) Sprachumfang von Mini-Python sowie die Dokumentation zum
[CBuilder](usage_cbuilder.md) und der [C-Laufzeitumgebung](usage_generated_code.md).

Sie brauchen ein **Java JDK** ab Version 9 (empfohlen: **JDK 21 LTS**) und **make** sowie den **gcc**-
oder **clang**-Compiler.
