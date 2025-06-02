package flappybird;

import javax.swing.*;
import java.awt.*;

public class MyPanel extends JPanel {

    private int score = 0;

    private final int frmWidth = 1000;
    private final int frmHeight = 600;
    private final int obsWidth = 90;
    private final int obsHeight = 250;
    private final int birdX = 300;
    private final int yTop = 0;
    private final int gap = 110; // vertical gap between top and bottom obstacles
    private final int spacing = 200; // horizontal spacing between obstacle pairs
    private final int maxX = 708;

    private boolean gameRunning = true;
    private final ImageIcon birdGif;
    private final ImageIcon background;

    // Obstacles hold their individual X positions
    private Obstacle obstacle1;
    private Obstacle obstacle2;
    private Obstacle obstacle3;
    private Obstacle obstacle4;
    private Obstacle obstacle5;
    private Obstacle obstacle6;
    private Obstacle obstacle7;
    private Obstacle obstacle8;
    private Obstacle obstacle9;
    private Obstacle obstacle10;

    public MyPanel() {
        birdGif = new ImageIcon("src/Assets/bird.gif");
        background = new ImageIcon("src/Assets/forest1.png");

        // Initialize obstacles with fixed initial X positions
        obstacle1 = new Obstacle(300, yTop, obsWidth, obsHeight, true);
        obstacle2 = new Obstacle(300, yTop + obsHeight + gap, obsWidth, obsHeight, false);
        obstacle3 = new Obstacle(500, yTop - 20, obsWidth, obsHeight, true);
        obstacle4 = new Obstacle(500, yTop - 20 + obsHeight + gap, obsWidth, obsHeight, false);
        obstacle5 = new Obstacle(700, yTop - 10, obsWidth, obsHeight, true);
        obstacle6 = new Obstacle(700, yTop - 10 + obsHeight + gap, obsWidth, obsHeight, false);
        obstacle7 = new Obstacle(900, yTop, obsWidth, obsHeight + 5, true);
        obstacle8 = new Obstacle(900, yTop + obsHeight + gap, obsWidth, obsHeight, false);
        obstacle9 = new Obstacle(1100, yTop, obsWidth, obsHeight, true);
        obstacle10 = new Obstacle(1100, yTop + 15 + obsHeight + gap, obsWidth, obsHeight, false);

        Timer timer = new Timer(20, e -> {
            if (!gameRunning) return;

            // Move obstacles left by 2 each tick
            moveObstacle(obstacle1, obstacle2);
            moveObstacle(obstacle3, obstacle4);
            moveObstacle(obstacle5, obstacle6);
            moveObstacle(obstacle7, obstacle8);
            moveObstacle(obstacle9, obstacle10);

            // Reposition off-screen obstacles to right of furthest obstacle pair
            repositionIfOffScreen();

            // Check if bird has passed obstacles for scoring
            checkPass(obstacle1);
            checkPass(obstacle3);
            checkPass(obstacle5);
            checkPass(obstacle7);
            checkPass(obstacle9);

            repaint();
        });
        timer.start();
    }




    private void moveObstacle(Obstacle top, Obstacle bottom) {
        top.setX(top.getX() - 2);
        bottom.setX(bottom.getX() - 2);
    }

    private void repositionIfOffScreen() {

    if (obstacle1.getX() + obsWidth < 0) {
        obstacle1.setX(maxX + spacing);
        obstacle2.setX(maxX + spacing);
        resetPassed(obstacle1, obstacle2);
        System.out.println("Repositioned obstacle1 and obstacle2 to: " + (maxX + spacing));
    }
    if (obstacle3.getX() + obsWidth < 0) {
        obstacle3.setX(maxX + spacing);
        obstacle4.setX(maxX + spacing);
        resetPassed(obstacle3, obstacle4);
        System.out.println("Repositioned obstacle3 and obstacle4 to: " + (maxX + spacing));
    }
    if (obstacle5.getX() + obsWidth < 0) {
        obstacle5.setX(maxX + spacing);
        obstacle6.setX(maxX + spacing);
        resetPassed(obstacle5, obstacle6);
        System.out.println("Repositioned obstacle5 and obstacle6 to: " + (maxX + spacing));
    }
    if (obstacle7.getX() + obsWidth < 0) {
        obstacle7.setX(maxX + spacing);
        obstacle8.setX(maxX + spacing);
        resetPassed(obstacle7, obstacle8);
        System.out.println("Repositioned obstacle7 and obstacle8 to: " + (maxX + spacing));
    }
    if (obstacle9.getX() + obsWidth < 0) {
        obstacle9.setX(maxX + spacing);
        obstacle10.setX(maxX + spacing);
        resetPassed(obstacle9, obstacle10);
        System.out.println("Repositioned obstacle9 and obstacle10 to: " + (maxX + spacing));
    }
}

    private void resetPassed(Obstacle top, Obstacle bottom) {
        top.setPassed(false);
        bottom.setPassed(false);
    }

    private void checkPass(Obstacle obs) {
        if (!obs.isPassed() && (obs.getX() + obs.getWidth()) < birdX - 60) {
            System.out.println("obX = "+ obs.getX());
            obs.setPassed(true);
            score++;
            System.out.println("Score: " + score);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int panelHeight = getHeight();
        int birdHeight = birdGif.getIconHeight();
        int yBird = (panelHeight - birdHeight) / 2 + 20;

        // Draw background and bird
        g.drawImage(background.getImage(), 0, 0, frmWidth, frmHeight, this);
        g.drawImage(birdGif.getImage(), 200, yBird, this);

        // Draw obstacles
        obstacle1.draw(g);
        obstacle2.draw(g);
        obstacle3.draw(g);
        obstacle4.draw(g);
        obstacle5.draw(g);
        obstacle6.draw(g);
        obstacle7.draw(g);
        obstacle8.draw(g);
        obstacle9.draw(g);
        obstacle10.draw(g);
    }
}

