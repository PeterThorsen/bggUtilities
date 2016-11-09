package Main.Views;

import Main.Containers.GameNameAndPlayHolder;
import Main.Containers.Play;
import Main.Controllers.FacadeController;
import Main.InsertionSortStrings;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

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
    panel1.setPreferredSize(new Dimension(width/2, height/2));

    fillGamesTable();
    fillPlayersTable();
    fillPlaysTable();
  }

  public void fillGamesTable() {
    TableModel dataModel = new
            AbstractTableModel() {

              String[] gameNames = facadeController.getAllGameNames();
              int[] minLengths = facadeController.getAllMinLengths();
              int[] maxLengths = facadeController.getAllMaxLengths();
              int[] minPlayers = facadeController.getAllMinPlayers();
              int[] maxPlayers = facadeController.getAllMaxPlayers();
              int[] numPlays = facadeController.getAllNumberOfPlays();
              String[] personalRatings = facadeController.getAllPersonalRatings();
              double[] complexities = facadeController.getAllComplexities();
              String[] averageRatings = facadeController.getAllAverageRatings();
              public int getColumnCount() {
                return 7;
              }

              public int getRowCount() {
                return facadeController.getNumberOfGames();
              }

              public Object getValueAt(int row, int col) {

                // Name
                if(col == 0) {
                  return gameNames[row];
                }
                if (col == 1) {
                  if(minLengths[row] == maxLengths[row]) {
                    return minLengths[row];
                  }
                  return minLengths[row] + "-" + maxLengths[row];
                }
                if(col == 2) {
                  return complexities[row];
                }

                if(col == 3) {
                  if(minPlayers[row] == maxPlayers[row]) {
                    return minPlayers[row];
                  }
                  return minPlayers[row] + "-" + maxPlayers[row];
                }
                if(col == 4) {
                  return numPlays[row];
                }
                 if(col == 5) {
                  return personalRatings[row];
                }

                if(col == 6) {
                  return averageRatings[row];
                }
                else {
                  return "Rest";
                }
              }


              public String getColumnName(int column) {
                switch (column){
                  case 0: return "Title";
                  case 1: return "Playtime";
                  case 2: return "Complexity";
                  case 3: return "Players";
                  case 4: return "Plays";
                  case 5: return "Your rating";
                  case 6: return "Average rating";
                  default: return "REST";
                }
              }

            };

    gamesTable.setModel(dataModel);
  }

  private void fillPlayersTable() {
    String[] playerNames = facadeController.getPlayerNames();
    playerNames = InsertionSortStrings.sort(playerNames);

    String[] finalPlayerNames = playerNames;
    TableModel dataModel = new
            AbstractTableModel() {


              HashMap<String, Integer> noOfPlaysByPlayer = facadeController.getNumberOfPlaysByPlayers();
              HashMap<String, GameNameAndPlayHolder> favoriteGames = facadeController.getMostPlayedGamesByPlayers();
              HashMap<String, String> lastPlayDates = facadeController.getDateOfLastPlayForEachPlayer();
              public int getColumnCount() {
                return 4;
              }

              public int getRowCount() {
                return facadeController.getNumberOfPlayers();
              }

              public Object getValueAt(int row, int col) {

                // Name
                if(col == 0) {
                  return finalPlayerNames[row];
                }
                if(col == 1) {
                  return noOfPlaysByPlayer.get(finalPlayerNames[row]);
                }
                if(col == 2) {
                  GameNameAndPlayHolder holder = favoriteGames.get(finalPlayerNames[row]);
                  return holder.gameName + " (" + holder.plays + " plays)";
                }
                if(col == 3) {
                  return lastPlayDates.get(finalPlayerNames[row]);
                }
                else {
                  return "Rest";
                }
              }


              public String getColumnName(int column) {
                switch (column){
                  case 0: return "Name";
                  case 1: return "Total plays";
                  case 2: return "Most played";
                  case 3: return "Last play";
                  default: return "REST";
                }
              }

            };

    playersTable.setModel(dataModel);
  }


  private void fillPlaysTable() {
    TableModel dataModel = new
            AbstractTableModel() {

              Play[] allPlaysSorted = facadeController.getAllPlaysSorted();
              public int getColumnCount() {
                return 4;
              }

              public int getRowCount() {
                return allPlaysSorted.length;
              }

              public Object getValueAt(int row, int col) {

                // Name
                if(col == 0) {
                  return allPlaysSorted[row].getGame().getName();
                }
                if(col == 1) {
                  return allPlaysSorted[row].getQuantity();
                }
                if(col == 2) {
                  return allPlaysSorted[row].getDate();
                }
                if(col == 3) {
                  String[] players = allPlaysSorted[row].getPlayers();
                  players = InsertionSortStrings.sort(players);
                  String printValue = players[0];
                  for (int i = 1; i < players.length-1; i++) {
                    printValue = printValue.concat(", " + players[i]);
                  }
                  printValue = printValue.concat(" and " + players[players.length-1] + ".");
                  return printValue;
                }
                else {
                  return "Rest";
                }
              }

              public String getColumnName(int column) {
                switch (column){
                  case 0: return "Name";
                  case 1: return "Quantity";
                  case 2: return "Date";
                  case 3: return "Players";
                  default: return "REST";
                }
              }
            };

    playsTable.setModel(dataModel);
  }
}
