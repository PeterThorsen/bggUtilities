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
    
    ArrayList<Boardgame> games = buildCollection(document);
    BoardGameCollection collection = new BoardGameCollection(games);
    return collection;
  }

  private ArrayList<Boardgame> buildCollection(Document document) {
    NodeList nodeList = document.getElementsByTagName("item");
    ArrayList<Boardgame> games = new ArrayList<>();
    for(int i = 0; i<nodeList.getLength(); i++) {
      String name = nodeList.item(i).getChildNodes().item(1).getTextContent();
      String uniqueIDString = nodeList.item(i).getAttributes().item(1).getTextContent();
      int uniqueID = Integer.valueOf(uniqueIDString);
      Boardgame game = new Boardgame(name, uniqueID);
      games.add(game);
    }
    return games;
  }
}
