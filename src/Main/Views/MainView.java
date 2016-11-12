package Main.Views;

import Main.Containers.BoardGame;
import Main.Containers.GameNameAndPlayHolder;
import Main.Containers.Play;
import Main.Containers.Player;
import Main.Controllers.FacadeController;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.text.DecimalFormat;

/**
 * Created by Peter on 10/10/2016.
 */
public class MainView {
  private final FacadeController facadeController;
  public JPanel panel1;
  public JTable gamesTable;
  private JTabbedPane tabbedPane1;
  private JScrollPane Games;
  private JScrollPane Players;
  private JTable playersTable;
  private JTable playsTable;
  private JScrollPane Plays;

  public MainView(FacadeController controller) {
    facadeController = controller;
    // make the frame half the height and width
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int width = screenSize.width;
    int height = screenSize.height;
    panel1.setPreferredSize(new Dimension(width / 2, height / 2));

    fillGamesTable();
    fillPlayersTable();
    fillPlaysTable();
  }

  public void fillGamesTable() {
    DecimalFormat df = new DecimalFormat("####0.00");
    TableModel dataModel = new
            AbstractTableModel() {
              final BoardGame[] allGames = facadeController.getAllGames();

              public int getColumnCount() {
                return 7;
              }

              public int getRowCount() {
                return allGames.length;
              }

              public Object getValueAt(int row, int col) {

                // Name
                if (col == 0) {
                  return allGames[row].getName();
                }
                if (col == 1) {
                  if (allGames[row].getMinPlaytime() == allGames[row].getMaxPlaytime()) {
                    return allGames[row].getMinPlaytime();
                  }
                  return allGames[row].getMinPlaytime() + "-" + allGames[row].getMaxPlaytime();
                }
                if (col == 2) {
                  return df.format(allGames[row].getComplexity());
                }

                if (col == 3) {
                  if (allGames[row].getMinPlayers() == allGames[row].getMaxPlayers()) {
                    return allGames[row].getMinPlayers();
                  }
                  return allGames[row].getMinPlayers() + "-" + allGames[row].getMaxPlayers();
                }
                if (col == 4) {
                  return allGames[row].getNumberOfPlays();
                }
                if (col == 5) {
                  return allGames[row].getPersonalRating();
                }

                if (col == 6) {
                  String temp = allGames[row].getAverageRating();
                  double avgRatingAsDouble = Double.valueOf(temp);
                  return df.format(avgRatingAsDouble);
                } else {
                  return "Rest";
                }
              }

              public String getColumnName(int column) {
                switch (column) {
                  case 0:
                    return "Title";
                  case 1:
                    return "Playtime";
                  case 2:
                    return "Complexity";
                  case 3:
                    return "Players";
                  case 4:
                    return "Plays";
                  case 5:
                    return "Your rating";
                  case 6:
                    return "Average rating";
                  default:
                    return "REST";
                }
              }

            };

    gamesTable.setModel(dataModel);
  }

  private void fillPlayersTable() {
    Player[] players = facadeController.getAllPlayers();

    TableModel dataModel = new
            AbstractTableModel() {
              public int getColumnCount() {
                return 4;
              }

              public int getRowCount() {
                return players.length;
              }

              public Object getValueAt(int row, int col) {

                // Name
                if (col == 0) {
                  return players[row].name;
                }
                // Total plays
                if (col == 1) {
                  return players[row].totalPlays;
                }
                // Most played
                if (col == 2) {
                  GameNameAndPlayHolder holder = players[row].getMostPlayedGame();
                  return holder.gameName + " (" + holder.plays + " plays)";
                }
                if (col == 3) {
                  return players[row].getMostRecentGame();
                } else {
                  return "Rest";
                }
              }


              public String getColumnName(int column) {
                switch (column) {
                  case 0:
                    return "Name";
                  case 1:
                    return "Total plays";
                  case 2:
                    return "Most played";
                  case 3:
                    return "Last play";
                  default:
                    return "REST";
                }
              }

            };

    playersTable.setModel(dataModel);
  }

  private void fillPlaysTable() {
    TableModel dataModel = new
            AbstractTableModel() {

              final Play[] allPlaysSorted = facadeController.getAllPlaysSorted();

              public int getColumnCount() {
                return 4;
              }

              public int getRowCount() {
                return allPlaysSorted.length;
              }

              public Object getValueAt(int row, int col) {

                // Name
                if (col == 0) {
                  return allPlaysSorted[row].getGame().getName();
                }
                if (col == 1) {
                  return allPlaysSorted[row].getQuantity();
                }
                if (col == 2) {
                  return allPlaysSorted[row].getDate();
                }
                if (col == 3) {
                  String[] players = allPlaysSorted[row].playerNames;
                  if (players.length == 0) {
                    return "";
                  }
                  String printValue = players[0];
                  for (int i = 1; i < players.length - 1; i++) {
                    printValue = printValue.concat(", " + players[i]);
                  }
                  printValue = printValue.concat(" and " + players[players.length - 1] + ".");
                  return printValue;
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
                  case 3:
                    return "Players";
                  default:
                    return "REST";
                }
              }
            };

    playsTable.setModel(dataModel);
  }
}
