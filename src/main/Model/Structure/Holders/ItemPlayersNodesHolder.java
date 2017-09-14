package Model.Structure.Holders;

import org.w3c.dom.Node;

public class ItemPlayersNodesHolder {
  public final Node itemNode;
  public final Node playersNode;

  public ItemPlayersNodesHolder(Node itemNode, Node playersNode) {
    this.itemNode = itemNode;
    this.playersNode = playersNode;
  }
}
