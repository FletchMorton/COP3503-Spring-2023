/* Fletcher Morton
   Dr. Steinberg
   COP3503 Fall 2022
   Programming Assignment 2
*/

//Imports
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


//Magic Maze
public class MagicMaze {

	//Variables
	private static char maze[][];
	private static Teleporter terminals[];
	private String filename;
	private int rows;
	private int columns;
	
	
	
	//Constructors
	//Constructor with no parameters
	public MagicMaze() {
		this.rows = 0;
		this.columns = 0;
		this.filename = null;
	}
	
	//Constructor with three parameters(String, int, int)
	public MagicMaze(String filename, int rows, int columns) {
		this.rows = rows;
		this.columns = columns;
		this.filename = filename;
		maze = new char[rows][columns];
		terminals = new Teleporter[10];
	}
	
	
	
	//Method to read maze layout from file into char array
	private void readInput() {
		Scanner read = null;
		String line;
		
		//Connect scanner to file at passed filename
		try {
			read = new Scanner(new File(filename));
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		//Read each line and place each character in each line into a 2D char array
		while(read.hasNextLine()) {
			for(int i = 0; i < rows; i++) {
				line = read.nextLine();
				
				for(int j = 0; j < columns; j++) {
					maze[i][j] = line.charAt(j);
					
					//If the current space is a teleporter, add the it to the teleporter array
					if(maze[i][j] >= 48 && maze[i][j] <= 57)
						setTeleporter(Character.getNumericValue(maze[i][j]), j, i);
				}
			}
		}
		
		//End of method
		return;
	}
	
	
	//Wrapper method
	public boolean solveMagicMaze() {
		//Initialize
		readInput();
		
		//Recursive Call
		return solveMagicMazeR(rows - 1, 0);
	}
	
	
	
	//Recursive method to solve the magic maze
	public boolean solveMagicMazeR(int curRow, int curCol) {
		
		//Return false if the current space is out of bounds
		if(!checkBounds(curRow, curCol)) return false;
		
		//Return true if the current space is the exit
		if(maze[curRow][curCol] == 'X') return true;
		
		//If the space occupied is a teleporter, teleport
		if(maze[curRow][curCol] >= 48 && maze[curRow][curCol] <= 57) {
			int index = Character.getNumericValue(maze[curRow][curCol]);
			
			//Source -> Destination
			if(curCol == terminals[index].getSourceX() && curRow == terminals[index].getSourceY()) {
				curCol = terminals[index].getDestinationX();
				curRow = terminals[index].getDestinationY();
				
			//Destination -> Source
			} else if(curCol == terminals[index].getDestinationX() && curRow == terminals[index].getDestinationY()) {
				curCol = terminals[index].getSourceX();
				curRow = terminals[index].getSourceY();
				
			//Hanging terminal. No receiving end exists. ERROR
			} else {
				return false;
			}
		}
		
		//Mark path
		if(maze[curRow][curCol] == '*') maze[curRow][curCol] = '#';	
		
		//Try moving right
		if(solveMagicMazeR(curRow, curCol + 1)) return true;
		
		//Try moving up
		if(solveMagicMazeR(curRow - 1, curCol)) return true;
		
		//Try moving down
		if(solveMagicMazeR(curRow + 1, curCol)) return true;
		
		//Try moving left
		if(solveMagicMazeR(curRow, curCol - 1)) return true;
		
		//Backtrack
		return false;
	}
	
	
	
	//Method to check if the currently selected position is a valid one
	public boolean checkBounds(int curRow, int curCol) {
		//Invalid if space is out of bounds
		if(curRow >= rows || curCol >= columns || curRow < 0 || curCol < 0) return false;
		
		//Invalid if space is an obstacle
		if(maze[curRow][curCol] == '@') return false;
		
		//Invalid if space is already searched
		if(maze[curRow][curCol] == '#') return false;
		
		//Valid space
		return true;
	}
	
	
	
	//Set the teleporter's coordinates
	public void setTeleporter(int index, int x, int y) {
			
		//If the teleporter does not exist yet, create it
		if(terminals[index] == null) {
			terminals[index] = new Teleporter(x, y);
				
		//If the teleporter exists, this must be the second terminal. Set its coordinates
		} else {
			terminals[index].setDestination(x, y);
				
		}
	}
	
	
	
	//_______________________________________________________________
	
	//-----------------------Teleporter object-----------------------
	public class Teleporter {
		
		//Variables
		private int sourceX;
		private int sourceY;
		private int destinationX;
		private int destinationY;
		
		//Constructor with no arguments
		public Teleporter() {
			sourceX = 0;
			sourceY = 0;
			destinationX = 0;
			destinationY = 0;
		}
		
		//Constructor with two arguments for coordinates
		public Teleporter(int x, int y) {
			setSource(x, y);
		}
		
		//Sets the coordinates of the source terminal
		public void setSource(int x, int y) {
			sourceX = x;
			sourceY = y;
		}
		
		//Sets the coordinates of the destination terminal
		public void setDestination(int x, int y) {
			destinationX = x;
			destinationY = y;
		}

		//Coordinate getters
		public int getSourceX() {
			return sourceX;
		}

		public int getSourceY() {
			return sourceY;
		}

		public int getDestinationX() {
			return destinationX;
		}

		public int getDestinationY() {
			return destinationY;
		}
	}
	
	//_______________________________________________________________
	
}

