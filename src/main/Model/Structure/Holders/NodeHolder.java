package Model.Structure.Holders;

import org.w3c.dom.Node;

public class NodeHolder {
  public final Node statsNode;
  public final Node numPlaysNode;
  public final String image;

  public NodeHolder(Node statsNode, Node numPlaysNode, String image) {
    this.statsNode = statsNode;
    this.numPlaysNode = numPlaysNode;
    this.image = image;
  }
}
