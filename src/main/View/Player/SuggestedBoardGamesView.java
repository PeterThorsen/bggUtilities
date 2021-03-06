package View.Player;

import Model.Structure.BoardGameCounter;
import Model.Structure.BoardGameSuggestion;
import Model.Structure.Reason;
import Util.Sorting.InsertionSort;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

/**
 * Created by Peter on 14/11/2016.
 */
public class SuggestedBoardGamesView {
  public JPanel panel1;
  private JTable table1;

  public SuggestedBoardGamesView(BoardGameSuggestion suggestions) {
    fillTable(suggestions.suggestedCombination);
  }

  private void fillTable(BoardGameCounter[] suggestedGames) {
    TableModel dataModel = new
            AbstractTableModel() {

              public int getColumnCount() {
                return 3;
              }

              public int getRowCount() {
                return suggestedGames.length;
              }

              public Object getValueAt(int row, int col) {

                // Name
                if (col == 0) {
                  return suggestedGames[row].game.name;
                }
                if (col == 1) {
                  return suggestedGames[row].approximateTime;
                }
                if (col == 2) {
                  Reason[] reasons = InsertionSort.sortReasons(suggestedGames[row].reasons);
                  StringBuilder sb = new StringBuilder();
                  sb.append(reasons[0]);
                  for (int i = 1; i < 3; i++) {
                    sb.append(", ").append(reasons[i]);
                  }
                  return sb.toString();
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
                    return "Approximate time spent";
                  default:
                    return "REST";
                }
              }
            };

    table1.setModel(dataModel);
  }
}
