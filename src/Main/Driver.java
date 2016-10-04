package Main;

import Main.Views.StartView;
import Main.Views.WelcomeView;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Peter on 12/09/16.
 */
public class Driver {

  private String url;

  public static void main(String[] args) {
    new Driver();
  }

  public Driver() {
    JFrame frame = new JFrame("bggUtilities");
    frame.setContentPane(new StartView(this).panelMain);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    frame.pack();
    frame.setVisible(true);
    frame.setLocationRelativeTo(null);

  }

  public void hi() {
    System.out.println("hi");
  }
}
