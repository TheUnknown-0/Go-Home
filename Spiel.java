package gohome;

import java.util.Random;

public class Spiel {
    public Figur f1;
    public Figur f2;
    public Figur aktuelleFigur1;
    public Figur aktuelleFigur2;
    public Figur aktuellerSpieler;
    public String muenze1; // "blau" oder "rot"
    public String muenze2;
    private Random rand = new Random();

    public Spiel() {
        f1 = new Figur("blau", 0, 0);
        f2 = new Figur("rot", 4, 4);
        aktuellerSpieler = null;
    }

    // Starte das Spiel: wähle Startspieler zufällig und werfe die Münzen
    public void start() {
        aktuellerSpieler = rand.nextInt(2) == 0 ? f1 : f2;
        muenzenWerfen();
    }

    public void muenzenWerfen() {
        int z1 = rand.nextInt(2);
        int z2 = rand.nextInt(2);
        aktuelleFigur1 = (z1 == 0) ? f1 : f2;
        aktuelleFigur2 = (z2 == 0) ? f1 : f2;
        muenze1 = (z1 == 0) ? f1.farbe : f2.farbe;
        muenze2 = (z2 == 0) ? f1.farbe : f2.farbe;
    }

    public void nachObenBewegen() {
        aktuelleFigur1.gehe(0);
        aktuelleFigur2.gehe(0);
        neuenZugVorbereiten();
    }
    public void nachRechtsBewegen() {
        aktuelleFigur1.gehe(1);
        aktuelleFigur2.gehe(1);
        neuenZugVorbereiten();
    }
    public void nachUntenBewegen() {
        aktuelleFigur1.gehe(2);
        aktuelleFigur2.gehe(2);
        neuenZugVorbereiten();
    }
    public void nachLinksBewegen() {
        aktuelleFigur1.gehe(3);
        aktuelleFigur2.gehe(3);
        neuenZugVorbereiten();
    }

    private void setzeNeuenSpieler() {
        // einfacher Tausch, wer am Zug ist
        if (aktuellerSpieler == f1)
            aktuellerSpieler = f2;
        else
            aktuellerSpieler = f1;
    }

    public boolean spielIstFertig() {
        return f1.gewonnen() || f2.gewonnen();
    }

    public void neuenZugVorbereiten() {
        if (!spielIstFertig()) {
            setzeNeuenSpieler();
            muenzenWerfen();
        }
    }


    public String druckeGewinner() {
        boolean blauGewinnt = f1.gewonnen();
        boolean rotGewinnt = f2.gewonnen();
        if (blauGewinnt && rotGewinnt) {
            return "Beide haben gewonnen!"; // sollten beide gleichzeitig das Ziel erreichen
        }
        if (blauGewinnt) {
            return "Blau hat gewonnen!";
        }
        if (rotGewinnt) {
            return "Rot hat gewonnen!";
        }
        return "";
    }
/*
    public String getStatusString() {
        if (spielIstFertig()) {
            return druckeGewinner();
        }
        String aktueller = aktuellerSpieler.farbe;
        String m1 = muenze1;
        String m2 =  muenze2;
        String af1 = aktuelleFigur1.farbe;
        String af2 = aktuelleFigur2.farbe;
        return "Am Zug: " + aktueller + " | Münzen: " + m1 + ", " + m2 + " | Bewege: " + af1 + " & " + af2;
    }

 */
}
