import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

enum Suit {
    CLUBS, DIAMONDS, HEARTS, SPADES
}

public class Card {

    private Suit suit;
    private CardRank rank;
    private BufferedImage suitImage;

    public Card(Suit suit, CardRank rank) {
        this.suit = suit;
        this.rank = rank;
        try {
            suitImage = ImageIO.read(new File(suit.toString() + ".png"));
        } catch (Exception e) {
            System.out.println("Error loading image");
        }
    }

    public void drawMe(Graphics g, int x, int y) {
        g.drawRect(x, y, 100, 200);
        g.drawString(rank.toString(), x + 10, y + 20);
        g.drawString(rank.toString(), x + 80, y + 180);
        g.drawImage(suitImage, x + 25, y + 75, 50, 50, null);

    }

    public int getRank() {
        return rank.value();
    }

    public Suit getSuit() {
        return suit;
    }

    public boolean compareTo(Card other) {
        return this.getRank() > other.getRank();
    }
}
