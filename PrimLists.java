// Simple weighted graph representation 
// Uses an Adjacency Linked Lists, suitable for sparse graphs

import java.io.*;
import java.util.ArrayList;

class Heap
{
    private int[] a;	   // heap array
    private int[] hPos;	   // hPos[a[k]] == k
    private int[] dist;    // dist[v] = priority of v

    private int N;         // heap size
   
    // The heap constructor gets passed from the Graph:
    //    1. maximum heap size
    //    2. reference to the dist[] array
    //    3. reference to the hPos[] array
    public Heap(int maxSize, int[] _dist, int[] _hPos) 
    {
        N = 0;
        a = new int[maxSize + 1];
        dist = _dist;
        hPos = _hPos;
    }


    public boolean isEmpty() 
    {
        return N == 0;
    }


    public void siftUp( int k) 
    {
        int v = a[k];

        // code yourself
        // must use hPos[] and dist[] arrays
    }


    public void siftDown( int k) 
    {
        int v, j;
       
        v = a[k];  
        
        // code yourself 
        // must use hPos[] and dist[] arrays
    }


    public void insert( int x) 
    {
        a[++N] = x;
        siftUp( N);
    }


    public int remove() 
    {   
        int v = a[1];
        hPos[v] = 0; // v is no longer in heap
                
        a[1] = a[N--];
        siftDown(1);
        
        a[N+1] = 0;  // put null node into empty spot
        
        return v;
    }

    public void swap(int x, int y) {
        int temp = a[x];
        a[x] = a[y];
        a[y] = temp;
    }

}

class Graph {
    class Node {
        public int vert;
        public int wgt;
        public Node next;
    }

    class Edge {
        public int vert1;
        public int vert2;
        public int weight;
    }
    
    // V = number of vertices
    // E = number of edges
    // adj[] is the adjacency lists array
    private int V, E;
    private Node[] adj;
    private int[][] adjMatrix;
    private Node z;
    private Node tempNode;
    private ArrayList<Edge> mst = new ArrayList<Edge>();;
    private int mstWeight;
    private int viableNode;
    
    // used for traversing graph
    private ArrayList<Integer> toVisit = new ArrayList<Integer>();
    private int[] visited;
    private int id;
    
    
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
        System.out.println("Vertices: " + parts[0] + " Edges: " + parts[1]);
        
        V = Integer.parseInt(parts[0]);
        E = Integer.parseInt(parts[1]);

        adjMatrix = new int[V+1][V+1];
        
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

            //Putting both occurences of the edge into adjacency list
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
    public void display() {
        int v;
        Node n;

        System.out.println("\nAdjacency matrix\n"); 

        System.out.print("   ");
        for(int i = 1; i <= V; i++){
            System.out.print(toChar(i) + " ");
        }

        System.out.println("");
        
        for(int i = 1; i <= V; i++)
        {
            for(int j = 1; j <= V; j++)
            {
                if (j == 1)
                    System.out.print(toChar(i) + "| ");
                if(adjMatrix[i][j] == 0) // Looks better than printing null
                    System.out.print("0 ");   
                else
                    System.out.print(adjMatrix[i][j] + " "); 
            }
            System.out.println("");
        }

        for(v=1; v<=V; ++v)
        {
            System.out.print("\nAdjacency list for [" + toChar(v) + "] ->" );
            for(n = adj[v]; n != z; n = n.next) 
                System.out.print(" (" + toChar(n.vert) + " | " + n.wgt + ") ->");    
        }
        System.out.println("");
    }

	public void MST_Prim(int s)
	{
        
        visited = new int[V+1];
        mstWeight = 0;

        for(int v = 1; v <= V; v++)
            toVisit.add(v);

        
        visited[s] = 1; // Adding our starting point into list of visted nodes.

        toVisit.remove(Integer.valueOf(s)); // No longer consider this node since it's in our tree

        System.out.println("Prim's algorithm");
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
            if(visited[v] == 1)// If visited, consider the edges from node
            { 
                System.out.print("\nPossible connections from [" + toChar(v) + "] -> ");
                for(n = adj[v]; n != z; n = n.next) 
                {
                    if(toVisit.contains(n.vert)) // Check all remaining nodes in graph which are unvisited
                    {
                        System.out.print("(Node " + toChar(n.vert) + " | Weight: " + n.wgt + ") - ");
                        if(n.wgt < weight) // Finding connection with least weight
                        {   
                            weight = n.wgt;
                            viableNode = n.vert;
                            e.vert1 = v;
                        }
                    }
                }
            }
        }

        mstWeight += weight;

        e.vert2 = viableNode;
        e.weight = weight;

        System.out.println("\nAdding node " + toChar(viableNode) + " to minimum spanning tree");
        System.out.println(mstWeight);

        // Mark the closest node as part of the tree
        visited[viableNode] = 1; 
        toVisit.remove(Integer.valueOf(viableNode));
        mst.add(e);

    }
    
    public void showMST()
    {
        System.out.print("\n\nMinimum Spanning edges, in order:\n");

        for(int i = 0; i < mst.size(); i++) {   
            System.out.print("{" + toChar(mst.get(i).vert1) + " -> " + toChar(mst.get(i).vert2) + "} Weight: " + mst.get(i).weight + "\n");
        }  

        System.out.println("Total MST weight: " + mstWeight);
    }

}

public class PrimLists {
    public static void main(String[] args) throws IOException
    {
        int s = 4;
        String fname = "myGraph.txt";               

        Graph g = new Graph(fname);
       
        //g.display();

       //g.DF(s);
       //g.DF_iteration(s);
       g.MST_Prim(s);                  
    }
}
