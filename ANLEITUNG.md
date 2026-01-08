# Go Home - Brettspiel in Java mit Swing

## Übersicht

Dieses Projekt implementiert das klassische "Go Home" Brettspiel in Java mit einer grafischen Benutzeroberfläche (Swing). Das Spiel basiert auf dem Konzept von https://inf-schule.de/oop/java/beziehungen/gohome/.

## Inhaltsverzeichnis

1. [Spielregeln](#spielregeln)
2. [Architektur und Klassenstruktur](#architektur-und-klassenstruktur)
3. [Installation und Ausführung](#installation-und-ausführung)
4. [Schritt-für-Schritt Anleitung](#schritt-für-schritt-anleitung)
5. [Klassendiagramm und Beziehungen](#klassendiagramm-und-beziehungen)

## Spielregeln

**Ziel des Spiels**: Bringe alle 4 Figuren ins Ziel!

**Spielablauf**:
1. Würfle, um eine Zahl zu erhalten (1-6)
2. Du brauchst eine 6, um eine Figur vom Start auf das Spielfeld zu bringen
3. Bewege eine Figur um die gewürfelte Anzahl vorwärts
4. Wenn du auf ein Feld mit einer gegnerischen Figur kommst, wird diese zurück zum Start geschickt
5. Erreiche das Ziel mit allen 4 Figuren, um zu gewinnen
6. Klicke auf "Nächster Spieler" nach deinem Zug

**Spielerzahl**: 2-4 Spieler

## Architektur und Klassenstruktur

Das Projekt folgt objektorientierten Prinzipien und ist in mehrere Klassen unterteilt:

### Kern-Klassen (Model)

#### 1. **Field.java**
- Repräsentiert ein einzelnes Feld auf dem Spielbrett
- Kann eine Figur halten oder leer sein
- Wichtige Methoden:
  - `setFigure(Figure)` - Platziert eine Figur auf dem Feld
  - `getFigure()` - Gibt die Figur auf dem Feld zurück
  - `isEmpty()` - Prüft, ob das Feld leer ist
  - `removeFigure()` - Entfernt die Figur vom Feld

#### 2. **Figure.java**
- Repräsentiert eine Spielfigur (Spielstein)
- Eigenschaften: Farbe, Position, Status (zu Hause oder nicht), Besitzer
- Wichtige Methoden:
  - `move(int steps)` - Bewegt die Figur um eine Anzahl von Schritten
  - `setHome()` - Markiert die Figur als im Ziel angekommen
  - `getCurrentPosition()` - Gibt die aktuelle Position zurück

#### 3. **Player.java**
- Repräsentiert einen Spieler
- Besitzt 4 Figuren
- Eigenschaften: Name, Farbe, Startposition, Zielposition
- Wichtige Methoden:
  - `hasWon()` - Prüft, ob alle Figuren im Ziel sind
  - `getFiguresAtHome()` - Zählt Figuren im Ziel
  - `getFigures()` - Gibt alle Figuren des Spielers zurück

#### 4. **Dice.java**
- Repräsentiert einen sechsseitigen Würfel
- Wichtige Methoden:
  - `roll()` - Würfelt und gibt eine Zahl zwischen 1 und 6 zurück
  - `getLastRoll()` - Gibt das letzte Würfelergebnis zurück

#### 5. **Board.java**
- Repräsentiert das Spielbrett mit 40 Feldern
- Verwaltet die Platzierung und Bewegung von Figuren
- Wichtige Methoden:
  - `placeFigure(Figure, position)` - Platziert eine Figur an einer Position
  - `moveFigure(from, to)` - Bewegt eine Figur und behandelt Kollisionen
  - `getField(position)` - Gibt ein Feld an einer bestimmten Position zurück

#### 6. **Game.java**
- Hauptsteuerung des Spiels
- Verwaltet Spielzustand, Spieler, Brett und Würfel
- Wichtige Methoden:
  - `rollDice()` - Würfelt den Würfel
  - `moveFigure(Figure)` - Bewegt eine Figur gemäß der Spielregeln
  - `nextPlayer()` - Wechselt zum nächsten Spieler
  - `isGameOver()` - Prüft, ob das Spiel vorbei ist
  - `reset()` - Startet ein neues Spiel

### UI-Klassen (View)

#### 7. **BoardPanel.java**
- JPanel zur Visualisierung des Spielbretts
- Zeichnet das runde Brett, Felder und Figuren
- Zeigt Spielerinformationen an
- Wichtige Methoden:
  - `paintComponent(Graphics)` - Zeichnet das gesamte Spielbrett
  - `drawBoard(Graphics2D)` - Zeichnet die Felder
  - `drawFigures(Graphics2D)` - Zeichnet alle Figuren
  - `refresh()` - Aktualisiert die Anzeige

#### 8. **GameUI.java**
- Hauptfenster der Anwendung
- Enthält Steuerungselemente (Würfelbutton, Figurenauswahl)
- Zeigt Spielstatus und Nachrichten an
- Menü mit Optionen (Neues Spiel, Spielregeln, Über)
- Wichtige Methoden:
  - `rollDice()` - Behandelt Würfelwurf
  - `moveFigure(Figure)` - Behandelt Figurenbewegung
  - `nextPlayer()` - Wechselt Spieler
  - `updateUI()` - Aktualisiert alle UI-Komponenten

#### 9. **Main.java**
- Einstiegspunkt der Anwendung
- Startet die GameUI im Event Dispatch Thread

## Installation und Ausführung

### Voraussetzungen
- Java Development Kit (JDK) 8 oder höher
- Keine zusätzlichen Bibliotheken erforderlich (verwendet nur Java Standard-Bibliotheken)

### Kompilierung

```bash
cd "Go Home/scr"
javac *.java
```

### Ausführung

```bash
java Main
```

## Schritt-für-Schritt Anleitung

### Schritt 1: Spieleranzahl wählen
- Beim Start des Programms erscheint ein Dialog
- Wähle die Anzahl der Spieler (2, 3 oder 4)
- Klicke auf die gewünschte Option

### Schritt 2: Spielfeld verstehen
- **Kreisförmiges Brett**: 40 Felder im Kreis angeordnet
- **Farbige Felder**: Startpositionen der verschiedenen Spieler
- **Spielerinformationen**: Links am Rand mit Name, Farbe und Fortschritt
- **Figuren**: Kreise in Spielerfarben auf dem Brett oder in den Ecken (wenn im Ziel)

### Schritt 3: Würfeln
- Der aktuelle Spieler ist mit "← Aktuell" markiert
- Klicke auf den Button "Würfeln"
- Die gewürfelte Zahl wird groß angezeigt

### Schritt 4: Figur auswählen
- Nach dem Würfeln werden Buttons für jede Figur angezeigt
- Button-Text zeigt den Status der Figur:
  - "(Start)" - Figur noch nicht auf dem Brett
  - "(Pos X)" - Figur auf Position X
  - "(Ziel)" - Figur im Ziel (Button deaktiviert)
- Wähle eine Figur zum Bewegen

### Schritt 5: Spielregeln beachten
- **Figur starten**: Benötigt eine 6
- **Figur bewegen**: Um die gewürfelte Anzahl vorwärts
- **Gegner schlagen**: Landest du auf einem Feld mit gegnerischer Figur, geht diese zurück zum Start
- **Ziel erreichen**: Figur erreicht automatisch das Ziel, wenn die Zielposition erreicht wird

### Schritt 6: Zug beenden
- Klicke auf "Nächster Spieler" um den Zug zu beenden
- Der nächste Spieler ist nun an der Reihe

### Schritt 7: Spiel gewinnen
- Bringe alle 4 Figuren ins Ziel
- Ein Dialog zeigt den Gewinner an
- Starte ein neues Spiel über das Menü "Spiel" → "Neues Spiel"

## Klassendiagramm und Beziehungen

### Beziehungen zwischen Klassen

```
Game
├── Board (hat ein)
│   └── Field[] (hat viele)
│       └── Figure (hat 0 oder 1)
├── Dice (hat ein)
└── Player[] (hat viele)
    └── Figure[] (hat viele)
```

### Konzepte aus der objektorientierten Programmierung

#### 1. **Referenzen**
- `Figure` hat eine Referenz auf seinen `Player` (Besitzer)
- `Field` hat eine Referenz auf eine `Figure` (wenn besetzt)
- `Game` hat Referenzen auf `Board`, `Dice` und `Player[]`

#### 2. **Komposition**
- `Player` besitzt `Figure` Objekte (starke Beziehung)
- `Board` besitzt `Field` Objekte (starke Beziehung)

#### 3. **Aggregation**
- `Game` aggregiert `Player` Objekte (schwächere Beziehung)
- `Field` aggregiert `Figure` (kann wechseln)

#### 4. **Kapselung**
- Private Attribute mit public Getter/Setter
- Beispiel: `Figure.color` ist privat, Zugriff über `getColor()`

#### 5. **Verantwortlichkeiten**
- Jede Klasse hat eine klar definierte Aufgabe
- `Board` verwaltet nur Felder, nicht Spiellogik
- `Game` verwaltet Spielregeln und Ablauf
- `UI` Klassen trennen Darstellung von Logik (MVC-Prinzip)

## Erweiterungsmöglichkeiten

Das Programm kann erweitert werden durch:
1. **Speichern/Laden**: Spielstand speichern und laden
2. **Animationen**: Figuren animiert bewegen
3. **Sounds**: Würfelgeräusche, Siegesmusik
4. **Netzwerk-Modus**: Online gegen andere Spieler
5. **KI-Gegner**: Computer-Spieler implementieren
6. **Zusätzliche Regeln**: Spezialfelder, Bonuswürfe bei 6
7. **Statistiken**: Gewinnstatistiken anzeigen
8. **Themes**: Verschiedene visuelle Designs

## Technische Details

### Verwendete Java-Konzepte
- **Collections**: `ArrayList` für Listen von Spielern, Figuren, Feldern
- **Swing Components**: `JFrame`, `JPanel`, `JButton`, `JLabel`, `JTextArea`
- **Graphics**: `Graphics2D` für das Zeichnen des Spielbretts
- **Event Handling**: `ActionListener` für Button-Klicks
- **Layout Manager**: `BorderLayout`, `BoxLayout`, `GridLayout`

### Design Patterns
- **MVC (Model-View-Controller)**: Trennung von Daten (Game, Board, Player, etc.), Darstellung (GameUI, BoardPanel) und Steuerung
- **Observer Pattern** (implizit): UI reagiert auf Änderungen im Spielzustand

## Lizenz und Credits

Basiert auf den Konzepten von: https://inf-schule.de/oop/java/beziehungen/gohome/

Implementiert als Lernprojekt für objektorientierte Programmierung in Java.
