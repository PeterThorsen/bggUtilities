package Main;

import Main.Controllers.FacadeController;
import Main.Controllers.LoginController;
import Main.Factories.ReleaseStartupFactory;
import Main.Views.MainView;
import Main.Views.StartView;

import javax.swing.*;

/**
 * Created by Peter on 12/09/16.
 */
public class Driver {

  private LoginController loginController;
  private FacadeController mainController;
  private JFrame frame;

  public static void main(String[] args) {
    new Driver();
  }

  public Driver() {
    loginController = new LoginController(new ReleaseStartupFactory());
    frame = new JFrame("bggUtilities");
    frame.setContentPane(new StartView(this).panelMain);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    frame.pack();
    frame.setVisible(true);
    frame.setLocationRelativeTo(null);

  }
  public void login(String givenUsername) {
    mainController = loginController.verifyUser(givenUsername);
    if(mainController == null) {
      // Error message
      return;
    }
    startMainView();

    // Start new view with mainController sent as parameter in constructor
  }

  private void startMainView() {
    MainView mainView = new MainView();

    frame.setContentPane(new MainView().panel1);
    frame.pack();
  }
}
