package Main.Views;

import Main.Containers.BoardGame;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

/**
 * Created by Peter on 14/11/2016.
 */
public class SuggestedBoardGamesView {
  public JPanel panel1;
  private JTable table1;

  public SuggestedBoardGamesView(BoardGame[] suggestedGames) {
    fillTable(suggestedGames);
  }

  private void fillTable(BoardGame[] suggestedGames) {
    TableModel dataModel = new
            AbstractTableModel() {

              public int getColumnCount() {
                return 1;
              }

              public int getRowCount() {
                return suggestedGames.length;
              }

              public Object getValueAt(int row, int col) {

                // Name
                if (col == 0) {
                  return suggestedGames[row].getName();
                }
                else {
                  return "Rest";
                }
              }

              public String getColumnName(int column) {
                switch (column) {
                  case 0:
                    return "Name";
                  default:
                    return "REST";
                }
              }

            };

    table1.setModel(dataModel);
  }
}
