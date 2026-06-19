# Blatt 07 – Zoo-Verwaltung mit Generics, Streams und Logging

## Projektbeschreibung

In diesem Projekt wurde ein kleines Verwaltungssystem für einen Zoo entwickelt. Das Ziel war, verschiedene moderne Java-Konzepte gemeinsam in einer Anwendung einzusetzen.

Verwendet wurden insbesondere:

* versiegelte Interfaces
* Records
* Generics
* Collections
* Stream-API
* Prädikate
* systematisches Logging

Das System verwaltet verschiedene Tierarten und Gehege. Durch die Verwendung von Generics wird bereits beim Kompilieren geprüft, welche Tiere in ein bestimmtes Gehege eingefügt werden dürfen.

## Aufbau des Projekts

Das Projekt ist in mehrere Bereiche unterteilt:

```text
zoo
├── animal
│   ├── Animal
│   ├── Mammal
│   ├── Fish
│   ├── Reptile
│   ├── Bird
│   └── konkrete Tierarten
├── enclosure
│   ├── Enclosure
│   ├── Aquarium
│   ├── Terrarium
│   ├── MammalHouse
│   └── CatHouse
├── Zoo
└── Main
```

Die Tierklassen befinden sich im Package `zoo.animal`. Die allgemeinen und spezialisierten Gehege befinden sich im Package `zoo.enclosure`.

## Tierhierarchie

Die gemeinsame Basisschnittstelle aller Tiere ist:

```java
public sealed interface Animal
```

Jedes Tier stellt mindestens seinen Namen über folgende Methode bereit:

```java
String name();
```

Von `Animal` werden die Hauptgruppen abgeleitet:

* `Mammal`
* `Fish`
* `Reptile`
* `Bird`

Die Säugetiere werden zusätzlich unterteilt in:

* `Primate`
* `Rodent`
* `Cat`

Konkrete Tiere wurden als Records umgesetzt, zum Beispiel:

```java
public record Lion(String name) implements Cat {}
```

Weitere Beispiele sind:

* `Tiger`
* `Gorilla`
* `Chimpanzee`
* `Mouse`
* `Squirrel`
* `Trout`
* `Salmon`
* `Snake`
* `Turtle`
* `Eagle`
* `Parrot`

Records eignen sich hier gut, weil die konkreten Tierklassen hauptsächlich unveränderliche Daten speichern.

## Generisches Gehege

Die allgemeine Gehegeklasse wurde mit Generics umgesetzt:

```java
public class Enclosure<T extends Animal>
```

Der Typparameter `T` legt fest, welche Tiergruppe in einem Gehege erlaubt ist.

Ein Gehege enthält:

* einen Namen
* eine Sammlung seiner Bewohner
* eine Methode zum Hinzufügen
* eine Methode zum Entfernen
* eine Methode zum Abrufen aller Bewohner

Wichtige Methoden sind:

```java
boolean add(T animal)
boolean remove(T animal)
List<T> getInhabitants()
```

Intern wird ein `LinkedHashSet` verwendet. Dadurch wird verhindert, dass dasselbe Tier mehrfach in einem Gehege gespeichert wird. Gleichzeitig bleibt die Reihenfolge der eingefügten Tiere erhalten.

## Spezialisierte Gehege

Auf Grundlage der generischen Klasse wurden spezielle Gehege erstellt:

```java
Aquarium extends Enclosure<Fish>
Terrarium extends Enclosure<Reptile>
MammalHouse extends Enclosure<Mammal>
CatHouse extends Enclosure<Cat>
```

Durch diese Typisierung kann beispielsweise nur ein Fisch in ein Aquarium eingefügt werden.

Ein solcher Aufruf ist erlaubt:

```java
aquarium.add(new Trout("Toni"));
```

Ein Löwe kann dagegen nicht in ein Aquarium eingefügt werden. Dieser Fehler wird bereits vom Compiler erkannt und nicht erst während der Programmausführung.

## Zoo-Verwaltung

Die Klasse `Zoo` verwaltet alle vorhandenen Gehege.

Die Gehege werden in folgender Sammlung gespeichert:

```java
List<Enclosure<? extends Animal>>
```

Dadurch kann der Zoo unterschiedliche Gehegetypen gemeinsam verwalten.

Zu den wichtigsten Funktionen gehören:

```java
addEnclosure(...)
getEnclosures()
findEnclosureByName(...)
getAllAnimals()
getAllMammals()
getAnimalsByPredicate(...)
countAnimalsByType()
getOvercrowdedEnclosures(...)
summary()
```

## Einsatz der Stream-API

Die Stream-API wird verwendet, um Tiere aus verschiedenen Gehegen zu sammeln, zu filtern und zu gruppieren.

### Alle Tiere sammeln

Mit `flatMap` werden die Bewohner aller Gehege zu einem gemeinsamen Stream verbunden.

```java
enclosures.stream()
        .flatMap(enclosure -> enclosure.getInhabitants().stream())
```

### Säugetiere filtern

Mit `filter` und `map` können ausschließlich Säugetiere ausgewählt werden.

```java
animals.stream()
        .filter(Mammal.class::isInstance)
        .map(Mammal.class::cast)
```

### Tiere nach Typ zählen

Mit `groupingBy` und `counting` werden die Tiere anhand ihrer konkreten Klasse gruppiert.

```java
Collectors.groupingBy(Animal::getClass, Collectors.counting())
```

### Benutzerdefinierte Filter

Die Methode `getAnimalsByPredicate` akzeptiert ein `Predicate<Animal>`. Dadurch kann beim Methodenaufruf frei festgelegt werden, nach welcher Eigenschaft gefiltert werden soll.

Beispiel:

```java
zoo.getAnimalsByPredicate(animal -> animal.name().length() > 4);
```

## Logging

Für die Protokollierung wird `java.util.logging` verwendet.

In der Klasse `Zoo` kommen verschiedene Log-Level zum Einsatz:

* `INFO` beim Aufruf wichtiger öffentlicher Methoden
* `FINE` für zusätzliche Informationen zum Ergebnis
* `WARNING`, wenn ein gesuchtes Gehege nicht gefunden wird
* `SEVERE` bei ungültigen oder inkonsistenten Zuständen

Beispiel:

```java
LOGGER.log(Level.INFO, "Searching enclosure by name: {0}", name);
```

Im Hauptprogramm wird das Log-Level so eingestellt, dass auch Meldungen der Stufe `FINE` sichtbar werden.

## Demonstration in Main

Die Klasse `Main` erstellt mehrere Gehege und fügt verschiedene Tiere hinzu.

Danach werden unter anderem folgende Funktionen demonstriert:

* Ausgabe der Zoo-Zusammenfassung
* Anzeige aller Tiere
* Anzeige aller Säugetiere
* Filterung mit einem Predicate
* Gruppierung nach Tierart
* Ermittlung überfüllter Gehege
* Suche nach einem nicht vorhandenen Gehege
* Ausgabe unterschiedlicher Logging-Meldungen

## Beispielausgabe

Eine mögliche Zusammenfassung lautet:

```text
Zoo mit 4 Gehegen und 6 Tieren
```

Danach werden die einzelnen Tiere und die Ergebnisse der Filteroperationen ausgegeben.

## Reflexion

### Generics

Generics waren in dieser Aufgabe besonders wichtig, weil unterschiedliche Gehege nur bestimmte Tiergruppen aufnehmen sollen.

Ohne Generics müsste zur Laufzeit geprüft werden, ob ein Tier in ein Gehege gehört. Mit `Enclosure<T extends Animal>` übernimmt der Compiler einen großen Teil dieser Prüfung.

Dadurch werden falsche Kombinationen früh erkannt und der Code wird verständlicher.

### Sealed Interfaces

Durch `sealed interfaces` wird die Tierhierarchie kontrolliert. Es ist genau festgelegt, welche Interfaces und Klassen die Hierarchie erweitern dürfen.

Das ist hilfreich, weil alle möglichen Tiergruppen bekannt sind und die Struktur nicht unkontrolliert erweitert werden kann.

### Records

Records reduzieren den Code für konkrete Tierarten. Konstruktor, Zugriffsmethoden, `equals`, `hashCode` und `toString` werden automatisch erzeugt.

Für einfache unveränderliche Tierdaten ist diese Darstellung übersichtlich und passend.

### Streams

Streams eignen sich gut für Operationen wie Filtern, Sammeln und Gruppieren. Besonders bei der Verwaltung mehrerer Gehege ist `flatMap` hilfreich, um alle Tiere gemeinsam zu verarbeiten.

Bei sehr komplexen Bedingungen können Streams jedoch schwer lesbar werden. In solchen Fällen kann eine klassische Schleife verständlicher sein.

### Logging

Logging ist flexibler als einfache Konsolenausgaben. Über die Log-Level kann gesteuert werden, wie viele Informationen angezeigt werden.

Während der Entwicklung kann beispielsweise `FINE` aktiviert werden. Im normalen Betrieb können nur wichtigere Meldungen wie `INFO`, `WARNING` oder `SEVERE` angezeigt werden.

## Projekt bauen

```powershell
.\gradlew.bat clean build
```

## Projekt starten

```powershell
.\gradlew.bat run
```

## Verwendete Java-Version

Das Projekt verwendet Java 21.
