package Test.StubsAndMocks;

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

/**
 * Created by Peter on 28/09/16.
 */
public class ConnectionHandlerMock implements Main.Network.IConnectionHandler {
  @Override
  public Document getCollection(String username) {
    try {
      File file = new File("res/bggCollectionData.xml");
      DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
              .newInstance();
      DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
      Document document = documentBuilder.parse(file);

      return document;
    }
    catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public Document getGames(int[] gameIDArray) {
    return null;
  }
}
