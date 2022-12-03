# Verwendung des CBuilders für Mini-Python

Der CBuilder stellt das Backend für unseren Mini-Python-Compiler bereit. Damit müssen Sie
die Code-Generierung nicht selbst schreiben, sondern können über die Java-API des CBuilders
aus Ihrem AST den passenden C-Code erzeugen lassen, den Sie mit einem C-Compiler und der
zusätzlich im CBuilder enthaltenen [C-Runtime](../c-runtime/) in ein ausführbares Programm
übersetzen können.

## Setup

Der CBuilder ist ein Java-basiertes Projekt. Sie können den CBuilder entsprechend mit dem
üblichen Vorgehen in Ihr eigenes Java-Projekt einbinden. Ein weiteres Setup ist für den
CBuilder selbst nicht erforderlich. Für das Übersetzen des mit dem CBuilder erzeugten
C-Codes benötigen Sie noch einen C-Compiler wie in [Verwendung des generierten
C-Codes](usage_generated_code.md) beschrieben.

Es existiert eine einfache [Gradle-Konfiguration](../build.gradle), mit der Sie das Projekt
in der Konsole oder auch in der IDE bauen können. Vermutlich wollen Sie dort noch Ihre
Main-Klasse festlegen, indem sie das folgende Snippet in die `build.gradle` einfügen und
anpassen:

``` gradle
application {
    mainClass = 'wuppie.fluppie.foo.Main'
}
```

(Dabei ist `wuppie.fluppie.foo.Main` mit Ihrer Main-Klasse zu ersetzen.)

## Basis-Funktionalität

### ProgramBuilder

Die Klasse `ProgramBuilder` stellt den CBuilder bereit.

Erzeugen Sie ein Objekt vom Typ `ProgramBuilder`, rufen nach Bedarf die verschiedenen
Methoden zum Erzeugen der Programmstrukturen auf, und am Ende lassen Sie den passenden
C-Code mit der Methode `ProgramBuilder#writeProgram()` erzeugen. Dabei wird eine Datei
`program.c` erzeugt, die inklusive der C-Runtime an den als Parameter übergebenen Pfad
geschrieben wird.

``` java
ProgramBuilder builder = new ProgramBuilder();  // Einen neuen ProgramBuilder erzeugen

builder.addVariable(...);       // Deklaration einer Variable
builder.addFunction(...);       // Deklaration einer Funktion
builder.addClass(...);          // Deklaration einer Klasse
builder.addStatement(...);      // Alle weiteren Programmteile wie z.B. Funktionsaufrufe

builder.writeProgram(outputFolder); // Erzeugt den C-Code (inkl. Runtime) im übergebenen Verzeichnis
```

*Hinweis*: Der `ProgramBuilder` sucht die C-Runtime im Ordner `c-runtime/` parallel zum
`src/`-Ordner. Wenn der `ProgramBuilder` die C-Runtime nicht findet, wird er auch das
generierte C-Programm `program.c` nicht schreiben.

### Builtin-Datentypen

Die in [Datentypen](syntax_definition.md#datentypen) beschriebenen eingebauten
Basis-Datentypen können über die zugehörigen Klassen in der CBuilder-API erzeugt werden.

``` java
new StringLiteral("foo");   // String mit Inhalt "foo" erzeugen
new IntLiteral(5);          // Integer mit Wert "5" erzeugen
new BoolLiteral(true);      // Boolean mit Wert "True" erzeugen
```

### Referenzen

Für den Zugriff auf Variablen und Funktionen und Klassen erzeugen Sie ein Objekt vom Typ
`Reference`. Diese hat einen Namen, mit dem diese Referenz später zur Laufzeit aufgelöst
wird und so den Zugriff auf das gleichnamige Variablen-/Funktions-/Klassen-Objekt im
aktuellen Scope ermöglicht.

``` java
Reference varA = new Reference("a");    // Referenz auf Variable, Funktion oder Klasse mit dem Namen "a"
```

### Variablen

``` python
a = 10
```

Variablen müssen Sie zunächst über das Objekt `VariableDeclaration` deklarieren. Globale
Variablen machen Sie anschließend im CBuilder über die Methode
`ProgramBuilder#addVariable()` bekannt.

Anschließend können Sie auf die Variablen über eine gleichnamige Referenz zugreifen.

``` java
VariableDeclaration varADecl = new VariableDeclaration("a");
builder.addVariable(varADecl);                                  // nur für globale Variablen
```

Die Zuweisung erfolgt über ein Objekt der Klasse `Assignment`, welches Sie über die Methode
`ProgramBuilder#addStatement()` dem CBuilder übergeben.

``` java
Reference varA = new Reference("a");
Assignment assignIntToA = new Assignment(varA, new IntLiteral(10));
builder.addStatement(assignIntToA);
```

## Funktionen

### Funktionen aufrufen

``` python
print(a)
```

Für Funktionsaufrufe erzeugen Sie sich ein `Reference` mit dem Funktionsnamen. Für eine
eventuell vorhandene Parameterliste erzeugen Sie sich eine Liste von `Expression`-Objekten,
die Sie beispielsweise über Referenzen oder Builtin-Datentypen befüllen. Mit diesen beiden
“Zutaten” können Sie nun ein `Call`-Objekt für den Funktionsaufruf anlegen und dieses per
`ProgramBuilder#addStatement()` an den CBuilder übergeben.

[Builtin-Funktionen](semantic_definition.md#globale-builtin-funktionen) können ohne
vorherige Definition direkt per Referenz verwendet werden.

``` java
Reference printRef = new Reference("print");                            // Referenz zur Builtin-Funktion print
List<Expression> parameterRefList = List.of(new Expression[] {varA});   // Parameterliste mit Referenz auf Variable "a"
Call printCall = new Call(printRef, parameterRefList);                  // Call mit Referenz auf die Funktion und die Parameterliste
builder.addStatement(printCall);                                        // Funktionsaufruf als Statement im CBuilder
```

### Funktionen definieren

``` python
def func1(x):
    y = x
    print(y)
    return y
#end

print(func1(a))
```

Eine Funktion wird über ein Objekt der Klasse `Function` erzeugt. Der Konstruktor hat einen
Parameter `funcName`, der der Name der Funktion wie im Python-Code ist und der zum Auflösen
über Referenzen innerhalb des Scopes dient.

Der Body einer Funktion wird als Liste von Objekten vom Typ `Statement` repräsentiert und im
Konstruktor über den Parameter `body` übergeben.

Die Parameter der Funktion werden als Liste von Objekten vom Typ `Argument` repräsentiert
und im Konstruktor über den Parameter `positionalArgs` übergeben. Dabei hat jedes `Argument`
neben einer Referenz noch einen Integerwert, der die Position des Parameters in der
Parameterliste repräsentiert.

Sämtliche lokalen Variablen einer Funktion müssen als `VariableDeclaration` angelegt und
über den Parameter `localVariables` an den Konstruktor von `Function` übergeben werden. Eine
spätere Deklaration von lokalen Variablen ist nicht möglich!

Für die Rückgabewerte im `return`-Statement in der Funktion können Sie die Klasse
`ReturnStatement` nutzen.

``` java
// Statements im Funktionskörper
VariableDeclaration localVarYDecl = new VariableDeclaration("y");                   // Lokale Variable "y" (darf NICHT dem CBuilder DIREKT übergeben werden)
Assignment assignYWithX = new Assignment(new Reference("y"), new Reference("x"));   // Zuweisung "y = x"
Call printY = new Call(printRef, List.of(new Expression[] {new Reference("y")}));   // Aufruf von "print(y)"
Statement returnY = new ReturnStatement(new Reference("y"));                        // Rückgabe aus der Funktion

// Argumente für Konstruktor von Function
List<Statement> body = List.of(new Expression[] {assignYWithX, printY, returnY});               // Anweisungsliste für den Body der Funktion
List<Argument> parameterArguments = List.of(new Argument[] {new Argument("x", 0)});             // Parameterliste der Funktion
List<VariableDeclaration> localVariables = List.of(new VariableDeclaration[] {localVarYDecl});  // Liste der lokalen Variablen

// Function erstellen
Function func1 = new Function("func1", body, parameterArguments, localVariables);       // Funktion erzeugen
builder.addFunction(func1);                                                             // Function dem CBuilder übergeben
```

``` java
// Aufruf der vorher definierten Funktion
Call callFunc1 = new Call(new Reference("func1"), List.of(new Expression[] {varA}));    // Aufruf von "func1(a)"
Call callPrint = new Call(printRef, List.of(new Expression[] {callFunc1}));             // Aufruf von "print(func1(a))"
builder.addStatement(callPrint);                                                        // Aufruf dem CBuilder übergeben
```

## Operatoren

Die [Operatoren](semantic_definition.md#operatoren) müssen in entsprechende Methodenaufrufe
der jeweiligen Klasse der linken Seite des Operators umgewandelt werden.

``` python
a + b           # Verwendung eines Operators
a.__add__(b)    # äquivalent als Methodenaufruf
```

### Logische Operatoren

[Logische Operatoren](semantic_definition.md#logische-operatoren) weichen von der obigen
Regel ab. Hier werden im Paket `keywords.bool` spezielle Klassen angeboten.

``` python
k = True
l = False

print(not k)
print(k or l)
print(k and True)
```

Entsprechend würde der obige Python-Code in folgende Aufrufe der CBuilder-API übersetzt:

``` java
// Globale Variable "k" mit dem Wert "true" erzeugen
Reference varK = new Reference("k");
builder.addVariable(new VariableDeclaration("k"));
builder.addStatement(new Assignment(varK, new BoolLiteral(true)));

// Globale Variable "l" mit dem Wert "false" erzeugen
Reference varL = new Reference("l");
builder.addVariable(new VariableDeclaration("l"));
builder.addStatement(new Assignment(varL, new BoolLiteral(false)));

// Erzeugen und Ausgabe der negierten Variable "k"
Expression notK = new NotKeyword(varK);
builder.addStatement(new Call(printRef, List.of(new Expression[] {notK})));

// Erzeugen und Ausgabe von "k or l"
Expression kOrL = new OrKeyword(varK, varL);
builder.addStatement(new Call(printRef, List.of(new Expression[] {kOrL})));

// Erzeugen und Ausgabe von "k and True"
Expression kAndTrue = new AndKeyword(varK, new BoolLiteral(true));
builder.addStatement(new Call(printRef, List.of(new Expression[] {kAndTrue})));
```

## Kontrollstrukturen

### While-Schleife

``` python
while True:
    print("foo")
#end
```

Die Bedingung muss ein atomarer Boolean sein oder eine Expression (die implizit über die
eingebaute Methode `__bool__` zu einem atomaren Boolean auflöst werden kann). Der
Schleifenkörper besteht wie üblich aus einer Liste von Statements.

``` java
Expression printFoo = new Call(printRef, List.of(new Expression[] {new StringLiteral("foo")}));
List<Statement> body = List.of(new Expression[] {printFoo});
Statement whileStatement = new WhileStatement(new BoolLiteral(true), body);
builder.addStatement(whileStatement);
```

### Bedingte Anweisung (If-Elif-Else)

``` python
if False:
    print("if")
elif k and True:
    print("elif")
else:
    print("else")
#end
```

Der Aufbau der `if`- und `elif`-Blöcke erfolgt nach dem Prinzip eines While-Statements. Der
Aufbau des `else`-Blocks weicht etwas davon ab, da dieser keine `condition` besitzt.

Abschließend werden die einzelnen Blöcke in einem Objekt der Klasse `IfThenElseStatement`
zusammengefasst und dem CBuilder übergeben. Dabei werden gegebenenfalls vorhandene `elif`-
und `else`-Blöcke in ein `Optional` verpackt.

``` java
Reference varK = new Reference("k");

// Die Bedingungen erstellen
Expression conditionIf = new BoolLiteral(false);
Expression conditionElif = new AndKeyword(varK, new BoolLiteral(true));

// Den Body der einzelnen Blöcke anlegen
Statement printIf = new Call(printRef, List.of(new Expression[] {new StringLiteral("if")}));
Statement printElif = new Call(printRef, List.of(new Expression[] {new StringLiteral("elif")}));
Statement printElse = new Call(printRef, List.of(new Expression[] {new StringLiteral("else")}));
List<Statement> bodyIf = List.of(new Statement[] {printIf}));
List<Statement> bodyElif = List.of(new Statement[] {printElif}));
List<Statement> bodyElse = List.of(new Statement[] {printElse}));

// Die einzelnen Blöcke des Conditional Statements erstellen
IfStatement ifStatement = new IfStatement(conditionIf, bodyIf);
ElifStatement elifStatement = new ElifStatement(conditionElif, bodyElif);
ElseStatement elseStatement = new ElseStatement(bodyElse);
List<ElifStatement> elifList = List.of(new ElifStatement[] {elifStatement});

// Conditional Statement zusammensetzen
Statement conditionalStatement = new IfThenElseStatement(ifStatement, Optional.of(elifList), Optional.of(elseStatement));

builder.addStatement(conditionalStatement);
```

## Klassen

### Klasse anlegen

``` python
class A:
    def foo(self, x):
        # Beim originalen Python müsste das Schlüsselwort `pass` für eine leere Function verwendet werden
    #end
#end
```

Klassen können mit Objekten vom Typ `MPyClass` angelegt werden. Dabei muss eine Referenz auf
die jeweilige Elternklasse oder auf `__MPyType_Object` mitgegeben werden - alle Klassen
erben also direkt oder indirekt von `__MPyType_Object`.

Alle Klassen müssen die Methode `__init__` implementieren. In dieser `__init__`-Methode muss
zwingend als erstes Statement ein Aufruf von `super` (Klasse `SuperCall`) erfolgen. Falls
dies im geparsten Mini-Python-Code nicht vorhanden ist, muss dies hier entsprechend ergänzt
werden.

Alle Methoden müssen bei der Deklaration/Definition als ersten Parameter `self` besitzen.
Beim Methodenaufruf darf `self` aber nicht in der Parameterliste vorkommen. (Statische
Methoden, die nicht umgesetzt werden müssen, haben kein `self`.)

Methoden haben zwei Methodennamen. Der Parameter `funcName` im `Function`-Konstruktor
entspricht dem Funktions-/Methodennamen im Python-Code. Über diesen Namen werden die
Methoden im jeweiligen Scope aufgelöst. Da Methoden vom CBuilder als normale C-Funktion im
globalen Scope angelegt werden, müssen sie noch einen im gesamten Programm eindeutigen
(internen) Namen bekommen, der mit der Methode `Function#createUniqueCName()` festgelegt
wird - diese Methode wird automatisch vom Konstruktor von `MPyClass` aufgerufen. (Dennoch
können Methoden nicht als normale Funktionen aufgerufen werden, sondern immer nur über den
Kontext ihrer Klasse.) Beim Überschreiben von Methoden muss der `funcName` entsprechend
identisch sein.

*Anmerkung*: Der Parameter `classAttributes` im Konstruktor von `MPyClass` wird nur für
statische Attribute verwendet und kann ignoriert werden, da statische Attribute hier nicht
zum geforderten Sprachumfang gehören. Sie können hier einfach ein `Map.of()` übergeben.

``` java
// Methode "__init__(self)" anlegen
Statement simpleSuperCall = new SuperCall(List.of());                                   // Aufruf von Super
List<Statement> initBody = List.of(new Statement[] { simpleSuperCall });                // Body der Methode: super() kommt als erstes Statement
List<Argument> initParamList = List.of(new Argument[] {new Argument("self", 0)});       // Parameterliste für "__init__" erstellen
Function methodInit = new Function("__init__", initBody, initParamList, List.of());     // Methode "__init__(self)" erstellen

// Methode "foo(self, x)" anlegen
List<Argument> fooParamList = List.of(new Argument[] {new Argument("self", 0), new Argument("x", 1)});  // Parameterliste für "foo"
Function methodFooA = new Function("foo", List.of(), fooParamList, List.of());                          // Methode "foo" mit leerem Body

// Klasse "A" anlegen: Erbt implizit von __MPyType_Object
List<Function> functionListA = List.of(new Function[] { methodInit, methodFooA });  // Liste der Methoden in A
Reference refToObject = new Reference("__MPyType_Object");                          // Referenz auf die globale Superklasse __MPyType_Object
MPyClass classA = new MPyClass("A", refToObject, functionListA, Map.of());          // Klasse "A"

// Und die Klasse "A" dem Builder übergeben
builder.addClass(classA);
```

### Vererbung

``` python
class B(A):
    def __init__(self):
        super()
    #end

    def foo(self, x):
        print(x)
    #end
#end

i = B()
i.foo("test")
```

Für die Vererbung werden die bereits bekannten Elemente verwendet.

``` java
// "__init__(self)"
Statement simpleSuperCall = new SuperCall(List.of());
List<Statement> initBody = List.of(new Statement[] { simpleSuperCall });
List<Argument> initParamList = List.of(new Argument[] {new Argument("self", 0)});
Function methodInitB = new Function("__init__", initBody, initParamList, List.of());

// "foo(self, x)"
Statement fooPrint = new Call(printRef, List.of(new Expression[] { new Reference("x")}));
List<Argument> fooParamList = List.of(new Argument[] {new Argument("self", 0), new Argument("x", 1)});
List<Statement> fooBody = List.of(new Statement[]{ fooPrint });
Function methodFooB = new Function("foo", fooBody, fooParamList, List.of());

// Klasse "B"
List<Function> functionListB = List.of(new Function[]{ methodInitB, methodFooB });
MPyClass classB = new MPyClass("B", new Reference("A"), functionListB, Map.of());
builder.addClass(classB);


// Ausblick: Benutzung der Klassen
Reference varI = new Reference("i");
VariableDeclaration varIDecl = new VariableDeclaration("i");
builder.addVariable(varIDecl);

Assignment assignX = new Assignment(varI, new Call(new Reference("B"), List.of()));
builder.addStatement(assignX);

Expression callFoo = new Call(new AttributeReference("foo", varI), List.of(new StringLiteral("test")));
builder.addStatement(callFoo);
```

Ausblick: Für den Aufruf von Methoden auf Objekten erzeugen Sie wieder einen `Call`, der als
Referenz eine `AttributeReference` erhält. Diese `AttributeReference` gruppiert den
Methodennamen (`funcName`) und eine Referenz auf das Objekt. Weitere Details [siehe
unten](usage_cbuilder.md#methoden-auf-objekten-aufrufen).

### Verwendung von `self`

``` python
class C:
    def __init__(self, y):
        self.x = y
    #end

    def getX(self):
        return self.x
    #end
#end
```

Der Zugriff auf Attribute des `self`-Objekts erfolgt über eine `AttributeReference`. Die
Zuweisung von Attributen des `self`-Objekts erfolgt über ein `AttributeAssignment`.

``` java
 // Weise "self.x" den Methodenparameter "y" zu
Statement assignSelfX = new AttributeAssignment(new AttributeReference("x", new Reference("self")), new Reference("y"));

// Zugriff auf "self.x" in "getX(self)"
Expression getSelfX = new AttributeReference("x", new Reference("self"));
Statement returnX = new ReturnStatement(getSelfX);

// "__init__(self, y)"
List<Statement> initBodyWithSelfAssign = List.of(new Statement[] { simpleSuperCall, assignSelfX });
List<Argument> initParamListWithX = List.of(new Argument[] {new Argument("self", 0), new Argument("y", 1)});
Function methodInitWithSelf = new Function("__init__", initBodyWithSelfAssign, initParamListWithX, List.of());

// "getX(self)"
List<Statement> getXBody = List.of(new Statement[] { returnX });
List<Argument> paramListGetX = List.of(new Argument[] {new Argument("self", 0)});
Function getX = new Function("getX", getXBody, paramListGetX, List.of());

// Class "C"
List<Function> functionListC = List.of(new Function[] { methodInitWithSelf, getX });
MPyClass classC = new MPyClass("C", refToObject, functionListC, Map.of());
builder.addClass(classC);
```

### Methoden auf Objekten aufrufen

``` python
objectC = C(5)
objectC = C.__init__(5)     # Alternative zu "C(5)"

print(objectC.getX())
```

Das Instanziieren einer Klasse erfolgt wie der Aufruf einer Funktion.

Der Aufruf von Methoden wird über ein Objekt der Klasse `AttributeReference` und einen
`Call` realisiert. Diese `AttributeReference` gruppiert den Methodennamen (`funcName`) und
eine Referenz auf das Objekt. Der Parameter `self` darf beim Methodenaufruf nicht übergeben
werden.

``` java
// Variable "objectC"
Reference varObjectC = new Reference("objectC");
VariableDeclaration varObjectCDecl = new VariableDeclaration("objectC");
builder.addVariable(varObjectCDecl);

// Erzeugung und Zuweisung eines Objekts der Klasse "C" mit "__init__(self, 5)"
Call newC = new Call(new Reference("C"), List.of(new IntLiteral(5)));
Assignment assignObjectC = new Assignment(varObjectC, newC);
builder.addStatement(assignObjectC);

// Auf dem Objekt der Klasse "C" die Methode "getX" aufrufen und Rückgabewert ausgeben.
Expression callGetX = new Call(new AttributeReference("getX", varObjectC), List.of());
builder.addStatement(new Call(printRef, List.of(callGetX)));
```

Analog können Sie (nicht-logische) [Operatoren](semantic_definition.md#operatoren) in den
passenden Methodenaufruf umsetzen. Dabei können die Operator-Methoden der Builtin-Datentypen
ohne vorherige Deklaration verwendet werden. Der Parameter `self` darf nicht in der
Parameterliste vorkommen, hier wird automatisch das Objekt, auf dem der Aufruf stattfindet,
ergänzt.

``` python
a + 10
a.__add__(10)   # Äquivalenter Methodenaufruf
```

``` java
Reference varA = new Reference("a");
AttributeReference varAAdd = new AttributeReference("__add__", varA);                   // Referenz auf die Methode `__add__` des Objekts `a`
Call addInteger = new Call(varAAdd, List.of(new Expression[] { new IntLiteral(10)}));   // Erzeugen des Aufrufs `a + 10`
builder.addStatement(addInteger);
```
