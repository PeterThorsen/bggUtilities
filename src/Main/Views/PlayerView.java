package Main.Views;

import Main.Containers.Play;
import Main.Containers.Player;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
  private Player selectedPlayer;

  public PlayerView(Player selectedPlayer) {
    this.selectedPlayer = selectedPlayer;
    playerNameLabel.setText(selectedPlayer.name);

    fillMostPlayedGamesTable();
    fillRecentPlaysTable();
    fillMostCommonPlayersTable();
    addListeners();

  }

  private void fillMostPlayedGamesTable() {
  }

  private void fillRecentPlaysTable() {
    Play[] allPlays = selectedPlayer.allPlays;


    TableModel dataModel = new
            AbstractTableModel() {

              public int getColumnCount() {
                return 3;
              }

              public int getRowCount() {
                return allPlays.length;
              }

              public Object getValueAt(int row, int col) {

                // Name
                if (col == 0) {
                  return allPlays[row].getGame().getName();
                }
                if (col == 1) {
                  return allPlays[row].getQuantity();
                }
                if (col == 2) {
                  return allPlays[row].getDate();
                }
                else {
                  return "Rest";
                }
              }

              public String getColumnName(int column) {
                switch (column) {
                  case 0:
                    return "Name";
                  case 1:
                    return "Quantity";
                  case 2:
                    return "Date";
                  default:
                    return "REST";
                }
              }
            };

    recentPlaysTable.setModel(dataModel);
  }

  private void fillMostCommonPlayersTable() {
  }

  private void addListeners() {
    showMoreStatsButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {

      }
    });
    suggestGamesButtonButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {

      }
    });
  }
}
