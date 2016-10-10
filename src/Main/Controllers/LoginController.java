package Main.Controllers;

import Main.Factories.IStartupFactory;
import Main.Models.Storage.ICollectionBuilder;

/**
 * Created by Peter on 05/10/2016.
 */
public class LoginController implements ILoginController {

  private final IStartupFactory factory;

  public LoginController(IStartupFactory factory) {
    this.factory = factory;
  }

  @Override
  public FacadeController verifyUser(String username) {
    ICollectionBuilder collectionBuilder = factory.getCollectionBuilder();
    if (collectionBuilder.verifyUser(username)) {
      return new FacadeController(collectionBuilder, username);
    }
    return null;
  }
}
