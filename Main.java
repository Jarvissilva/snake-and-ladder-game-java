import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class Main {

    static String[] boardArray = {
            "AB", "", "", "", "+3",
            "", "-4", "", "", "+3",
            "", "", "", "+2", "",
            "", "", "-6", "", "",
            "", "", "", "", "E",
    };

    static int current_player = 0;
    static int[] player_positions = { 0, 0 };

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
        boardArray[4] = "+3";
        boardArray[6] = "-4";
        boardArray[9] = "+3";
        boardArray[13] = "+2";
        boardArray[17] = "-6";
        boardArray[boardArray.length - 1] = "E";

        if (player_positions[0] < boardArray.length) {
            boardArray[player_positions[0]] += "A";
        }
        if (player_positions[1] < boardArray.length) {
            boardArray[player_positions[1]] += "B";
        }

        for (int i = 0; i < components.length; i++) {
            JLabel cellLabel = (JLabel) components[i];
            String cellText = boardArray[i];

            cellLabel.setText(cellText);

            if (boardArray[i].contains("+")) {
                cellLabel.setBackground(Color.GREEN);
            } else if (boardArray[i].contains("-")) {
                cellLabel.setBackground(Color.ORANGE);
            } else {
                cellLabel.setBackground(Color.WHITE);
            }
            // Set player colors
            if (cellText.contains("A")) {
                cellLabel.setForeground(Color.BLUE); // Player A color
            } else if (cellText.contains("B")) {
                cellLabel.setForeground(Color.ORANGE); // Player B color
            } else {
                cellLabel.setForeground(Color.BLACK); // Default color
            }

            // Set font size for player symbols
            if (cellText.contains("A") || cellText.contains("B")) {
                cellLabel.setFont(new Font("Arial", Font.BOLD, 20)); // Adjust font size here
            } else {
                cellLabel.setFont(new Font("Arial", Font.PLAIN, 12)); // Default font size for other texts
            }

            cellLabel.setOpaque(true);
        }
    }

    static void restart_game(JPanel boardPanel, JLabel currentPlayerLabel, JLabel diceResultLabel,
            JButton rollDiceButton) {
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
        boardPanel.setLayout(new GridLayout(5, 5));
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
                        case 4:
                            new_player_position += 3;
                            message += " You got a small boost of +2";
                            break;
                        case 6:
                            new_player_position -= 4;
                            message += " Bummer! You go back -4";
                            break;
                        case 9:
                            new_player_position += 3;
                            message += " Bummer! You move ahead +3";
                            break;
                        case 13:
                            new_player_position += 2;
                            message += " Nice! You move ahead +2";
                            break;
                        case 17:
                            new_player_position -= 6;
                            message += " Bummer! You go back -6";
                            break;
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

        // frame.pack();
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
        update_board(boardPanel);
    }

}

// Fix errors with snake
// Add color to player and snakes and ladders
// optimize
