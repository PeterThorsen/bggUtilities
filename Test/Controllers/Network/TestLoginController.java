package Controllers.Network;

import Main.Controller.SubControllers.LoginController;
import Main.Controller.Factories.IStartupFactory;
import Main.Controller.Factories.ReleaseStartupFactory;
import Main.Model.Storage.ICollectionBuilder;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;

/**
 * Created by Peter on 05/10/2016.
 */
public class TestLoginController {


  private LoginController controller;
  private ICollectionBuilder collectionBuilder;

  @Before
  public void setUp() {
    IStartupFactory factory = new ReleaseStartupFactory();
    controller = new LoginController(factory);
  }

  @Test
  public void controllerShouldVerifyCorrectUsername() {
    String username="cwaq";
    assertNotNull(controller.verifyUser(username, null));
  }

  @Test
  public void controllerShouldReturnErrorOnWrongUsername() {
    String username = "notanusername";
    assertNull(controller.verifyUser(username, null));
  }
}
