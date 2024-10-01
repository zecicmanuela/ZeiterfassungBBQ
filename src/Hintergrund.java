import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Hintergrund extends JPanel {
    private Image backgroundImage;

    public Hintergrund (String filePath) {
        try {
            backgroundImage = ImageIO.read(new File("src/ressourcen/hintergrundBBQ-3.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}