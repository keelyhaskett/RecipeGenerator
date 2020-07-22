import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 */
public abstract class GUI {
    private JPanel mainPanel;
    private JFrame startWindow;
    private JFrame mainWindow;

    public GUI() {
        build();
    }

    /**
     *
     */
    private void build() {
        JButton start = new JButton();
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { openMainWindow(); } //opens the new window on click
        });

        JButton quit = new JButton();
        quit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { System.exit(0); } //ends the program
        });


        JLabel title = new JLabel();
        title.setText("Recipe Generator");




        startWindow = new JFrame("Recipe Generator");

        startWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //makes the instance finish when GUI is closed
        startWindow.setContentPane(mainPanel);
        startWindow.pack();
        startWindow.setVisible(true); //so we can see the gui
    }

    /**
     *
     */
    private void openMainWindow() {
        mainWindow = new JFrame("Recipe Generator");
        startWindow.setVisible(false); //make the start window go away

        //TODO: create load, generate, and quit buttons

        //TODO: allow for 3 panels, button panel, recipes total, and something else

        //TODO: make 3rd window on generation showing the titles of the generated recipes.

        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //makes the instance finish when GUI is closed
        mainWindow.setContentPane(mainPanel);
        mainWindow.pack();
        mainWindow.setVisible(true); //so we can see the gui


    }

}
