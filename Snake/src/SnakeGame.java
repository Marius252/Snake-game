import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {
    private final int WIDTH = 600, HEIGHT = 600;
    private final int BLOCK_SIZE = 20;
    private final int NUM_BLOCKS_X = WIDTH / BLOCK_SIZE;
    private final int NUM_BLOCKS_Y = HEIGHT / BLOCK_SIZE;
    private final int INITIAL_LENGTH = 3;

    private LinkedList<Point> snake;
    private Point food;
    private boolean isGameOver;
    private int direction;
    private Timer gameTimer;

    public SnakeGame() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);

        snake = new LinkedList<>();
        for (int i = 0; i < INITIAL_LENGTH; i++) {
            snake.add(new Point(NUM_BLOCKS_X / 2 - i, NUM_BLOCKS_Y / 2));
        }

        spawnFood();
        isGameOver = false;
        direction = 2;

        gameTimer = new Timer(100, this); // Refresh every 100 ms
        gameTimer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (isGameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("Game Over!", WIDTH / 4, HEIGHT / 2);
        } else {
            g.setColor(Color.GREEN);
            for (Point p : snake) {
                g.fillRect(p.x * BLOCK_SIZE, p.y * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
            }

            g.setColor(Color.RED);
            g.fillRect(food.x * BLOCK_SIZE, food.y * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (isGameOver) {
            return;
        }

        Point head = snake.getFirst();
        Point newHead = null;

        switch (direction) {
            case 0: newHead = new Point(head.x - 1, head.y); break; // Left
            case 1: newHead = new Point(head.x, head.y - 1); break; // Up
            case 2: newHead = new Point(head.x + 1, head.y); break; // Right
            case 3: newHead = new Point(head.x, head.y + 1); break; // Down
        }

        if (newHead.x < 0 || newHead.x >= NUM_BLOCKS_X || newHead.y < 0 || newHead.y >= NUM_BLOCKS_Y || snake.contains(newHead)) {
            isGameOver = true;
            gameTimer.stop();
            repaint();
            return;
        }

        snake.addFirst(newHead);

        if (newHead.equals(food)) {
            spawnFood();
        } else {
            snake.removeLast();
        }

        repaint();
    }

    private void spawnFood() {
        Random rand = new Random();
        food = new Point(rand.nextInt(NUM_BLOCKS_X), rand.nextInt(NUM_BLOCKS_Y));

        while (snake.contains(food)) {
            food = new Point(rand.nextInt(NUM_BLOCKS_X), rand.nextInt(NUM_BLOCKS_Y));
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int newDirection = direction;

        if (e.getKeyCode() == KeyEvent.VK_LEFT && direction != 2) newDirection = 0;
        if (e.getKeyCode() == KeyEvent.VK_UP && direction != 3) newDirection = 1;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT && direction != 0) newDirection = 2;
        if (e.getKeyCode() == KeyEvent.VK_DOWN && direction != 1) newDirection = 3;

        direction = newDirection;
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        SnakeGame game = new SnakeGame();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
