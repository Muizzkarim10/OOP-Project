package flappybird;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.LineUnavailableException;


public class MyPanel extends JPanel {

    private int score = 0;

    private final int frmWidth = 1000;
    private final int frmHeight = 600;
    private final int obsWidth = 90;
    private final int obsHeight = 250;
    private final int yTop = 0;
    private final int gap = 110; // vertical gap
    private final int spacing = 200; // horizontal spacing
    private final int maxX = 708;

    private final int birdX = 190;
    private int birdY = 280; // changed from final to mutable
    private final int birdWidth = 40;
    private final int birdHeight = 40;

    private boolean gameRunning = true;
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

    private JPanel gameOverPanel;
    private JButton retryButton;
    private JLabel scoreLabel;
    private JLabel highScoreLabel;

    private JPanel startPanel;
    private JButton startButton;
    private boolean gameStarted = false;
    private Timer timer;

    private double velocity = 0;
    private final double gravity = 0.2;
    private final double jumpStrength = -2;

    private int obstacleSpeed = 2;

    private String playerName = "Player";

    public MyPanel() {
        setPreferredSize(new Dimension(frmWidth, frmHeight));
        birdGif = new ImageIcon("src/Assets/bird.gif");
        background = new ImageIcon("src/Assets/forest1.png");

        // Initialize obstacles with fixed initial X positions
        obstacle1 = new Obstacle(300, yTop, obsWidth, obsHeight, true);
        obstacle2 = new Obstacle(300, yTop + obsHeight + gap, obsWidth, obsHeight, false);
        obstacle3 = new Obstacle(500, yTop - 35, obsWidth, obsHeight, true);
        obstacle4 = new Obstacle(500, yTop - 40 + obsHeight + gap, obsWidth, obsHeight, false);
        obstacle5 = new Obstacle(700, yTop, obsWidth, obsHeight, true);
        obstacle6 = new Obstacle(700, yTop - 10 + obsHeight + gap, obsWidth, obsHeight, false);
        obstacle7 = new Obstacle(900, yTop, obsWidth, obsHeight + 15, true);
        obstacle8 = new Obstacle(900, yTop + obsHeight + gap, obsWidth, obsHeight, false);
        obstacle9 = new Obstacle(1100, yTop - 5, obsWidth, obsHeight, true);
        obstacle10 = new Obstacle(1100, yTop + 15 + obsHeight + gap, obsWidth, obsHeight, false);

        // START MENU
        startPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(new Color(0, 0, 0, 180));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            }
        };
        startPanel.setOpaque(false);
        startPanel.setBounds(300, 180, 400, 240);
        startPanel.setLayout(new BoxLayout(startPanel, BoxLayout.Y_AXIS));
        startPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Flappy Bird");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        startButton = new JButton("Start");
        startButton.setPreferredSize(new Dimension(200, 50));
        startButton.setFont(new Font("Arial", Font.BOLD, 20));
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        // startButton.setFocusPainted(false);
        startButton.addActionListener(e -> {
            playerName = JOptionPane.showInputDialog(this, "Enter your name:");
            if (playerName == null || playerName.trim().isEmpty()) {
                playerName = "Player";
            }

            startPanel.setVisible(false);
            gameStarted = true;
            timer.start();
            requestFocusInWindow(); // allow key press
        });

        startPanel.add(titleLabel);
        startPanel.add(Box.createVerticalStrut(30));
        startPanel.add(startButton);
        add(startPanel);

        // GAME OVER LAYOUT
        setLayout(null);

        gameOverPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Rounded semi-transparent background
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(new Color(0, 0, 0, 180));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            }
        };
        gameOverPanel.setOpaque(false); // Needed for custom painting
        gameOverPanel.setSize(400, 240);
        gameOverPanel.setLocation((frmWidth - 400) / 2, (frmHeight - 240) / 2);
        gameOverPanel.setBounds(300, 180, 400, 240);
        gameOverPanel.setLayout(new BoxLayout(gameOverPanel, BoxLayout.Y_AXIS));
        gameOverPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Padding inside panel

        // Game Over label
        JLabel gameOverLabel = new JLabel("Game Over");
        gameOverLabel.setForeground(Color.WHITE);
        gameOverLabel.setFont(new Font("Arial", Font.BOLD, 36));
        gameOverLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Score label
        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setForeground(Color.LIGHT_GRAY);
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // High Score label
        highScoreLabel = new JLabel("High Score: 0");
        highScoreLabel.setForeground(Color.YELLOW);
        highScoreLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        highScoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Retry button
        retryButton = new JButton("Retry");
        retryButton.setPreferredSize(new Dimension(200, 50)); // width x height
        retryButton.setMargin(new Insets(10, 20, 10, 20)); // top, left, bottom, right
        retryButton.setFont(new Font("Arial", Font.BOLD, 20));
        retryButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        retryButton.addActionListener(e -> restartGame());

        // Add components
        gameOverPanel.add(gameOverLabel);
        gameOverPanel.add(Box.createVerticalStrut(10));
        gameOverPanel.add(scoreLabel);
        gameOverPanel.add(Box.createVerticalStrut(20));
        gameOverPanel.add(highScoreLabel);
        gameOverPanel.add(Box.createVerticalStrut(20));
        gameOverPanel.add(retryButton);
        gameOverPanel.setVisible(false);

        add(gameOverPanel);  

        setFocusable(true);
        requestFocusInWindow();  
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE && gameRunning) {
                    velocity = jumpStrength;
                    playSound("src/Assets/jump.wav");
                }
            }
        });

        timer = new Timer(20, e -> {
            if (!gameRunning || !gameStarted)
                return;
            velocity += gravity;
            birdY += (int) velocity;

            // Clamp birdY within screen bounds
            if (birdY + birdHeight >= frmHeight) {
                birdY = frmHeight - birdHeight;
                gameRunning = false;
                scoreLabel.setText("Score: " + score);
                gameOverPanel.setVisible(true);
            }
            if (birdY < 0) {
                birdY = 0;
                velocity = 0;
            }

            // Move obstacles left by 2 each tick
            moveObstacle(obstacle1, obstacle2);
            moveObstacle(obstacle3, obstacle4);
            moveObstacle(obstacle5, obstacle6);
            moveObstacle(obstacle7, obstacle8);
            moveObstacle(obstacle9, obstacle10);

            repositionIfOffScreen();

            checkPass(obstacle1);
            checkPass(obstacle3);
            checkPass(obstacle5);
            checkPass(obstacle7);
            checkPass(obstacle9);

            if (checkCollision()) {
                gameRunning = false;
                System.out.println("Collision Detected! Game Over.");
                scoreLabel.setText("Score: " + score);
                gameOverPanel.setVisible(true);

                playSound("src/Assets/die.wav");

                FileHandling fh = new FileHandling();
                fh.saveScore(playerName, score);
                int highScore = fh.getHighScore();
                scoreLabel.setText("Score: " + score);
                highScoreLabel.setText("High Score: " + highScore);
            }

            repaint();
        });
        timer.start();
    }

    private boolean checkCollision() {
        Rectangle birdRect = new Rectangle(birdX, birdY, birdWidth, birdHeight);

        Obstacle[] allObstacles = {
                obstacle1, obstacle2, obstacle3, obstacle4, obstacle5,
                obstacle6, obstacle7, obstacle8, obstacle9, obstacle10
        };

        for (Obstacle obs : allObstacles) {
            Rectangle obsRect = new Rectangle(obs.getX(), obs.getY(), obs.getWidth(), obs.getHeight());
            if (birdRect.intersects(obsRect)) {
                return true;
            }
        }
        return false;
    }

    private void moveObstacle(Obstacle top, Obstacle bottom) {
        top.setX(top.getX() - obstacleSpeed);
        bottom.setX(bottom.getX() - obstacleSpeed);
    }

    private void repositionIfOffScreen() {

        if (obstacle1.getX() + obsWidth < 0) {
            obstacle1.setX(maxX + spacing);
            obstacle2.setX(maxX + spacing);
            resetPassed(obstacle1, obstacle2);
        }
        if (obstacle3.getX() + obsWidth < 0) {
            obstacle3.setX(maxX + spacing);
            obstacle4.setX(maxX + spacing);
            resetPassed(obstacle3, obstacle4);
        }
        if (obstacle5.getX() + obsWidth < 0) {
            obstacle5.setX(maxX + spacing);
            obstacle6.setX(maxX + spacing);
            resetPassed(obstacle5, obstacle6);
        }
        if (obstacle7.getX() + obsWidth < 0) {
            obstacle7.setX(maxX + spacing);
            obstacle8.setX(maxX + spacing);
            resetPassed(obstacle7, obstacle8);
        }
        if (obstacle9.getX() + obsWidth < 0) {
            obstacle9.setX(maxX + spacing);
            obstacle10.setX(maxX + spacing);
            resetPassed(obstacle9, obstacle10);
        }
    }

    private void resetPassed(Obstacle top, Obstacle bottom) {
        top.setPassed(false);
        bottom.setPassed(false);
    }

    private void checkPass(Obstacle obs) {
        if (!obs.isPassed() && (obs.getX() + obs.getWidth()) < birdX) {
            System.out.println("obX = " + obs.getX());
            obs.setPassed(true);
            score++;

            if (score % 5 == 0) {
                obstacleSpeed++; 
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw background and bird
        g.drawImage(background.getImage(), 0, 0, frmWidth, frmHeight, this);
        g.drawImage(birdGif.getImage(), birdX, birdY, 70, 70, this);

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

        // Draw score box
        Graphics2D g2 = (Graphics2D) g;
        int boxWidth = 120;
        int boxHeight = 40;

        g2.setColor(new Color(0, 0, 0, 150)); // semi-transparent black background
        g2.fillRoundRect(10, 10, boxWidth, boxHeight, 15, 15);

        g2.setColor(Color.WHITE); // white border
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(10, 10, boxWidth, boxHeight, 15, 15);

        g2.setFont(new Font("Arial", Font.BOLD, 18));
        g2.drawString("Score: " + score, 20, 37); // Adjust text position
    }

    private void restartGame() {
        // Reset bird and game state
        score = 0;
        gameRunning = true;
        velocity = 0;
        obstacleSpeed = 2;
        gameOverPanel.setVisible(false);

        birdY = 280;

        // Reset all obstacle positions and passed flags
        obstacle1.setX(300);
        obstacle2.setX(300);
        obstacle3.setX(500);
        obstacle4.setX(500);
        obstacle5.setX(700);
        obstacle6.setX(700);
        obstacle7.setX(900);
        obstacle8.setX(900);
        obstacle9.setX(1100);
        obstacle10.setX(1100);

        resetPassed(obstacle1, obstacle2);
        resetPassed(obstacle3, obstacle4);
        resetPassed(obstacle5, obstacle6);
        resetPassed(obstacle7, obstacle8);
        resetPassed(obstacle9, obstacle10);

        repaint();
    }

    public void playSound(String filePath) {
        try {
            File soundFile = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}
