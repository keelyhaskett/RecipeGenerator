import javax.swing.*;

public class GUI extends JFrame {
    private JPanel mainPanel;

    public GUI(String title) {
        super(title);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.pack();
    }

}
