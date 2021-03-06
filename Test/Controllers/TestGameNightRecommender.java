package Controllers;

import Controller.FacadeController;
import Model.Logic.ChosenGameNightValues;
import Model.Network.IConnectionHandler;
import Model.Storage.CollectionBuilder;
import Model.Storage.ICollectionBuilder;
import Model.Structure.BoardGame;
import Model.Structure.BoardGameCounter;
import Model.Structure.BoardGameSuggestion;
import Model.Structure.Player;
import Models.StubsAndMocks.ConnectionHandlerStub;
import org.junit.Before;
import org.junit.Ignore;
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
@Ignore
public class TestGameNightRecommender {
  private FacadeController facadeController;
  private Player[] allPlayers;

  @Before
  public void setup() {
    IConnectionHandler stub = new ConnectionHandlerStub();
    ICollectionBuilder collectionBuilder = new CollectionBuilder(stub);
    facadeController = new FacadeController(collectionBuilder, "cwaq", new ChosenGameNightValues());
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
      if(game.name.equals("Sequence")) {
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
      if(game.name.equals("Camel Up")) {
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
      if(game.name.equals("The Resistance")) {
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
      if(game.name.equals("The Resistance")) {
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
      if(game.name.equals("Dixit Odyssey")) {
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
      if(game.name.equals("Codenames")) {
        foundCodenames = true;
      }
      if(game.name.equals("Bohnanza")) {
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
      if(game.name.equals("Carcassonne: Expansion 1 – Inns & Cathedrals")) {
        foundExpansion = true;
      }
    }
    assertFalse(foundExpansion);
  }

  // As Hive is rated low by her
  @Test
  public void recommendedGamesForCharlotteShouldNotContainHiveOn30MinSuggestion() {
    Player[] players = new Player[1];
    players[0] = allPlayers[6];
    BoardGameSuggestion suggestions = facadeController.suggestGames(players, 30);
    BoardGameCounter[] suggestedCombination = suggestions.suggestedCombination;

    boolean found = false;
    for (BoardGameCounter gameCounter : suggestedCombination) {
      if(gameCounter.game.name.equals("Hive")) {
        found = true;
      }
    }
    assertFalse(found);
  }

  // She still doesn't like the game. Suggest longer games instead
  @Test
  public void recommendedGamesForCharlotteShouldNotContainHiveOn150MinSuggestion() {
    Player[] players = new Player[1];
    players[0] = allPlayers[6];
    BoardGameSuggestion suggestions = facadeController.suggestGames(players, 150);
    BoardGameCounter[] suggestedCombination = suggestions.suggestedCombination;

    boolean found = false;
    for (BoardGameCounter gameCounter : suggestedCombination) {
      if(gameCounter.game.name.equals("Hive")) {
        found = true;
      }
    }
    assertFalse(found);
  }

  // Long games should be favored when having the time
  @Test
  public void recommendedGamesForCharlotteWhenHavingTwoHoursShouldContainLongGame() {
    Player[] players = new Player[1];
    players[0] = allPlayers[6];
    BoardGameSuggestion suggestions = facadeController.suggestGames(players, 120);
    BoardGameCounter[] suggestedCombination = suggestions.suggestedCombination;

    boolean found = false;
    for (BoardGameCounter gameCounter : suggestedCombination) {
      if(gameCounter.approximateTime >= 60) {
        found = true;
      }
    }
    assertTrue(found);
  }

  @Test
  public void recommendedGamesForGroupWithLowComplexityGameShouldBeOfLowComplexityGiven60Minutes() {
    Player[] players = new Player[5];
    players[0] = allPlayers[0];
    players[1] = allPlayers[6];
    players[2] = allPlayers[10];
    players[3] = allPlayers[15];
    players[4] = allPlayers[22];
    BoardGameSuggestion suggestions = facadeController.suggestGames(players, 60);
    BoardGameCounter[] suggestedCombination = suggestions.suggestedCombination;

    boolean found = false;
    for (BoardGameCounter gameCounter : suggestedCombination) {
      if(gameCounter.game.complexity > 2) {
        found = true;
        break;
      }
    }
    assertFalse(found);
  }
  @Test
  public void recommendedGamesForGroupWithLowComplexityGameShouldBeOfLowComplexityGiven120Minutes() {
    Player[] players = new Player[5];
    players[0] = allPlayers[0];
    players[1] = allPlayers[6];
    players[2] = allPlayers[10];
    players[3] = allPlayers[15];
    players[4] = allPlayers[22];
    BoardGameSuggestion suggestions = facadeController.suggestGames(players, 120);
    BoardGameCounter[] suggestedCombination = suggestions.suggestedCombination;

    boolean found = false;
    for (BoardGameCounter gameCounter : suggestedCombination) {
      if(gameCounter.game.complexity > 1.85) {
        found = true;
      }
    }
    assertFalse(found);
  }

  @Test
  public void recommendedGamesForGroupWithExperienceOfLongerGamesShouldContainHigherComplexityGamesGiven120Minutes() {
    Player[] players = new Player[2];
    players[0] = allPlayers[6];
    players[1] = allPlayers[18];
    BoardGameSuggestion suggestions = facadeController.suggestGames(players, 120);
    BoardGameCounter[] suggestedCombination = suggestions.suggestedCombination;

    boolean found = false;
    for (BoardGameCounter gameCounter : suggestedCombination) {
      System.out.println(gameCounter.game);
      if(gameCounter.game.complexity > 1.85) {
        found = true;
      }
    }
    assertTrue(found);
  }

  // He dislikes Hive and Hey thats my fish.
  @Test
  public void recommendedGamesForJesperShouldNotBeAbstract() {
    Player[] players = new Player[1];
    players[0] = allPlayers[12];
    BoardGameSuggestion suggestions = facadeController.suggestGames(players, 60);
    BoardGameCounter[] suggestedCombination = suggestions.suggestedCombination;

    boolean found = false;
    for (BoardGameCounter gameCounter : suggestedCombination) {
      if(gameCounter.game.type.equals("abstracts")) {
        found = true;
      }
    }
    assertFalse(found);
  }
  // He dislikes Agricola (too complex at 3.63), but likes Suburbia at 2.78 complexity
  @Test
  public void recommendedGamesForJesperShouldNotBeTooComplex() {
    Player[] players = new Player[1];
    players[0] = allPlayers[12];
    BoardGameSuggestion suggestions = facadeController.suggestGames(players, 120);
    BoardGameCounter[] suggestedCombination = suggestions.suggestedCombination;

    boolean found = false;
    for (BoardGameCounter gameCounter : suggestedCombination) {
      if(gameCounter.game.complexity >= 3.0) {
        found = true;
      }
    }
    assertFalse(found);
  }
}
