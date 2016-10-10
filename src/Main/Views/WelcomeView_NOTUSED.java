package Main.Views;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Peter on 04/10/16.
 */
public class WelcomeView_NOTUSED extends JFrame {
  private JButton connectButton  = new JButton("Connect!");
  JLabel label = new JLabel("Some info");
  JPanel panel = new JPanel();

  public WelcomeView_NOTUSED() {
    setTitle("bggUtilities");

    pack();

    // make the frame half the height and width
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int height = screenSize.height;
    int width = screenSize.width;
    setSize(width/2, height/2);

    // here's the part where i center the jframe on screen
    setLocationRelativeTo(null);

    panel.add(connectButton);
    panel.setSize(width/4, height/4);

    //this.add(label, GridLayout.NORTH);
    this.add(panel, BorderLayout.SOUTH);

    connectButton.setLayout(null);
    this.add(connectButton);
    connectButton.setVisible(true);

    setVisible(true);
  }
}