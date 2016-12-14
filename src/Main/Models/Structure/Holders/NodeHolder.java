package Main.Models.Structure.Holders;

import org.w3c.dom.Node;

/**
 * Created by Peter on 06-Dec-16.
 */
public class NodeHolder {
  public final Node statsNode;
  public final Node numPlaysNode;

  public NodeHolder(Node statsNode, Node numPlaysNode) {
    this.statsNode = statsNode;
    this.numPlaysNode = numPlaysNode;
  }
}
