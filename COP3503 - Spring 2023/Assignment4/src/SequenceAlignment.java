/* Fletcher Morton
 * Dr. Andrew Steinberg
 * COP3503 Spring 2023
 * Programming Assignment 4
 */

//SequenceAlignment
public class SequenceAlignment {

	//Variables
	private int subseqArr[][];
	private char pathArr[][];
	private String word1;
	private String word2;
	private int row;
	private int col;
	
	
	//Constructors
	//Constructor with no parameters
	public SequenceAlignment() {
		this.word1 = null;
		this.word2 = null;
		this.row = 0;
		this.col = 0;
	}
	
	//Overloaded constructor with two String parameters
	public SequenceAlignment(String word1, String word2) {
		this.word1 = word1;
		this.word2 = word2;
		this.row = word1.length() + 1;
		this.col = word2.length() + 1;
		
		this.subseqArr = new int[row][col];
		this.pathArr = new char[row][col];
	}
	
	
	//Methods
	
	//DP solution to find optimal alignment. Consider costs of adjacent cells and calculate the lowest cell value for each cell
	public void computeAlignment(int delta) {
		//Initialize the subsequence array
		initializeArrays(delta);
		
		//Traverse through each cell in the 2D array, beginning with 1,1
		for(int i = 1; i < row; i++) {
			for(int j = 1; j < col; j++) {
				int alpha = computeAlpha(i-1, j-1); //Compute the cell's respective alpha value
				
				//Taking the vertical value is the best option
				if(subseqArr[i][j-1] + delta > subseqArr[i-1][j] + delta) {
					subseqArr[i][j] = subseqArr[i-1][j] + delta;
					pathArr[i][j] = 'v';
				
				//Taking the horizontal value is the best option
				} else {
					subseqArr[i][j] = subseqArr[i][j-1] + delta;
					pathArr[i][j] = 'h';
				}
				
				//Taking the diagonal value is the best option
				if(subseqArr[i][j] >= subseqArr[i-1][j-1] + alpha) {
					subseqArr[i][j] = subseqArr[i-1][j-1] + alpha;
					pathArr[i][j] = 'd';
				}
			}
		}
	}
	
	//Decide where to displace the two strings, based on the choices made in computeAlignment (and stored in pathArr[][])
	public String getAlignment() {
		//Utilizing StringBuilder for string manipulation/character insertions
		StringBuilder alignedW1 = new StringBuilder(word1);
		StringBuilder alignedW2 = new StringBuilder(word2);
		
		//Current cell coordinates
		int x = row-1;
		int y = col-1;
		
		//Current position in each string
		int w1Position = word1.length();
		int w2Position = word2.length();
		
		//Analyze the move that computeAlignment took to get to the current cell, and displace the strings accordingly
		char direction = pathArr[x][y];
		while(x > 0 && y > 0 && w1Position >= 0 && w2Position >= 0) {
			
			direction = pathArr[x][y];
			switch(direction) {
				case 'd': //It was a diagonal move. Do not insert any gaps
					x--;
					y--;
					w1Position--;
					w2Position--;
					break;
				
				case 'v': //It was a vertical move. Insert a gap at the current character position of string 2
					alignedW2.insert(w2Position, '-');
					x--;
					w1Position--;
					break;
					
				case 'h': //It was a horizontal move. Insert a gap at the current character position of string 1
					alignedW1.insert(w1Position, '-');
					y--;
					w2Position--;
					break;
					
				default: //Terrible fate
					System.out.println("An unexpected value was placed in the path array.");
					System.exit(0);
					break;
			
			}

		}
		
		//Return the aligned words as one string separated by a whitespace character
		return alignedW1 + " " + alignedW2;
	}
	
	//Initialize certain values in the two global arrays
	public void initializeArrays(int delta) {
		
		//Initialize the first column of both arrays
		for(int x = 1; x < row; x++) {
			subseqArr[x][0] = x*delta;
			pathArr[x][0] = 'v';
		}
		
		//Initialize the first row of both arrays
		for(int x = 1; x < col; x++) {
			subseqArr[0][x] = x*delta;
			pathArr[0][x] = 'h';
		}
		
		//Set the origin in the path array
		pathArr[0][0] = 'o';
	}
	
	//Calculate the alpha value for the current cell based on the characters from the respective column and row
	public int computeAlpha(int i, int j) {
		//Characters at the respective row and column
		char ch1 = word1.toLowerCase().charAt(i);
		char ch2 = word2.toLowerCase().charAt(j);
		
		//Same Symbol
		if(ch1 == ch2) return 0;
		
		//Check if the characters are vowels or consonants, and determine alpha accordingly
		if(ch1 == 97 | ch1 == 101 | ch1 == 105 | ch1 == 111 | ch1 == 117) {
			
			if(ch2 == 97 | ch2 == 101 | ch2 == 105 | ch2 == 111 | ch2 == 117) {
				return 1; // Vowel|Vowel Mismatch
			
			} else return 3; // Vowel|Consonant Mismatch
		
		} else if (ch2 == 97 | ch2 == 101 | ch2 == 105 | ch2 == 111 | ch2 == 117) {
			return 3; // Vowel|Consonant Mismatch
		
		} else return 1; // Consonant|Consonant Mismatch
		
	}
}
