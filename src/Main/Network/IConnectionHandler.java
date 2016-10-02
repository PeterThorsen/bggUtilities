package Main.Network;

import org.w3c.dom.Document;

/**
 * Created by Peter on 28/09/16.
 */
public interface IConnectionHandler {
  Document getCollection(String username);
  Document getGame(int gameID);
}
