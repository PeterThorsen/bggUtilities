package Main.Views;

import Main.Driver;

import javax.swing.*;

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

    // Lambda expression. e comes from super action listener. When action happens on connectButton, execute the code below
    connectButton.addActionListener(e -> {
      String givenUsername = usernameField.getText();
      String[] split = givenUsername.split(" ");
      givenUsername = split[0];
      for (int i = 1; i < split.length; i++) {
        givenUsername = givenUsername.concat("%20" + split[i]);
      }
      caller.tryLogin(givenUsername);
    });
  }

}
