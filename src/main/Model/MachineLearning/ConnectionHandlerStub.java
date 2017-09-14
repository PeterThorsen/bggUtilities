package Model.MachineLearning;

import Model.Network.IConnectionHandler;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;

class ConnectionHandlerStub implements IConnectionHandler {

  @Override
  public Document getCollection(String username) {
    try {
      File file = new File("Test/res/bggCollectionData.xml");
      return buildDoc(file);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public Document getGames(int[] gameIDArray) {
    try {
      File file = new File("Test/res/thingData.xml");
      return buildDoc(file);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public ArrayList<Document> getPlays(String username) {
    try {
      File file1 = new File("Test/res/plays1.xml");
      File file2 = new File("Test/res/plays2.xml");
      ArrayList<Document> arr = new ArrayList<>();
      arr.add(buildDoc(file1));
      arr.add(buildDoc(file2));
      return arr;
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
