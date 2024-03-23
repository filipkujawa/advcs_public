import javax.swing.JFrame;

public class Runner {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Bank");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Screen screen = new Screen();
        frame.add(screen);
        frame.pack();
        frame.setFocusable(true);
        frame.setVisible(true);
    }
}