/*
* Project Phase 3 of CPCS 324 
* section: DAR
* Group NO. : 3
* Members:
* Lameer Shamsaldeen - 1806835
* Nada Balsharaf - 1807769
* Mariam Mahdi - 1825889
* instryctor: Dr.Bassma Alsulami
*code taken and modified from https://github.com/SleekPanther/ford-fulkerson/blob/master/FordFulkerson.java
 */
package edmonds.karp_algorithm;

import java.util.*;

public class EdmondsKarp_Algorithm {

    static final int vertexCount = 6; 	//Number of vertices in graph is hardcoded 
    //maps array index 1 to "source", & successive indexes to a string equivalent
    private String[] arrayIndexStringEquivalents;	//arrayIndexStringEquivalents[1]="Source" & arrayIndexStringEquivalents[vertexCount]="sink"
    private Set<Pair> cutSet;
    private ArrayList<Integer> reachable;
    private ArrayList<Integer> unreachable;

    public EdmondsKarp_Algorithm(String[] arrayIndexStringEquivalents) {
        this.arrayIndexStringEquivalents = arrayIndexStringEquivalents;	//pass by reference, but don't care since main doesn't modify this
    }

    // Returns max flow from Source to sink in a graph
    public int maxFlow(int graph[][], int vertexSource, int vertexSink) {
        cutSet = new HashSet<Pair>();
        reachable = new ArrayList<Integer>();
        unreachable = new ArrayList<Integer>();
        int maxFlow = 0;
        int parent[] = new int[vertexCount + 1];	//holds parent of a vertex when a path if found (filled by BFS)
        int vertexU = 0;	//iterator vertices to loop over the matrix
        int vertexV = 0;

        int residualGraph[][] = new int[vertexCount + 1][vertexCount + 1];	//residualGraph[i][j] tells you if there's an edge between vertex i & j. 0=no edge, positive number=capacity of that edge
        for (vertexU = 1; vertexU <= vertexCount; vertexU++) {		//copy over every edge from the original graph into residual
            for (vertexV = 1; vertexV <= vertexCount; vertexV++) {
                residualGraph[vertexU][vertexV] = graph[vertexU][vertexV];
            }
        }

        while (bfs(residualGraph, vertexSource, vertexSink, parent)) {		//if a path exists from S to T
            String pathString = "";		//Shows the augmented path taken

            //find bottleneck by looping over path from BFS using parent[] array
            int bottleneckFlow = Integer.MAX_VALUE;		//we want the bottleneck (minimum), so initially set it to the largest number possible. Loop updates value if it's smaller
            vertexV = 0;
            vertexU = 0;
            for (vertexV = vertexSink; vertexV != vertexSource; vertexV = parent[vertexV]) {		//loop backward through the path using parent[] array
                vertexU = parent[vertexV];		//get the previous vertex in the path
                bottleneckFlow = Math.min(bottleneckFlow, residualGraph[vertexU][vertexV]);		//minimum of previous bottleneck & the capacity of the new edge

                pathString = " --> " + arrayIndexStringEquivalents[vertexV - 1] + pathString;	//prepend vertex to path
            }
            pathString = "Source 1" + pathString;		//loop stops before it gets to S, so add S to the beginning
            System.out.println("Augmentation path \n" + pathString);
            System.out.println("bottleneck (min flow on path added to max flow) = " + bottleneckFlow + "\n");

            //Update residual graph capacities & reverse edges along the path
            for (vertexV = vertexSink; vertexV != vertexSource; vertexV = parent[vertexV]) {	//loop backwards over path (same loop as above)
                vertexU = parent[vertexV];
                residualGraph[vertexU][vertexV] -= bottleneckFlow;		//back edge
                residualGraph[vertexV][vertexU] += bottleneckFlow;		//forward edge
            }

            maxFlow += bottleneckFlow;		//add the smallest flow found in the augmentation path to the overall flow
        }
        //calculate the cut set
        for (int vertex = 1; vertex <= vertexCount; vertex++) {
            if (bfs(residualGraph, vertexSource, vertex, parent)) {
                reachable.add(vertex);
            } else {
                unreachable.add(vertex);
            }
        }
        for (int i = 0; i < reachable.size(); i++) {
            for (int j = 0; j < unreachable.size(); j++) {
                if (graph[reachable.get(i)][unreachable.get(j)] > 0) {
                    cutSet.add(new Pair(reachable.get(i), unreachable.get(j)));
                }
            }
        }
        return maxFlow;
    }

    public void printCutSet() {
        Iterator<Pair> iterator = cutSet.iterator();
        while (iterator.hasNext()) {
            Pair pair = iterator.next();
            System.out.println(pair.source + "-" + pair.destination);
        }
    }

    //Returns true if it finds a path from Source to Sink
    //saves the vertices in the path in parent[] array
    public boolean bfs(int residualGraph[][], int vertexSource, int vertexSink, int parent[]) {
        boolean visited[] = new boolean[vertexCount + 1];	//has a vertex been visited when finding a path. Boolean so all values start as false

        LinkedList<Integer> vertexQueue = new LinkedList<Integer>();		//queue of vertices to explore (BFS to FIFO queue)
        vertexQueue.add(vertexSource);	//add source vertex
        visited[vertexSource] = true;	//visit it
        parent[vertexSource] = -1;			//"Source" has no parent

        while (!vertexQueue.isEmpty()) {
            int vertexU = vertexQueue.remove();		//get a vertex from the queue

            for (int vertexV = 1; vertexV <= vertexCount; vertexV++) {	//Check all edges to vertexV by checking all values in the row of the matrix
                if (visited[vertexV] == false && residualGraph[vertexU][vertexV] > 0) {	//residualGraph[u][v] > 0 means there actually is an edge
                    vertexQueue.add(vertexV);
                    parent[vertexV] = vertexU;		//used to calculate path later
                    visited[vertexV] = true;
                }
            }
        }
        return visited[vertexSink];	//return true/false if we found a path to Sink
    }

    public static void main(String[] args) {
        // 	as index source = 1
        // 	as index sink = 6
        System.out.println("The maximum flow based on Edmonds-Karp algorithm and minimum-cut\n"
                + "------------------------------------------------------------------");
        String[] arrayIndexStringEquivalents = {"Source 1", "2", "3", "4", "5", "Sink 6"};	//map human readable names to each vertex, not just array indexes
        int graphMatrix[][] = new int[][]{
            {0, 0, 0, 0, 0, 0, 0}, // add one row and one clomn to make matrix 7*7 to start a sourse from index 1
            {0, 0, 2, 7, 0, 0, 0}, //edges FROM Source TO anything
            {0, 0, 0, 0, 3, 4, 0},
            {0, 0, 0, 0, 4, 2, 0},
            {0, 0, 0, 0, 0, 0, 1},
            {0, 0, 0, 0, 0, 0, 5},
            {0, 0, 0, 0, 0, 0, 0} //Sink row (no edges leaving)
        };

        EdmondsKarp_Algorithm maxFlowFinder = new EdmondsKarp_Algorithm(arrayIndexStringEquivalents);

        int vertexS = 1;
        int vertexT = vertexCount;	//Sink is the last thing in the list
        System.out.println("------------------------------------------------------------------\n"
                + "The capacity of maximum flow based on Edmonds-Karp algorithm is: " + maxFlowFinder.maxFlow(graphMatrix, vertexS, vertexT));
        System.out.println("The minimum cut are:");
        maxFlowFinder.printCutSet();
    }
}

// to make a minimum cut as pair
class Pair {

    public int source;
    public int destination;

    public Pair(int source, int destination) {
        this.source = source;
        this.destination = destination;
    }

    public Pair() {
    }
}
