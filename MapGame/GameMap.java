import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import javax.imageio.ImageIO;

public class GameMap implements Serializable {

    private MyHashTable<Square, Element> map;
    private int squareSize; // Variable for square width/size

    private int rows;
    private int columns;

    public GameMap(int rows, int columns, int squareSize) {
        map = new MyHashTable<Square, Element>();
        this.squareSize = squareSize; // Set the default square size
        this.rows = rows;
        this.columns = columns;
        initMap(rows, columns);
    }

    public void initMap(int rows, int columns) {
        map.clear();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                Square square = new Square(i, j);
                map.put(square, new Grass());
                map.put(square, new None());

            }
        }
    }

    public void setSquareSize(int squareSize) {
        this.squareSize = squareSize;
    }

    public int getSquareSize() {
        return squareSize;
    }

    public int getCols() {
        return columns;
    }

    // Set base on the map
    public void setBase(Square square, Base base) {
        DLList<Element> elements = map.get(square);
        for (int i = 0; i < elements.size(); i++) {
            if (elements.get(i) instanceof Base) {
                map.remove(square, elements.get(i));
            }
        }

        map.put(square, base);
    }

    // Set obstacle on the map
    public void setObstacle(Square square, Obstacle obstacle) {
        DLList<Element> elements = map.get(square);
        for (int i = 0; i < elements.size(); i++) {
            if (elements.get(i) instanceof Obstacle) {
                map.remove(square, elements.get(i));
            }
        }

        map.put(square, obstacle);
    }

    public boolean checkObstacle(Square square) {
        DLList<Element> elements = map.get(square);
        for (int i = 0; i < elements.size(); i++) {
            if (elements.get(i) instanceof Obstacle && !(elements.get(i) instanceof None)) {
                return true;
            } else if (elements.get(i) instanceof Water) {
                return true;
            }
        }

        return false; // No obstacle
    }

    // Squares form a 100x100 grid. Each square has a row and column
    public void drawMap(Graphics g, boolean borders, BufferedImage treeImage, BufferedImage rockImage,
            BufferedImage bushImage, BufferedImage waterImage, BufferedImage sandImage, BufferedImage grassImage) {
        DLList<Square> squares = map.keySet().toDLList();
        g.setColor(Color.BLACK);
        g.fillRect(65, 5, squareSize * columns + 10, squareSize * rows + 10);
        for (int i = 0; i < squares.size(); i++) {
            Square square = squares.get(i);
            Element base = null;
            Element obstacle = null;
            DLList<Element> elements = map.get(square);
            for (int j = 0; j < elements.size(); j++) {
                if (elements.get(j) instanceof Base) {
                    base = elements.get(j);
                } else if (elements.get(j) instanceof Obstacle) {
                    obstacle = elements.get(j);
                }
            }
            if (base instanceof Base) {
                if (borders) {
                    g.setColor(Color.BLACK);
                    g.fillRect(70 + square.column * squareSize, 10 + square.row * squareSize, squareSize, squareSize);
                }

                if (base instanceof Grass) {
                    ((Base) base).drawSelf(g, 70 + square.column * squareSize, 10 + square.row * squareSize,
                            squareSize, grassImage);
                } else if (base instanceof Water) {
                    ((Base) base).drawSelf(g, 70 + square.column * squareSize, 10 + square.row * squareSize,
                            squareSize, waterImage);
                } else if (base instanceof Sand) {
                    ((Base) base).drawSelf(g, 70 + square.column * squareSize, 10 + square.row * squareSize,
                            squareSize, sandImage);
                }

            }
            if (obstacle instanceof Obstacle) {
                if (borders) {
                    if (obstacle instanceof Tree) {
                        ((Obstacle) obstacle).drawSelf(g, 70 + square.column * squareSize,
                                10 + square.row * squareSize - 1,
                                squareSize - 1, treeImage);
                    } else if (obstacle instanceof Rock) {
                        ((Obstacle) obstacle).drawSelf(g, 70 + square.column * squareSize,
                                10 + square.row * squareSize - 1,
                                squareSize - 1, rockImage);
                    } else if (obstacle instanceof Bush) {
                        ((Obstacle) obstacle).drawSelf(g, 70 + square.column * squareSize,
                                10 + square.row * squareSize - 1,
                                squareSize - 1, bushImage);
                    }
                } else {
                    if (obstacle instanceof Tree) {
                        ((Obstacle) obstacle).drawSelf(g, 70 + square.column * squareSize, 10 + square.row * squareSize,
                                squareSize, treeImage);
                    } else if (obstacle instanceof Rock) {
                        ((Obstacle) obstacle).drawSelf(g, 70 + square.column * squareSize, 10 + square.row * squareSize,
                                squareSize, rockImage);
                    } else if (obstacle instanceof Bush) {
                        ((Obstacle) obstacle).drawSelf(g, 70 + square.column * squareSize, 10 + square.row * squareSize,
                                squareSize, bushImage);
                    }
                }

            }
        }
    }

    public void drawMapPortion(Graphics g, int topLeftRow, int topLeftCol, int rows, int columns,
            BufferedImage treeImage, BufferedImage rockImage,
            BufferedImage bushImage, BufferedImage waterImage, BufferedImage sandImage, BufferedImage grassImage) {
        DLList<Square> squares = map.keySet().toDLList();
        for (int i = 0; i < squares.size(); i++) {

            if (squares.get(i).row < topLeftRow || squares.get(i).row >= topLeftRow + rows) {
                continue;
            }
            if (squares.get(i).column < topLeftCol || squares.get(i).column >= topLeftCol + columns) {
                continue;
            }

            Square square = squares.get(i);
            Element base = null;
            Element obstacle = null;
            DLList<Element> elements = map.get(square);

            int newSquareSize = 1080 / rows;

            for (int j = 0; j < elements.size(); j++) {
                if (elements.get(j) instanceof Base) {
                    base = elements.get(j);
                } else if (elements.get(j) instanceof Obstacle) {
                    obstacle = elements.get(j);
                }
            }
            if (base instanceof Base) {
                if (base instanceof Grass) {
                    ((Base) base).drawSelf(g, (square.column - topLeftCol) * newSquareSize,
                            (square.row - topLeftRow) * newSquareSize,
                            newSquareSize, grassImage);
                } else if (base instanceof Water) {
                    ((Base) base).drawSelf(g, (square.column - topLeftCol) * newSquareSize,
                            (square.row - topLeftRow) * newSquareSize,
                            newSquareSize, waterImage);
                } else if (base instanceof Sand) {
                    ((Base) base).drawSelf(g, (square.column - topLeftCol) * newSquareSize,
                            (square.row - topLeftRow) * newSquareSize,
                            newSquareSize, sandImage);
                }

            }
            if (obstacle instanceof Obstacle) {

                if (obstacle instanceof Tree) {
                    ((Obstacle) obstacle).drawSelf(g, (square.column - topLeftCol) * newSquareSize,
                            (square.row - topLeftRow) * newSquareSize,
                            newSquareSize, treeImage);
                } else if (obstacle instanceof Rock) {
                    ((Obstacle) obstacle).drawSelf(g, (square.column - topLeftCol) * newSquareSize,
                            (square.row - topLeftRow) * newSquareSize,
                            newSquareSize, rockImage);
                } else if (obstacle instanceof Bush) {
                    ((Obstacle) obstacle).drawSelf(g, (square.column - topLeftCol) * newSquareSize,
                            (square.row - topLeftRow) * newSquareSize,
                            newSquareSize, bushImage);
                }

            }
        }
    }

    public void drawMiniMap(Graphics g, int miniMapX, int miniMapY, int miniMapSize, BufferedImage waterImage,
            BufferedImage sandImage, BufferedImage grassImage, int playerRow, int playerCol) {

        int miniSquareSize = miniMapSize / rows; // Adjust the size of mini-squares based on the mini-map dimensions

        // Draw the background for the mini-map
        g.setColor(Color.BLACK);
        int borderSize = 2;
        g.setColor(Color.BLACK);
        g.fillRect(miniMapX - borderSize, miniMapY - borderSize, miniMapSize + (2 * borderSize),
                miniMapSize + (2 * borderSize));
        g.setColor(Color.WHITE);
        g.fillRect(miniMapX, miniMapY, miniMapSize, miniMapSize);

        // Iterate through all squares in the map
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                Square square = new Square(i, j);
                int miniMapSquareX = miniMapX + (j * miniSquareSize);
                int miniMapSquareY = miniMapY + (i * miniSquareSize);

                DLList<Element> elements = map.get(square);

                // Draw each element in the square
                for (int k = 0; k < elements.size(); k++) {
                    Element element = elements.get(k);
                    if (element instanceof Base) {
                        // Draw bases in the mini-map
                        if (element instanceof Grass) {
                            ((Base) element).drawSelf(g, miniMapSquareX, miniMapSquareY, miniSquareSize, grassImage);
                        } else if (element instanceof Water) {
                            ((Base) element).drawSelf(g, miniMapSquareX, miniMapSquareY, miniSquareSize, waterImage);
                        } else if (element instanceof Sand) {
                            ((Base) element).drawSelf(g, miniMapSquareX, miniMapSquareY, miniSquareSize, sandImage);
                        }
                    }
                }

                // Draw red square for player
                if (i == playerRow && j == playerCol) {
                    g.setColor(Color.RED);
                    g.fillRect(miniMapSquareX, miniMapSquareY, miniSquareSize, miniSquareSize);
                }
            }
        }
    }
}

abstract class Element implements Serializable {
    abstract public String toString();
}

abstract class Base extends Element {
    abstract public void drawSelf(Graphics g, int x, int y, int squareSize, BufferedImage image);
}

abstract class Obstacle extends Element {
    abstract public void drawSelf(Graphics g, int x, int y, int squareSize, BufferedImage image);
}

class Grass extends Base {
    public String toString() {
        return "Grass";
    }

    public void drawSelf(Graphics g, int x, int y, int squareSize, BufferedImage image) {

        g.drawImage(image, x, y, squareSize, squareSize, null);

    }
}

class Water extends Base {
    public String toString() {
        return "Water";
    }

    public void drawSelf(Graphics g, int x, int y, int squareSize, BufferedImage image) {

        g.drawImage(image, x, y, squareSize, squareSize, null);

    }
}

class Sand extends Base {
    public String toString() {
        return "Sand";
    }

    public void drawSelf(Graphics g, int x, int y, int squareSize, BufferedImage image) {

        g.drawImage(image, x, y, squareSize, squareSize, null);

    }
}

class Tree extends Obstacle {
    public String toString() {
        return "Tree";
    }

    public void drawSelf(Graphics g, int x, int y, int squareSize, BufferedImage image) {

        g.drawImage(image, x, y, squareSize, squareSize, null);

    }
}

class Rock extends Obstacle {
    public String toString() {
        return "Rock";
    }

    public void drawSelf(Graphics g, int x, int y, int squareSize, BufferedImage image) {

        g.drawImage(image, x, y, squareSize, squareSize, null);
    }
}

class Bush extends Obstacle {

    public String toString() {
        return "Bush";
    }

    public Bush() {

    }

    public void drawSelf(Graphics g, int x, int y, int squareSize, BufferedImage image) {

        g.drawImage(image, x, y, squareSize, squareSize, null);

    }
}

class None extends Obstacle {
    public String toString() {
        return "None";
    }

    public void drawSelf(Graphics g, int x, int y, int squareSize, BufferedImage image) {
        return;
    }
}
