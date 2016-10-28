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
  private JPanel panelMain;

  public static void main(String[] args) {
    new Driver();
  }

  public Driver() {
    loginController = new LoginController(new ReleaseStartupFactory());
    frame = new JFrame("bggUtilities");
    StartView startView = new StartView(this);
    panelMain = startView.panelMain;
    frame.setContentPane(panelMain);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    frame.pack();
    frame.setVisible(true);
    frame.setLocationRelativeTo(null);
    frame.setResizable(false);

  }
  public void tryLogin(String givenUsername) {
    Thread t = new Thread(new Runnable() {
      @Override
      public void run() {
        // panelMain button!
        mainController = loginController.verifyUser(givenUsername);
        if(mainController == null) {
          // Error message
          return;
        }
        startMainView();
      }
    });

    t.start();
  }

  private void startMainView() {
    MainView mainView = new MainView(mainController);
    frame.setContentPane(mainView.panel1);
    frame.pack();
    frame.setLocationRelativeTo(null);




    //mainView.textArea1.setText(mainController.getAllGames().get(0).getName());
  }

}
