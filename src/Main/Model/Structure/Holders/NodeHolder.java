package Main.Model.Structure.Holders;

import org.w3c.dom.Node;

public class NodeHolder {
  public final Node statsNode;
  public final Node numPlaysNode;

  public NodeHolder(Node statsNode, Node numPlaysNode) {
    this.statsNode = statsNode;
    this.numPlaysNode = numPlaysNode;
  }
}
