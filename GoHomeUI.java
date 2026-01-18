// Von: Moritz und Lennart
// Letzte Änderung: 18.01.2026
// Die Klasse GoHomeUI stellt die grafische Benutzeroberfläche (GUI) für das Spiel "Go Home" bereit.

package gohome;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Arc2D;
import java.awt.geom.RoundRectangle2D;
import java.util.Random;

// Die Klasse GoHomeUI stellt die grafische Benutzeroberfläche (GUI) für das Spiel "Go Home" bereit.
// Sie verwendet Java Swing für die Fensterdarstellung.
// Die UI besteht aus:
// - Einem Hauptfenster (JFrame) mit Titel und Layout.
// - Einem BoardPanel in der Mitte, das das Spielbrett, die Figuren und Animationen zeichnet.
// - Einem Seitenpanel (rechts) für Spielinformationen (Münzen, aktueller Spieler) und Steuerungselemente.
// Die Klasse verknüpft die Logik der Klasse Spiel mit der visuellen Darstellung und
// verarbeitet Benutzereingaben (Mausklicks auf Buttons, Tastatureingaben).
public class GoHomeUI extends JFrame {

    // Referenz auf das zugrundeliegende Spiel-Logik-Objekt.
    // Beinhaltet den Zustand des Spiels (Positionen, Münzen, Spieler).
    private Spiel spiel;

    // Das Panel, das für das Zeichnen des Spielfelds (5x5 Gitter) zuständig ist.
    // Hier findet auch die Animation der Figuren statt.
    private BoardPanel boardPanel;

    // Das Panel, das die aktuellen Münzwürfe visualisiert.
    private CoinPanel coinPanel;

    // Das Panel, das anzeigt, welcher Spieler gerade am Zug ist.
    private PlayerPanel playerPanel;

    // Button zum Starten eines neuen Spiels.
    private JButton btnNewGame;

    // Button zum erneuten Werfen der Münzen.
    private JButton btnThrow;

    // Umschalter für den automatischen Modus (Bot-Spiel).
    // Ist aktuell im Code auskommentiert, aber vorbereitet.
    private JToggleButton btnAuto;

    // Timer für den automatischen Spielablauf (Bot).
    // Führt in regelmäßigen Abständen Züge aus, wenn aktiv.
    private Timer autoTimer;

    // Konstruktor der GUI.
    // Erstellt eine neue Spielinstanz, startet das Spiel logisch und baut dann die Oberfläche auf.
    public GoHomeUI() {
        // Initialisiere die Spiel-Logik
        spiel = new Spiel();
        spiel.start();

        // Baue die grafische Oberfläche auf
        initUI();

        // Aktualisiere die Anzeige initial
        updateUIFromGame();
    }

    // Initialisiert die Komponenten der Benutzeroberfläche.
    // Setzt Fenstereigenschaften (Titel, Größe), Layoutmanager und fügt die
    // einzelnen Panels (Board, Controls) hinzu.
    private void initUI() {
        setTitle("Go Home — Swing"); // Fenstertitel
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Programm beenden beim Schließen
        setMinimumSize(new Dimension(1000, 1000)); // Minimale Fenstergröße
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Starte im Vollbildmodus
        setLocationRelativeTo(null); // Zentriere das Fenster (falls nicht maximiert)

        JPanel content = new JPanel(new BorderLayout(12, 12));
        content.setBackground(new Color(245, 246, 250));
        content.setBorder(new EmptyBorder(12, 12, 12, 12));

        // Header-Bereich mit Titel erstellen
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel title = new JLabel("Go Home");
        title.setFont(new Font("SansSerif", Font.BOLD, 36));
        title.setForeground(new Color(34, 40, 49));
        JLabel subtitle = new JLabel("");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subtitle.setForeground(new Color(99, 102, 106));
        header.add(title, BorderLayout.WEST);
        header.add(subtitle, BorderLayout.SOUTH);
        content.add(header, BorderLayout.NORTH);

        // Zentrales Brett-Panel hinzufügen
        boardPanel = new BoardPanel();
        content.add(boardPanel, BorderLayout.CENTER);

        // Rechtes Steuerungs-Panel erstellen
        JPanel right = new JPanel();
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.setOpaque(false);
        right.setBorder(new EmptyBorder(6, 6, 6, 6));

        coinPanel = new CoinPanel();
        right.add(coinPanel);
        right.add(Box.createVerticalStrut(12));

        playerPanel = new PlayerPanel();
        right.add(playerPanel);
        right.add(Box.createVerticalStrut(8));

        // Button für neues Spiel
        btnNewGame = makeButton("Neues Spiel", new Color(34, 197, 94));
        btnNewGame.setFocusable(false);
        btnNewGame.setForeground(new Color(0, 0, 0));
        // ActionListener für neues Spiel registrieren
        btnNewGame.addActionListener(_ -> {
            // Sofern das Spiel noch läuft, Bestätigung erfordern
            if (!spiel.spielIstFertig()) {
                int ans = JOptionPane.showConfirmDialog(this, "Neues Spiel starten? Der aktuelle Spielstand geht verloren.", "Bestätigen", JOptionPane.YES_NO_OPTION);
                if (ans != JOptionPane.YES_OPTION) return;
            }
            // Neues Spiel initialisieren
            spiel = new Spiel();
            spiel.start();
            boardPanel.stopAnimationAndReset();
            updateUIFromGame();
        });
        right.add(btnNewGame);
        right.add(Box.createVerticalStrut(8));

        // Button für Münzen werfen
        btnThrow = makeButton("Münzen werfen", new Color(59, 130, 246));
        btnThrow.setForeground(new Color(0, 0, 0));
        btnThrow.addActionListener(_ -> {
            spiel.muenzenWerfen();
            updateUIFromGame();
        });
        right.add(btnThrow);
        right.add(Box.createVerticalStrut(8));

        content.add(right, BorderLayout.EAST);

        add(content);

        setupKeyBindings();

        autoTimer = new Timer(700, _ -> {
            if (spiel.spielIstFertig()) {
                stopAuto();
                return;
            }
            int dir = new Random().nextInt(4);
            performMove(dir);
        });
    }

    // Erstellt einen grafisch angepassten Button mit Hover-Effekt.
    //
    // @param text Der anzuzeigende Text auf dem Button.
    // @param bg   Die Hintergrundfarbe des Buttons.
    // @return Ein konfigurierter JButton.
    private JButton makeButton(String text, Color bg) {
        JButton b = new JButton(text);
        b.setAlignmentX(Component.LEFT_ALIGNMENT);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false); // Entfernt den Fokus-Rahmen beim Klicken
        b.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12)); // Innenabstand
        b.setOpaque(true);

        // Speichere die Basisfarbe für den Hover-Effekt
        b.putClientProperty("baseColor", bg);

        // Füge Maus-Listener für Hover-Effekt (Aufhellen) hinzu
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                b.setBackground(((Color) b.getClientProperty("baseColor")).brighter());
            }

            public void mouseExited(MouseEvent e) {
                b.setBackground((Color) b.getClientProperty("baseColor"));
            }
        });
        return b;
    }

    // Registriert Tastatureingaben (Pfeiltasten) für die Steuerung derFiguren.
    // Verwendet InputMap und ActionMap, um Tastenaktionen an Methoden zu binden.
    private void setupKeyBindings() {
        InputMap im = getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getRootPane().getActionMap();

        // Tasten zu Aktionsnamen zuordnen
        im.put(KeyStroke.getKeyStroke("UP"), "moveUp");
        im.put(KeyStroke.getKeyStroke("RIGHT"), "moveRight");
        im.put(KeyStroke.getKeyStroke("DOWN"), "moveDown");
        im.put(KeyStroke.getKeyStroke("LEFT"), "moveLeft");

        // Aktionsnamen zu Aktionen (Methodenaufrufen) zuordnen
        am.put("moveUp", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                performMove(0);
            }
        });
        am.put("moveRight", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                performMove(1);
            }
        });
        am.put("moveDown", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                performMove(2);
            }
        });
        am.put("moveLeft", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                performMove(3);
            }
        });
    }

    // Deaktiviert den automatischen Spielmodus (Bot) und gibt die Steuerung frei.
    private void stopAuto() {
        btnAuto.setSelected(false);
        autoTimer.stop();
        toggleControls();
    }

    // Aktiviert oder deaktiviert Buttons basierend auf dem Spielstatus.
    // (Hier vereinfacht implementiert, aktiviert sie immer).
    private void toggleControls() {
        btnNewGame.setEnabled(true);
        btnThrow.setEnabled(true);
    }

    // Führt einen Spielzug in die angegebene Richtung aus.
    // Ablauf:
    // 1. Prüfen, ob eine Animation läuft (Input ignorieren).
    // 2. Prüfen, ob das Spiel bereits beendet ist.
    // 3. Positionen VOR dem Zug speichern.
    // 4. Logische Bewegung im `spiel`-Objekt ausführen.
    // 5. Positionen NACH dem Zug vergleichen.
    // 6. Wenn sich etwas bewegt hat: Animation starten.
    //
    // @param richtung Die Bewegungsrichtung (0=oben, 1=rechts, 2=unten, 3=links).
    private void performMove(int richtung) {
        // Blockiere Eingaben während einer laufenden Animation
        if (boardPanel != null && boardPanel.isAnimating()) return;

        // Falls das Spiel vorbei ist, zeige den Gewinner
        if (spiel.spielIstFertig()) {
            showWinner();
            return;
        }

        // Snapshot: Positionen vor der Bewegung
        int f1xBefore = spiel.f1.x, f1yBefore = spiel.f1.y;
        int f2xBefore = spiel.f2.x, f2yBefore = spiel.f2.y;

        // Führe die Logik-Operation aus
        switch (richtung) {
            case 0:
                spiel.nachObenBewegen();
                break;
            case 1:
                spiel.nachRechtsBewegen();
                break;
            case 2:
                spiel.nachUntenBewegen();
                break;
            case 3:
                spiel.nachLinksBewegen();
                break;
        }

        // Snapshot: Positionen nach der Bewegung
        int f1xAfter = spiel.f1.x, f1yAfter = spiel.f1.y;
        int f2xAfter = spiel.f2.x, f2yAfter = spiel.f2.y;

        // Überprüfen, ob eine sichtbare Änderung stattgefunden hat
        boolean moved = (f1xBefore != f1xAfter || f1yBefore != f1yAfter ||
                         f2xBefore != f2xAfter || f2yBefore != f2yAfter);

        if (!moved) {
            // Keine Bewegung (z.B. theoretisch möglich, wenn Logik blockiert,
            // hier aber unwahrscheinlich wegen Wrap-Around).
            // Trotzdem UI aktualisieren (z.B. für Münz-Update).
            updateUIFromGame();
            if (spiel.spielIstFertig()) {
                showWinner();
                stopAuto();
            }
            return;
        }

        // Starte die grafische Animation von den alten zu den neuen Koordinaten
        boardPanel.animateMoves(f1xBefore, f1yBefore, f1xAfter, f1yAfter,
                f2xBefore, f2yBefore, f2xAfter, f2yAfter,
                () -> {
                    // Wird ausgeführt, wenn Animation fertig ist
                    updateUIFromGame();
                    if (spiel.spielIstFertig()) {
                        showWinner();
                        stopAuto();
                    }
                });
    }

    // Zeigt eine Meldung mit dem Gewinner an.
    private void showWinner() {
        String winner = spiel.druckeGewinner();
        JOptionPane.showMessageDialog(this, winner, "Gewinner", JOptionPane.INFORMATION_MESSAGE);
        boardPanel.repaint(); // Neuzeichnen, um Overlay anzuzeigen
    }

    // Aktualisiert alle UI-Komponenten (Repaint), um den aktuellen Spielzustand widerzuspiegeln.
    private void updateUIFromGame() {
        boardPanel.repaint();
        coinPanel.repaint();
        playerPanel.repaint();
        // spiel.spielIstFertig(); // Nur Aufruf ohne Effekt? Ggf. entfernen oder nutzen.
    }


    // -------------------------------------------------------------------------
    // Innere Klassen für spezifische UI-Komponenten
    // -------------------------------------------------------------------------

    // Das BoardPanel zeichnet das eigentliche Spielfeld und die Figuren.
    // Es handhabt auch die flüssige Animation der Bewegungen.
    private class BoardPanel extends JPanel {
        // Farbdefinitionen für das Design
        private final Color TILE = new Color(237, 242, 247);      // Helle Kachelfarbe
        private final Color HOME = new Color(250, 213, 102);      // Goldene Farbe für das Zielfeld
        private final Color GRID_LINE = new Color(200, 206, 214); // Gitterlinien
        private final Color BLUE = new Color(59, 130, 246);       // Spieler Blau
        private final Color RED = new Color(239, 68, 68);         // Spieler Rot

        // Animations-Variablen
        private Timer animTimer;
        private long animStart;
        private final int animDuration = 400; // Dauer einer Animation in Millisekunden

        // Flags, welche Figur sich gerade bewegt
        private boolean animateF1 = false;
        private boolean animateF2 = false;

        // Koordinaten für Animation: Start (s), Ende (e), Current (c)
        private double s1x, s1y, e1x, e1y, c1x, c1y; // Für Figur 1
        private double s2x, s2y, e2x, e2y, c2x, c2y; // Für Figur 2

        // Callback, der nach Ende der Animation ausgeführt wird
        private Runnable animCallback;

        /**
         * @return true, wenn gerade eine Animation läuft.
         */
        public boolean isAnimating() {
            return animTimer != null && animTimer.isRunning();
        }

        /**
         * Bricht laufende Animationen ab und setzt die Anzeige auf den harten Zustand des Spiels zurück.
         */
        public void stopAnimationAndReset() {
            if (animTimer != null) {
                animTimer.stop();
                animTimer = null;
            }
            animateF1 = false;
            animateF2 = false;

            // Setze aktuelle Zeichenposition auf echte Spielposition
            if (spiel != null && spiel.f1 != null && spiel.f2 != null) {
                c1x = spiel.f1.x;
                c1y = spiel.f1.y;
                c2x = spiel.f2.x;
                c2y = spiel.f2.y;
            } else {
                c1x = c1y = c2x = c2y = 0;
            }
            animCallback = null;
            repaint();
        }

        /**
         * Startet die Animation von einer alten Position zu einer neuen.
         *
         * @param f1sx Start X Figur 1
         * @param f1sy Start Y Figur 1
         * @param f1tx Ziel X Figur 1
         * @param f1ty Ziel Y Figur 1
         * @param f2sx Start X Figur 2
         * @param f2sy Start Y Figur 2
         * @param f2tx Ziel X Figur 2
         * @param f2ty Ziel Y Figur 2
         * @param onComplete Callback nach Abschluss
         */
        public void animateMoves(int f1sx, int f1sy, int f1tx, int f1ty,
                                 int f2sx, int f2sy, int f2tx, int f2ty,
                                 Runnable onComplete) {
            // Bestimmen, welche Figur sich überhaupt bewegt
            animateF1 = (f1sx != f1tx || f1sy != f1ty);
            animateF2 = (f2sx != f2tx || f2sy != f2ty);

            if (!animateF1 && !animateF2) {
                // Keine Bewegung notwendig
                if (onComplete != null) SwingUtilities.invokeLater(onComplete);
                return;
            }

            // Start- und Zielwerte setzen
            s1x = f1sx; s1y = f1sy; e1x = f1tx; e1y = f1ty;
            s2x = f2sx; s2y = f2sy; e2x = f2tx; e2y = f2ty;

            // Startwerte als aktuelle Werte initialisieren
            c1x = s1x; c1y = s1y; c2x = s2x; c2y = s2y;

            animCallback = onComplete;
            animStart = System.currentTimeMillis();

            // Bestehenden Timer stoppen
            if (animTimer != null && animTimer.isRunning()) animTimer.stop();

            // Neuer Timer für ca. 60 FPS (16ms)
            animTimer = new Timer(16, _ -> {
                long now = System.currentTimeMillis();
                // Berechne Fortschritt t von 0.0 bis 1.0
                double t = Math.min(1.0, (now - animStart) / (double) animDuration);

                // Anwenden einer Easing-Funktion für weichere Bewegung
                double et = ease(t);

                // Interpoliere Positionen
                if (animateF1) {
                    c1x = lerp(s1x, e1x, et);
                    c1y = lerp(s1y, e1y, et);
                }
                if (animateF2) {
                    c2x = lerp(s2x, e2x, et);
                    c2y = lerp(s2y, e2y, et);
                }

                repaint(); // Zeichne Frame

                // Animation beendet?
                if (t >= 1.0) {
                    animTimer.stop();
                    // Setze exakt auf Zielwerte (gegen Rundungsfehler)
                    if (animateF1) { c1x = e1x; c1y = e1y; }
                    if (animateF2) { c2x = e2x; c2y = e2y; }
                    repaint();
                    // Callback ausführen
                    if (animCallback != null) SwingUtilities.invokeLater(animCallback);
                }
            });
            animTimer.setCoalesce(true);
            animTimer.start();
        }

        /** Lineare Interpolation: a + (b-a)*t */
        private double lerp(double a, double b, double t) {
            return a + (b - a) * t;
        }

        /** Easing Funktion: Smoothstep (t^3 * (t * (t * 6 - 15) + 10)) */
        private double ease(double t) {
            return t * t * t * (t * (t * 6 - 15) + 10);
        }

        BoardPanel() {
            setPreferredSize(new Dimension(520, 520));
            setBackground(new Color(245, 246, 250));
            setOpaque(true);
            setBorder(new EmptyBorder(8, 8, 8, 8));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Hochwertiges Rendering aktivieren (Antialiasing)
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Hintergrund
            g2.setColor(getBackground());
            g2.fillRect(0, 0, getWidth(), getHeight());

            // Gitterberechnung
            int pad = 20;
            int w = getWidth() - pad * 2;
            int h = getHeight() - pad * 2;
            int GRID = 5;
            int cell = Math.min(w / GRID, h / GRID); // Kachelgröße
            int gridW = cell * GRID;
            int offsetX = (getWidth() - gridW) / 2;
            int offsetY = (getHeight() - gridW) / 2;

            // 1. Kacheln zeichnen
            for (int y = 0; y < GRID; y++) {
                for (int x = 0; x < GRID; x++) {
                    int rx = offsetX + x * cell;
                    int ry = offsetY + y * cell;
                    RoundRectangle2D rr = new RoundRectangle2D.Double(rx + 6, ry + 6, cell - 12, cell - 12, 12, 12);

                    // Zielkachel (Mitte) hervorheben, solange Spiel noch nicht fertig
                    if (x == 2 && y == 2 && !spiel.spielIstFertig()) {
                        g2.setColor(HOME);
                        g2.fill(rr);
                    } else {
                        g2.setColor(TILE);
                        g2.fill(rr);
                    }
                    g2.setColor(GRID_LINE);
                    g2.setStroke(new BasicStroke(2));
                    g2.draw(rr);
                }
            }

            // 2. Aktive Figuren (Hervorhebung)
            // Im Flat-Design (wie gewünscht) keine zusätzlichen Ringe oder Glanzeffekte um die aktive Figur.

            // 3. Figuren zeichnen
            // Nutzt interpolierte Koordinaten (c1x, c1y) während Animation, sonst echte Koordinaten
            int radius = (int) (cell * 0.36);
            double f1gx = animateF1 ? c1x : spiel.f1.x;
            double f1gy = animateF1 ? c1y : spiel.f1.y;
            double f2gx = animateF2 ? c2x : spiel.f2.x;
            double f2gy = animateF2 ? c2y : spiel.f2.y;

            // Pixel-Koordinaten berechnen
            int px1 = offsetX + (int) (f1gx * cell) + cell / 2;
            int py1 = offsetY + (int) (f1gy * cell) + cell / 2;
            int px2 = offsetX + (int) (f2gx * cell) + cell / 2;
            int py2 = offsetY + (int) (f2gy * cell) + cell / 2;

            // Spezialfall: Beide Figuren auf der exakt gleichen Position (Überlappung)
            // Zeichne beide als Halbkreise
            if (Math.abs(f1gx - f2gx) < 0.01 && Math.abs(f1gy - f2gy) < 0.01) {
                int r2 = (int) (cell * 0.34);
                // Linke Hälfte Blau
                g2.setColor(BLUE);
                g2.fill(new Arc2D.Double(px1 - r2, py1 - r2, r2 * 2, r2 * 2, 90, 180, Arc2D.PIE));
                // Rechte Hälfte Rot
                g2.setColor(RED);
                g2.fill(new Arc2D.Double(px1 - r2, py1 - r2, r2 * 2, r2 * 2, 270, 180, Arc2D.PIE));
                // Rahmen entfernt für Flat-Design
            } else {
                // Normalfall: Zeichne Blau
                g2.setColor(BLUE);
                g2.fillOval(px1 - radius, py1 - radius, radius * 2, radius * 2);
                // Glanzlicht entfernt für Flat-Design

                // Normalfall: Zeichne Rot
                g2.setColor(RED);
                g2.fillOval(px2 - radius, py2 - radius, radius * 2, radius * 2);
                // Glanzlicht entfernt für Flat-Design
            }

            // 4. Haus-Icon in der Mitte zeichnen
            if (!spiel.spielIstFertig()) {
                int cx = offsetX + 2 * cell + cell / 2;
                int cy = offsetY + 2 * cell + cell / 2;
                g2.setColor(new Color(36, 36, 36, 140));
                g2.setFont(new Font("SansSerif", Font.BOLD, cell / 3));
                FontMetrics fm = g2.getFontMetrics();
                String s = "🏠";
                int sw = fm.stringWidth(s);
                g2.drawString(s, cx - sw / 2, cy + fm.getAscent() / 2 - 4);
            }

            // 5. Gewinner-Overlay (Bildschirm abdimmen und Text anzeigen)
            if (spiel.spielIstFertig()) {
                String winnerText = spiel.druckeGewinner();
                // Farbe basierend auf Gewinner
                Color winnerColor = new Color(127, 0, 255); // Default Lila (Beide)
                if (spiel.f1.gewonnen() && !spiel.f2.gewonnen()) winnerColor = BLUE;
                if (spiel.f2.gewonnen() && !spiel.f1.gewonnen()) winnerColor = RED;

                // Dunkles Overlay
                g2.setColor(new Color(0, 0, 0, 160));
                g2.fillRect(offsetX, offsetY, gridW, gridW);

                // Hintergrund-Banner für Text
                g2.setColor(winnerColor);
                g2.fillRoundRect(offsetX + gridW / 8, offsetY + gridW / 2 - cell / 2 - 10, gridW * 3 / 4, cell, 16, 16);

                // Text
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("SansSerif", Font.BOLD, 36));
                FontMetrics fm = g2.getFontMetrics();
                int sw = fm.stringWidth(winnerText);
                int sx = offsetX + (gridW - sw) / 2;
                int sy = offsetY + gridW / 2 + fm.getAscent() / 2 - 4;
                g2.drawString(winnerText, sx, sy);
                // Kleine Feier-Punkte wurden für Klarheit entfernt
            }

            g2.dispose();
        }
    }

    // Panel zur Anzeige der beiden geworfenen Münzen.
    // Nutzt Farben (Blau/Rot) zur Visualisierung des Ergebnisses.
    private class CoinPanel extends JPanel {
        CoinPanel() {
            setPreferredSize(new Dimension(400, 90));
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            // Hintergrundbox (Karte)
            RoundRectangle2D card = new RoundRectangle2D.Double(4, 4, w - 8, h - 8, 12, 12);
            g2.setColor(new Color(255, 255, 255, 0)); // Transparent (oder soll es gefüllt sein?)
            // Im Original wars transparent mit grauem Rahmen.
            g2.fill(card);
            g2.setColor(new Color(220, 220, 220));
            g2.draw(card);

            int cx1 = (int) (w * 0.25); // X-Koordinate Münze 1
            int cx2 = (int) (w * 0.75); // X-Koordinate Münze 2
            int cy = (int) (h * 0.45);  // Y-Koordinate Mitte

            int r = (int) (h * 0.2); // Radius der Münzen

            Color blue = new Color(59, 130, 246);
            Color red = new Color(239, 68, 68);
            Color gray = new Color(200, 200, 200);

            // Farben Bestimmen
            Color c1 = gray;
            Color c2 = gray;
            if (spiel.muenze1 != null) c1 = spiel.muenze1.equals("blau") ? blue : red;
            if (spiel.muenze2 != null) c2 = spiel.muenze2.equals("blau") ? blue : red;

            // Zeichne Münze 1
            g2.setColor(c1);
            g2.fillOval(cx1 - r, cy - r, r * 2, r * 2);

            // Zeichne Münze 2
            g2.setColor(c2);
            g2.fillOval(cx2 - r, cy - r, r * 2, r * 2);

            // Text unter Münzen
            g2.setColor(new Color(80, 80, 80));
            g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
            g2.drawString(spiel.muenze1 == null ? "-" : spiel.muenze1, cx1 - 10, cy + r + 18);
            g2.drawString(spiel.muenze2 == null ? "-" : spiel.muenze2, cx2 - 10, cy + r + 18);

            g2.dispose();
        }
    }

    // Panel zur Anzeige des aktuellen Spielers.
    // Zeigt einen farbigen Kreis und "Am Zug: [Farbe]".
    private class PlayerPanel extends JPanel {
        PlayerPanel() {
            setPreferredSize(new Dimension(200, 44));
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int h = getHeight();
            int r = 12; // Radius Kreis

            String text = "Am Zug: " + (spiel.aktuellerSpieler == null ? "(nicht gesetzt)" : spiel.aktuellerSpieler.farbe);

            // Farbe bestimmen
            Color col = new Color(140, 140, 140); // Standard Grau
            if (spiel.aktuellerSpieler == spiel.f1) col = new Color(59, 130, 246);
            else if (spiel.aktuellerSpieler == spiel.f2) col = new Color(239, 68, 68);

            // Kreis zeichnen
            g2.setColor(col);
            g2.fillOval(8, (h - r * 2) / 2, r * 2, r * 2);

            // Text zeichnen
            g2.setColor(new Color(0, 0, 0, 140));
            g2.setFont(new Font("SansSerif", Font.BOLD, 14));
            g2.drawString(text, 36, (h + g2.getFontMetrics().getAscent()) / 2 - 3);

            g2.dispose();
        }
    }

    /**
     * Einstiegspunkt der Anwendung.
     * Setzt das LookAndFeel und startet die GUI im Event-Dispatch-Thread.
     */
    static void main() {
        SwingUtilities.invokeLater(() -> {
            try {
                // Versuche, das Look & Feel des Betriebssystems zu nutzen
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
                // Bei Fehler Standard-Java-Look verwenden, keine Ausgabe nötig
            }
            new GoHomeUI().setVisible(true);
        });
    }
}
