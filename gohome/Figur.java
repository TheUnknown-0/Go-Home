package gohome;

public class Figur {
    public String farbe;
    public int x;
    public int y;

    public Figur(String farbe, int startX, int startY) {
        this.farbe = farbe;
        this.x = startX;
        this.y = startY;
    }

    public boolean gewonnen() {
        return x == 2 && y == 2;
    }

    // richtung: 0=oben,1=rechts,2=unten,3=links
    public void gehe(int richtung) {
        switch (richtung) {
            case 0: y = (y - 1 + 5) % 5; break;
            case 1: x = (x + 1) % 5; break;
            case 2: y = (y + 1) % 5; break;
            case 3: x = (x - 1 + 5) % 5; break;
        }
    }
}
