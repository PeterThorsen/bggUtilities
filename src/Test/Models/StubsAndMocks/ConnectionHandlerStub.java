package Test.Models.StubsAndMocks;

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

/**
 * Created by Peter on 28/09/16.
 */
public class ConnectionHandlerStub implements Main.Models.Network.IConnectionHandler {

  @Override
  public Document getCollection(String username) {
    try {
      File file = new File("res/bggCollectionData.xml");
      return buildDoc(file);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public Document getGames(int[] gameIDArray) {
    try {
      File file = new File("res/thingData.xml");
      return buildDoc(file);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public Document getPlays(String username) {
    try {
      File file = new File("res/playsData.xml");
      return buildDoc(file);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  private Document buildDoc(File file) {
    try {
      DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
      Document document = documentBuilder.parse(file);
      return document;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
}
