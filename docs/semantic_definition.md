# Definition der semantischen Spracheigenschaften von Mini-Python

Ihre Umsetzung des Compiler-Frontends soll die in diesem Dokument definierten semantischen
Sprachelemente der Sprache “Mini-Python” unterstützen. Als Grundlage werden die semantischen
Sprachelemente der Sprache Python 3 verwendet. Da die gesamte Sprache zu umfangreich ist,
wird in diesem Kapitel ein kleinerer und vereinfachter Sprachumfang festgelegt.

Die syntaktischen Eigenschaften der Sprache “Mini-Python” und die Abweichungen zu Python 3
werden im Kapitel [Definition der syntaktischen Spracheigenschaften](syntax_definition.md)
beschrieben.

Einige Angaben zur Semantik sind für den AST und die Symboltabelle noch nicht relevant und
werden erst bei der Verwendung des CBuilders wichtig. Um die Verwendung des CBuilders zu
erleichtern, sollten Sie diese Punkte allerdings schon in der semantischen Analyse
betrachten.

Die Verwendung des CBuilders wird im Kapitel [Verwendung des CBuilders](usage_cbuilder.md)
beschrieben. (Wird rechtzeitig ergänzt.)

## Scopes

Die Scopes sind im Vergleich zu Python 3 deutlich vereinfacht. Es gibt den globalen Scope,
und es gibt weitere (lokale) Scopes für die Funktionen, für Klassen und Klassenobjekte
(`self`).

Die Hierarchie der Scopes kann an dem folgenden Graphen abgelesen werden:

``` mermaid
graph BT;
    Klasse_A-->Global;
    Funktion-->Global;
    Klasse_B_geerbt-->Klasse_B_super;
    Klasse_B_super-->Global;
    Objekt_A-->Klasse_A;
    Objekt_B_geerbt-->Klasse_B_geerbt;
    Objekt_B_super-->Klasse_B_super;
```

Sollte das jeweilige Symbol nicht in einem Scope gefunden werden, muss im übergeordneten
Scope danach gesucht werden. Dies erfolgt, bis der globale Scope erreicht wird. Eventuelle
Besonderheiten zu den Scopes werden bei der jeweiligen Funktionalität in diesem Dokument
beschrieben.

## Objekte

Wie in Python 3 werden intern alle Datentypen als Objekte der Klasse `Object` betrachtet.
Zur Laufzeit soll eine dynamische Typisierung realisiert werden, d.h. die Variablen werden
ohne Typdeklaration angelegt und können zur Laufzeit ihren Typ verändern.

## Builtin-Datentypen

Die in der syntaktischen Definition festgelegten [eingebauten
Datentypen](syntax_definition.md#datentypen) sollen implizit angelegt werden können. Sie
verhalten sich zur Laufzeit wie Objekte mit einem festgelegten Umfang unterstützter Methoden
(u.a. `to_string`, Operatoren).

``` python
a = 1       # Integer
b = "foo"   # String
c = True    # Boolean
```

## Funktionen

Alle Funktionen besitzen einen eigenen Scope. In diesem müssen die Parameter sowie die in
der Funktion definierten Variablen zugreifbar sein.

Um eine Funktion zu verwenden, muss diese zuerst definiert werden.

Es müssen nur einfache Funktionen unterstützt werden - verschachtelte Funktionen können Sie
ignorieren.

### Globale Builtin-Funktionen

Die folgenden Funktionen sind immer vorhanden und global verfügbar. Abweichend zu den selbst
definierten Funktionen müssen Builtin-Funktionen nicht vor der Verwendung definiert werden.

| Python-Funktion | Beschreibung                                                         | Rückgabe |
|:----------------|:---------------------------------------------------------------------|:---------|
| `print()`       | Gibt ein oder mehrere übergeben String-Literale auf der Konsole aus. | `void`   |
| `input()`       | Liest eine Zeile von der Konsole ein.                                | `String` |

### Funktionen für Builtin-Datentypen

Die Funktionen für Builtin-Datentypen verhalten sich intern wie Methodenaufrufe auf dem
Datenobjekt. Sie können unter der Verwendung der passenden Methodennamen auch für Klassen
überladen werden. Die folgende Liste stellt die unterstützten Datentypen sowie die
Methodennamen dar:

| Python-Funktion | Beschreibung                                                   | Integer | String | Boolean | Methodenname |
|:----------------|:---------------------------------------------------------------|:-------:|:------:|:-------:|:------------:|
| `str()`         | Gibt die String Representation des übergebenen Objekts zurück. |    ✓    |   ✓    |    ✓    |  `__str__`   |
| `int()`         | Cast zu Integer                                                |    ✗    |   ✓    |    ✓    |  `__int__`   |

Der CBuilder unterstützt keine direkte Übergabe dieser Funktionen, sondern erwartet die
Übergabe als Methodenaufruf auf dem Datenobjekt. Sie können diese Funktionsaufrufe bereits
bei der semantischen Analyse durch passende Methodenaufrufe ersetzen.

``` python
a = "5"

b = int(a)          # Verwendung als Funktion
b = a.__int__()     # äquivalent als Methodenaufruf
```

## Operatoren

Die Builtin-Datentypen unterstützen jeweils nur einige der vorhandenen Operatoren. Zudem
sind die Operatoren für die Builtin-Datentypen nur jeweils für die Verwendung mit den selben
Datentypen vorgesehen.

Für Klassen können alle Operatoren überladen werden, indem die Methode für den jeweiligen
Operator implementiert wird (beispielsweise `__add__()` für den Operator `+`, siehe unten).

Im Folgenden sehen Sie Listen, welche Operatoren von welchen Datentypen unterstützt werden
sollen. Hier sind auch die Methodennamen zur Verwendung bei Klassen mit angegeben.

Der CBuilder unterstützt keine direkte Übergabe von Operatoren, sondern erwartet die
Übergabe als Methodenaufruf auf dem linken Wert (Objekt) mit dem rechten Wert (Objekt) als
Parameter. Sie können also bereits bei der semantischen Analyse die Operatoren durch
passende Methodenaufrufe ersetzen.

Der Zuweisungsoperator und die logischen Operatoren besitzen explizite Aufrufe und müssen
nicht umgewandelt werden.

``` python
a = 3
b = 4

c = a + b           # Verwendung eines Operators
c = a.__add__(b)    # äquivalent als Methodenaufruf
```

### Logische Operatoren

Die logischen Operatoren stellen eine Abweichung dar und sollen nicht in Methodenaufrufe
umgewandelt werden. Im CBuilder sind dafür explizite Aufrufe vorgesehen.

In Python 3 können Sie über die Methode `__bool__` Bool-Werte für eigenen Klassen
implementieren. Allerdings wird die Methode `__bool__` dabei nicht implizit aufgerufen. Sie
müssen diese Methode explizit aufrufen. Überlegen Sie sich, wie Sie den Methodenaufruf von
`__bool__` dennoch vor dem Nutzer ihres Compilers verbergen können.

| Operation   | Operator | Integer | String | Boolean |
|:------------|:--------:|:-------:|:------:|:-------:|
| Disjunktion |   `or`   |    ✗    |   ✗    |    ✓    |
| Konjunktion |  `and`   |    ✗    |   ✗    |    ✓    |
| Negation    |  `not`   |    ✗    |   ✗    |    ✓    |

### Vergleichsoperatoren

| Operation      | Operator | Integer | String | Boolean | Methodenname |
|:---------------|:--------:|:-------:|:------:|:-------:|:------------:|
| Gleichheit     |   `==`   |    ✓    |   ✓    |    ✓    |   `__eq__`   |
| Ungleichheit   |   `!=`   |    ✓    |   ✓    |    ✓    |   `__ne__`   |
| Größer gleich  |   `>=`   |    ✓    |   ✓    |    ✗    |   `__ge__`   |
| Größer         |   `>`    |    ✓    |   ✓    |    ✗    |   `__gt__`   |
| Kleiner gleich |   `<=`   |    ✓    |   ✓    |    ✗    |   `__le__`   |
| Kleiner        |   `<`    |    ✓    |   ✓    |    ✗    |   `__lt__`   |

### Arithmetische Operatoren

| Operation                            | Operator | Integer | String | Boolean | Methodenname |
|:-------------------------------------|:--------:|:-------:|:------:|:-------:|:------------:|
| Addition / String-Literal-Verkettung |   `+`    |    ✓    |   ✓    |    ✗    |  `__add__`   |
| Subtraktion / negative Werte         |   `-`    |    ✓    |   ✗    |    ✗    |  `__sub__`   |
| Multiplikation                       |   `*`    |    ✓    |   ✗    |    ✗    |  `__mul__`   |
| Division                             |   `/`    |    ✓    |   ✗    |    ✗    |  `__div__`   |

## Klassen

Die Klassen müssen wie die Funktionen vor der Verwendung definiert werden.

Alle Klassen besitzen einen eigenen Scope in dem die Methoden definiert sind. Der
übergeordnete Scope von Klassen ist der Scope der Superklasse oder der globale Scope.

Statische Methoden und Attribute müssen nicht unterstützt werden.

### Konstruktor

Alle Klassen erben implizit von `Object`.

Die Objekte von Klassen sollen wie in Python 3 üblich über den Konstruktor, also die Methode
`__init__`, erzeugt werden. Der Aufruf des Konstruktors der Superklasse soll im Gegensatz zu
Python 3 vereinfacht und abweichend realisiert werden. Dieser Aufruf entspricht syntaktisch
einem Funktionsaufruf mit einer Parameterliste, die auch leer sein kann. Semantisch ist
dieser Aufruf allerdings ein Sonderfall und entspricht dem Aufruf des nächst höheren
Superkonstruktors für das aktuell zu erzeugende Objekt.

``` python
class A:
    def __init__(self, x):
        ...
    #end
#end

class B(A):
    def __init__(self, x):
        super(x)
    #end
#end
```

Der CBuilder erwartet für alle Klassen die Methode `__init__` mit dem Aufruf des
Konstruktors der Superklasse als erste Anweisung. Sie können also zur Vorbereitung für den
nächsten Schritt bereits bei allen Klassen die Methode `__init__` mit dem Aufruf von
`super()` ergänzen.

Klassen ohne selbst definierte Superklasse müssen im CBuilder explizit von der Klasse
`__MPyType_Object` erben.

### Klassenobjekte

Klassenobjekte besitzen jeweils einen eigenen Scope, der über das Schlüsselwort `self`
zugreifbar ist.

``` python
class A:
    def __init__(self, x):
        self.x = x
    #end

    def foo(self):
        return self.x
    #end
#end
```

### Methoden

Die Methoden verhalten sich im Wesentlichen wie Funktionen. Folgende Unterschiede zu
Funktionen müssen beachtet werden:

1.  Bei der Definition von Methoden muss der Parameter `self` immer als erster Parameter
    definiert/übergeben werden.

    Somit wird nicht wie in Python 3 der erste Parameter implizit als Objektreferenz
    interpretiert, sondern immer der erste Parameter mit dem festgelegten Namen `self`.

2.  Abweichend zu Funktionen sind Methoden immer Aufrufe auf einem Objekt und besitzen als
    übergeordneten Scope den Scope ihrer Klasse bzw. den Scope des Objekts, falls auf `self`
    zugegriffen wird.

3.  Beim Aufruf von Methoden darf der Parameter `self` im Gegensatz zur Definition nicht
    vorhanden sein bzw. übergeben werden. Der CBuilder ergänzt `self` implizit mit der
    Objektreferenz, auf dem die Methode aufgerufen wurde.
