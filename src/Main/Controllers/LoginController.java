package Main.Controllers;

import Main.Factories.IFactory;
import Main.Models.Storage.ICollectionBuilder;

/**
 * Created by Peter on 05/10/2016.
 */
public class LoginController {

  private final IFactory factory;

  public LoginController(IFactory factory) {
    this.factory = factory;
  }

  public FacadeController verifyUser(String username) {
    ICollectionBuilder collectionBuilder = factory.getCollectionBuilder();
    if(collectionBuilder.verifyUser(username)){
      return new FacadeController();
    }
    return null;
  }
}
