package flappybird;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("FlappyBird");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);

        MyPanel Panel = new MyPanel();
        frame.add(Panel);
        frame.setVisible(true);
    }
}