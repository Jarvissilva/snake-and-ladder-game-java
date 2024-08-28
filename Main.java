import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class Main {

    static String[] boardArray = {
        "AB", "", "", "", "+2", "", "-4", "", "", "",
        "", "-2", "", "", "+3", "", "", "", "-3", "",
        "-5", "", "", "+2", "", "", "", "-4", "", "",
        "", "", "-6", "", "", "+4", "", "", "", "",
        "", "", "", "", "-7", "", "+5", "", "", "E",
    };

    static int current_player = 0;
    static int[] player_positions = {0, 0};

    static int roll_dice() {
        Random random = new Random();
        int min = 1;
        int max = 6;
        int randomNumber = random.nextInt(max - min + 1) + min;
        return randomNumber;
    }

    static void update_board(JPanel boardPanel) {
        Component[] components = boardPanel.getComponents();
        
        for (int i = 0; i < boardArray.length; i++) {
            boardArray[i] = "";
        }
        boardArray[4] = "+2";
        boardArray[6] = "-4";
        boardArray[10] = "-2";
        boardArray[14] = "+3";
        boardArray[18] = "-3";
        boardArray[20] = "-5";
        boardArray[23] = "+2";
        boardArray[27] = "-4";
        boardArray[32] = "-6";
        boardArray[35] = "+4";
        boardArray[44] = "-7";
        boardArray[46] = "+5";
        boardArray[boardArray.length - 1] = "E"; 
    
        if (player_positions[0] < boardArray.length) {
            boardArray[player_positions[0]] += "A";
        }
        if (player_positions[1] < boardArray.length) {
            boardArray[player_positions[1]] += "B";
        }
    
        for (int i = 0; i < components.length; i++) {
            JLabel cellLabel = (JLabel) components[i];
            cellLabel.setText(boardArray[i]);
        }
    }
    static void restart_game(JPanel boardPanel, JLabel currentPlayerLabel, JLabel diceResultLabel, JButton rollDiceButton) {
        current_player = 0;
        player_positions[0] = 0;
        player_positions[1] = 0;
        
        update_board(boardPanel);

        currentPlayerLabel.setText("Player A's Turn");
        diceResultLabel.setText("");
        rollDiceButton.setEnabled(true);
    }
    public static void main(String[] args) {
        // Creating a frame
        JFrame frame = new JFrame("Snake & Ladder Game");
        frame.setSize(800, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Adding board
        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(10, 10));
        Border cellBorder = BorderFactory.createLineBorder(Color.BLACK);

        for (String cellContent : boardArray) {
            JLabel cellLabel = new JLabel(cellContent);
            cellLabel.setHorizontalAlignment(SwingConstants.CENTER);
            cellLabel.setVerticalAlignment(SwingConstants.CENTER);
            cellLabel.setBorder(cellBorder);

            int cellSize = 50; 
            cellLabel.setPreferredSize(new Dimension(cellSize, cellSize));
            boardPanel.add(cellLabel);
        }

        frame.add(boardPanel, BorderLayout.CENTER);

        // Adding buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        JLabel currentPlayerLabel = new JLabel("Player A turn");
        JLabel diceResultLabel = new JLabel();
        JButton rollDiceButton = new JButton("Roll Dice");
        JButton restartBtn = new JButton("Restart");
        
        currentPlayerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        diceResultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        rollDiceButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        restartBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        restartBtn.setForeground(Color.WHITE);
        restartBtn.setBackground(Color.RED);



        rollDiceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int diceResult = roll_dice();
                int new_player_position = player_positions[current_player] + diceResult; 

                String message = "You rolled a " + diceResult;

                // Checking for special cells
                if (new_player_position < boardArray.length) {
                    switch (new_player_position) {
                        case 4:  new_player_position += 2; message += " You got a small boost of +2"; break;
                        case 6:  new_player_position -= 4; message += " Bummer! You go back -4"; break;
                        case 10: new_player_position -= 2; message += " Bummer! You go back -2"; break;
                        case 14: new_player_position += 3; message += " Nice! You move ahead +3"; break;
                        case 18: new_player_position -= 3; message += " Bummer! You go back -3"; break;
                        case 20: new_player_position -= 5; message += " Bummer! You go back -5"; break;
                        case 23: new_player_position += 2; message += " Nice! You move ahead +2"; break;
                        case 27: new_player_position -= 4; message += " Bummer! You go back -4"; break;
                        case 32: new_player_position -= 6; message += " Bummer! You go back -6"; break;
                        case 35: new_player_position += 4; message += " Nice! You move ahead +4"; break;
                        case 44: new_player_position -= 7; message += " Bummer! You go back -7"; break;
                        case 46: new_player_position += 5; message += " Nice! You move ahead +5"; break;
                    }
                }


                if (new_player_position >= boardArray.length) {
                    message = "You rolled too high " + diceResult;
                } else {
                    player_positions[current_player] = new_player_position;

                    if (player_positions[current_player] == boardArray.length - 1) {
                        update_board(boardPanel);
                        message = "Player " + (current_player == 0 ? "A" : "B") + " wins!";
                        rollDiceButton.setEnabled(false); 
                    }
                }

                diceResultLabel.setText(message);
                update_board(boardPanel);

                current_player = current_player == 0 ? 1 : 0;
                currentPlayerLabel.setText("Player " + (current_player == 0 ? "A" : "B") + "'s Turn");
            }
        });
        restartBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restart_game(boardPanel, currentPlayerLabel, diceResultLabel, rollDiceButton);
            }
        });
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonPanel.add(currentPlayerLabel);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonPanel.add(rollDiceButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonPanel.add(diceResultLabel);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonPanel.add(restartBtn);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        frame.pack();
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

}


// Fix errors with snake
// Add color to player and snakes and ladders
// optimize
