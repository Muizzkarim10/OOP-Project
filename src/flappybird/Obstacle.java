package flappybird;

import java.awt.*;
import javax.swing.*;

public class Obstacle {

    private int x;
    private final int y;
    private final int width;
    private final int height;
    private Image image;

    private boolean passed = false;

    public Obstacle(int x, int y, int width, int height, boolean isTop) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        String imagePath = isTop ? "src/Assets/up.png" : "src/Assets/ob.png";
        ImageIcon icon = new ImageIcon(imagePath);

        this.image = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }

    public void draw(Graphics g) {
        g.drawImage(image, x, y, null);
    }

    public void setX(int newX) {
        this.x = newX;
    }

    public int getX() {
        return x;
    }

    public int getWidth() {
        return width;
    }

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    public int getY() {
    return y;
    }

    public int getHeight() {
     return height;
    }
}


