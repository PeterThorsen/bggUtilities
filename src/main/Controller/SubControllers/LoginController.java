package main.Controller.SubControllers;

import main.Controller.FacadeController;
import main.Controller.Factories.IStartupFactory;
import main.Model.Storage.ICollectionBuilder;

import javax.swing.*;

/**
 * Created by Peter on 05/10/2016.
 */
public class LoginController {

  private final IStartupFactory factory;

  public LoginController(IStartupFactory factory) {
    this.factory = factory;
  }

  public FacadeController verifyUser(String username, JTextArea loadingInfoTextArea) {
    ICollectionBuilder collectionBuilder = factory.getCollectionBuilder();
    if (collectionBuilder.verifyUser(username)) {

      if(loadingInfoTextArea != null) {
        loadingInfoTextArea.setText("User verified, retrieving user data!");
      }
      return new FacadeController(collectionBuilder, username, factory.getGameNightValues());
    }
    return null;
  }
}
