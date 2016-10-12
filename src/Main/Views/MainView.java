package Main.Views;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Peter on 10/10/2016.
 */
public class MainView {
  public JPanel panel1;
  private JList list1;
  private JTextPane textPane1;

  public MainView() {
    // make the frame half the height and width
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int height = screenSize.height;
    int width = screenSize.width;
    //setSize(width/2, height/2);
    panel1.setPreferredSize(new Dimension(width/2, height/2));


  }
}
