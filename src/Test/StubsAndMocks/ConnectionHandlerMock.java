package Test.StubsAndMocks;

import Main.Containers.BoardGameCollection;
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
      System.out.println("before file");
      File file = new File("res/bggCollectionData.xml");
      System.out.println("after file");

      DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
              .newInstance();
      DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
      Document document = documentBuilder.parse(file);
      System.out.println("returning doc");

      return document;
    }
    catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public Document getGame(String gameID) {
    return null;
  }
}
