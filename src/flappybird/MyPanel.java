package flappybird;

import javax.swing.*;
import java.awt.*;

public class MyPanel extends JPanel {

    private int score = 0;

    private int frmWidth = 1200;
    private int frmHeight = 700;
    private int obsWidth = 90;
    private int obsHeight = 250;
    private int obsX = 300;
    private int yTop = 0;
    private int gap = 110; // change this to control vertical space between top and bottom pipes
    private int spacing = 200; // horizontal spacing between each pair

    
    private boolean gameRunnig = true;
    private final ImageIcon birdGif;
    private final ImageIcon background;


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

    private void checkPass(Obstacle obs) {
        int birdX = 300;  // bird's fixed horizontal position

        if (!obs.isPassed() && (obs.getX() + obs.getWidth()) < birdX) {
            obs.setPassed(true);
            score++;
            System.out.println("Score: " + score);
        }
    }


    public MyPanel() {
        birdGif = new ImageIcon("src/Assets/bird.gif");
        background = new ImageIcon("src/Assets/forest1.png");

        obstacle1 = new Obstacle(obsX, yTop, obsWidth, obsHeight, true);
        obstacle2 = new Obstacle(obsX, yTop + obsHeight + gap, obsWidth, obsHeight, false);

        obstacle3 = new Obstacle(obsX + 200, yTop - 20, obsWidth, obsHeight, true);
        obstacle4 = new Obstacle(obsX + 200, yTop - 20 + obsHeight + gap, obsWidth, obsHeight, false);

        obstacle5 = new Obstacle(obsX + 400, yTop - 10, obsWidth, obsHeight, true);
        obstacle6 = new Obstacle(obsX + 400, yTop - 10 + obsHeight + gap, obsWidth, obsHeight, false);

        obstacle7 = new Obstacle(obsX + 600, yTop, obsWidth, obsHeight + 5, true);
        obstacle8 = new Obstacle(obsX + 600, yTop + obsHeight + gap, obsWidth, obsHeight, false);

        obstacle9 = new Obstacle(obsX + 800, yTop, obsWidth, obsHeight, true);
        obstacle10 = new Obstacle(obsX + 800, yTop + 15 + obsHeight + gap, obsWidth, obsHeight, false);


        Timer timer = new Timer(20, e -> {
            obsX -= 2;

            obstacle1.setX(obsX);
            obstacle2.setX(obsX);
            obstacle3.setX(obsX + 200);
            obstacle4.setX(obsX + 200);
            obstacle5.setX(obsX + 400);
            obstacle6.setX(obsX + 400);
            obstacle7.setX(obsX + 600);
            obstacle8.setX(obsX + 600);
            obstacle9.setX(obsX + 800);
            obstacle10.setX(obsX + 800);

           if(gameRunnig){
               int lastX = obsX + 800;
               // Reposition obstacles when off screen and update lastX
               if (obstacle1.getX() + obsWidth < 0) {
                   lastX += spacing;
                   obstacle1.setX(lastX);
                   obstacle2.setX(lastX);
                   obstacle1.setPassed(false);
               }
               if (obstacle3.getX() + obsWidth < 0) {
                   lastX += spacing;
                   obstacle3.setX(lastX);
                   obstacle4.setX(lastX);
                   obstacle3.setPassed(false);
               }
               if (obstacle5.getX() + obsWidth < 0) {
                   lastX += spacing;
                   obstacle5.setX(lastX);
                   obstacle6.setX(lastX);
                   obstacle5.setPassed(false);
               }
               if (obstacle7.getX() + obsWidth < 0) {
                   lastX += spacing;
                   obstacle7.setX(lastX);
                   obstacle8.setX(lastX);
                   obstacle7.setPassed(false);
               }
               if (obstacle9.getX() + obsWidth < 0) {
                   lastX += spacing;
                   obstacle9.setX(lastX);
                   obstacle10.setX(lastX);
                   obstacle9.setPassed(false);
               }

               checkPass(obstacle1);
               checkPass(obstacle3);
               checkPass(obstacle5);
               checkPass(obstacle7);
               checkPass(obstacle9);

               repaint();
           }
        });
        timer.start();


    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int panelHeight = getHeight();
        int imageHeight = birdGif.getIconHeight();
        int y = ((panelHeight - imageHeight) / 2) + 20;

        g.drawImage(background.getImage(), 0, 0, frmWidth, frmHeight, this);
        g.drawImage(birdGif.getImage(), 200, y, this);

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