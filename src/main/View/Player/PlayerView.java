package View.Player;

import Controller.FacadeController;
import Model.Structure.BoardGame;
import Model.Structure.Holders.BoardGameIntHolder;
import Model.Structure.Holders.StringIntHolder;
import Model.Structure.Play;
import Model.Structure.Player;
import Util.Sorting.InsertionSort;

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
    HashMap<BoardGame, Integer> mostPlaysMap = new HashMap<>();

    for (Play play : allPlays) {
      BoardGame game = play.game;

      if (mostPlaysMap.containsKey(game)) {
        int currentPlays = mostPlaysMap.get(game);
        currentPlays += play.noOfPlays;
        mostPlaysMap.put(game, currentPlays);
      } else {
        mostPlaysMap.put(game, play.noOfPlays);
      }
    }

    int pos = 0;
    BoardGameIntHolder[] gamesSortedByPlays = new BoardGameIntHolder[mostPlaysMap.size()];
    for (BoardGame key : mostPlaysMap.keySet()) {
      gamesSortedByPlays[pos] = new BoardGameIntHolder(key, mostPlaysMap.get(key));
      pos++;
    }

    gamesSortedByPlays = InsertionSort.sortBoardGameIntHolder(gamesSortedByPlays);
    BoardGameIntHolder[] finalGamesSortedByPlays = gamesSortedByPlays;

    TableModel dataModel = new
            AbstractTableModel() {

              public int getColumnCount() {
                return 3;
              }

              public int getRowCount() {
                return finalGamesSortedByPlays.length;
              }

              public Object getValueAt(int row, int col) {

                // Name
                if (col == 0) {
                  return finalGamesSortedByPlays[row].game;
                }

                // Personal rating
                if(col == 1) {
                  double personalRating = selectedPlayer.getPersonalRating(finalGamesSortedByPlays[row].game);
                  if(personalRating == 0.0) {
                    return "N/A";
                  }
                  return (int) selectedPlayer.getPersonalRating(finalGamesSortedByPlays[row].game) + "/10";
                }

                // Quantity
                if (col == 2) {
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
                    return "Rating";
                  case 2:
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
                  return allPlays[allPlays.length-row-1].game.name;
                }
                if (col == 1) {
                  return allPlays[allPlays.length-row-1].noOfPlays;
                }
                if (col == 2) {
                  return allPlays[allPlays.length-row-1].date;
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
    HashMap<String, Integer> mostPlaysMap = new HashMap<>();

    for (Play play : allPlays) {
      String gameName = play.game.name;
      String[] names = play.playerNames;

      for (String name : names) {
        if (name.equals(selectedPlayer.name)) {
          continue;
        }

        if (mostPlaysMap.containsKey(name)) {
          int currentPlays = mostPlaysMap.get(name);
          currentPlays += play.noOfPlays;
          mostPlaysMap.put(name, currentPlays);
        } else {
          mostPlaysMap.put(name, play.noOfPlays);
        }

        if (mostCommonGamesForEachPlayerMap.containsKey(name)) {
          HashMap<String, Integer> tempMap = mostCommonGamesForEachPlayerMap.get(name);
          if (tempMap.containsKey(gameName)) {
            int currentPlays = tempMap.get(gameName);
            currentPlays += play.noOfPlays;
            tempMap.put(gameName, currentPlays);
          } else {
            tempMap.put(gameName, play.noOfPlays);
          }
          mostCommonGamesForEachPlayerMap.put(name, tempMap);

        } else {
          HashMap<String, Integer> tempMap = new HashMap<>();
          tempMap.put(gameName, play.noOfPlays);
          mostCommonGamesForEachPlayerMap.put(name, tempMap);
        }
      }
    }

    StringIntHolder[] mostCommonPlayersSorted = new StringIntHolder[mostPlaysMap.size()];
    int pos = 0;
    for (String key : mostPlaysMap.keySet()) {
      StringIntHolder holder = new StringIntHolder(key, mostPlaysMap.get(key));
      mostCommonPlayersSorted[pos] = holder;
      pos++;
    }

    mostCommonPlayersSorted = InsertionSort.sortStringIntHolder(mostCommonPlayersSorted);

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
