package Models.Storage;

import Main.Containers.BoardGame;
import Main.Containers.BoardGameCollection;
import Main.Containers.GameCategory;
import Main.Containers.Player;
import Main.Models.Network.IConnectionHandler;
import Main.Models.Storage.CollectionBuilder;
import Main.Models.Storage.ICollectionBuilder;
import Main.Containers.GameMechanism;
import Models.StubsAndMocks.ConnectionHandlerStub;
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
    BoardGame game = games[23];
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
    BoardGame game = games[23];
    minPlayers = game.getMinPlayers();
    assertEquals(2, minPlayers);
  }

  @Test
  public void hiveShouldHaveMaxPlayers2() {
    int maxPlayers = 0;
    BoardGame game = games[23];
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
    BoardGame game = games[23];
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
  public void hiveShouldHaveNumPlays26() {
    int numPlays;
    BoardGame game = games[23];
    numPlays = game.getNumberOfPlays();
    assertEquals(26, numPlays);
  }

  @Test
  public void hiveShouldHaveComplexityBetween2And3() {
    double complexity;
    BoardGame game = games[23];
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
  public void dixitShouldHaveAverageRating7Dot54213() {
    BoardGame game = games[14];
    System.out.println(game);
    assertEquals(7.54213, Double.valueOf(game.getAverageRating()));
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
    BoardGame game = games[24];
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
    BoardGame game = games[23];
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
    BoardGame game = games[23];
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

  @Test
  public void camelUpShouldHaveBestWith4_5() {
    BoardGame game = games[3];
    int[] bestWith = game.getBestWith();

    boolean found4 = false;
    boolean found5 = false;

    for (int i = 0; i < bestWith.length; i++) {
      if(bestWith[i] == 4) {
        found4 = true;
      }
      else if (bestWith[i] == 5) {
        found5 = true;
      }
    }
    assertTrue(found4);
    assertTrue(found5);
  }


  @Test
  public void camelUpShouldHaveRecommendedWith2_3_6_7_8() {
    BoardGame game = games[3];
    int[] recommendedWith = game.getRecommendedWith();

    boolean found2 = false;
    boolean found3 = false;
    boolean found6 = false;
    boolean found7 = false;
    boolean found8 = false;

    for (int i = 0; i < recommendedWith.length; i++) {
      if(recommendedWith[i] == 2) {
        found2 = true;
      }
      else if (recommendedWith[i] == 3) {
        found3 = true;
      }
      else if (recommendedWith[i] == 6) {
        found6 = true;
      }
      else if (recommendedWith[i] == 7) {
        found7 = true;
      }
      else if (recommendedWith[i] == 8) {
        found8 = true;
      }
    }
    assertTrue(found2);
    assertTrue(found3);
    assertTrue(found6);
    assertTrue(found7);
    assertTrue(found8);
  }

  @Test
  public void camelUpShouldNotHaveRecommendedWith4_5() {
    BoardGame game = games[3];
    int[] recommendedWith = game.getRecommendedWith();

    boolean found4 = false;
    boolean found5 = false;

    for (int i = 0; i < recommendedWith.length; i++) {
      if(recommendedWith[i] == 4) {
        found4 = true;
      }
      else if (recommendedWith[i] == 5) {
        found5 = true;
      }
    }
    assertFalse(found4);
    assertFalse(found5);
  }

  @Test
  public void camelUpShouldNotHaveBestWith2_3_6_7_8() {
    BoardGame game = games[3];
    int[] bestWith = game.getBestWith();

    boolean found2 = false;
    boolean found3 = false;
    boolean found6 = false;
    boolean found7 = false;
    boolean found8 = false;

    for (int i = 0; i < bestWith.length; i++) {
      if(bestWith[i] == 2) {
        found2 = true;
      }
      else if (bestWith[i] == 3) {
        found3 = true;
      }
      else if (bestWith[i] == 6) {
        found6 = true;
      }
      else if (bestWith[i] == 7) {
        found7 = true;
      }
      else if (bestWith[i] == 8) {
        found8 = true;
      }
    }
    assertFalse(found2);
    assertFalse(found3);
    assertFalse(found6);
    assertFalse(found7);
    assertFalse(found8);
  }

  @Test
  public void agricolaShouldHaveType_strategygames() {
    BoardGame game = games[0];
    String type = game.getType();
    assertEquals("strategygames", type);
  }

  @Test
  public void bohnanzaShouldHaveType_familygames() {
    BoardGame game = games[2];
    String type = game.getType();
    assertEquals("familygames", type);  }
}
