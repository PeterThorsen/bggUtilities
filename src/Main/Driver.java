package Main;

import Main.Controllers.DataDisplayController;
import Main.Models.Network.ConnectionHandler;
import Main.Models.Network.IConnectionHandler;
import Main.Models.Storage.CollectionBuilder;
import Main.Models.Storage.ICollectionBuilder;
import Main.Views.StartView;
import Main.Views.WelcomeView;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Peter on 12/09/16.
 */
public class Driver {

  DataDisplayController controller;

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
  public void login(String givenUsername) {
    IConnectionHandler connectionHandler = new ConnectionHandler();
    ICollectionBuilder collectionBuilder = new CollectionBuilder(connectionHandler);
    controller = new DataDisplayController(collectionBuilder, givenUsername);
    controller.verifyUser();
    System.out.println("hi!");

  }
}
