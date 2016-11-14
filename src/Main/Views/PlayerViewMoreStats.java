package Main.Views;

import Main.Containers.BoardGame;
import Main.Containers.Play;
import Main.Containers.Player;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

/**
 * Created by Peter on 13-Nov-16.
 */
public class PlayerViewMoreStats {
  public JPanel panel1;
  private JButton goBackButton;
  private JLabel complexityRange;
  private JLabel weighedAverage;
  private JLabel average;
  private Player selectedPlayer;

  public PlayerViewMoreStats(JFrame frame, Player selectedPlayer, PlayerView parentView) {
    this.selectedPlayer = selectedPlayer;

    Play[] allPlays = selectedPlayer.allPlays;
    HashMap<BoardGame, Integer> map = new HashMap<>();

    for (Play play : allPlays) {
      int quantityPlays = play.getQuantity();
      BoardGame game = play.getGame();

      if(map.containsKey(game)) {
        int value = map.get(game);
        value += quantityPlays;
        map.put(game, value);
      }
      else {
        map.put(game, quantityPlays);
      }
    }
    
    calculateComplexityRange(map);
    calculateAverageWeighedComplexity(map);
    calculateAverageComplexity(map);
    
    goBackButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        frame.setContentPane(parentView.panelMain);
        frame.repaint();
        frame.pack();
      }
    });
  }

  private void calculateAverageComplexity(HashMap<BoardGame, Integer> map) {
    if(map.size() == 1) {
      average.setText("");
      return;
    }
    double totalPlays = 0;
    double totalComplexity = 0;

    for (BoardGame key : map.keySet()) {
      totalPlays++;
      totalComplexity += key.getComplexity();
    }
    double averageComplexity = totalComplexity / totalPlays;
    average.setText(selectedPlayer.name + "'s average complexity is: " + averageComplexity);
  }

  private void calculateAverageWeighedComplexity(HashMap<BoardGame, Integer> map) {
    if(map.size() == 1) {
      weighedAverage.setText("");
      return;
    }

    double totalPlays = 0;
    double totalComplexity = 0;

    for (BoardGame key : map.keySet()) {
      int currentValue = map.get(key);
      totalPlays += currentValue;
      double currentComplexity = key.getComplexity() * currentValue;
      totalComplexity += currentComplexity;
    }
    double weighedComplexity = totalComplexity / totalPlays;
    weighedAverage.setText(selectedPlayer.name + "'s average weighed complexity is: " + weighedComplexity);
  }

  private void calculateComplexityRange(HashMap<BoardGame, Integer> map) {

    double minComplexity = 6;
    double maxComplexity = 0;
    for (BoardGame key : map.keySet()) {
      double current = key.getComplexity();
      if(current == 0.0) continue;
      if (current < minComplexity) {
        minComplexity = current;
      }
      if (current > maxComplexity) {
        maxComplexity = current;
      }
    }

    if(minComplexity == maxComplexity) {
      complexityRange.setText(selectedPlayer.name + "'s only played game have a complexity of: " + minComplexity);
      return;
    }
    complexityRange.setText(selectedPlayer.name + "'s complexity range is: " + minComplexity + " - " + maxComplexity);

  }
}
