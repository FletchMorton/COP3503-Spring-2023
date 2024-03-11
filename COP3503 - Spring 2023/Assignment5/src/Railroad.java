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
	private ArrayList<DisjointSet> graph;	//Graph representing the railroad system with all tracks
	private ArrayList<Vertex> vertices;		//List of vertices in the graph
	private ArrayList<Edge> edges;			//List of edges in the graph
	private String filename;
	private int numTracks;
	
	
	//Constructors
	//Constructor with no parameters
	public Railroad() {
		this.numTracks = 0;
		this.filename = null;
	}
	
	//Overloaded constructor with an integer and String parameter
	public Railroad(int numTracks, String filename) {
		this.numTracks = numTracks;
		this.filename = filename;
		readInput();
	}
	
	
	
	
	//Methods
	public void readInput() {
		this.vertices = new ArrayList<Vertex>();
		this.edges = new ArrayList<Edge>();
		Scanner read = null;
		
		
		//Connect Scanner to the input file
		try {
			read = new Scanner(new File(this.filename));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(0);
		}

		
		String line;
		String [] buffer;
		
		//Read each line and place each integer value into the greed array
		while(read.hasNextLine()) {
			line = read.nextLine();
			buffer = line.split(" ");
			if(!isDuplicate(buffer[0])) vertices.add(new Vertex(vertices.size(), buffer[0]));
			if(!isDuplicate(buffer[1])) vertices.add(new Vertex(vertices.size(), buffer[1]));
			edges.add(new Edge(getNum(buffer[0]), getNum(buffer[1]), atoi(buffer[2])));
		}
	
		numTracks = edges.size();
		
		//Files reading is finished
		read.close();
	}
	
	
	
	public String buildRailroad() {
		
		DisjointSet mst;
		this.graph = new ArrayList<DisjointSet>();
		
		//Make each vertex its own set
		for(int i = 0; i < vertices.size(); i++)
			graph.add(new DisjointSet(1));
		
		//Sort edges based on increasing weight
		Collections.sort(edges, new CompareEdge());
		
		printDebug();
		
		return null;
	}
	
	//...
	public boolean isDuplicate(String str) {
		
		for(int i = 0; i < vertices.size(); i++) 
			if(str.equals(vertices.get(i).name)) return true;
		
		return false;
	}
	
	
	//Convert string to int
	public int atoi(String str) {
		int length = (str.length() - 1); //Length of string to see how many digits the number has
		
		int result = 0;
		for(int i = 0; i <= length; i++)
			result += ((str.charAt(i) - 48) * ((int) Math.pow(10, length-i))); //value of digit * 10^(digit places)
		
		return result;
	}
	
	
	public void printDebug() {
		System.out.println("Vertices:");
		for(int i = 0; i < vertices.size(); i++)
			System.out.println(vertices.get(i).num + " " + vertices.get(i).name);
		
		System.out.println();
		
		System.out.println("Edges:");
		for(int i = 0; i < edges.size(); i++)
			System.out.println(edges.get(i).src + " " + edges.get(i).dst + " " + edges.get(i).weight);
		
		
		System.exit(0);
	}
	

	//Return the number representative for the vertex of the passed name
	public int getNum(String name) {
		for(int i = 0; i < vertices.size(); i++)
			if(vertices.get(i).name.equals(name)) return vertices.get(i).num;
		
		return -1;
	}
	
	//------------------Comparator Class------------------
	class CompareEdge implements Comparator<Edge> {
		//Overridden compare method to compare by increasing weight
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
		
		//Constructor
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
		
		//Travel a node representative and compress paths along the way
		public int find(int x) {
			if(parent[x] != x) parent[x] = find(parent[x]);
			return parent[x];
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
	
	//---------------------Vertex Class---------------------
	public class Vertex {
		//Variables
		int num;
		String name;
				
		//Constructor
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
