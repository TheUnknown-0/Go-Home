// Von: Moritz und Lennart
// Letzte Änderung: 18.01.2026
// Über die Klasse: Die Klasse Figur repräsentiert eine Spielfigur im Spiel "Go Home".

package gohome;

// Die Klasse Figur repräsentiert eine Spielfigur im Spiel "Go Home".
// Jede Figur hat eine Farbe und eine Position (x, y) auf dem 5x5-Spielfeld.
// Die Positionen sind 0-basiert, d.h. x und y reichen von 0 bis 4.
public class Figur {
    // Die Farbe der Figur (z.B. "blau" oder "rot").
    // Dient zur Identifikation, welchem Spieler die Figur gehört.
    public String farbe;

    // Die aktuelle X-Position der Figur auf dem Gitter (Spalte).
    // Wertebereich: 0 bis 4.
    public int x;

    // Die aktuelle Y-Position der Figur auf dem Gitter (Zeile).
    // Wertebereich: 0 bis 4 (0 ist oben, 4 ist unten).
    public int y;

    // Konstruktor zum Erstellen einer neuen Figur.
    //
    // @param farbe  Die Farbe der Figur.
    // @param startX Die Start-X-Position.
    // @param startY Die Start-Y-Position.
    public Figur(String farbe, int startX, int startY) {
        this.farbe = farbe;
        this.x = startX;
        this.y = startY;
    }

    // Überprüft, ob die Figur das Ziel erreicht hat.
    // Das Ziel befindet sich in der Mitte des 5x5-Feldes bei (2, 2).
    //
    // @return true, wenn die Figur auf Position (2, 2) steht, sonst false.
    public boolean gewonnen() {
        return x == 2 && y == 2;
    }

    // Bewegt die Figur einen Schritt in die angegebene Richtung.
    // Das Spielfeld verhält sich wie ein Torus: Wenn eine Figur über den Rand hinausgeht,
    // kommt sie auf der gegenüberliegenden Seite wieder herein.
    //
    // @param richtung Die Richtung der Bewegung:
    //                 0 = oben
    //                 1 = rechts
    //                 2 = unten
    //                 3 = links
    public void gehe(int richtung) {
        switch (richtung) {
            case 0: // Nach oben bewegen
                // (y - 1 + 5) % 5 sorgt dafür, dass bei y=0 der Wert zu 4 wird (Wrap-around)
                y = (y - 1 + 5) % 5;
                break;
            case 1: // Nach rechts bewegen
                // (x + 1) % 5 sorgt dafür, dass bei x=4 der Wert zu 0 wird
                x = (x + 1) % 5;
                break;
            case 2: // Nach unten bewegen
                y = (y + 1) % 5;
                break;
            case 3: // Nach links bewegen
                x = (x - 1 + 5) % 5;
                break;
        }
    }
}
