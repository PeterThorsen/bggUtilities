package spring.main;

import Controller.FacadeController;
import Controller.Factories.ReleaseStartupFactory;
import Controller.SubControllers.LoginController;
import Model.Structure.BoardGame;
import com.google.gson.Gson;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Server {

  FacadeController controller;

  private final LoginController loginController = new LoginController(new ReleaseStartupFactory());

  @CrossOrigin
  @RequestMapping("/login")
  public boolean login(@RequestParam(value = "userName", defaultValue = "") String userName) {
    if(!isUsernameValid(userName)) return false;
    controller = tryLogin(userName);
    return controller != null;
  }


  @CrossOrigin
  @RequestMapping("/getGames")
  public String getGames() {

    Gson gson = new Gson();
    BoardGame[] games = controller.getAllGames();

    return gson.toJson(games);
  }


  private FacadeController tryLogin(String givenUsername) {
    return loginController.verifyUser(givenUsername, null);
  }



  private boolean isUsernameValid(String userName) {
    return userName.indexOf(' ') == -1;
  }
}