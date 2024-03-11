/* Fletcher Morton
 * Dr. Steinberg
 * COP3503 Spring 2023
 * Programming Assignment 5
 */

//Imports
import java.util.Collections;
import java.util.Comparator;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;



//Railroad
public class Railroad {

	//Variables
	private ArrayList<Vertex> vertices;		//List of vertices in the graph
	private ArrayList<Edge> edges;			//List of edges in the graph
	private String filename;
	private int numTracks;
	
	
	//Constructors
	//Constructor with an integer and String parameter
	public Railroad(int numTracks, String filename) {
		this.numTracks = numTracks;
		this.filename = filename;
		readInput();
	}
	
	
	//Methods
	//Read each line in the file, and populate the vertex and edge ArrayLists with the retrieved data
	public void readInput() {
		//Create ArrayLists
		this.vertices = new ArrayList<Vertex>();
		this.edges = new ArrayList<Edge>();
		
		//Connect Scanner to the file
		Scanner read = null;
		try {
			read = new Scanner(new File(this.filename));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(0);
		}

		//Read each line and place each integer value into the greed array
		String line;
		String [] buffer;
		while(read.hasNextLine()) { //Loop until EOF is reached
			
			//Parse the line
			line = read.nextLine();
			buffer = line.split(" ");
			
			//Add the data to the appropriate ArrayLists
			if(!isDuplicate(buffer[0])) vertices.add(new Vertex(vertices.size(), buffer[0])); //Only add unique vertices to the list
			if(!isDuplicate(buffer[1])) vertices.add(new Vertex(vertices.size(), buffer[1])); //Only add unique vertices to the list
			edges.add(new Edge(getNum(buffer[0]), getNum(buffer[1]), atoi(buffer[2]))); //Add an edge with the numbers that correspond to the two vertices, as well as the weight from the file
		}
	
		//If the number of tracks do not match the expected value, an error has occurred
		if(numTracks != edges.size()) {
			System.err.println("Number of tracks did not match the expected value!");
			System.exit(1);
		}
		
		//File reading is finished
		read.close();
	}
	
	
	//Build the cheapest railroad system possible through a Minimum Spanning Tree
	public String buildRailroad() {
		
		//Variables
		String result = "";
		int total = 0;
		
		//Minimum Spanning Tree representing the cheapest railroad system
		DisjointSet graph = null;
		
		//Make each vertex into its own set
		graph = new DisjointSet(vertices.size());
		
		//Sort edges based on increasing weight
		Collections.sort(edges, new CompareEdge());
		
		//For each edge, if the source and destination do not share a representative node, union them
		for(int i = 0; i < edges.size(); i++) {
			//Check that the two nodes do not share a common representative
			if(graph.find(edges.get(i).src) != graph.find(edges.get(i).dst)) {
				result += printEdge(i); //Add the edge information to the result string
				total += edges.get(i).weight; //Increase the total cost
				graph.union(edges.get(i).src, edges.get(i).dst); //Union the two vertices
				
			}
		}
		
		//Add the final price to the result string and return it
		result += "The cost of the railroad is $" + total + ".";
		return result;
	}
	
	//Create a string representing information about the indexed edge
	public String printEdge(int i) {
		//Variables
		String src = vertices.get(edges.get(i).src).name;
		String dst = vertices.get(edges.get(i).dst).name;
		int order = src.compareTo(dst); // order > 0 if s1 < s2, order < 0 if s1 > s2
		
		//Return the string with edge information, making sure that nodes are printed lexicographically
		if(order < 1) return src + "---" +  dst + "\t$" + edges.get(i).weight + "\n";
		return dst + "---" +  src + "\t$" + edges.get(i).weight + "\n";
	}
	
	//----------------Helper Methods----------------
	
	//Check if the passed string matches a string already in the vertices array
	public boolean isDuplicate(String str) {
		
		for(int i = 0; i < vertices.size(); i++) 
			if(str.equals(vertices.get(i).name)) return true;
		
		return false;
	}
	
	
	//Convert a String to an integer
	public int atoi(String str) {
		int length = (str.length() - 1); //Length of string to see how many digits the number has
		
		//For each digit
		int result = 0;
		for(int i = 0; i <= length; i++)
			result += ((str.charAt(i) - 48) * ((int) Math.pow(10, length-i))); //value of digit * 10^(digit places)
		
		//Return the result integer
		return result;
	}
	

	//Return the number representative for the vertex of the passed name
	public int getNum(String name) {
		for(int i = 0; i < vertices.size(); i++)
			if(vertices.get(i).name.equals(name)) return vertices.get(i).num;
		
		//If the for loop was exited, there was an error
		System.err.println("Method getNum was passed a name for a vertex that does not exist!");
		System.exit(1);
		
		//Should never execute
		return -1;
	}
	
	//------------------Comparator Class------------------
	class CompareEdge implements Comparator<Edge> {
		//Overridden compare method to compare edge objects by increasing weight
		@Override
		public int compare(Railroad.Edge e1, Railroad.Edge e2) {
			return Integer.valueOf(e1.weight).compareTo(e2.weight);
		}
	}
	
	//----------------------------------------------------
	
	//-----------------Disjoint Set Class-----------------
	public class DisjointSet {
		//Variables
		int [] rank;
		int [] parent;
		int nodes;
		
		//Constructor with one integer as a parameter
		public DisjointSet(int nodes) {
			rank = new int[nodes];
			parent = new int[nodes];
			this.nodes = nodes;
			makeSet();
		}
		
		//Methods
		//Create a new set for each node, containing only that node
		public void makeSet() {
			for(int i = 0; i < nodes; i++)
				parent[i] = i;
		}
		
		//Return the representative of a node
		public int find(int x) {
			if(parent[x] == x) return x;
			
			return find(parent[x]);
		}
		
		//Join two sets, with respect to rank
		public void union(int x, int y) {
			//Grab representative
			int rootX = find(x);
			int rootY = find(y);
			
			//If the roots are part of the same set, return
			if(rootX == rootY) return;
			
			//Join according to rank
			if(rank[rootX] < rank[rootY]) { //Y has a higher rank
				parent[rootX] = rootY;
				
			} else if(rank[rootX] > rank[rootY]) { //X has a higher rank
				parent[rootY] = rootX;
			
			} else { //The ranks are equal
				parent[rootY] = rootX;
				rank[rootX]++;
			}
			
		}
		
	}
	
	//----------------------------------------------------
	
	//--------------------Vertex Class--------------------
	public class Vertex {
		//Variables
		int num;
		String name;
				
		//Constructor with an integer and string parameter
		public Vertex(int num, String name) {
			this.num = num;
			this.name = name;
		}
	}
				
	//----------------------------------------------------
		
	//---------------------Edge Class---------------------
	public class Edge {
		//Variables
		int src;
		int dst;
		int weight;
				
		//Constructor
		public Edge(int src, int dst, int weight) {
			this.src = src;
			this.dst = dst;
			this.weight = weight;
		}
	}
				
	//----------------------------------------------------
	
}
