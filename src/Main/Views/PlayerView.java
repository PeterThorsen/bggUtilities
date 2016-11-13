package Main.Views;

import Main.Containers.Player;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Peter on 13-Nov-16.
 */
public class PlayerView {

  public JPanel panel1;
  private JLabel playerNameLabel;
  private JTable favoriteGamesTable;
  private JTable recentPlaysTable;
  private JTable mostCommonPlayers;
  private JButton showMoreStatsButton;
  private JButton suggestGamesButtonButton;

  public PlayerView(Player selectedPlayer) {
    playerNameLabel.setText(selectedPlayer.name);
  }
}
