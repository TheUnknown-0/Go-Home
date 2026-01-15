package gohome;

public class Figur {
    public String farbe; // Farbe, um Farbe des Spielers zu bestimmen
    public int x;   // Position auf der X achse
    public int y;   // -||- Y -||-

    public Figur(String farbe, int startX, int startY) {
        this.farbe = farbe;
        this.x = startX;
        this.y = startY;
    }

    public boolean gewonnen() {
        return x == 2 && y == 2; // Gewinnbedingung
    }

    // richtung: 0=oben,1=rechts,2=unten,3=links
    public void gehe(int richtung) {
        switch (richtung) {
            case 0: y = (y - 1 + 5) % 5; break; // mit dem Modulo verhindern, dass jemand außerhalb des Spiels ist
            case 1: x = (x + 1) % 5; break; // mit dem Modulo verhindern, dass jemand außerhalb des Spiels ist
            case 2: y = (y + 1) % 5; break; // mit dem Modulo verhindern, dass jemand außerhalb des Spiels ist
            case 3: x = (x - 1 + 5) % 5; break; // mit dem Modulo verhindern, dass jemand außerhalb des Spiels ist
        }
    }
}
