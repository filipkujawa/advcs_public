import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Screen extends JPanel implements ActionListener {

    DLList<Card> deck;
    DLList<Card> hand;

    JButton drawButton;
    JTextArea textArea;
    JButton resetButton;

    JButton[] cardButtons;
    ArrayList<Integer> cardsToKeep;

    int drawRound;

    int balance;

    public Screen() {
        this.setLayout(null);
        this.setFocusable(true);

        balance = 50;
        drawRound = 0;

        deck = new DLList<Card>();
        hand = new DLList<Card>();

        drawButton = new JButton("Draw");
        drawButton.setBounds(50, 350, 100, 50);
        drawButton.addActionListener(this);
        this.add(drawButton);

        resetButton = new JButton("Play Again");
        resetButton.setBounds(50, 350, 100, 50);
        resetButton.addActionListener(this);
        this.add(resetButton);

        cardsToKeep = new ArrayList<Integer>();

        cardButtons = new JButton[5];
        for (int i = 0; i < cardButtons.length; i++) {
            cardButtons[i] = new JButton("Keep");
            cardButtons[i].setBounds(50 + i * 150, 300, 100, 50);
            cardButtons[i].addActionListener(this);
            this.add(cardButtons[i]);
        }

        textArea = new JTextArea("""
                250 Points: Royal Flush
                50 Points: Straight Flush
                25 Points: Four of a Kind
                9 Points: Full House
                6 Points: Flush
                4 Points: Straight
                3 Points: Three of a Kind
                2 Points: Two Pair
                1 Point: Pair of Jacks or Higher
                0 Points: Everything Else
                """);

        textArea.setBounds(300, 350, 200, 250);
        textArea.setEditable(false);
        this.add(textArea);

        for (Suit suit : Suit.values()) {
            for (CardRank rank : CardRank.values()) {
                deck.add(new Card(suit, rank));
            }
        }

        resetGame();
    }

    public void resetGame() {

        for (int i = 0; i < hand.size(); i++) {
            deck.add(hand.get(i));
        }

        hand.clear();

        deck.shuffle();

        for (int i = 0; i < 5; i++) {
            hand.add(deck.remove(0));
        }

        for (int i = 0; i < 5; i++) {
            cardButtons[i].setEnabled(true);
        }

        balance -= 1;
        drawRound = 1;

    }

    public Dimension getPreferredSize() {
        return new Dimension(800, 800);
    }

    public void paintComponent(Graphics g) {

        super.paintComponent(g);

        if (drawRound == 1) {
            drawButton.setVisible(true);
            resetButton.setVisible(false);
            for (int i = 0; i < cardButtons.length; i++) {
                cardButtons[i].setVisible(true);
            }
        } else if (drawRound == 2) {
            drawButton.setVisible(false);
            resetButton.setVisible(true);
            for (int i = 0; i < cardButtons.length; i++) {
                cardButtons[i].setVisible(false);
            }
        }

        sortHand(hand);

        for (int i = 0; i < hand.size(); i++) {
            if (cardsToKeep.contains(i)) {
                g.setColor(Color.GREEN);
                g.fillRect(50 + i * 150, 100, 100, 200);
            } else {
                g.setColor(Color.WHITE);
                g.fillRect(50 + i * 150, 100, 100, 200);
            }
        }
        g.setColor(Color.BLACK);

        for (int i = 0; i < hand.size(); i++) {
            hand.get(i).drawMe(g, 50 + i * 150, 100);
        }

        g.drawString("Balance: " + balance, 50, 50);

        if (drawRound == 1) {
            g.drawString("Click on the cards you want to keep", 400, 50);
        } else if (drawRound == 2) {
            int winnings = calculateWinnings();
            if (winnings > 0) {
                g.drawString("You won " + winnings + " credits!", 400, 50);
            } else {
                g.drawString("You lost!", 400, 50);
            }
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(resetButton)) {
            int winnings = calculateWinnings();
            balance += winnings;
            resetGame();
        } else if (e.getSource().equals(drawButton)) {
            for (int i = 0; i < hand.size(); i++) {
                if (!cardsToKeep.contains(i)) {
                    deck.add(hand.get(i));
                    hand.set(deck.remove(0), i);
                }
            }
            cardsToKeep.clear();
            drawRound = 2;
        } else if (drawRound == 1) {
            for (int i = 0; i < cardButtons.length; i++) {
                if (e.getSource().equals(cardButtons[i])) {
                    cardsToKeep.add(i);
                    cardButtons[i].setEnabled(false);
                }
            }

        }
        repaint();
    }

    public int calculateWinnings() {
        DLList<Card> sortedHand = new DLList<Card>();
        for (int i = 0; i < hand.size(); i++) {
            sortedHand.add(hand.get(i));
        }
        sortHand(sortedHand);

        // Royal Flush
        if (sortedHand.get(0).getRank() == 10 && sortedHand.get(1).getRank() == 11
                && sortedHand.get(2).getRank() == 12 && sortedHand.get(3).getRank() == 13
                && sortedHand.get(4).getRank() == 14) {
            return 250;
        }

        // Straight flush
        if (sortedHand.get(0).getRank() == sortedHand.get(1).getRank() - 1
                && sortedHand.get(1).getRank() == sortedHand.get(2).getRank() - 1
                && sortedHand.get(2).getRank() == sortedHand.get(3).getRank() - 1
                && sortedHand.get(3).getRank() == sortedHand.get(4).getRank() - 1) {

            if (sortedHand.get(0).getSuit() == sortedHand.get(1).getSuit()
                    && sortedHand.get(1).getSuit() == sortedHand.get(2).getSuit()
                    && sortedHand.get(2).getSuit() == sortedHand.get(3).getSuit()
                    && sortedHand.get(3).getSuit() == sortedHand.get(4).getSuit()) {
                return 50;
            }
        }

        // Four of a kind
        if (sortedHand.get(0).getRank() == sortedHand.get(1).getRank()
                && sortedHand.get(1).getRank() == sortedHand.get(2).getRank()
                && sortedHand.get(2).getRank() == sortedHand.get(3).getRank()) {
            return 25;
        }

        // Full house
        if (sortedHand.get(0).getRank() == sortedHand.get(1).getRank()
                && sortedHand.get(1).getRank() == sortedHand.get(2).getRank()
                && sortedHand.get(3).getRank() == sortedHand.get(4).getRank()) {
            return 9;
        } else if (sortedHand.get(0).getRank() == sortedHand.get(1).getRank()
                && sortedHand.get(2).getRank() == sortedHand.get(3).getRank()
                && sortedHand.get(3).getRank() == sortedHand.get(4).getRank()) {
            return 9;
        }

        // Flush
        if (sortedHand.get(0).getSuit() == sortedHand.get(1).getSuit()
                && sortedHand.get(1).getSuit() == sortedHand.get(2).getSuit()
                && sortedHand.get(2).getSuit() == sortedHand.get(3).getSuit()
                && sortedHand.get(3).getSuit() == sortedHand.get(4).getSuit()) {
            return 6;
        }

        // Straight
        if (sortedHand.get(0).getRank() == sortedHand.get(1).getRank() - 1
                && sortedHand.get(1).getRank() == sortedHand.get(2).getRank() - 1
                && sortedHand.get(2).getRank() == sortedHand.get(3).getRank() - 1
                && sortedHand.get(3).getRank() == sortedHand.get(4).getRank() - 1) {
            return 4;
        }

        // 3 of a kind
        if (sortedHand.get(0).getRank() == sortedHand.get(1).getRank()
                && sortedHand.get(1).getRank() == sortedHand.get(2).getRank()) {
            return 3;
        } else if (sortedHand.get(1).getRank() == sortedHand.get(2).getRank()
                && sortedHand.get(2).getRank() == sortedHand.get(3).getRank()) {
            return 3;
        } else if (sortedHand.get(2).getRank() == sortedHand.get(3).getRank()
                && sortedHand.get(3).getRank() == sortedHand.get(4).getRank()) {
            return 3;
        }

        // 2 pair
        int pairCount = 0;

        if (sortedHand.get(0).getRank() == sortedHand.get(1).getRank()) {
            pairCount++;
        }
        if (sortedHand.get(1).getRank() == sortedHand.get(2).getRank()) {
            pairCount++;
        }
        if (sortedHand.get(2).getRank() == sortedHand.get(3).getRank()) {
            pairCount++;
        }
        if (sortedHand.get(3).getRank() == sortedHand.get(4).getRank()) {
            pairCount++;
        }

        if (pairCount == 2) {
            return 2;
        }

        // Pair of jacks or higher
        if (sortedHand.get(0).getRank() == sortedHand.get(1).getRank()
                && sortedHand.get(0).getRank() >= 11) {
            return 1;
        } else if (sortedHand.get(1).getRank() == sortedHand.get(2).getRank()
                && sortedHand.get(1).getRank() >= 11) {
            return 1;
        } else if (sortedHand.get(2).getRank() == sortedHand.get(3).getRank()
                && sortedHand.get(2).getRank() >= 11) {
            return 1;
        } else if (sortedHand.get(3).getRank() == sortedHand.get(4).getRank()
                && sortedHand.get(3).getRank() >= 11) {
            return 1;
        }

        return 0;

    }

    public void sortHand(DLList<Card> sHand) {
        for (int i = 0; i < sHand.size(); i++) {
            for (int j = 0; j < sHand.size() - 1; j++) {
                if (sHand.get(j).compareTo(sHand.get(j + 1))) {
                    Card temp = sHand.get(j);
                    sHand.set(sHand.get(j + 1), j);
                    sHand.set(temp, j + 1);
                }
            }
        }
    }

}