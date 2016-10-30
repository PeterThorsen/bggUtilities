package Main.Views;

import Main.Controllers.FacadeController;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.awt.*;

/**
 * Created by Peter on 10/10/2016.
 */
public class MainView {
  private final FacadeController facadeController;
  public JPanel panel1;
  public JTable gamesTable;
  private JButton button1;
  private JButton button2;
  private JTabbedPane tabbedPane1;
  private JScrollPane Games;
  private JScrollPane Players;
  private JTable playersTable;

  public MainView(FacadeController controller) {
    facadeController = controller;
    // make the frame half the height and width
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int width = screenSize.width;
    int height = screenSize.height;
    panel1.setPreferredSize(new Dimension(width/2, height/2));

    fillGamesTable();
    fillPlayersTable();
  }


  public void fillGamesTable() {
    TableModel dataModel = new
            AbstractTableModel() {

              String[] gameNames = facadeController.getGameNames();
              int[] minLengths = facadeController.getMinLengths();
              int[] maxLengths = facadeController.getMaxLengths();
              double[] complexities = facadeController.getComplexities();
              public int getColumnCount() {
                return 4;
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
                  return minLengths[row];
                }
                if (col == 2) {
                  return maxLengths[row];
                }
                if(col == 3) {
                  return complexities[row];
                }
                else {
                  return "Rest";
                }
              }


              public String getColumnName(int column) {
                switch (column){
                  case 0: return "Name";
                  case 1: return "Min length";
                  case 2: return "Max length";
                  case 3: return "Complexity";
                  default: return "REST";
                }
              }

            };

    gamesTable.setModel(dataModel);
  }

  private void fillPlayersTable() {
    TableModel dataModel = new
            AbstractTableModel() {

              String[] gameNames = facadeController.getGameNames();
              int[] minLengths = facadeController.getMinLengths();
              int[] maxLengths = facadeController.getMaxLengths();
              double[] complexities = facadeController.getComplexities();
              public int getColumnCount() {
                return 4;
              }

              public int getRowCount() {
                return facadeController.getNumberOfGames();
              }

              public Object getValueAt(int row, int col) {

                // Name
                if(col == 0) {
                  return "A" + gameNames[row];
                }
                if (col == 1) {
                  return minLengths[row];
                }
                if (col == 2) {
                  return maxLengths[row];
                }
                if(col == 3) {
                  return complexities[row];
                }
                else {
                  return "Rest";
                }
              }


              public String getColumnName(int column) {
                switch (column){
                  case 0: return "Name";
                  case 1: return "Min length";
                  case 2: return "Max length";
                  case 3: return "Complexity";
                  default: return "REST";
                }
              }

            };

    playersTable.setModel(dataModel);
  }
}
