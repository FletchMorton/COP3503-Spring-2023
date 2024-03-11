/* Fletcher Morton
   Dr. Steinberg
   COP3503 Spring 2023
   Programming Assignment 3
*/

//Imports
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;


//Cookies
public class Cookies {

	//Variables
	private ArrayList<Integer> greed; //Greedy factor for each camper
	private ArrayList<Integer> size; //Size of each cookie
	private String greedFile; //txt file containing the greedy factor levels
	private String sizeFile; //txt file containing the cookie size factors
	private int cookies; //Number of cookies supplied to pass out
	private int campers; //Number of campers
	private int happy; //Numbers of campers happy (after passCookies)
	private int angry; //Number of campers angry (after passCookies)
	
	
	
	//Constructors
	//Constructor with no parameters
	public Cookies() {
		this.greedFile = null;
		this.sizeFile = null;
		this.cookies = 0;
		this.campers = 0;
	}
	
	//Constructor with four parameters(int, int, String, String)
	public Cookies(int cookies, int campers, String greedFile, String sizeFile) {
		this.greedFile = greedFile;
		this.sizeFile = sizeFile;
		this.cookies = cookies;
		this.campers = campers;
	}
	
	
	
	//Method to scan the greed and size text files and initialize their respective arrays
	public void read() {
		this.greed = new ArrayList<Integer>();
		this.size = new ArrayList<Integer>();
		Scanner read = null;
		
		
		//Connect Scanner to the GREED file
		try {
			read = new Scanner(new File(this.greedFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		//Read each line and place each integer value into the greed array
		while(read.hasNextLine()) {
			greed.add(read.nextInt());
		}
		
		//Connect Scanner to the SIZE file
		try {
			read = new Scanner(new File(this.sizeFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(0);
		}
				
		//Read each line and place each integer value into the size array
		while(read.hasNextLine()) {
			size.add(read.nextInt());
		}
	
		//Files reading is finished
		read.close();
	}
	
	
	
	//Method that makes a greedy decision when passing out cookies such that the maximum number of campers get their desired cookie
	public void passCookies() {
		
		//Read in the contents of the Cookie object's test files
		read();
		
		//Sort the two read in ArrayLists in order form least to greatest integers
		Collections.sort(this.greed);
		Collections.sort(this.size);
		
		//Greedy technique is to give out cookies to the least greedy campers first, so that as many campers as possible are satisfied with the size of their cookie
		//Minimum Greedy
		int j = 0; //Cookie index
		for(int i = 0; i <= campers; i++) { //Pass out a cookie to each camper (starting with least greedy)
			
			if(j == cookies) break;
			
			//Try to give cookie j to camper i
			if(size.get(j) >= greed.get(i)) { //If camper i is satisfied with the cookie size
				j++; //Give them the cookie
				happy++; //Mark them as a happy camper
			} else { //If camper i was not satisfied with the cookie size
				i--; //Don't move on just yet
				j++; //Prepare to give them the next largest cookie
			}
		}
		
		//The remaining, more greedy campers, get the smaller cookies that were skipped over earlier
		//Calculate the number of angry campers
		angry = campers - happy;
	}
	
	
	
	//Method to display the results of passCookies
	public void display() {
		System.out.println("There are " + happy + " happy campers.");
		System.out.println("There are " + angry + " angry campers.");
	}
}
