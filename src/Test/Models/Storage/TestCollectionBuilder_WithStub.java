package Test.Models.Storage;

import Main.Containers.BoardGame;
import Main.Containers.BoardGameCollection;
import Main.Containers.GameCategory;
import Main.Containers.Player;
import Main.Models.Network.IConnectionHandler;
import Main.Models.Storage.CollectionBuilder;
import Main.Models.Storage.ICollectionBuilder;
import Test.Containers.GameMechanism;
import Test.Models.StubsAndMocks.ConnectionHandlerStub;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.*;

/**
 * Created by Peter on 28/09/16.
 */
public class TestCollectionBuilder_WithStub {
  IConnectionHandler connectionHandler;
  ICollectionBuilder collectionBuilder;
  BoardGameCollection collection;
  BoardGame[] games;

  @Before
  public void setUp() {
    connectionHandler = new ConnectionHandlerStub();
    collectionBuilder = new CollectionBuilder(connectionHandler);
    collection = collectionBuilder.getCollection("cwaq");
    games = collection.getGames();
  }

  @Test
  public void connectionHandlerStubShouldWork() {
    assertNotNull(collection);
  }

  @Test
  public void collectionShouldContainArrayListOfBoardgames() {
    assertNotNull(games);
  }

  @Test
  public void collectionShouldContainNonEmptyListOfBoardGames() {
    assertTrue(games.length > 0);
  }

  @Test
  public void collectionShouldContainAgricola() {
    boolean agricolaExists = false;

    for (BoardGame game : games) {
      if (game.getName().equals("Agricola")) {
        agricolaExists = true;
        break;
      }
    }
    assertTrue(agricolaExists);
  }

  @Test
  public void collectionShouldContainHive() {
    boolean hiveExists = false;

    for (BoardGame game : games) {
      if (game.getName().equals("Hive")) {
        hiveExists = true;
        break;
      }
    }
    assertTrue(hiveExists);
  }

  @Test
  public void collectionShouldNotContainMonopoly() {
    boolean monopolyExists = false;

    for (BoardGame game : games) {
      if (game.getName().equals("Monopoly")) {
        monopolyExists = true;
        break;
      }
    }
    assertFalse(monopolyExists);
  }

  @Test
  public void agricolaShouldHaveUniqueID31260() {
    int uniqueID = 0;
    BoardGame game = games[0];
    uniqueID = game.getID();
    assertEquals(31260, uniqueID);
  }

  @Test
  public void hiveShouldHaveUniqueID2655() {
    int uniqueID = 0;
    BoardGame game = games[20];
    uniqueID = game.getID();
    assertEquals(2655, uniqueID);
  }

  @Test
  public void agricolaShouldHaveMinPlayers1() {
    int minPlayers = 0;
    BoardGame game = games[0];
    minPlayers = game.getMinPlayers();
    assertEquals(1, minPlayers);
  }

  @Test
  public void hiveShouldHaveMinPlayers2() {
    int minPlayers = 0;
    BoardGame game = games[20];
    minPlayers = game.getMinPlayers();
    assertEquals(2, minPlayers);
  }

  @Test
  public void hiveShouldHaveMaxPlayers2() {
    int maxPlayers = 0;
    BoardGame game = games[20];
    maxPlayers = game.getMaxPlayers();
    assertEquals(2, maxPlayers);
  }

  @Test
  public void agricolaShouldHaveMaxPlayers5() {
    int maxPlayers = 0;
    BoardGame game = games[0];
    maxPlayers = game.getMaxPlayers();
    assertEquals(5, maxPlayers);
  }

  @Test
  public void agricolaShouldHaveMinPlaytime30Min() {
    int minPlaytime = 0;
    BoardGame game = games[0];
    minPlaytime = game.getMinPlaytime();
    assertEquals(30, minPlaytime);
  }

  @Test
  public void agricolaShouldHaveMaxPlaytime150Min() {
    int maxPlaytime;
    BoardGame game = games[0];
    maxPlaytime = game.getMaxPlaytime();
    assertEquals(150, maxPlaytime);
  }

  @Test
  public void agricolaShouldHavePersonalRating8() {
    String personalRating;
    BoardGame game = games[0];
    personalRating = game.getPersonalRating();
    int rating = Integer.valueOf(personalRating);
    assertEquals(8, rating);
  }

  @Test
  public void hiveShouldHavePersonalRating10() {
    String personalRating;
    BoardGame game = games[20];
    personalRating = game.getPersonalRating();
    int rating = Integer.valueOf(personalRating);
    assertEquals(10, rating);
  }

  @Test
  public void agricolaShouldHaveNumPlays7() {
    int numPlays;
    BoardGame game = games[0];
    numPlays = game.getNumberOfPlays();
    assertEquals(7, numPlays);
  }

  @Test
  public void hiveShouldHaveNumPlays21() {
    int numPlays;
    BoardGame game = games[20];
    numPlays = game.getNumberOfPlays();
    assertEquals(21, numPlays);
  }

  @Test
  public void hiveShouldHaveComplexityBetween2And3() {
    double complexity;
    BoardGame game = games[20];
    complexity = game.getComplexity();
    assertTrue(complexity > 2.0 && complexity < 3.0);
  }

  @Test
  public void agricolaShouldHaveComplexityBetween3And4() {
    double complexity;
    BoardGame game = games[0];
    complexity = game.getComplexity();
    assertTrue(complexity > 3.0 && complexity < 4.0);
  }

  @Test
  public void dixitShouldHaveAverageRating7Dot54256() {
    BoardGame game = games[13];
    assertEquals(7.54256, Double.valueOf(game.getAverageRating()));
  }

  @Test
  public void playersShouldContainMultiplePeople() {
    Player[] players = collectionBuilder.getPlayers();
    assertTrue(players.length > 1);
  }

  @Test
  public void agricolaShouldNotBeDefinedAsExpansion() {
    BoardGame game = games[0];
    assertFalse(game.isExpansion());
  }

  @Test
  public void HiveTheLadybugShouldBeDefinedAsExpansion() {
    BoardGame game = games[21];
    assertTrue(game.isExpansion());
  }

  @Test
  public void agricolaShouldHaveCategoryArrayContainingAnimalsEconomicAndFarming() {
    BoardGame game = games[0];
    GameCategory[] categories = game.getCategories();
    boolean foundAnimals = false;
    boolean foundEconomic = false;
    boolean foundFarming = false;

    for (int i = 0; i < categories.length; i++) {
      GameCategory cat = categories[i];
      if(cat.category.equals("Animals")) foundAnimals = true;
      else if (cat.category.equals("Economic")) foundEconomic = true;
      else if (cat.category.equals("Farming")) foundFarming = true;
    }

    assertTrue(foundAnimals);
    assertTrue(foundEconomic);
    assertTrue(foundFarming);
  }

  @Test
  public void hiveShouldHaveCategoryArrayContainingAbstractStrategyAndAnimals() {
    BoardGame game = games[21];
    GameCategory[] categories = game.getCategories();
    boolean foundAnimals = false;
    boolean foundAbstractStrategy = false;

    for (int i = 0; i < categories.length; i++) {
      GameCategory cat = categories[i];
      if(cat.category.equals("Animals")) foundAnimals = true;
      else if (cat.category.equals("Abstract Strategy")) foundAbstractStrategy = true;
    }

    assertTrue(foundAnimals);
    assertTrue(foundAbstractStrategy);
  }

  @Test
  public void agricolaShouldHaveMechanicsArrayOf_AreaEnclosure_CardDrafting_HandManagement_WorkerPlacement() {
    BoardGame game = games[0];
    GameMechanism[] mechanisms = game.getMechanisms();
    boolean foundAreaEnclosure = false;
    boolean foundCardDrafting = false;
    boolean foundHandManagement = false;
    boolean foundWorkerPlacement = false;

    for (int i = 0; i < mechanisms.length; i++) {
      GameMechanism mech = mechanisms[i];
      if(mech.mechanism.equals("Area Enclosure")) foundAreaEnclosure = true;
      else if (mech.mechanism.equals("Card Drafting")) foundCardDrafting = true;
      else if (mech.mechanism.equals("Hand Management")) foundHandManagement = true;
      else if (mech.mechanism.equals("Worker Placement")) foundWorkerPlacement = true;
    }

    assertTrue(foundAreaEnclosure);
    assertTrue(foundCardDrafting);
    assertTrue(foundHandManagement);
    assertTrue(foundWorkerPlacement);
  }

  @Test
  public void hiveShouldHaveMechanicsArrayOf_GridMovement_TilePlacement() {
    BoardGame game = games[20];
    GameMechanism[] mechanisms = game.getMechanisms();
    boolean foundGridMovement = false;
    boolean foundTilePlacement = false;

    for (int i = 0; i < mechanisms.length; i++) {
      GameMechanism mech = mechanisms[i];
      if(mech.mechanism.equals("Grid Movement")) foundGridMovement = true;
      else if (mech.mechanism.equals("Tile Placement")) foundTilePlacement = true;
    }

    assertTrue(foundGridMovement);
    assertTrue(foundTilePlacement);
  }

  @Test
  public void agricolaShouldHaveBestWith3And4() {
    BoardGame game = games[0];
    int[] bestWith = game.getBestWith();
    
    boolean found3 = false;
    boolean found4 = false;

    for (int i = 0; i < bestWith.length; i++) {
      if(bestWith[i] == 3) {
        found3 = true;
      }
      else if (bestWith[i] == 4) {
        found4 = true;
      }
    }
    assertTrue(found3);
    assertTrue(found4);
  }

  @Test
  public void agricolaShouldHaveRecommendedWith3And4() {
    BoardGame game = games[0];
    int[] recommendedWith = game.getRecommendedWith();

    boolean found1 = false;
    boolean found2 = false;

    for (int i = 0; i < recommendedWith.length; i++) {
      if(recommendedWith[i] == 1) {
        found1 = true;
      }
      else if (recommendedWith[i] == 2) {
        found2 = true;
      }
    }
    assertTrue(found1);
    assertTrue(found2);
  }
}
