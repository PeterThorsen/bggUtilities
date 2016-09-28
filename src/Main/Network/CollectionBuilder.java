package Main.Network;

import Main.Containers.BoardGameCollection;
import Main.Containers.Boardgame;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

/**
 * Created by Peter on 28/09/16.
 */
public class CollectionBuilder implements ICollectionBuilder {
  private IConnectionHandler connectionHandler;
  public CollectionBuilder(IConnectionHandler connectionHandler) {
    this.connectionHandler = connectionHandler;
  }

  @Override
  public BoardGameCollection getCollection(String username) {

    Document document = connectionHandler.getCollection(username);

    if(document == null) {
      // Invalid user
      return null;
    }
    /**
    NodeList nodeList = document.getElementsByTagName("item");
    ArrayList<Boardgame> listOfGames = new ArrayList<Boardgame>();
    for(int i = 0; i< nodeList.getLength(); i++) {

    } */
    BoardGameCollection collection = new BoardGameCollection();
    return collection;
  }
}
