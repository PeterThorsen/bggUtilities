package Main.Containers;

import org.w3c.dom.Node;

/**
 * Created by Peter on 06-Dec-16.
 */
public class ItemPlayersNodesHolder {
  public final Node itemNode;
  public final Node playersNode;

  public ItemPlayersNodesHolder(Node itemNode, Node playersNode) {
    this.itemNode = itemNode;
    this.playersNode = playersNode;
  }
}
