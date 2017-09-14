package Model.Network;

import org.w3c.dom.Document;

import java.util.ArrayList;

/**
 * Created by Peter on 28/09/16.
 */
public interface IConnectionHandler {
  Document getCollection(String username);
  Document getGames(int[] gameIDArray);
  ArrayList<Document> getPlays(String username);
}
