# Definition der syntaktischen Sprachelemente von Mini-Python

Ihre Umsetzung des Compiler-Frontends soll die in diesem Dokument definierten syntaktischen
Sprachelemente der Sprache Mini-Python unterstützen. Als Grundlage wird die Syntax der
Sprache Python 3 verwendet. Da die gesamte Sprache zu umfangreich ist, wird in diesem
Kapitel ein kleinerer und vereinfachter Sprachumfang festgelegt.

Die semantischen Eigenschaften der Sprache und die Abweichungen zu Python 3 werden im
Kapitel [Definition der semantischen Spracheigenschaften](semantic_definition.md)
beschrieben.

## Datentypen

| Datentyp  | Definition                                                                                                                                                     |
|:----------|:---------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `Integer` | Integer-Literale bestehen aus einer beliebigen Folge der Ziffern `0-9`.                                                                                        |
| `String`  | String-Literale bestehen aus einer beliebigen Folge an ASCII-Zeichen, die von `'` oder `"` eingeschlossen sind. Sie müssen keine Unicode-Zeichen unterstützen. |
| `Boolean` | Bestehen aus einem der beiden Schlüsselwörter `True` oder `False`                                                                                              |
| Klassen   | siehe [Klassen](#klassen)                                                                                                                                      |

## Basis-Funktionalität

### Kommentare

Kommentare werden durch das Zeichen `#` eingeleitet und umfassen sämtliche Zeichen bis zum
nächsten Newline. Es existiert ein spezielles Schlüsselwort `#end`, welches keinen Kommentar
darstellt.

### `#end`

Das Schlüsselwort `#end` stellt eine Besonderheit dar und dient zum Schließen von
Funktionen, Methoden, Klassen und Kontrollstrukturen. Durch die Verwendung dieses
Schlüsselworts kann auf die Auswertung der Einrückung verzichtet werden.

### Bezeichner

Werden zur Bezeichnung von Variablen, Funktionsnamen sowie Klassennamen verwendet. Bestehen
aus einer Zeichenkette der Zeichen `a-z`,`A-Z`, `0-9`, `_`. Bezeichner dürfen nicht mit
einer Ziffer `0-9` beginnen.

### Variablen

Variablen bestehen aus einem eindeutigen Bezeichner (Variablennamen). Den Variablen können
Werte zugewiesen werden und Variablen können als Werte verwendet werden. Die Zuweisung
erfolgt mithilfe des `=`-Operators. Auf der rechten Seite der Zuweisung können auch einfache
Ausdrücke stehen.

``` python
a = 5
a = 2 + 3
print(a)
```

### Anweisung

Eine Anweisung ist eine einzeilige Befehlsfolge, beispielsweise eine Zuweisung, ein
Funktionsaufruf oder eine Operation. Sie muss immer mit einem Newline abgeschlossen werden.

``` python
a = 10 - 5
a = "foo"
func1(a, b)
...
```

### Funktionsdefinition

Funktionsdefinitionen werden mit dem Schlüsselwort `def` eingeleitet. Sie besitzen einen
eindeutigen Bezeichner (Funktionsnamen), eine in Klammern angegebene Parameterliste, die
auch leer sein kann und eine Abfolge von Anweisungen. Der Anweisungsblock wird durch einen
Doppelpunkt `:` eingeleitet. Funktionsdefinitionen können eine Rückgabe besitzen, die über
das Schlüsselwort `return` eingeleitet wird. Eine Funktionsdefinition muss immer mit dem
Schlüsselwort `#end` geschlossen werden.

**_Hinweis_: Verschachtelte Funktionsdefinitionen müssen nicht umgesetzt werden.**

``` python
def bezeichner(param1, param2):
    <Anweisung_1>
    <Anweisung_2>
    return <Bezeichner, Wert oder Operation>
#end
```

``` python
def func1(a, b):
    c = a + b
    return c == a + b
#end
```

### Funktionsaufrufe

Funktionsaufrufe bestehen aus einem Bezeichner (Funktionsname) gefolgt von einer in Klammern
angegebenen Parameterliste, die auch leer sein kann. Als Parameter können Variablen, Werte
der Datentypen, weitere Funktionsaufrufe und Ausdrücke wie z.B. `1 + 1` dienen.

``` python
func1(var1, 5)
func1(func2(), 1 + 1)
```

## Operatoren

Operatoren definieren den Typ einer Operation. Eine Operation besitzt im Regelfall einen
linken und rechten Operanden. Als Operanden können Variablen, Werte der Datentypen,
Funktionsaufrufe sowie Operationen oder Ausdrücke dienen.

Die Operatoren besitzen eine Rangfolge, um verschachtelte Operationen aufzulösen. Sie dürfen
daher nicht einfach von links nach rechts aufgelöst werden. Die Rangfolge der Operatoren
können Sie der [Python
Dokumentation](https://docs.python.org/3/reference/expressions.html#operator-precedence)
entnehmen.

``` python
1 == 1
7 >= 1 + 2
```

### Logische Operatoren

| Operation   | Operator |
|:------------|:--------:|
| Disjunktion |   `or`   |
| Konjunktion |  `and`   |
| Negation    |  `not`   |

Der Operator `not` stellt eine Besonderheit dar und besitzt nur einen (rechten) Operanden.

### Vergleichsoperatoren

| Operation      | Operator |
|:---------------|:--------:|
| Gleichheit     |   `==`   |
| Ungleichheit   |   `!=`   |
| Größer gleich  |   `>=`   |
| Größer         |   `>`    |
| Kleiner gleich |   `<=`   |
| Kleiner        |   `<`    |

### Arithmetische Operatoren

| Operation                            | Operator |
|:-------------------------------------|:--------:|
| Addition / String-Literal-Verkettung |   `+`    |
| Subtraktion                          |   `-`    |
| Multiplikation                       |   `*`    |
| Division                             |   `/`    |

## Kontrollstrukturen

### While-Schleife

While-Schleifen werden mit dem Schlüsselwort `while` eingeleitet. Sie bestehen im Weiteren
aus einer Bedingung, die durch einen Doppelpunkt `:` abgeschlossen wird, einer Folge von
Anweisungen und werden mit dem Schlüsselwort `#end` abgeschlossen.

Die Bedingung kann aus einem atomaren Boolean-Wert oder einem Vergleichsausdruck bestehen.

``` python
while <Bedingung>:
    <Anweisung_1>
    <Anweisung_2>
#end
```

``` python
a = 10
while (a >= 0):
    print(a)
    a = a- 1
#end
```

### Bedingte Anweisung (If-Elif-Else)

Eine bedingte Anweisung besteht immer aus genau einer `if`-Anweisung, keiner oder beliebig
vieler `elif`-Anweisungen und einer oder keiner `else`-Anweisung. Die bedingte Anweisung
muss mit dem Schlüsselwort `#end` abgeschlossen werden.

Eine `if`-Anweisung wird mit dem Schlüsselwort `if` eingeleitet und besteht aus einer
Bedingung, die mit einem Doppelpunkt `:` abgeschlossen wird und einer Folge von Anweisungen.

Eine `elif`-Anweisung wird mit dem Schlüsselwort `elif` eingeleitet und ist ansonsten
identisch zur `if`-Anweisung.

Eine `else`-Anweisung wird mit dem Schlüsselwort `else` eingeleitet. Auf das Schlüsselwort
folgt ein Doppelpunkt `:` und eine Folge von Anweisungen.

``` python
if <Bedingung>:
    <Anweisung_1>
    <Anweisung_2>
#end
```

``` python
if <Bedingung>:
    <Anweisung>
elif <Bedingung>:
    <Anweisung>
else:
    <Anweisung>
#end
```

``` python
a = "abc"
if a < "adc":
    print("a kleiner als ", "adc")
elif a == "adf":
    print("a ist ", "adf")
else:
    print("a passt nicht")
#end
```

## Klassen

### Klassendefinitionen

Klassendefinitionen werden mit dem Schlüsselwort `class` eingeleitet. Nach dem Schlüsselwort
folgt ein eindeutiger Bezeichner (Klassenname). Auf den Bezeichner kann in Klammern die
Angabe des Bezeichners einer Superklasse folgen. Nach dem Bezeichner und der möglicherweise
vorhandenen Angabe der Superklasse folgt ein Doppelpunkt `:` sowie eine Auflistung von
Methodendefinitionen. Klassendefinitionen müssen immer mit dem Schlüsselwort `#end` beendet
werden.

Die Methodendefinitionen sind wie [Funktionsdefinitionen](#funktionsdefinition) aufgebaut.
Dabei besteht die Einschränkung, dass als erster Parameter das Schlüsselwort `self`
übergeben werden muss. Zudem erfolgt der Zugriff auf Klassenattribute über das Schlüsselwort
`self` gefolgt von einem Punkt `.` und dem Bezeichner des Attributes.

**_Hinweis_: Weitere Sprachelemente wie Mehrfachvererbung oder statische Klassenattribute
und -methoden müssen nicht umgesetzt werden.**

``` python
class Bezeichner(<Bezeichner der Superklasse>):
    <Methodendefinition_1>
    <Methodendefinition_2>
#end
```

``` python
class A:
    def method_1(self):
        ...
    #end
#end

class B(A):
    def setA(self, a):
        self.a = a
    #end

    def getA(self):
        return self.a
    #end
#end
```

### Erzeugung von Klassenobjekten

Die Erzeugung von Klassenobjekten erfolgt wie ein [Funktionsaufruf](#funktionsaufrufe) mit
dem Klassennamen als “Funktionsnamen”.

``` python
a = A(<Parameterliste oder leer>)
```

### Methodenaufrufe

Methodenaufrufe besitzen einen zusammengesetzten Bezeichner und sind ansonsten wie
[Funktionsaufrufe](#funktionsaufrufe) aufgebaut. Der Bezeichner setzt sich aus dem
Bezeichner des Objektes, einem Punkt `.` und dem Bezeichner der Methode zusammen.

``` python
a = B()
a.setA(5)
```
