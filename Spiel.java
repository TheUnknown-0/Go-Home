// Von: Lennart und Moritz
// Letzte Änderung: 18.01.2026
// Über die Klasse: Die Klasse Spiel steuert die Logik des Spiels "Go Home".

package gohome;

import java.util.Random;

// Die Klasse Spiel steuert die Logik des Spiels "Go Home".
//
// Spielregeln:
// - Es gibt zwei Spieler/Figuren: "blau" (Start oben links) und "rot" (Start unten rechts).
// - Ziel ist es, das Feld in der Mitte (2, 2) zu erreichen.
// - Vor jedem Zug wird für zwei Münzen geworfen. Jede Münze zeigt "blau" oder "rot".
// - Wenn z.B. Münze 1 "blau" und Münze 2 "rot" zeigt, dann bewegt sich bei einem Zug
//   sowohl die blaue als auch die rote Figur. Zeigen beide Münzen "blau", bewegt sich
//   die blaue Figur zweimal (bzw. zwei Schritte, da beide "Slots" für Blau sind).
// - Die Bewegungsrichtung wird vom Spieler gewählt (oder zufällig bestimmt), gilt aber
//   für alle durch die Münzen aktivierten Figuren gleichzeitig.
public class Spiel {

    // Die blaue Figur, startet beim Spielstart auf Position (0, 0).
    public Figur f1;

    // Die rote Figur, startet beim Spielstart auf Position (4, 4).
    public Figur f2;

    // Referenz auf die Figur, die durch das Ergebnis der ersten Münze bewegt werden soll.
    public Figur aktuelleFigur1;

    // Referenz auf die Figur, die durch das Ergebnis der zweiten Münze bewegt werden soll.
    public Figur aktuelleFigur2;

    // Der Spieler, der aktuell an der Reihe ist, eine Entscheidung zu treffen.
    // Dient im aktuellen Code vor allem zur Anzeige ("Wer ist dran?").
    public Figur aktuellerSpieler;

    // Textuelle Darstellung der ersten Münze ("blau" oder "rot"). Dient der Anzeige.
    public String muenze1;

    // Textuelle Darstellung der zweiten Münze ("blau" oder "rot"). Dient der Anzeige.
    public String muenze2;

    // Zufallsgenerator für Münzwürfe und die Bestimmung des Startspielers.
    private final Random rand = new Random();

    // Standard-Konstruktor.
    // Initialisiert die Spielfiguren auf ihren Startpositionen:
    // - Blau bei (0,0)
    // - Rot bei (4,4)
    // Der aktuelle Spieler ist zu Beginn noch nicht gesetzt.
    public Spiel() {
        f1 = new Figur("blau", 0, 0);
        f2 = new Figur("rot", 4, 4);
        aktuellerSpieler = null;
    }

    // Startet eine neue Spielrunde.
    // 1. Bestimmt zufällig, welcher Spieler (Figur) beginnt.
    // 2. Führt den ersten Münzwurf durch, um die bewegbaren Figuren für den ersten Zug festzulegen.
    public void start() {
        // Zufällige Wahl: 0 -> f1 (blau), 1 -> f2 (rot)
        aktuellerSpieler = rand.nextInt(2) == 0 ? f1 : f2;
        muenzenWerfen();
    }

    // Simuliert das Werfen zweier Münzen.
    //
    // Für jede Münze wird per Zufall entschieden, ob sie "blau" (0) oder "rot" (1) zeigt.
    // Basierend darauf werden `aktuelleFigur1` und `aktuelleFigur2` gesetzt.
    // Diese Variablen bestimmen, welche Figuren beim nächsten Bewegungsbefehl verschoben werden.
    // Zusätzlich werden die Strings `muenze1` und `muenze2` für die Benutzeroberfläche aktualisiert.
    public void muenzenWerfen() {
        int z1 = rand.nextInt(2); // Zufallszahl 0 oder 1 für Münze 1
        int z2 = rand.nextInt(2); // Zufallszahl 0 oder 1 für Münze 2

        // Zuweisung der Figur basierend auf dem Wurfergebnis
        aktuelleFigur1 = (z1 == 0) ? f1 : f2;
        aktuelleFigur2 = (z2 == 0) ? f1 : f2;

        // Speichern der Farbe als String für die Anzeige
        muenze1 = (z1 == 0) ? f1.farbe : f2.farbe;
        muenze2 = (z2 == 0) ? f1.farbe : f2.farbe;
    }

    // Führt einen Spielzug nach OBEN durch.
    // Bewegt beide durch die Münzen bestimmten Figuren (`aktuelleFigur1` und `aktuelleFigur2`)
    // um ein Feld nach oben (Richtung 0).
    // Bereitet anschließend den nächsten Zug vor.
    public void nachObenBewegen() {
        aktuelleFigur1.gehe(0);
        aktuelleFigur2.gehe(0);
        neuenZugVorbereiten();
    }

    // Führt einen Spielzug nach RECHTS durch.
    // Bewegt beide durch die Münzen bestimmten Figuren um ein Feld nach rechts (Richtung 1).
    // Bereitet anschließend den nächsten Zug vor.
    public void nachRechtsBewegen() {
        aktuelleFigur1.gehe(1);
        aktuelleFigur2.gehe(1);
        neuenZugVorbereiten();
    }

    // Führt einen Spielzug nach UNTEN durch.
    // Bewegt beide durch die Münzen bestimmten Figuren um ein Feld nach unten (Richtung 2).
    // Bereitet anschließend den nächsten Zug vor.
    public void nachUntenBewegen() {
        aktuelleFigur1.gehe(2);
        aktuelleFigur2.gehe(2);
        neuenZugVorbereiten();
    }

    // Führt einen Spielzug nach LINKS durch.
    // Bewegt beide durch die Münzen bestimmten Figuren um ein Feld nach links (Richtung 3).
    // Bereitet anschließend den nächsten Zug vor.
    public void nachLinksBewegen() {
        aktuelleFigur1.gehe(3);
        aktuelleFigur2.gehe(3);
        neuenZugVorbereiten();
    }

    // Wechselt den aktiven Spieler für die nächste Runde.
    // Blau wird zu Rot und umgekehrt.
    // Dies ist eine interne Hilfsmethode.
    private void setzeNeuenSpieler() {
        if (aktuellerSpieler == f1) {
            aktuellerSpieler = f2;
        } else {
            aktuellerSpieler = f1;
        }
    }

    // Überprüft den aktuellen Spielstatus auf ein Spielende.
    // @return true, wenn mindestens eine der beiden Figuren das Gewinnfeld (2, 2) erreicht hat.
    public boolean spielIstFertig() {
        return f1.gewonnen() || f2.gewonnen();
    }

    // Schließt den aktuellen Zug ab und bereitet den nächsten vor.
    //
    // Überprüft zuerst, ob das Spiel bereits gewonnen wurde. Wenn nicht,
    // wird der Spieler gewechselt und es werden neue Münzen geworfen,
    // um die Bedingungen für den nächsten Zug festzulegen.
    public void neuenZugVorbereiten() {
        if (!spielIstFertig()) {
            setzeNeuenSpieler();
            muenzenWerfen();
        }
    }

    // Erzeugt einen String, der den Gewinner des Spiels bekanntgibt.
    //
    // @return Ein String wie "Blau hat gewonnen!", "Rot hat gewonnen!",
    //         "Beide haben gewonnen!" oder ein leerer String, falls noch niemand gewonnen hat.
    public String druckeGewinner() {
        boolean blauGewinnt = f1.gewonnen();
        boolean rotGewinnt = f2.gewonnen();

        if (blauGewinnt && rotGewinnt) {
            return "Beide haben gewonnen!"; // Fall: Beide erreichen gleichzeitig das Ziel
        }
        if (blauGewinnt) {
            return "Blau hat gewonnen!";
        }
        if (rotGewinnt) {
            return "Rot hat gewonnen!";
        }
        return "";
    }
}
