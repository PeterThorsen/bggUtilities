package main.View.Player;

import main.Model.Structure.BoardGameSuggestion;
import main.Model.Structure.Player;
import main.Controller.FacadeController;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * Created by Peter on 14-Nov-16.
 */
public class SelectPlayersTimeAndComplexityView {
  private JTable table1;
  private JTable table2;
  private JTextField maxTimeField;
  private JButton calculateSuggestedGames;
  private JPanel belowTablesPanel;
  private JPanel rightTablePanel;
  private JPanel leftTablePanel;
  private JLabel complexityRangeValue;
  public JPanel panelMain;
  private JFrame frame;
  private Player selectedPlayer;
  private PlayerView playerView;
  private FacadeController facadeController;

  private ArrayList<Player> playersInTable2;

  public SelectPlayersTimeAndComplexityView(JFrame frame, Player selectedPlayer, PlayerView playerView, FacadeController facadeController) {
    this.frame = frame;
    this.selectedPlayer = selectedPlayer;
    this.playerView = playerView;
    this.facadeController = facadeController;
    
    fillTable1();

    playersInTable2 = new ArrayList<>();
    playersInTable2.add(selectedPlayer);
    fillTable2(playersInTable2);
    
    calculateSuggestedGames.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if(verifyAllInputs()) {
          suggestGames();
        }
        else {
          System.out.println("Not all input correct"); // TODO: 14/11/2016
        }
      }
    });
  }

  private boolean verifyAllInputs() {
    String maxText = maxTimeField.getText();

    try {
      int maxValue = Integer.valueOf(maxText);
      return true;
    }
    catch (NumberFormatException e) {
      return false;
    }
  }

  private void suggestGames() {
    int maxTime = Integer.valueOf(maxTimeField.getText());

    Player[] array = new Player[playersInTable2.size()];
    for (int i = 0; i < array.length; i++) {
      array[i] = playersInTable2.get(i);
    }

    BoardGameSuggestion suggestedGames = facadeController.suggestGames(array, maxTime);

    SuggestedBoardGamesView view = new SuggestedBoardGamesView(suggestedGames);

    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    frame.setContentPane(view.panel1);
    frame.repaint();
    frame.pack();
  }

  private void fillTable1() {
    Player[] allPlayers = facadeController.getAllPlayers();
    TableModel dataModel = new
            AbstractTableModel() {

              public int getColumnCount() {
                return 1;
              }

              public int getRowCount() {
                return allPlayers.length;
              }

              public Object getValueAt(int row, int col) {

                // Name
                if (col == 0) {
                  return allPlayers[row].name;
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

    table1.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
          JTable target = (JTable) e.getSource();
          int row = target.getSelectedRow();
          Player doubleClicked = allPlayers[row];
          addToTable2(doubleClicked);
        }
      }
    });
  }


  private void fillTable2(ArrayList<Player> playersInTable2) {
    TableModel dataModel = new
            AbstractTableModel() {

              public int getColumnCount() {
                return 1;
              }

              public int getRowCount() {
                return playersInTable2.size()+1;
              }

              public Object getValueAt(int row, int col) {

                // Name
                if (row == 0) {
                  return facadeController.username;
                }
                else {
                  return playersInTable2.get(row-1).name;
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

    table2.setModel(dataModel);

    table2.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
          JTable target = (JTable) e.getSource();
          int row = target.getSelectedRow();
          if(row == 0) {
            return;
          }
          removeFromTable2(row-1); // since "username" is always at first spot, but not included in Players array
        }
      }
    });

    // Changing complexity range
    double min = 5;
    double max = 1;
    double average = 0;
    for (Player player : playersInTable2) {
      double currentMin = player.getMinComplexity();
      double currentMax = player.getMaxComplexity();
      average += player.getAverageComplexity();
      if(currentMin < min) {
        min = currentMin;
      }
      if(currentMax > max) {
        max = currentMax;
      }
    }
    average = average / playersInTable2.size();
    if (playersInTable2.size() > 0) {
      complexityRangeValue.setText(min + " - " + max + " (average: " + average + ")");
    }
    else {
      complexityRangeValue.setText("-");
    }
  }

  private void addToTable2(Player player) {
    playersInTable2.add(player);
    fillTable2(playersInTable2);
  }

  private void removeFromTable2(int positionInArray) {

    // Sometimes mouseclick called this twice, one of the times with position -2. This fixes the problem
    if(positionInArray < 0) {
      return;
    }
    playersInTable2.remove(positionInArray);
    fillTable2(playersInTable2);
  }
}
