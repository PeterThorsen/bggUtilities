package Main.Views.Player;

import Main.Containers.Holders.PlayerNodeInformationHolder;
import Main.Containers.Holders.StringIntHolder;
import Main.Containers.Play;
import Main.Containers.Player;
import Main.Controllers.FacadeController;
import Main.Sorting.InsertionSortStringIntHolder;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

/**
 * Created by Peter on 13-Nov-16.
 */
public class PlayerView {

  public JPanel panelMain;
  private JLabel playerNameLabel;
  private JTable favoriteGamesTable;
  private JTable recentPlaysTable;
  private JTable mostCommonPlayers;
  private JButton showMoreStatsButton;
  private JButton suggestGamesButtonButton;
  private JFrame frame;
  private Player selectedPlayer;
  private FacadeController facadeController;

  public PlayerView(JFrame frame, Player selectedPlayer, FacadeController facadeController) {
    this.frame = frame;
    this.selectedPlayer = selectedPlayer;
    this.facadeController = facadeController;
    playerNameLabel.setText(selectedPlayer.name);

    fillMostPlayedGamesTable();
    fillRecentPlaysTable();
    fillMostCommonPlayersTable();
    addListeners();

  }

  private void fillMostPlayedGamesTable() {
    Play[] allPlays = selectedPlayer.allPlays;
    HashMap<String, Integer> mostPlaysMap = new HashMap<>();

    for (Play play : allPlays) {
      String gameName = play.getGame().getName();

      if (mostPlaysMap.containsKey(gameName)) {
        int currentPlays = mostPlaysMap.get(gameName);
        currentPlays += play.getQuantity();
        mostPlaysMap.put(gameName, currentPlays);
      } else {
        mostPlaysMap.put(gameName, play.getQuantity());
      }
    }

    int pos = 0;
    StringIntHolder[] gamesSortedByPlays = new StringIntHolder[mostPlaysMap.size()];
    for (String key : mostPlaysMap.keySet()) {
      gamesSortedByPlays[pos] = new StringIntHolder(key, mostPlaysMap.get(key));
      pos++;
    }

    gamesSortedByPlays = InsertionSortStringIntHolder.sort(gamesSortedByPlays);
    StringIntHolder[] finalGamesSortedByPlays = gamesSortedByPlays;

    TableModel dataModel = new
            AbstractTableModel() {

              public int getColumnCount() {
                return 2;
              }

              public int getRowCount() {
                return finalGamesSortedByPlays.length;
              }

              public Object getValueAt(int row, int col) {

                // Name
                if (col == 0) {
                  return finalGamesSortedByPlays[row].str;
                }
                if (col == 1) {
                  return finalGamesSortedByPlays[row].num;
                } else {
                  return "Rest";
                }
              }

              public String getColumnName(int column) {
                switch (column) {
                  case 0:
                    return "Name";
                  case 1:
                    return "Quantity";
                  default:
                    return "REST";
                }
              }
            };
    favoriteGamesTable.setModel(dataModel);
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
                } else {
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
    // name, quantity, favorite game together

    Play[] allPlays = selectedPlayer.allPlays;

    HashMap<String, HashMap<String, Integer>> mostCommonGamesForEachPlayerMap = new HashMap<>();
    HashMap<PlayerNodeInformationHolder, Integer> mostPlaysMap = new HashMap<>();

    for (Play play : allPlays) {
      String gameName = play.getGame().getName();
      PlayerNodeInformationHolder[] holders = play.playerInformation;

      for (PlayerNodeInformationHolder holder : holders) {
        if (holder.equals(selectedPlayer.name)) {
          continue;
        }

        if (mostPlaysMap.containsKey(holder)) {
          int currentPlays = mostPlaysMap.get(holder);
          currentPlays += play.getQuantity();
          mostPlaysMap.put(holder, currentPlays);
        } else {
          mostPlaysMap.put(holder, play.getQuantity());
        }

        if (mostCommonGamesForEachPlayerMap.containsKey(holder)) {
          HashMap<String, Integer> tempMap = mostCommonGamesForEachPlayerMap.get(holder);
          if (tempMap.containsKey(gameName)) {
            int currentPlays = tempMap.get(gameName);
            currentPlays += play.getQuantity();
            tempMap.put(gameName, currentPlays);
          } else {
            tempMap.put(gameName, play.getQuantity());
          }
          mostCommonGamesForEachPlayerMap.put(holder.playerName, tempMap);

        } else {
          HashMap<String, Integer> tempMap = new HashMap<>();
          tempMap.put(gameName, play.getQuantity());
          mostCommonGamesForEachPlayerMap.put(holder.playerName, tempMap);
        }
      }
    }

    StringIntHolder[] mostCommonPlayersSorted = new StringIntHolder[mostPlaysMap.size()];
    int pos = 0;
    for (PlayerNodeInformationHolder key : mostPlaysMap.keySet()) {
      StringIntHolder holder = new StringIntHolder(key.playerName, mostPlaysMap.get(key));
      mostCommonPlayersSorted[pos] = holder;
      pos++;
    }

    mostCommonPlayersSorted = InsertionSortStringIntHolder.sort(mostCommonPlayersSorted);

    StringIntHolder[] finalMostCommonPlayersSorted = mostCommonPlayersSorted;

    TableModel dataModel = new
            AbstractTableModel() {

              public int getColumnCount() {
                return 3;
              }

              public int getRowCount() {
                return finalMostCommonPlayersSorted.length;
              }

              public Object getValueAt(int row, int col) {

                // Name
                if (col == 0) {
                  return finalMostCommonPlayersSorted[row].str;
                }
                if (col == 1) {
                  return finalMostCommonPlayersSorted[row].num;
                }
                if (col == 2) {
                  String currentName = finalMostCommonPlayersSorted[row].str;
                  HashMap<String, Integer> allGamesForCurrentPlayer = mostCommonGamesForEachPlayerMap.get(currentName);

                  int maxValue = 0;
                  String maxName = "";
                  int secondMostCommon = 0;
                  String secondMostCommonName = "";
                  int thirdMostCommon = 0;
                  String thirdMostCommonName = "";

                  for (String key : allGamesForCurrentPlayer.keySet()) {
                    int value = allGamesForCurrentPlayer.get(key);

                    if (value > maxValue) {

                      thirdMostCommon = secondMostCommon;
                      thirdMostCommonName = secondMostCommonName;

                      secondMostCommon = maxValue;
                      secondMostCommonName = maxName;

                      maxValue = value;
                      maxName = key;
                    } else if (value < maxValue) {
                      if (value > secondMostCommon) {
                        thirdMostCommon = secondMostCommon;
                        thirdMostCommonName = secondMostCommonName;

                        secondMostCommon = value;
                        secondMostCommonName = key;
                      } else if (value == secondMostCommon && value > thirdMostCommon) {
                        thirdMostCommon = value;
                        thirdMostCommonName = key;
                      }
                    }
                  }
                  String returnString = maxName;
                  if (secondMostCommon > 0) {
                    returnString = returnString.concat(", " + secondMostCommonName);
                  }
                  if (thirdMostCommon > 0) {
                    returnString = returnString.concat(", " + thirdMostCommonName);
                  }

                  return returnString;
                } else {
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
                    return "Most common game";
                  default:
                    return "REST";
                }
              }
            };

    mostCommonPlayers.setModel(dataModel);
  }

  private void addListeners() {
    showMoreStatsButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        PlayerViewMoreStats moreStats = new PlayerViewMoreStats(frame, selectedPlayer, PlayerView.this);

        frame.setContentPane(moreStats.panel1);
        frame.repaint();
        frame.pack();

      }
    });
    suggestGamesButtonButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        SelectPlayersTimeAndComplexityView view = new SelectPlayersTimeAndComplexityView(frame, selectedPlayer, PlayerView.this, facadeController);

        frame.setContentPane(view.panelMain);
        frame.repaint();
        frame.pack();
      }
    });
  }
}
