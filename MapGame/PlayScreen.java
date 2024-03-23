
import javax.swing.JPanel;
import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;

public class PlayScreen extends JPanel implements KeyListener {

    GameMap map;
    Player player;

    BufferedImage treeImage;
    BufferedImage bushImage;
    BufferedImage rockImage;
    BufferedImage waterImage;
    BufferedImage grassImage;
    BufferedImage sandImage;

    int zoomX;
    int zoomY;

    MovingObject[] movingObjects;
    Coin[] coins;

    public PlayScreen() {
        this.addKeyListener(this);
        this.setLayout(null);
        this.setFocusable(true);

        zoomX = 0;
        zoomY = 0;

        // Load the images
        try {
            treeImage = ImageIO.read(new File("obstacles/tree.png"));
            bushImage = ImageIO.read(new File("obstacles/stump.png"));
            rockImage = ImageIO.read(new File("obstacles/rock.png"));
            waterImage = ImageIO.read(new File("bases/water.png"));
            grassImage = ImageIO.read(new File("bases/grass.png"));
            sandImage = ImageIO.read(new File("bases/sand.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            // Load the data file to read in
            FileInputStream fis = new FileInputStream("map.dat");

            // Create a data stream to read in
            ObjectInputStream in = new ObjectInputStream(fis);

            // Read in the object from file
            // Casting to generics to produce unchecked type warning
            map = (GameMap) in.readObject();
            map.setSquareSize((int) (1080 / map.getCols()));

            // Close all your data stream
            fis.close();
            in.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Using default list");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        int playerX = 4 * (1080 / 10); // Calculate the x-coordinate of the player
        int playerY = 4 * (1080 / 10); // Calculate the y-coordinate of the player
        player = new Player(playerX, playerY, (int) ((1080 / 10)));

        coins = new Coin[25];
        for (int i = 0; i < 25; i++) {

            int coinRow = (int) (Math.random() * 100);
            int coinCol = (int) (Math.random() * 100);

            while (map.checkObstacle(new Square(coinRow, coinCol))) {
                coinRow = (int) (Math.random() * 100);
                coinCol = (int) (Math.random() * 100);
            }

            coins[i] = new Coin(coinRow, coinCol, (int) (1080 / 10));
        }

        BufferedImage FlameImage = null;
        BufferedImage SpiritImage = null;
        BufferedImage SlimeImage = null;
        try {
            FlameImage = ImageIO.read(new File("characters/flame.png"));
            SpiritImage = ImageIO.read(new File("characters/spirit.png"));
            SlimeImage = ImageIO.read(new File("characters/slime.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }

        movingObjects = new MovingObject[3];
        movingObjects[0] = new Flame(2, 2, (int) (1080 / 10), map, FlameImage);
        movingObjects[1] = new Spirit(4, 1, (int) (1080 / 10), map, SpiritImage);
        movingObjects[2] = new Slime(6, 3, (int) (1080 / 10), map, SlimeImage);

        Thread FlameThread = new Thread(movingObjects[0]);
        Thread SpiritThread = new Thread(movingObjects[1]);
        Thread SlimeThread = new Thread(movingObjects[2]);

        FlameThread.start();
        SpiritThread.start();
        SlimeThread.start();

        Thread animateThread = new Thread(new Animate(this));
        animateThread.start();
    }

    public Dimension getPreferredSize() {
        return new Dimension(1080, 1080);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 1080, 1080);

        map.drawMapPortion(g, zoomY, zoomX, 10, 10, treeImage, rockImage, bushImage, waterImage, sandImage, grassImage);
        player.draw(g);

        int playerRow = zoomY + 4;
        int playerCol = zoomX + 4;

        // Draw mini map
        map.drawMiniMap(g, 1080 - 200, 0, 200, waterImage, sandImage, grassImage, playerRow, playerCol);

        // Check if player collected coin
        int coinsCollected = 0;
        for (int i = 0; i < coins.length; i++) {
            if (coins[i].checkCollision(player, zoomX, zoomY)) {
                coins[i].collect();
            }
            if (coins[i].isCollected()) {
                coinsCollected++;
            }
        }

        // g.setColor(Color.BLACK);
        // .fillRect(0, 0, 300, 50);
        g.setColor(Color.BLACK);
        g.drawString("Coins Collected: " + coinsCollected + "/ 25", 10, 30);

        // Iterate through rows and cols in frame
        for (int c = zoomX; c < zoomX + 10; c++) {
            for (int r = zoomY; r < zoomY + 10; r++) {
                for (int i = 0; i < movingObjects.length; i++) {
                    if (movingObjects[i].getRow() == r && movingObjects[i].getCol() == c) {
                        movingObjects[i].draw(g, (c - zoomX) * (1080 / 10), (r - zoomY) * (1080 / 10));
                    }
                }
                for (int i = 0; i < coins.length; i++) {
                    if (coins[i].row == r && coins[i].col == c) {
                        if (!coins[i].isCollected()) {
                            coins[i].draw(g, (c - zoomX) * (1080 / 10), (r - zoomY) * (1080 / 10));
                        }
                    }
                }
            }
        }

    }

    public void keyPressed(KeyEvent e) {
        // Convert player x, y to row column of game map.

        int row = zoomY + 4;
        int col = zoomX + 4;

        // Check if object stops player from moving.

        if (e.getKeyCode() == 40) {
            if (row < 100) {
                if (!map.checkObstacle(new Square(row + 1, col))) {
                    // player.move(0, (int) (1080 / map.getCols()));
                    zoomY += 1;

                }
            }
        }
        if (e.getKeyCode() == 38) {
            if (row > 0) {
                if (!map.checkObstacle(new Square(row - 1, col))) {
                    // player.move(0, (int) -(1080 / map.getCols()));
                    zoomY -= 1;
                }
            }
        }
        if (e.getKeyCode() == 37) {
            if (col > 0) {
                if (!map.checkObstacle(new Square(row, col - 1))) {
                    // player.move((int) -(1080 / map.getCols()), 0);
                    zoomX -= 1;
                }
            }
        }
        if (e.getKeyCode() == 39) {
            if (col < 100) {
                if (!map.checkObstacle(new Square(row, col + 1))) {
                    // player.move((int) (1080 / map.getCols()), 0);
                    zoomX += 1;
                }
            }
        }

        row = zoomY + 4;
        col = zoomX + 4;

        // repaint();
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }
}

class Animate implements Runnable {
    private PlayScreen screen;

    public Animate(PlayScreen screen) {
        this.screen = screen;
    }

    @Override
    public void run() {
        try {
            while (true) {
                // Repaint the screen
                screen.repaint();
                Thread.sleep(20); // Adjust the sleep duration to control the repaint speed
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Player {
    int x, y;
    int size;
    BufferedImage image;

    public Player(int x, int y, int size) {
        this.x = x;
        this.y = y;
        this.size = size;
        try {
            image = ImageIO.read(new File("player.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void move(int dx, int dy, Coin[] coins) {
        x += dx;
        y += dy;
    }

    public void draw(Graphics g) {
        g.drawImage(image, x, y, size, size, null);
    }
}

class Coin {
    int row, col;
    int size;
    BufferedImage image;

    boolean collected;

    public Coin(int row, int col, int size) {
        this.col = col;
        this.row = row;
        this.size = size;
        try {
            image = ImageIO.read(new File("GoldCoin.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics g, int x, int y) {
        g.drawImage(image, x + size / 4, y + size / 4, size / 4, size / 4, null);
    }

    public boolean checkCollision(Player player, int zoomX, int zoomY) {
        if (player.getX() == (col - zoomX) * (1080 / 10) && player.getY() == (row - zoomY) * (1080 / 10)) {
            return true;
        }

        return false;
    }

    public void collect() {
        collected = true;
    }

    public boolean isCollected() {
        return collected;
    }

}

abstract class MovingObject implements Runnable {
    int row, col;
    int size;
    int direction;
    GameMap map;
    BufferedImage image;

    public MovingObject(int row, int col, int size, GameMap map, BufferedImage image) {
        this.row = row;
        this.col = col;
        this.size = size;
        this.direction = 1;
        this.map = map;
        this.image = image;
    }

    public abstract void draw(Graphics g, int x, int y);

    public abstract void run();

    public void flipDirection() {
        direction *= -1;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void move(int dx, int dy) {
        row += dy;
        col += dx;
    }

    public boolean checkCollision(int row, int col) {

        return map.checkObstacle(new Square(row, col));

    }
}

class Flame extends MovingObject {

    public Flame(int row, int col, int size, GameMap map, BufferedImage image) {

        super(row, col, size, map, image);
    }

    public void draw(Graphics g, int x, int y) {
        // Draw a Flame
        g.drawImage(image, x, y, size, size, null);

    }

    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);

                if (checkCollision(row, col + 1 * direction)) {
                    flipDirection();
                }

                move(1 * direction, 0);
                if (col == 99) {
                    direction *= -1;
                } else if (col == 0) {
                    direction *= -1;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}

class Spirit extends MovingObject {

    public Spirit(int row, int col, int size, GameMap map, BufferedImage image) {
        super(row, col, size, map, image);
    }

    public void draw(Graphics g, int x, int y) {
        // Draw a Spirit
        g.drawImage(image, x, y, size, size, null);
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);

                if (checkCollision(row, col + 1 * direction)) {
                    flipDirection();
                }

                move(1 * direction, 0);
                if (col == 99) {
                    direction *= -1;
                } else if (col == 0) {
                    direction *= -1;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}

class Slime extends MovingObject {

    public Slime(int row, int col, int size, GameMap map, BufferedImage image) {
        super(row, col, size, map, image);
    }

    public void draw(Graphics g, int x, int y) {
        // Draw a Slime
        g.drawImage(image, x, y, size, size, null);
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);

                if (checkCollision(row, col + 1 * direction)) {
                    flipDirection();
                }

                move(1 * direction, 0);
                if (col == 99) {
                    direction *= -1;
                } else if (col == 0) {
                    direction *= -1;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
