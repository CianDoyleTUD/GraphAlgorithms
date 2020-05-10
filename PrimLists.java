// Simple weighted graph representation 
// Uses an Adjacency Linked Lists, suitable for sparse graphs

import java.io.*;
import java.util.ArrayList;
import java.util.Stack; 
import java.util.Scanner;

class Graph {
    class Node {
        public int vert;
        public int wgt;
        public Node next;
    }
   
    class Edge {  // Edge class used for minimum spanning tree, purely for display purposes
        public int vert1;
        public int vert2;
        public int weight;
    }
    
    private Node z; // sentinel node to mark end of adjacency list
    private Node tempNode; 
    private Node[] adj;

    private int V, E; // total number of vertices and edges in graph
    private int mstWeight;
    private int viableNode;
    private int[] visited;
    private int[][] adjMatrix;
    
    private ArrayList<Edge> mst = new ArrayList<Edge>(); // minimum spanning tree, represented as set of edges for display purposes
    private ArrayList<Integer> toVisit = new ArrayList<Integer>(); // keeping track of nodes needed to visit to build mst
    private Stack<Integer> dfStack; // stack used for iterative version of depth-first
    
    // default constructor
    public Graph(String graphFile)  throws IOException
    {
        int u, v;
        int e, wgt;
        Node t;

        FileReader fr = new FileReader(graphFile);
		BufferedReader reader = new BufferedReader(fr);
	           
        String splits = " +";  // multiple whitespace as delimiter
		String line = reader.readLine();        
        String[] parts = line.split(splits);
        System.out.println("Graph succesfully loaded.\nVertices: " + parts[0] + " Edges: " + parts[1]);
        
        V = Integer.parseInt(parts[0]);
        E = Integer.parseInt(parts[1]);
        
        adjMatrix = new int[V+1][V+1]; // Initialise adjacency matrix to appropriate size

        visited = new int[V+1];

        // create sentinel node
        z = new Node(); 
        z.next = z;
        
        // create adjacency lists, initialised to sentinel node z       
        adj = new Node[V+1];        
        for(v = 1; v <= V; ++v)
            adj[v] = z;               
        
        // read the edges
        for(e = 1; e <= E; ++e)
        {
            line = reader.readLine();
            parts = line.split(splits);
            u = Integer.parseInt(parts[0]);
            v = Integer.parseInt(parts[1]); 
            wgt = Integer.parseInt(parts[2]);

            // Putting edges into adjacency matrix;
            adjMatrix[u][v] = wgt;
            adjMatrix[v][u] = wgt;

            // Putting both occurences of the edge into adjacency lists
            tempNode = new Node();
            tempNode.vert = u;
            tempNode.next = adj[v];
            tempNode.wgt = wgt;

            adj[v] = tempNode;

            tempNode = new Node();
            tempNode.vert = v;
            tempNode.next = adj[u];
            tempNode.wgt = wgt;

            adj[u] = tempNode;
            
        }	       

    }
   
    // convert vertex into char for pretty printing
    private char toChar(int u)
    {  
        return (char)(u + 64);
    }

    // method to display the graph representation
    public void display() 
    {
        int v;
        Node n;

        System.out.println("\nAdjacency matrix\n"); 
        System.out.print("   ");

        for(int i = 1; i <= V; i++){
            System.out.print(toChar(i) + " ");
        }

        System.out.println("");
       
        for(int i = 1; i <= V; i++) // Loop through adjacency matrix and print values
        {
            for(int j = 1; j <= V; j++)
            {
                if (j == 1)
                    System.out.print(toChar(i) + "| ");
                if(adjMatrix[i][j] == 0) // prints null otherwise, which ruins formatting
                    System.out.print("0 ");   
                else
                    System.out.print(adjMatrix[i][j] + " "); 
            }
            System.out.println("");
        }
        
        for(v = 1; v <= V; ++v) // Print adjacency lists
        {
            System.out.print("\nAdjacency list for node " + toChar(v) + " ->" );

            for(n = adj[v]; n != z; n = n.next) 
                System.out.print(" (" + toChar(n.vert) + " | " + n.wgt + ") ->");    
        }
        System.out.println("");
    }

	public void MST_Prim(int s)
	{
        mstWeight = 0; // Total weight is 0 initially since no nodes in tree

        for(int v = 1; v <= V; v++)
            toVisit.add(v);

        visited[s] = 1; // Adding our starting point into list of visted nodes.

        toVisit.remove(Integer.valueOf(s)); // No longer consider this node since it's in our tree

        System.out.println("\n---Prim's algorithm---\n");
        System.out.println("Starting at vertex " + toChar(s));

        // Keep building MST until all nodes are visited
        while(!toVisit.isEmpty()) 
        {
            checkEdges();
        }
        
        showMST();
    }
    
    public void checkEdges() 
    {
        viableNode = 0;
        int weight = 99; 
        Node n;
        Edge e;
        e = new Edge();

        for(int v = 1; v <= V; v++)
        {
            if(visited[v] == 1)// If node is in our tree, consider the edges from node
            { 
                System.out.print("\nPossible connections from [" + toChar(v) + "] -> ");

                for(n = adj[v]; n != z; n = n.next) // Print neighbours
                {
                    if(toVisit.contains(n.vert)) // If the neighbour is not in MST yet
                    {
                        System.out.print("(Node " + toChar(n.vert) + " | Weight: " + n.wgt + ") - ");

                        if(n.wgt < weight) // Finding node connected with least weight
                        {   
                            weight = n.wgt;
                            viableNode = n.vert;
                            e.vert1 = v;
                        }
                    }
                }
            }
        }

        mstWeight += weight; // Keep track of total weight of spanning tree

        e.vert2 = viableNode;
        e.weight = weight;

        System.out.println("\n\nAdding node " + toChar(viableNode) + " to minimum spanning tree with weight " + weight + " from " + toChar(e.vert1));
        System.out.println("Current spanning tree weight: " + mstWeight);
        
        visited[viableNode] = 1; 
        toVisit.remove(Integer.valueOf(viableNode));
        mst.add(e); // Add closest node to tree

    }
    
    public void showMST()
    {
        System.out.print("\n\nEdges of spanning tree, in the order they were chosen:\n");

        for(int i = 0; i < mst.size(); i++) {   
            System.out.print("{" + toChar(mst.get(i).vert1) + " -> " + toChar(mst.get(i).vert2) + "} Weight: " + mst.get(i).weight + "\n");
        }  

        System.out.println("Total MST weight: " + mstWeight);
    }

    public void DF(int s) // Recursive version
    {
        Node n;
        n = adj[s]; 

        visited[s] = 1;

        System.out.print("\n Current node [" + toChar(s) + "]\n Visited nodes - " );

        for(int i = 1; i <= V; i++) 
        {
            System.out.print(toChar(i) + ":" + visited[i] + ", ");
        }
        
        for(n = adj[s]; n != z; n = n.next){

            if(visited[n.vert] == 0)
                DF(n.vert);    

        }
    }

    public void DF_iteration(int s) // Iterative version
    {
        for(int i = 1; i <= V; i++) // Initialise all nodes to unvisited, needed if running AFTER recursive version of function since they share the visited array
        {
            visited[i] = 0;
        }

        dfStack = new Stack<Integer>(); // Create a new stack
        dfStack.push(s); // Push root node onto top of stack

        int top;
        Node n;

        while(dfStack.empty() == false) 
        {
            top = dfStack.peek(); // Get value at the top of the stack
            dfStack.pop();

            n = adj[top];
            
            visited[top] = 1; // Mark node as visited

            System.out.print("\nCurrent value on top of stack [" + toChar(top) + "]\nVisited nodes - ");

            for(int i = 1; i <= V; i++) 
            {
                System.out.print(toChar(i) + ":" + visited[i] + ", ");
            }
            
            for(n = adj[top]; n != z; n = n.next){
                if(visited[n.vert] == 0)
                    dfStack.push(n.vert); // Push node onto top of stack
            }
        }
        System.out.print("\n\nStack is empty, all nodes visited\n");
    }
}

public class PrimLists {
    public static void main(String[] args) throws IOException
    {
        int s = 1;
        String fname = "";     
        String vertex = "";
        Scanner in = new Scanner(System.in);   
             
        // Read user input
        System.out.println("\nEnter the name of a txt file from which to load graph:");
        fname = in.nextLine();
        Graph g = new Graph(fname);

        System.out.println("Pick a starting vertex (enter either character or number):");
        vertex = in.nextLine();

        if(Character.isLetter(vertex.charAt(0)))
            s = (int)vertex.charAt(0) - 96; // convert a to 1, b to 2 and so on using ascii table.
        else
            s = Integer.parseInt(vertex);

        in.close(); 
       
        g.display();
        System.out.println("\n---RECURSIVE DEPTH FIRST---");
        g.DF(s);
        System.out.println("\n\n---ITERATIVE DEPTH FIRST---");
        g.DF_iteration(s);
        g.MST_Prim(s);                  
    }
}
