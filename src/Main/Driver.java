package Main;

/**
 * Created by Peter on 12/09/16.
 */
public class Driver {

  private String url;

  public static void main(String[] args) {
    new Driver();
  }

  public Driver() {
    url = "https://www.boardgamegeek.com/xmlapi/collection/cwaq";
    //https://www.boardgamegeek.com/xmlapi2/plays?username=cwaq
    //https://www.boardgamegeek.com/xmlapi2/thing?stats=1&id=2655a

  }
}
