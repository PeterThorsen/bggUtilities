package Main.Views;

import Main.Driver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Peter on 04/10/16.
 */
public class StartView {
  private final Driver caller;
  public JButton connectButton;
  public JPanel panelMain;
  private JTextArea textArea;
  private JTextField usernameField;

  public StartView(Driver driver) {
    caller = driver;
    connectButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        caller.hi();
      }
    });
  }

}
