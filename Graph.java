import java.util.*;
import java.util.ArrayList;
import java.io.*;
import java.nio.file.Files;

public class Graph
{

    class Node {

        int source;
        int destination;
        int weight;

        public Node(int source, int destination, int weight) {
            this.source = source;
            this.destination = destination;
            this.weight = weight;
        }

        public Node() {

        }

    }

    private int vertices, edges;
    private Node z;
    private LinkedList<Node> [] adjList;
    private ArrayList<Integer> nodeList = new ArrayList<Integer>(); 

    private int[] visited;
    private int id;

    private boolean duplicate = false;

    public Graph(String filename) throws IOException
    {   

        ArrayList<String> fileText = new ArrayList<>();
       
        FileReader fr = new FileReader(filename);

        BufferedReader br = new BufferedReader(fr);

        String line;
        
        String splits = " +";  

        z = new Node();

        while ((line = br.readLine()) != null) 
        {

            String[] parts = line.split(splits);

            if (vertices == 0 || edges == 0) // First line only
            {

                vertices = Integer.parseInt(parts[0]);
                edges = Integer.parseInt(parts[1]);

                adjList = new LinkedList[vertices+1];

                for (int i = 1; i <= vertices ; i++) // Create lists for vertices
                {

                    adjList[i] = new LinkedList<>();

                }

            }
            else // All other lines
            { 

                int start = Integer.parseInt(parts[0]);
                int end = Integer.parseInt(parts[1]);
                
                this.addNode(start, end, Integer.parseInt(parts[2]));
                this.addNode(end, start, Integer.parseInt(parts[2]));
                

            }   
            
        }

    }

    public void addNode(int source, int destination, int weight)
    {

        Node node = new Node(source, destination, weight);
        adjList[source].addLast(node); 

    }

    private char toChar(int u)
    {  
        return (char)(u + 64);
    }

    public void Display() 
    {   
        
        for (int i = 1; i <= vertices; i++) 
        {

            LinkedList<Node> list = adjList[i];

            System.out.printf(i + " -> ");

            for (int j = 0; j < list.size() ; j++) 
            {

                System.out.printf(list.get(j).destination + " - > ");
                        
            }  

            System.out.println("");
        }

    }

    public static void main(String[] arg) throws IOException
    {

        Graph g = new Graph("graph.txt");
        g.Display();
       
    }

}
