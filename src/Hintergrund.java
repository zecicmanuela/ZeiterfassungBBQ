import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class Hintergrund extends JPanel {
    private Image backgroundImage;

    public Hintergrund(String imagePath) {
        // Bild aus den Ressourcen laden
        try (InputStream is = getClass().getResourceAsStream(imagePath)) {
            if (is != null) {
                backgroundImage = ImageIO.read(is);
            } else {
                System.err.println("Bild nicht gefunden: " + imagePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            // Bild auf die Größe des Panels skalieren
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
