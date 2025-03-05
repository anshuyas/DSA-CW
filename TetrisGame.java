import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TetrisGame extends JFrame {

    // Constants
    private static final int BOARD_WIDTH = 10;
    private static final int BOARD_HEIGHT = 20;
    private static final int CELL_SIZE = 30;
    private static final int PREVIEW_WIDTH = 4;
    private static final int PREVIEW_HEIGHT = 4;

    // Game state
    private int[][] gameBoard = new int[BOARD_HEIGHT][BOARD_WIDTH]; // 0 = empty, 1 = filled
    private Stack<int[][]> boardStack = new Stack<>(); // Stack to represent the game board
    private Queue<Block> blockQueue = new LinkedList<>(); // Queue for falling blocks
    private Block currentBlock;
    private Block nextBlock;
    private int score = 0;
    private boolean gameOver = false;

    // GUI components
    private JPanel gamePanel;
    private JPanel previewPanel;
    private JLabel scoreLabel;

    public TetrisGame() {
        // Initialize the game
        initializeGame();

        // Set up the GUI
        setTitle("Tetris Game");
        setSize(BOARD_WIDTH * CELL_SIZE + 200, BOARD_HEIGHT * CELL_SIZE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Game board panel
        gamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawGameBoard(g);
                if (currentBlock != null) {
                    drawBlock(g, currentBlock);
                }
            }
        };
        gamePanel.setPreferredSize(new Dimension(BOARD_WIDTH * CELL_SIZE, BOARD_HEIGHT * CELL_SIZE));
        add(gamePanel, BorderLayout.CENTER);

        // Preview panel
        previewPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawPreview(g);
            }
        };
        previewPanel.setPreferredSize(new Dimension(PREVIEW_WIDTH * CELL_SIZE, PREVIEW_HEIGHT * CELL_SIZE));
        add(previewPanel, BorderLayout.EAST);

        // Score label
        scoreLabel = new JLabel("Score: 0");
        add(scoreLabel, BorderLayout.NORTH);

        // Buttons for user input
        JPanel buttonPanel = new JPanel();
        JButton leftButton = new JButton("Left");
        JButton rightButton = new JButton("Right");
        JButton rotateButton = new JButton("Rotate");
        buttonPanel.add(leftButton);
        buttonPanel.add(rightButton);
        buttonPanel.add(rotateButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Button listeners
        leftButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveBlockLeft();
            }
        });

        rightButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveBlockRight();
            }
        });

        rotateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rotateBlock();
            }
        });

        // Start the game loop
        startGameLoop();
    }

    // Initialize the game
    private void initializeGame() {
        // Clear the game board
        for (int i = 0; i < BOARD_HEIGHT; i++) {
            Arrays.fill(gameBoard[i], 0);
        }

        // Initialize the block queue
        blockQueue.clear();
        generateNewBlock();
        generateNewBlock();

        // Set the current and next blocks
        currentBlock = blockQueue.poll();
        nextBlock = blockQueue.peek();

        // Initialize the stack
        boardStack.clear();
        boardStack.push(gameBoard);

        // Reset score and game over state
        score = 0;
        gameOver = false;
    }

    // Generate a new random block and enqueue it
    private void generateNewBlock() {
        Random random = new Random();
        int blockType = random.nextInt(7); // 7 types of Tetris blocks
        Block block = new Block(blockType);
        blockQueue.add(block);
    }

    // Draw the game board
    private void drawGameBoard(Graphics g) {
        for (int i = 0; i < BOARD_HEIGHT; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                if (gameBoard[i][j] == 1) {
                    g.setColor(Color.BLUE);
                    g.fillRect(j * CELL_SIZE, i * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                }
                g.setColor(Color.BLACK);
                g.drawRect(j * CELL_SIZE, i * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }
    }

    // Draw the current block
    private void drawBlock(Graphics g, Block block) {
        int[][] shape = block.getShape();
        int x = block.getX();
        int y = block.getY();
        g.setColor(block.getColor());
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                if (shape[i][j] == 1) {
                    g.fillRect((x + j) * CELL_SIZE, (y + i) * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                }
            }
        }
    }

    // Draw the preview area
    private void drawPreview(Graphics g) {
        if (nextBlock != null) {
            int[][] shape = nextBlock.getShape();
            g.setColor(nextBlock.getColor());
            for (int i = 0; i < shape.length; i++) {
                for (int j = 0; j < shape[i].length; j++) {
                    if (shape[i][j] == 1) {
                        g.fillRect(j * CELL_SIZE, i * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                    }
                }
            }
        }
    }

    // Move the block left
    private void moveBlockLeft() {
        if (currentBlock != null && !gameOver) {
            currentBlock.moveLeft();
            if (checkCollision(currentBlock)) {
                currentBlock.moveRight(); // Undo the move if there's a collision
            }
            gamePanel.repaint();
        }
    }

    // Move the block right
    private void moveBlockRight() {
        if (currentBlock != null && !gameOver) {
            currentBlock.moveRight();
            if (checkCollision(currentBlock)) {
                currentBlock.moveLeft(); // Undo the move if there's a collision
            }
            gamePanel.repaint();
        }
    }

    // Rotate the block
    private void rotateBlock() {
        if (currentBlock != null && !gameOver) {
            currentBlock.rotate();
            if (checkCollision(currentBlock)) {
                currentBlock.rotateBack(); // Undo the rotation if there's a collision
            }
            gamePanel.repaint();
        }
    }

    // Check for collision
    private boolean checkCollision(Block block) {
        int[][] shape = block.getShape();
        int x = block.getX();
        int y = block.getY();
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                if (shape[i][j] == 1) {
                    int newX = x + j;
                    int newY = y + i;
                    if (newX < 0 || newX >= BOARD_WIDTH || newY >= BOARD_HEIGHT || (newY >= 0 && gameBoard[newY][newX] == 1)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // Start the game loop
    private void startGameLoop() {
        new Thread(() -> {
            while (!gameOver) {
                try {
                    Thread.sleep(500); // Adjust the speed of the game
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (currentBlock != null) {
                    currentBlock.moveDown();
                    if (checkCollision(currentBlock)) {
                        currentBlock.moveUp(); // Undo the move
                        placeBlock(); // Place the block on the board
                        checkCompletedRows(); // Check for completed rows
                        currentBlock = blockQueue.poll(); // Get the next block
                        generateNewBlock(); // Generate a new block
                        nextBlock = blockQueue.peek();
                        if (checkCollision(currentBlock)) {
                            gameOver = true; // Game over if the new block collides
                        }
                    }
                    gamePanel.repaint();
                }
            }
            JOptionPane.showMessageDialog(this, "Game Over! Final Score: " + score);
        }).start();
    }

    // Place the block on the board
    private void placeBlock() {
        int[][] shape = currentBlock.getShape();
        int x = currentBlock.getX();
        int y = currentBlock.getY();
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                if (shape[i][j] == 1) {
                    gameBoard[y + i][x + j] = 1;
                }
            }
        }
    }

    // Check for completed rows
    private void checkCompletedRows() {
        for (int i = 0; i < BOARD_HEIGHT; i++) {
            boolean rowComplete = true;
            for (int j = 0; j < BOARD_WIDTH; j++) {
                if (gameBoard[i][j] == 0) {
                    rowComplete = false;
                    break;
                }
            }
            if (rowComplete) {
                removeRow(i);
                score += 100; // Increase score for each completed row
                scoreLabel.setText("Score: " + score);
            }
        }
    }

    // Remove a completed row
    private void removeRow(int row) {
        for (int i = row; i > 0; i--) {
            System.arraycopy(gameBoard[i - 1], 0, gameBoard[i], 0, BOARD_WIDTH);
        }
        Arrays.fill(gameBoard[0], 0);
    }

    // Block class
    private class Block {
        private int[][] shape;
        private int x, y;
        private Color color;

        public Block(int type) {
            this.x = BOARD_WIDTH / 2 - 2;
            this.y = 0;
            this.color = Color.RED; // Default color
            this.shape = getBlockShape(type);
        }

        public int[][] getShape() {
            return shape;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public Color getColor() {
            return color;
        }

        public void moveLeft() {
            x--;
        }

        public void moveRight() {
            x++;
        }

        public void moveDown() {
            y++;
        }

        public void moveUp() {
            y--;
        }

        public void rotate() {
            int[][] newShape = new int[shape[0].length][shape.length];
            for (int i = 0; i < shape.length; i++) {
                for (int j = 0; j < shape[i].length; j++) {
                    newShape[j][shape.length - 1 - i] = shape[i][j];
                }
            }
            shape = newShape;
        }

        public void rotateBack() {
            rotate();
            rotate();
            rotate();
        }

        private int[][] getBlockShape(int type) {
            switch (type) {
                case 0: return new int[][]{{1, 1, 1, 1}}; // I-block
                case 1: return new int[][]{{1, 1}, {1, 1}}; // O-block
                case 2: return new int[][]{{0, 1, 0}, {1, 1, 1}}; // T-block
                case 3: return new int[][]{{1, 0, 0}, {1, 1, 1}}; // L-block
                case 4: return new int[][]{{0, 0, 1}, {1, 1, 1}}; // J-block
                case 5: return new int[][]{{0, 1, 1}, {1, 1, 0}}; // S-block
                case 6: return new int[][]{{1, 1, 0}, {0, 1, 1}}; // Z-block
                default: return new int[][]{{1}};
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TetrisGame game = new TetrisGame();
            game.setVisible(true);
        });
    }
}