package Main;

import Main.Controllers.FacadeController;
import Main.Controllers.LoginController;
import Main.Factories.ReleaseStartupFactory;
import Main.Views.MainView;
import Main.Views.StartView;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Peter on 12/09/16.
 */
public class Driver {

  private final LoginController loginController;
  private final JTextArea loadingInfoTextArea;
  private FacadeController mainController;
  private final JFrame frame;
  private final JPanel panelMain;

  public static void main(String[] args) {
    new Driver();
  }

  private Driver() {
    loginController = new LoginController(new ReleaseStartupFactory());
    frame = new JFrame("bggUtilities");
    StartView startView = new StartView(this);
    panelMain = startView.panelMain;
    loadingInfoTextArea = startView.loadingInfoTextArea;
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

        if(givenUsername.equals("")) {
          loadingInfoTextArea.setText("No username written!");
          return;
        }
        loadingInfoTextArea.setText("Verifying user. Please wait.");
        mainController = loginController.verifyUser(givenUsername, loadingInfoTextArea);
        if(mainController == null) {
          loadingInfoTextArea.setText("Username not found on\nBoardGameGeek. Are you sure you\nwrote the correct name?");
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
  }

}
