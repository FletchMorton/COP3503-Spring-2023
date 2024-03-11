/* Fletcher Morton
   Dr. Steinberg
   COP3503 Fall 2022
   Programming Assignment 1
*/

//Imports
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

//Game
public class Game {
	
	//Variables
	private static final int ERROR = 999;
	int size;
	String fileName;
	int board[][];
	char moves[];
	
	
	
	//Constructor
	public Game(int size, String fileName) {
		this.size = size;
		this.board = new int[size][size];
		this.moves = new char[size*size];
		this.fileName = fileName;
	}
	
	
	
	//Initializes the array with zeroes
	public void initializeBoard() {
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				board[i][j] = 0;
			}
		}
				
		//Place the chip at the top left of the board
		board[0][0] = 1;
	}

	
	
	//Read moves from file into the char array
	public void readMoves(String fileName) {
		
		Scanner read = null;
		char move;
		int i = 0;
		
		//Connect Scanner to the passed file name
		try {
			read = new Scanner(new File(fileName));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(0);
		}
				
		//Read each line and place each character into the moves array
		while(read.hasNextLine()) {
			move = read.nextLine().charAt(0);
			
			moves[i] = move;
			i++;
		}

		//File reading is finished
		read.close();
	}
	
	
	
	//Simulates a round of the game
	public int play(int victor) {
		int curRow = 0;
		int curCol = 0;
		int player = 1; //Player 1 always moves first
		
		//Read the moves file
		readMoves(this.fileName);
		
		//Initialize the board
		initializeBoard();
		
		//Run through all the moves
		for(int i = 0; i < moves.length; i++) {
			
			
			//------------------------------PLAYER 1------------------------------

			//If this is the opening move, AND we are running strategy 1, move diagonally
			if(i == 0 && player == 1 && strategize(victor) == 1) {
				board[curRow][curCol] = 0;
				curRow++;
				curCol++;
				board[curRow][curCol] = 1;
					
			//Otherwise, move right
			} else {
				if(curCol+1 < size) { //Make sure the move is possible
					board[curRow][curCol] = 0;
					curCol++;
					board[curRow][curCol] = 1;
						
				} else { //If not, the only other possible move to make is moving downward
					board[curRow][curCol] = 0;
					curRow++;
					board[curRow][curCol] = 1;
				}
			}
			
			//If the game piece is on the win square, return player
			if(curRow == size-1 && curCol == size-1) return player;
			
			//Switch players
			if(player == 1) player++;
			else player--;
			
			//--------------------------------------------------------------------
			
		
			//If you read a move that is invalid, the end of the file was reached
			if(moves[i] != 'r' && moves[i] != 'd' && moves[i] != 'b') break;
			
			
			//------------------------------PLAYER 2------------------------------
			
			//Program read a move down
			if(moves[i] == 'b') {
				if(curRow+1 < size) { //Make sure the move is possible
					board[curRow][curCol] = 0;
					curRow++;
					board[curRow][curCol] = 1;
					
				} else { //If not, the only other possible move to make is moving right
					board[curRow][curCol] = 0;
					curCol++;
					board[curRow][curCol] = 1;
				}
			}
			
			//Program read a move right
			if(moves[i] == 'r') {
				if(curCol+1 < size) { //Make sure the move is possible
					board[curRow][curCol] = 0;
					curCol++;
					board[curRow][curCol] = 1;
					
				} else { //If not, the only other possible move to make is moving downward
					board[curRow][curCol] = 0;
					curRow++;
					board[curRow][curCol] = 1;
				}
			}
			
			//Program read a move diagonally
			if(moves[i] == 'd') {
				if(curRow+1 < size && curCol+1 < size) { //Make sure the move is possible
					board[curRow][curCol] = 0;
					curRow++;
					curCol++;
					board[curRow][curCol] = 1;
					
				} else {
					if(curRow+1 < size) { //If a downward move is valid, that means we were restricted by the right bound. Can only move downward.
						board[curRow][curCol] = 0;
						curRow++;
						board[curRow][curCol] = 1;
						
					} else { //If not, that means we were restricted by the downward bound. Can only move right.
						board[curRow][curCol] = 0;
						curCol++;
						board[curRow][curCol] = 1;
					}
				}
			}
			
			//If you get to the win square, return player
			if(curRow == size-1 && curCol == size-1) return player;

			//Switch players
			if(player == 1) player++;
			else player--;
			
			//--------------------------------------------------------------------
			
		}
		
		//End of Execution
		return ERROR;
	}
	
	
	
	//Depending on the desired victor, chooses an opening move for PLAYER 1 (every other move besides the opening move for PLAYER 1 is r) 
	public int strategize(int victor) {
		int curRow = 0;
		int curCol = 0;
		int player = 1;
			
		//Simulate a game where PLAYER 1 has a hand of all rights. Exits as soon as game piece enters the last column
		for(int i = 0; i < moves.length; i++) {
			
			//Player 1 moves right
			curCol++;
			if(curCol == size-1) break;
			
			//Switch players
			if(player == 1) player++;
			else player--;
			
			//Player 2 moves in scripted direction
			if(moves[i] == 'r') {
				curCol++;
			} else if(moves[i] == 'b') {
				curRow++;
			} else if(moves[i] == 'd') {
				curRow++;
				curCol++;
			}
			
			if(curCol == size-1) break;
			
			//Switch players
			if(player == 1) player++;
			else player--;
			
		}
		
		//Check if the game piece is at the rightmost column of the board
		if(curCol == size-1) { //It had hit the right
			
			//Game piece is in an even row
			if((curRow+1)%2 == 0) {
				
				//The playing board size is an even number
				if(size%2 == 0) {
					if(victor == 1) {
						if(player == 1) return 0; //RIGHT
						if(player == 2) return 1; //DIAG
						
					} else if(victor == 2) { //Return the opposite of the winning strategies
						if(player == 1) return 1; //DIAG
						if(player == 2) return 0; //RIGHT
					
					} else {
						System.out.printf("Player %d does not exist.\n", victor);
						System.exit(0);
					}
					
				//The playing board size is an odd number
				} else {
					if(victor == 1) {
						if(player == 1) return 1; //DIAG
						if(player == 2) return 0; //RIGHT
					
					} else if(victor == 2) {
						if(player == 1) return 0; //RIGHT
						if(player == 2) return 1; //LEFT
					
					} else {
						System.out.printf("Player %d does not exist.\n", victor);
						System.exit(0);
					}
				}
				
			//Game piece is in an even row
			} else {
				
				//The playing board size is an even number
				if(size%2 == 0) {
					if(victor == 1) {
						if(player == 1) return 1; //DIAG
						if(player == 2) return 0; //RIGHT
						
					} else if (victor == 2) {
						if(player == 1) return 0; //RIGHT
						if(player == 2) return 1; //DIAG
					
					} else {
						System.out.printf("Player %d does not exist.\n", victor);
						System.exit(0);
					}
					
				//The playing board size is an odd number
				} else {
					if(victor == 1) {
						if(player == 1) return 0; //RIGHT
						if(player == 2) return 1; //DIAG
					
					} else if(victor == 2) {
						if(player == 1) return 1; //DIAG
						if(player == 2) return 0; //RIGHT
						
					} else {
						System.out.printf("Player %d does not exist.\n", victor);
						System.exit(0);
					}
				}
			}
			
		} else {
			System.out.println("Fatal error.");
			System.exit(0);
		}
			
		//End of execution reached
		return ERROR;
	}
	
}
