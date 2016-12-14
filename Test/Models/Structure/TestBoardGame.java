package Models.Structure;

import Main.Models.Structure.BoardGame;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Peter on 13-Nov-16.
 */
public class TestBoardGame {

  @Test
  public void testEqualsWithTwoObjectExactlyTheSame() {
    BoardGame game1 = new BoardGame("Agricola",31260,1,5,30,150,String.valueOf(8),0, "8.07978", "strategygames");
    BoardGame game2 = new BoardGame("Agricola",31260,1,5,30,150,String.valueOf(8),0, "8.07978", "strategygames");

    assertTrue(game1.equals(game2));
  }

  @Test
  public void testEqualsWithTwoObjectWithSameNameButDifferentStats() {
    BoardGame game1 = new BoardGame("Agricola",31260,1,5,30,150,String.valueOf(8),0, "8.07978", "strategygames");
    BoardGame game2 = new BoardGame("Agricola",31260,1,5,30,150,String.valueOf(8),3, "8.07978", "strategygames");

    assertTrue(game1.equals(game2));
  }

  @Test
  public void testEqualsWithTwoDifferentGameNames() {

    BoardGame game1 = new BoardGame("Agricola",31260,1,5,30,150,String.valueOf(8),0, "8.07978", "strategygames");
    BoardGame game2 = new BoardGame("Hive",31260,1,5,30,150,String.valueOf(8),0, "8.07978", "abstracts");

    assertFalse(game1.equals(game2));
  }

  @Test
  public void testHashCodeOfTwoBoardGamesWithSameName() {
    BoardGame game1 = new BoardGame("Agricola",31260,1,5,30,150,String.valueOf(8),0, "8.07978", "strategygames");
    BoardGame game2 = new BoardGame("Agricola",31260,1,5,30,150,String.valueOf(8),0, "8.07978", "strategygames");

    assertEquals(game1.hashCode(), game2.hashCode());
  }

  @Test
  public void testHashCodeOfTwoBoardGamesWithSameNameButDifferentStats() {
    BoardGame game1 = new BoardGame("Agricola",31260,1,5,30,150,String.valueOf(8),0, "8.07978", "strategygames");
    BoardGame game2 = new BoardGame("Agricola",31260,1,5,30,150,String.valueOf(8),3, "8.07978", "strategygames");

    assertEquals(game1.hashCode(), game2.hashCode());
  }

  @Test
  public void testHashCodeOfTwoGamesWithDifferentNames() {
    BoardGame game1 = new BoardGame("Agricola",31260,1,5,30,150,String.valueOf(8),0, "8.07978", "strategygames");
    BoardGame game2 = new BoardGame("Hive",31260,1,5,30,150,String.valueOf(8),0, "8.07978", "abstracts");

    assertNotEquals(game1.hashCode(), game2.hashCode());
  }
}
