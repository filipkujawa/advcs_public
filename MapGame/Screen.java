import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.w3c.dom.events.MouseEvent;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

enum SelectMode {
    None,
    Grass,
    Water,
    Sand,
    Tree,
    Rock,
    Bush

}

public class Screen extends JPanel implements ActionListener, MouseListener, MouseMotionListener {
    GameMap map;

    SelectMode selectMode;

    int rows, columns, squareSize;

    // Increase/Decrease grid size buttons
    JButton increaseGridSizeButton;
    JButton decreaseGridSizeButton;

    // Editor Controls

    // Base
    JButton grassButton;
    JButton waterButton;
    JButton sandButton;

    // Obstacle
    JButton treeButton;
    JButton rockButton;
    JButton bushButton;

    // Other
    JButton saveButton;
    JButton loadButton;
    JButton clearButton;
    JButton playButton;

    // Bulk map edit tools
    JButton fillButton;
    JTextField fillTextField;
    boolean fillModeEnabled;

    JButton lineButton;
    int lineStartX, lineStartY;
    int lineEndX, lineEndY;
    boolean lineModeEnabled;

    BufferedImage treeImage;
    BufferedImage bushImage;
    BufferedImage rockImage;
    BufferedImage waterImage;
    BufferedImage grassImage;
    BufferedImage sandImage;

    public Screen() {
        this.setLayout(null);
        this.setFocusable(true);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);

        rows = 100;
        columns = 100;
        squareSize = 8;
        map = new GameMap(rows, columns, squareSize);

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

            // Close all your data stream
            fis.close();
            in.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Using default list");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        addButtons();

        selectMode = SelectMode.None;
        fillModeEnabled = false;

        lineStartX = -1;
        lineStartY = -1;
        lineEndX = -1;
        lineEndY = -1;
        lineModeEnabled = false;

    }

    public Dimension getPreferredSize() {
        return new Dimension(1500, 900);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(getBackground());
        g.fillRect(0, 0, 1500, 900);

        map.drawMap(g, true, treeImage, rockImage, bushImage, waterImage, sandImage, grassImage);

        g.setColor(Color.BLACK);
        g.fillRect(100 + (columns * 10), 50, 300, 600);
        g.setColor(getBackground());
        g.fillRect(100 + (columns * 10) + 5, 50 + 5, 300 - 10, 600 - 10);

        grassButton.setBounds(100 + (columns * 10) + 50, 60 + 5, 100, 40);
        waterButton.setBounds(100 + (columns * 10) + 150, 60 + 5, 100, 40);
        sandButton.setBounds(100 + (columns * 10) + 50, 120 + 5, 100, 40);
        treeButton.setBounds(100 + (columns * 10) + 150, 120 + 5, 100, 40);
        rockButton.setBounds(100 + (columns * 10) + 50, 180 + 5, 100, 40);
        bushButton.setBounds(100 + (columns * 10) + 150, 180 + 5, 100, 40);

        saveButton.setBounds(100 + (columns * 10) + 50, 240 + 5, 100, 40);
        loadButton.setBounds(100 + (columns * 10) + 150, 240 + 5, 100, 40);

        clearButton.setBounds(100 + (columns * 10) + 50, 300 + 5, 100, 40);
        playButton.setBounds(100 + (columns * 10) + 150, 300 + 5, 100, 40);

        fillButton.setBounds(100 + (columns * 10) + 50, 360 + 5, 100, 40);
        fillTextField.setBounds(100 + (columns * 10) + 150, 360 + 5, 100, 40);

        lineButton.setBounds(100 + (columns * 10) + 50, 420 + 5, 100, 40);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == increaseGridSizeButton) {
            if (squareSize < 8) {
                squareSize++;
                map.setSquareSize(squareSize);
                repaint();
            }
        } else if (e.getSource() == decreaseGridSizeButton) {
            if (squareSize > 5) {
                squareSize--;
                map.setSquareSize(squareSize);
                repaint();
            }
        } else if (e.getSource() == grassButton) {
            selectMode = SelectMode.Grass;
        } else if (e.getSource() == waterButton) {
            selectMode = SelectMode.Water;
        } else if (e.getSource() == sandButton) {
            selectMode = SelectMode.Sand;
        } else if (e.getSource() == treeButton) {
            selectMode = SelectMode.Tree;
        } else if (e.getSource() == rockButton) {
            selectMode = SelectMode.Rock;
        } else if (e.getSource() == bushButton) {
            selectMode = SelectMode.Bush;
        } else if (e.getSource() == fillButton) {
            fillModeEnabled = !fillModeEnabled;
            if (fillModeEnabled) {
                fillButton.setText("Fill: On");
            } else {
                fillButton.setText("Fill: Off");
            }
        } else if (e.getSource() == lineButton) {
            lineModeEnabled = !lineModeEnabled;
            if (lineModeEnabled) {
                lineButton.setText("Line: On");
            } else {
                lineButton.setText("Line: Off");
            }

        } else if (e.getSource() == clearButton) {
            map.initMap(rows, columns);
        } else if (e.getSource() == playButton) {
            try {
                FileOutputStream fos = new FileOutputStream("map.dat");
                ObjectOutputStream out = new ObjectOutputStream(fos);
                out.writeObject(map);
                fos.close();
                out.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            JFrame frame = new JFrame("Game");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            PlayScreen screen = new PlayScreen();
            frame.add(screen);
            frame.pack();
            frame.setVisible(true);

        } else if (e.getSource() == saveButton) {
            // Save map to file
            try {
                FileOutputStream fos = new FileOutputStream("map.dat");
                ObjectOutputStream out = new ObjectOutputStream(fos);
                out.writeObject(map);
                fos.close();
                out.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (e.getSource() == loadButton) {
            try {
                // Load the data file to read in
                FileInputStream fis = new FileInputStream("map.dat");

                // Create a data stream to read in
                ObjectInputStream in = new ObjectInputStream(fis);

                // Read in the object from file
                // Casting to generics to produce unchecked type warning
                map = (GameMap) in.readObject();

                // Close all your data stream
                fis.close();
                in.close();
            } catch (FileNotFoundException ex) {
                System.out.println("Using default list");
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

        repaint();
    }

    @Override
    public void mouseClicked(java.awt.event.MouseEvent e) {
        if (e.getX() > 70 && e.getX() < 70 + this.columns * squareSize) {
            if (e.getY() > 10 && e.getY() < 10 + this.rows * squareSize) {
                // Now convert x y cordinate to row column for map
                int row = (e.getY() - 10) / squareSize;
                int column = (e.getX() - 70) / squareSize;
                Square square = new Square(row, column);

                if (lineModeEnabled) {
                    if (lineStartX == -1 && lineStartY == -1) {
                        lineStartX = row;
                        lineStartY = column;
                    } else {
                        lineEndX = row;
                        lineEndY = column;
                        drawLine(lineStartX, lineStartY, lineEndX, lineEndY);
                        lineStartX = -1;
                        lineStartY = -1;
                        lineEndX = -1;
                        lineEndY = -1;
                    }
                } else {
                    lineStartX = -1;
                    lineStartY = -1;
                    lineEndX = -1;
                    lineEndY = -1;
                }

                System.out.println(lineStartX + " " + lineEndX);

                if (fillModeEnabled) {
                    int fillRadius = Integer.parseInt(fillTextField.getText());
                    // Fill the map with the selected base/obstacle around clicked point
                    for (int i = -fillRadius; i <= fillRadius; i++) {
                        for (int j = -fillRadius; j <= fillRadius; j++) {
                            if (row + i >= 0 && row + i < rows && column + j >= 0 && column + j < columns) {
                                Square fillSquare = new Square(row + i, column + j);
                                System.out.println(fillSquare);
                                setStuff(fillSquare);
                            }
                        }
                    }
                } else {
                    setStuff(square);
                }
            }
        }
        repaint();
    }

    public void setStuff(Square square) {
        if (selectMode == SelectMode.Grass) {
            map.setBase(square, new Grass());
        } else if (selectMode == SelectMode.Water) {
            map.setBase(square, new Water());
        } else if (selectMode == SelectMode.Sand) {
            map.setBase(square, new Sand());
        } else if (selectMode == SelectMode.Tree) {
            map.setObstacle(square, new Tree());
        } else if (selectMode == SelectMode.Rock) {
            map.setObstacle(square, new Rock());
        } else if (selectMode == SelectMode.Bush) {
            map.setObstacle(square, new Bush());
        }
    }

    public void drawLine(int lineStartX, int lineStartY, int lineEndX, int lineEndY) {
        int x1 = lineStartX;
        int y1 = lineStartY;
        int x2 = lineEndX;
        int y2 = lineEndY;

        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        int sx = (x1 < x2) ? 1 : -1;
        int sy = (y1 < y2) ? 1 : -1;
        int err = dx - dy;

        while (true) {
            Square lineSquare = new Square(x1, y1);
            setStuff(lineSquare);

            if (x1 == x2 && y1 == y2) {
                break;
            }
            int e2 = 2 * err;
            if (e2 > -dy) {
                err -= dy;
                x1 += sx;
            }
            if (e2 < dx) {
                err += dx;
                y1 += sy;
            }
        }
    }

    @Override
    public void mousePressed(java.awt.event.MouseEvent e) {
    }

    @Override
    public void mouseReleased(java.awt.event.MouseEvent e) {

    }

    @Override
    public void mouseEntered(java.awt.event.MouseEvent e) {
    }

    @Override
    public void mouseExited(java.awt.event.MouseEvent e) {
    }

    @Override
    public void mouseDragged(java.awt.event.MouseEvent e) {
        // Check if dragging within the map
        if (e.getX() > 70 && e.getX() < 70 + this.columns * squareSize) {
            if (e.getY() > 10 && e.getY() < 10 + this.rows * squareSize) {
                // Now convert x y cordinate to row column for map
                int row = (e.getY() - 10) / squareSize;
                int column = (e.getX() - 70) / squareSize;
                Square square = new Square(row, column);
                setStuff(square);
            }
        }
        repaint();
    }

    @Override
    public void mouseMoved(java.awt.event.MouseEvent e) {
    }

    public void addButtons() {
        increaseGridSizeButton = new JButton("+");
        increaseGridSizeButton.setBounds(0, 10, 50, 30);
        increaseGridSizeButton.addActionListener(this);
        this.add(increaseGridSizeButton);

        decreaseGridSizeButton = new JButton("-");
        decreaseGridSizeButton.setBounds(0, 40, 50, 30);
        decreaseGridSizeButton.addActionListener(this);
        this.add(decreaseGridSizeButton);

        // Editor Controls
        grassButton = new JButton("Grass");
        grassButton.setBounds(0, 0, 100, 30);
        grassButton.addActionListener(this);
        this.add(grassButton);

        waterButton = new JButton("Water");
        waterButton.setBounds(0, 0, 100, 30);
        waterButton.addActionListener(this);
        this.add(waterButton);

        sandButton = new JButton("Sand");
        sandButton.setBounds(0, 0, 100, 30);
        sandButton.addActionListener(this);
        this.add(sandButton);

        treeButton = new JButton("Tree");
        treeButton.setBounds(0, 0, 100, 30);
        treeButton.addActionListener(this);
        this.add(treeButton);

        rockButton = new JButton("Rock");
        rockButton.setBounds(0, 0, 100, 30);
        rockButton.addActionListener(this);
        this.add(rockButton);

        bushButton = new JButton("Stump");
        bushButton.setBounds(0, 0, 100, 30);
        bushButton.addActionListener(this);
        this.add(bushButton);

        saveButton = new JButton("Save");
        saveButton.setBounds(0, 0, 100, 30);
        saveButton.addActionListener(this);
        this.add(saveButton);

        loadButton = new JButton("Load");
        loadButton.setBounds(0, 0, 100, 30);
        loadButton.addActionListener(this);
        this.add(loadButton);

        clearButton = new JButton("Clear");
        clearButton.setBounds(0, 0, 100, 30);
        clearButton.addActionListener(this);
        this.add(clearButton);

        playButton = new JButton("Play");
        playButton.setBounds(0, 0, 100, 30);
        playButton.addActionListener(this);
        this.add(playButton);

        fillButton = new JButton("Fill: Off");
        fillButton.setBounds(0, 0, 100, 30);
        fillButton.addActionListener(this);
        this.add(fillButton);

        fillTextField = new JTextField("0");
        fillTextField.setBounds(0, 0, 100, 30);
        fillTextField.addActionListener(this);
        this.add(fillTextField);

        lineButton = new JButton("Line: Off");
        lineButton.setBounds(0, 0, 100, 30);
        lineButton.addActionListener(this);
        this.add(lineButton);

    }

}