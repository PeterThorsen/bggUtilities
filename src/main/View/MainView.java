package View;

import Controller.FacadeController;
import Model.Structure.BoardGame;
import Model.Structure.Holders.GamePlayHolder;
import Model.Structure.Play;
import Model.Structure.Player;
import View.Player.PlayerView;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
  private Dimension halfDimension;

  public MainView(FacadeController controller) {
    facadeController = controller;

    // make the frame half the height and width
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int width = screenSize.width;
    int height = screenSize.height;
    halfDimension = new Dimension(width/2, height/2);
    panel1.setPreferredSize(halfDimension);

    fillGamesTable();
    fillPlayersTable();
    fillPlaysTable();
  }

  public void fillGamesTable() {
    DecimalFormat df = new DecimalFormat("####0.00");
    TableModel dataModel = new
            AbstractTableModel() {
              final BoardGame[] allGames = facadeController.getAllGames(true);

              public int getColumnCount() {
                return 7;
              }

              public int getRowCount() {
                return allGames.length;
              }

              public Object getValueAt(int row, int col) {

                // Name
                if (col == 0) {
                  return allGames[row].name;
                }
                if (col == 1) {
                  if (allGames[row].minPlaytime == allGames[row].maxPlaytime) {
                    return allGames[row].minPlaytime;
                  }
                  return allGames[row].minPlaytime + "-" + allGames[row].maxPlaytime;
                }
                if (col == 2) {
                  return df.format(allGames[row].complexity);
                }

                if (col == 3) {
                  if (allGames[row].minPlayers == allGames[row].maxPlayers) {
                    return allGames[row].minPlayers;
                  }
                  return allGames[row].minPlayers + "-" + allGames[row].maxPlayers;
                }
                if (col == 4) {
                  return allGames[row].numPlays;
                }
                if (col == 5) {
                  return allGames[row].personalRating;
                }

                if (col == 6) {
                  String temp = allGames[row].averageRating;
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
                  GamePlayHolder holder = players[row].getMostPlayedGame();
                  return holder.game + " (" + holder.plays + " plays)";
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

    playersTable.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
          JTable target = (JTable) e.getSource();
          int row = target.getSelectedRow();
          Player selectedPlayer = players[row];


          JFrame frame = new JFrame(selectedPlayer.name);
          PlayerView playerView = new PlayerView(frame, selectedPlayer, facadeController);
          frame.setContentPane(playerView.panelMain);
          frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

          frame.setPreferredSize(halfDimension);
          frame.setSize(halfDimension);
          frame.setLocationRelativeTo(null);
          frame.setVisible(true);
        }
      }
    });
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
                  return allPlaysSorted[row].game.name;
                }
                if (col == 1) {
                  return allPlaysSorted[row].noOfPlays;
                }
                if (col == 2) {
                  return allPlaysSorted[row].date;
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
                  if(players.length > 1) {
                    printValue = printValue.concat(" and " + players[players.length - 1] + ".");
                  }
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
