package Controllers;

import Main.Containers.BoardGame;
import Main.Containers.BoardGameCounter;
import Main.Containers.BoardGameSuggestion;
import Main.Containers.Player;
import Main.Controllers.FacadeController;
import Main.Models.Network.IConnectionHandler;
import Main.Models.Storage.CollectionBuilder;
import Main.Models.Storage.ICollectionBuilder;
import Models.StubsAndMocks.ConnectionHandlerStub;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Peter on 14/11/2016.
 *
 *
 0 : Alf
 1 : Andreas
 2 : Arnar
 3 : Bolette
 4 : Bulbjerg
 5 : Camilla
 6 : Charlotte
 7 : Christian
 8 : Daniella
 9 : Emil
 10 : Famse
 11 : Inna
 12 : Jesper
 13 : Joachim
 14 : Josefine
 15 : Lisbeth
 16 : Liss
 17 : Marian
 18 : Martin
 19 : Merle
 20 : Michael
 21 : Michelle
 22 : Mikkel
 23 : Morten
 24 : Palle
 25 : Poul
 26 : Signe
 27 : Steffen
 28 : Thomas
 29 : Ulrik
 */
public class TestLogicController {
  private FacadeController facadeController;
  private Player[] allPlayers;

  @Before
  public void setup() {
    IConnectionHandler stub = new ConnectionHandlerStub();
    ICollectionBuilder collectionBuilder = new CollectionBuilder(stub);
    facadeController = new FacadeController(collectionBuilder, "cwaq");
    allPlayers = facadeController.getAllPlayers();
  }

  /**
   * Player 10 has played sequence twice and resistance three times.
   * Since sequence can be played by two players in time 10-120 minutes, it should be on the suggested list
   */
  @Test
  public void allGamesShouldContainSequence() {
    Player[] players = new Player[1];
    players[0] = allPlayers[10];
    BoardGame[] allGames = facadeController.suggestGames(players, 40).allOptions;
    boolean foundGame = false;
    for (BoardGame game : allGames) {
      if(game.getName().equals("Sequence")) {
        foundGame = true;
        break;
      }
    }
    assertTrue(foundGame);
  }

  /**
   * Player 10 has played sequence twice and resistance three times.
   * Sequence has a complexity of 1.37 and resistance a weight of 1.64.
   * Camel up has a weight of 1.50 and a playtime of 20-30 minutes. It can also play 2 players,
   * which is the main criteria.
   */
  @Test
  public void allGamesShouldContainCamelUp() {
    Player[] players = new Player[1];
    players[0] = allPlayers[10];
    BoardGameSuggestion suggestions = facadeController.suggestGames(players, 100);
    BoardGame[] allGames = suggestions.allOptions;

    boolean foundGame = false;
    for (BoardGame game : allGames) {
      if(game.getName().equals("Camel Up")) {
        foundGame = true;
        break;
      }
    }
    assertTrue(foundGame);
  }

  @Test
  public void allGamesShouldNotContainResistanceAsPlayerNumberIsWrong() {
    Player[] players = new Player[1];
    players[0] = allPlayers[10];
    BoardGame[] allGames = facadeController.suggestGames(players, 40).allOptions;

    boolean foundGame = false;
    for (BoardGame game : allGames) {
      if(game.getName().equals("The Resistance")) {
        foundGame = true;
        break;
      }
    }
    assertFalse(foundGame);
  }

  @Test
  public void allGamesForSixPlayersShouldContainResistanceAsTheyHaveAllPlayedIt() {
    Player[] players = new Player[5];
    players[0] = allPlayers[10];
    players[1] = allPlayers[0];
    players[2] = allPlayers[6];
    players[3] = allPlayers[22];
    players[4] = allPlayers[15];
    BoardGame[] allGames = facadeController.suggestGames(players, 40).allOptions;

    boolean foundGame = false;
    for (BoardGame game : allGames) {
      if(game.getName().equals("The Resistance")) {
        foundGame = true;
        break;
      }
    }
    assertTrue(foundGame);
  }

  @Test
  public void allGamesForSixPlayersShouldContainDixitOdysseyAsCriteriasAreFilledAndRatingIsHigh() {
    Player[] players = new Player[5];
    players[0] = allPlayers[10];
    players[1] = allPlayers[0];
    players[2] = allPlayers[6];
    players[3] = allPlayers[22];
    players[4] = allPlayers[15];
    BoardGame[] allGames = facadeController.suggestGames(players, 40).allOptions;

    boolean foundGame = false;
    for (BoardGame game : allGames) {
      if(game.getName().equals("Dixit Odyssey")) {
        foundGame = true;
        break;
      }
    }
    assertTrue(foundGame);
  }

  @Test
  public void allGamesShouldNotContainCodeNamesOrBohnanza() {
    Player[] players = new Player[1];
    players[0] = allPlayers[10];
    BoardGameSuggestion suggestions = facadeController.suggestGames(players, 60);
    BoardGame[] allGames = suggestions.allOptions;

    boolean foundCodenames = false;
    boolean foundBohnanza = false;
    for (BoardGame game : allGames) {
      if(game.getName().equals("Codenames")) {
        foundCodenames = true;
      }
      if(game.getName().equals("Bohnanza")) {
        foundBohnanza = true;
      }
    }
    assertFalse(foundBohnanza);
    assertFalse(foundCodenames);
  }

  @Test
  public void expansionsShouldNotBeInAllGames() {
    Player[] players = new Player[1];
    players[0] = allPlayers[10];
    BoardGameSuggestion suggestions = facadeController.suggestGames(players, 60);
    BoardGame[] allGames = suggestions.allOptions;


    boolean foundExpansion = false;
    for (BoardGame game : allGames) {
      if(game.getName().equals("Carcassonne: Expansion 1 – Inns & Cathedrals")) {
        foundExpansion = true;
      }
    }
    assertFalse(foundExpansion);
  }

  // Jaipur is close to the average complexity of the current player. It is a 2 player only game and holds a high
  // personal rating by me. It is also a family (type) card game (category) utilising hand management mechanisms,
  // which is similar to sequence.
  @Test
  public void recommendedGamesShouldContainJaipur() {
    Player[] players = new Player[1];
    players[0] = allPlayers[10];
    BoardGameSuggestion suggestions = facadeController.suggestGames(players, 60);
    BoardGameCounter[] suggestedCombination = suggestions.suggestedCombination;

    boolean found = false;
    for (BoardGameCounter gameCounter : suggestedCombination) {
      if(gameCounter.game.getName().equals("Jaipur")) {
        found = true;
      }
    }
    assertTrue(found);
  }

  @Test
  public void recommendedGamesShouldContainJaipurAndCarcassonne() {
    Player[] players = new Player[1];
    players[0] = allPlayers[10];
    BoardGameSuggestion suggestions = facadeController.suggestGames(players, 80);
    BoardGameCounter[] suggestedCombination = suggestions.suggestedCombination;

    boolean foundJaipur = false;
    boolean foundCarcassonne = false;

    for (BoardGameCounter gameCounter : suggestedCombination) {
      if(gameCounter.game.getName().equals("Jaipur")) {
        foundJaipur = true;
      }
      else if(gameCounter.game.getName().equals("Carcassonne")) {
        foundCarcassonne = true;
      }
      else {
        break; // Too many suggestions, should be exactly these two
      }
    }
    assertTrue(foundJaipur);
    assertTrue(foundCarcassonne);
  }
}
