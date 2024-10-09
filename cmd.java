import java.util.Random;
import java.util.Scanner;

public class Main {
    static String[] board = {
        "AB","*","*","*","*",
        "*","*","*","*","*",
        "*","*","*","*","*",
        "*","*","*","*","*",
        "*","*","*","*","E",
    };

    static boolean is_game_finished = false;

    static String player_1 = "A";
    static String player_2 = "B";

    static int player_1_position = 0;
    static int player_2_position = 0;

    static String current_player = player_1;
    
    static void display_board(){
        for (int i = 0; i < board.length; i++) {
            if (i % 5 == 0) {
                System.out.println();    
                System.out.println();    
            }
            System.out.print(board[i] + "   ");
        }
        System.out.println();
    }
    static int roll_dice() {
        Random random = new Random();
        int min = 1;
        int max = 3;
        int randomNumber = random.nextInt(max - min + 1) + min;
        return randomNumber;
    }

    static void play(int dice_value) {
        System.out.println("You got a " + dice_value);
        
        int currentPosition = (current_player == player_1) ? player_1_position : player_2_position;

        int new_position = currentPosition + dice_value;
        
        // if (currentPosition == 4) {
        //     new_position = 0;
        // }
        // if (currentPosition == 8) {
        //     new_position = 13;
        // }
        // if (currentPosition == 4) {
        //     new_position = 0;
        // }
        
        if (new_position > 24) {
            System.out.println("Bummer! You rolled too high.");
        } else {
            if (current_player == player_1) {
                board[player_1_position] = "*";
                player_1_position = new_position;
                board[player_1_position] += "A";
            } else {
                board[player_2_position] = "*";
                player_2_position = new_position;
                board[player_2_position] += "B";
            }
            
            check_game();
            
            current_player = (current_player == player_1) ? player_2 : player_1;
        }
    }
    static void check_game(){
        if(player_1_position == 24 || player_2_position == 24){
            is_game_finished = true;
            System.out.println("Player " + current_player + " wins!");
        }
    }

    public static void main(String[] args) {
        System.out.println("Welcome To Snake & Ladder Game");
        display_board();
        while (is_game_finished == false) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Player "+ current_player +" turn to roll the dice enter:");
            scanner.nextLine();
            int dice_rolled = roll_dice();
            play(dice_rolled);
            display_board();
        }
    }
}
