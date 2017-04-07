package Main.View.Player;

import Main.Model.Structure.Player;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Peter on 13-Nov-16.
 */
public class PlayerViewMoreStats {
  public JPanel panel1;
  private JButton goBackButton;
  private JLabel complexityRange;
  private JLabel average;
  private Player selectedPlayer;

  public PlayerViewMoreStats(JFrame frame, Player selectedPlayer, PlayerView parentView) {
    this.selectedPlayer = selectedPlayer;
    fillComplexityRange();
    fillAverageComplexity();
    
    goBackButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        frame.setContentPane(parentView.panelMain);
        frame.repaint();
        frame.pack();
      }
    });
  }

  private void fillAverageComplexity() {
    average.setText(selectedPlayer.name + "'s average complexity is: " + selectedPlayer.getAverageComplexity());
  }

  private void fillComplexityRange() {
    double min = selectedPlayer.getMinComplexity();
    double max = selectedPlayer.getMaxComplexity();
    if(min == max) {
      complexityRange.setText(selectedPlayer.name + "'s only played game have a complexity of: " + min);
      return;
    }
    complexityRange.setText(selectedPlayer.name + "'s complexity range is: " + min + " - " + max);

  }
}
