package Main;

import Main.Controllers.DataDisplayController;
import Main.Controllers.FacadeController;
import Main.Controllers.LoginController;
import Main.Factories.ReleaseFactory;
import Main.Models.Network.ConnectionHandler;
import Main.Models.Network.IConnectionHandler;
import Main.Models.Storage.CollectionBuilder;
import Main.Models.Storage.ICollectionBuilder;
import Main.Views.StartView;

import javax.swing.*;

/**
 * Created by Peter on 12/09/16.
 */
public class Driver {

  LoginController loginController;
  private FacadeController mainController;

  public static void main(String[] args) {
    new Driver();
  }

  public Driver() {
    loginController = new LoginController(new ReleaseFactory());
    JFrame frame = new JFrame("bggUtilities");
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
      System.out.println("A");
      return;
    }
    System.out.println("B");

    // Start new view with mainController sent as parameter in constructor
  }
}
