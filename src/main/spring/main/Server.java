package spring.main;

import Controller.FacadeController;
import Controller.Factories.ReleaseStartupFactory;
import Controller.SubControllers.LoginController;
import Model.Structure.BoardGame;
import Model.Structure.Play;
import Model.Structure.Player;
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
    if (controller != null) {
      return true;
    }

    if (!isUsernameValid(userName)) return false;
    controller = tryLogin(userName);
    return controller != null;
  }

  @CrossOrigin
  @RequestMapping("/forceLogin")
  public boolean forceLogin() {
    String userName = controller.username;
    controller = null;
    return login(userName);
  }


  @CrossOrigin
  @RequestMapping("/getGames")
  public String getGames(@RequestParam(value = "expansions", defaultValue = "") boolean expansions) {
    Gson gson = new Gson();
    BoardGame[] games = controller.getAllGames(expansions);
    return gson.toJson(games);
  }

  @CrossOrigin
  @RequestMapping("/getPlays")
  public String getPlays(@RequestParam(value = "id", defaultValue = "") int id) {
    Gson gson = new Gson();
    Play[] plays = controller.getSortedPlays(id).clone();
    BoardGame[] tempGames = new BoardGame[plays.length];
    for (int i = 0; i < plays.length; i++) {

      tempGames[i] = plays[i].game;
      plays[i].game = null;
    }
    String returnValue = gson.toJson(plays);
    for (int i = 0; i < plays.length; i++) {
      plays[i].game = tempGames[i];
    }

    return returnValue;
  }

  @CrossOrigin
  @RequestMapping("/getAllPlays")
  public String getAllPlays() {
    Gson gson = new Gson();
    Play[] plays = controller.getAllPlaysSorted();
    BoardGame[] tempGames = new BoardGame[plays.length];
    for (int i = 0; i < plays.length; i++) {
      tempGames[i] = plays[i].game;
      plays[i].game = new BoardGame(plays[i].game.name, plays[i].game.id, 0, 0, 0,
              0, null, 0, null, null, plays[i].game.image);
    }
    String returnValue = gson.toJson(plays);
    for (int i = 0; i < plays.length; i++) {
      plays[i].game = tempGames[i];
    }
    return returnValue;
  }

  @CrossOrigin
  @RequestMapping("/getAllPlayers")
  public String getAllPlayers() {
    Gson gson = new Gson();
    Player[] players = controller.getAllPlayers();
    return gson.toJson(players);
  }


  private FacadeController tryLogin(String givenUsername) {
    return loginController.verifyUser(givenUsername, null);
  }


  private boolean isUsernameValid(String userName) {
    return userName.indexOf(' ') == -1;
  }
}