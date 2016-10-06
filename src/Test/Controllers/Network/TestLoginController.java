package Test.Controllers.Network;

import Main.Controllers.LoginController;
import Main.Factories.IFactory;
import Main.Factories.ReleaseFactory;
import Main.Models.Storage.ICollectionBuilder;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.*;

/**
 * Created by Peter on 05/10/2016.
 */
public class TestLoginController {


  private LoginController controller;
  private ICollectionBuilder collectionBuilder;

  @Before
  public void setUp() {
    IFactory factory = new ReleaseFactory();
    controller = new LoginController(factory);
  }

  @Test
  public void controllerShouldVerifyCorrectUsername() {
    String username="cwaq";
    assertNotNull(controller.verifyUser(username));
  }

  @Test
  public void controllerShouldReturnErrorOnWrongUsername() {
    String username = "notanusername";
    assertNull(controller.verifyUser(username));
  }
}
