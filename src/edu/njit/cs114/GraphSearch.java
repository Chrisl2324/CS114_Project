package edu.njit.cs114;

import java.util.*;

/**
 * Author: Ravi Varadarajan
 * Date created: 4/21/2024
 */
public class GraphSearch {

    public static final int VISITED = 1;
    public static final int UNVISITED = 0;

    public static void printDfsTreeEdges(Graph g, List<Graph.Edge> treeEdges) {
        int lastVertex = -1;
        for (Graph.Edge edge : treeEdges) {
            if (edge.from != lastVertex) {
                if (lastVertex >= 0) {
                    System.out.println();
                }
                System.out.print(edge.from + " ---> " + edge.to);
            } else {
                System.out.print(" ---> " + edge.to);
            }
            lastVertex = edge.to;
        }
        System.out.println();
    }

    public static void printBfsTreeEdges(Graph g, List<Graph.Edge> treeEdges) {
        int fromVertex = -1;
        for (Graph.Edge edge : treeEdges) {
            if (edge.from != fromVertex) {
                if (fromVertex >= 0) {
                    System.out.println();
                }
                System.out.print(edge.from + "(" + g.getMark(edge.from) +
                        ")" + " ---> " +
                        edge.to + "(" + g.getMark(edge.to) + ")");
            } else {
                System.out.print("," + edge.to+ "(" + g.getMark(edge.to) + ")");
            }
            fromVertex = edge.from;
        }
        System.out.println();
    }


    public static void graphTraverseBFS(Graph g) {
        System.out.println("breadth-first search of graph..");
        for (int v = 0; v < g.numVertices(); v++) {
            g.setMark(v, UNVISITED);
        }
        for (int v = 0; v < g.numVertices(); v++) {
            if (g.getMark(v) == UNVISITED) {
                System.out.println("Start vertex : "+v);
                LinkedList<Graph.Edge> treeEdges = bfs(g, v, -1);
                printBfsTreeEdges(g, treeEdges);
            }
        }
    }

    public static void graphTraverseDFS(Graph g) {
        System.out.println("depth-first search of graph..");
        for (int v = 0; v < g.numVertices(); v++) {
            g.setMark(v, UNVISITED);
        }
        for (int v = 0; v < g.numVertices(); v++) {
            if (g.getMark(v) == UNVISITED) {
                System.out.println("Start vertex : "+v);
                LinkedList<Graph.Edge> treeEdges = dfs(g, v, -1);
                printDfsTreeEdges(g, treeEdges);
            }
        }
    }

    // do a DFS starting from vertex start and optionally ending at vertex end if end >=0
    public static LinkedList<Graph.Edge> dfs(Graph g, int v, int end) {
        g.setMark(v,VISITED);
        Iterator<Graph.Edge> outEdgeIter = g.getOutgoingEdges(v);
        // search-tree edges rooted at v
        LinkedList<Graph.Edge> subTreeEdges = new LinkedList<>();

        while(outEdgeIter.hasNext()){
            Graph.Edge current = outEdgeIter.next();
            if(g.getMark(current.to) == UNVISITED){
                subTreeEdges.add(current);
                if(current.to == end){
                    return subTreeEdges;
                }
                subTreeEdges.addAll(dfs(g, current.to, end));
            }

        }
        /**
         * Complete code here lab assignment
         */
        return subTreeEdges;
    }

    public static LinkedList<Graph.Edge> bfs(Graph g, int start, int end) {
        Queue<Integer> vertexQueue = new LinkedList<>();
        vertexQueue.add(start);
        g.setMark(start, 1);

        LinkedList<Graph.Edge> treeEdges = new LinkedList<>();

        while (!vertexQueue.isEmpty()) {
            int u = vertexQueue.poll();

            Iterator<Graph.Edge> iter = g.getOutgoingEdges(u);
            while (iter.hasNext()) {
                Graph.Edge edge = iter.next();
                int v = edge.to;

                if (g.getMark(v) == UNVISITED) {

                    g.setMark(v, 1 + g.getMark(u));

                    vertexQueue.add(v);
                    treeEdges.add(edge);

                    if (v == end) {
                        return treeEdges;
                    }
                }
            }
        }
        return treeEdges;
    }


    /**
     * Find a path in the graph from fromVertex to toVertex using BFS or DFS
     * @param g
     * @param fromVertex
     * @param toVertex
     * @param isBfs use BFS if set to true else use DFS
     * @return
     */
    public static List<Integer> graphPath(Graph g, int fromVertex, int toVertex,
                                          boolean isBfs) {
        for (int v = 0; v < g.numVertices(); v++) {
            g.setMark(v, UNVISITED);
        }
        LinkedList<Integer> pathVertices = new LinkedList<>();
        LinkedList<Graph.Edge> treeEdges = isBfs ? bfs(g, fromVertex, toVertex) :
                dfs(g, fromVertex, toVertex);
        // get edges in the path starting from toVertex
        Iterator<Graph.Edge> edgeIter = treeEdges.descendingIterator();

        int lastVertex = -1;

        while(edgeIter.hasNext()){
            Graph.Edge current = edgeIter.next();
            if(current.to == toVertex){
                pathVertices.addFirst(current.to);
                lastVertex = current.to;
            }

            if(current.to == lastVertex){
                pathVertices.addFirst(current.from);
                lastVertex = current.from;
            }

        }
        /**
         * Complete code here for lab assignment
         */
        return pathVertices;
    }

    /**
     * (complete it for homework assignment)
     * Returns true if a cycle exists in undirected graph
     * @param g undirected graph
     * @return
     */
    public static boolean cycleExists(Graph g) {
        int numVertices = g.numVertices();
        int[] visited = new int[numVertices];
        Arrays.fill(visited, UNVISITED);

        // Check for a cycle in different components
        for (int v = 0; v < numVertices; v++) {
            if (visited[v] == UNVISITED) {
                if (dfsForCycle(g, v, visited, -1)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean dfsForCycle(Graph g, int v, int[] visited, int parent) {
        visited[v] = VISITED; // Mark the vertex as visited
        Iterator<Graph.Edge> edges = g.getOutgoingEdges(v);
        while (edges.hasNext()) {
            Graph.Edge edge = edges.next();
            int toVertex = edge.to;

            // If the adjacent vertex has not been visited, recurse on it
            if (visited[toVertex] == UNVISITED) {
                if (dfsForCycle(g, toVertex, visited, v)) {
                    return true; // Cycle found
                }
            }
            // Check for a cycle (if it is visited and not the parent)
            else if (toVertex != parent) {
                return true; // Cycle found since it's a back edge
            }
        }
        return false; // No cycle found
    }



    /**
     * (complete it for homework assignment)
     * Returns true if a simple cycle with odd number of edges exists in undirected graph
     * @param g undirected graph
     * @return
     */
    public static boolean oddCycleExists(Graph g) {
        int numVertices = g.numVertices();
        // Array to store colors assigned to all vertices.
        // Vertex color values: 0 = not colored, 1 and -1 are the two colors.
        int[] colors = new int[numVertices];

        // Process all vertices to handle disconnected components
        for (int v = 0; v < numVertices; v++) {
            if (colors[v] == 0) { // Vertex has not been colored yet
                // If we find a component with an odd cycle, return true
                if (isOddCycleInComponent(g, v, colors)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Helper method to check if there's an odd cycle in the component using BFS.
     * @param g the graph
     * @param start starting vertex for BFS
     * @param colors array of colors
     * @return true if an odd cycle is found, false otherwise
     */
    private static boolean isOddCycleInComponent(Graph g, int start, int[] colors) {
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(start);
        colors[start] = 1;

        while (!queue.isEmpty()) {
            int current = queue.poll();
            Iterator<Graph.Edge> edges = g.getOutgoingEdges(current);
            while (edges.hasNext()) {
                Graph.Edge edge = edges.next();
                int neighbor = edge.to;

                if (colors[neighbor] == 0) {  // If not colored
                    // Color the neighbor with opposite color
                    colors[neighbor] = -colors[current];
                    queue.offer(neighbor);
                } else if (colors[neighbor] == colors[current]) {
                    // A neighbor has the same color, odd cycle found
                    return true;
                }
            }
        }
        return false;  // No odd cycle found
    }


    /**
     * Find the diameter (length of longest path) in the tree
     * @param tree (undirected graph which is a tree)
     * @return
     */
    public static int diameter(Graph tree) {
        if (tree.numVertices() == 0) {
            return 0; // The diameter of an empty graph is zero
        }

        //BFS
        int farthestNode = bfsToFindFarthestNode(tree, 0);


        int diameter = bfsToFindFarthestNodeDistance(tree, farthestNode);

        return diameter;
    }

    /**
     * Performs BFS on the graph starting from the vertex 'start' and returns the farthest node.
     */
    private static int bfsToFindFarthestNode(Graph g, int start) {
        Queue<Integer> queue = new LinkedList<>();
        boolean[] visited = new boolean[g.numVertices()];
        int[] distance = new int[g.numVertices()];
        queue.add(start);
        visited[start] = true;
        int lastVisited = start;

        while (!queue.isEmpty()) {
            int current = queue.poll();
            Iterator<Graph.Edge> edges = g.getOutgoingEdges(current);
            while (edges.hasNext()) {
                Graph.Edge edge = edges.next();
                if (!visited[edge.to]) {
                    visited[edge.to] = true;
                    distance[edge.to] = distance[current] + 1;
                    queue.add(edge.to);
                    lastVisited = edge.to; // Store the last visited node
                }
            }
        }
        return lastVisited; // Return the last visited node
    }

    /**
     * Performs BFS on the graph starting from the vertex 'start' and returns the distance to the farthest node.
     */
    private static int bfsToFindFarthestNodeDistance(Graph g, int start) {
        Queue<Integer> queue = new LinkedList<>();
        boolean[] visited = new boolean[g.numVertices()];
        int[] distance = new int[g.numVertices()];
        queue.add(start);
        visited[start] = true;

        while (!queue.isEmpty()) {
            int current = queue.poll();
            Iterator<Graph.Edge> edges = g.getOutgoingEdges(current);
            while (edges.hasNext()) {
                Graph.Edge edge = edges.next();
                if (!visited[edge.to]) {
                    visited[edge.to] = true;
                    distance[edge.to] = distance[current] + 1;
                    queue.add(edge.to);
                }
            }
        }

        // Find the maximum distance reached in this BFS
        int maxDistance = 0;
        for (int dist : distance) {
            if (dist > maxDistance) {
                maxDistance = dist;
            }
        }
        return maxDistance;
    }


    /**
     * Does the directed graph have a cycle of directed edges ? (Extra credit)
     * @param g
     * @return
     */
    public static boolean cycleExistsDirect(Graph g) {
        int numVertices = g.numVertices();
        boolean[] visited = new boolean[numVertices]; // keep track of visited vertices
        boolean[] recStack = new boolean[numVertices]; // keep track of vertices in the recursion stack

        // find a cycle starting from each vertex not yet visited
        for (int i = 0; i < numVertices; i++) {
            if (!visited[i]) {
                if (dfsCycleUtil(g, i, visited, recStack)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean dfsCycleUtil(Graph g, int v, boolean[] visited, boolean[] recStack) {
        if (recStack[v]) {
            // check if vertex in cycle
            return true;
        }
        if (visited[v]) {
            // return false if already visited
            return false;
        }

        // Mark the current node as visited and add to recursion stack
        visited[v] = true;
        recStack[v] = true;

        // Recursive step
        Iterator<Graph.Edge> it = g.getOutgoingEdges(v);
        while (it.hasNext()) {
            Graph.Edge edge = it.next();
            if (dfsCycleUtil(g, edge.to, visited, recStack)) {
                return true;
            }
        }

        // Remove the vertex
        recStack[v] = false;
        return false;
    }



    public static void main(String [] args) throws Exception {
        Graph g = new AdjListGraph(8, true);
        g.addEdge(0,1);
        g.addEdge(0,2);
        g.addEdge(0,3);
        g.addEdge(1,2);
        g.addEdge(1,3);
        g.addEdge(2,0);
        g.addEdge(3,2);
        g.addEdge(4,3);
        g.addEdge(4,6);
        g.addEdge(5,7);
        g.addEdge(6,5);
        g.addEdge(7,5);
        g.addEdge(7,6);
        System.out.println(g);
        graphTraverseBFS(g);
        graphTraverseDFS(g);
        System.out.println("Directed cycle exists in g ? " + cycleExistsDirect(g));
        g = new AdjListGraph(8, false);
        g.addEdge(0,1);
        g.addEdge(1,3);
        g.addEdge(3,2);
        g.addEdge(3,4);
        g.addEdge(4,5);
        g.addEdge(5,7);
        g.addEdge(4,6);
        System.out.println(g);
        graphTraverseBFS(g);
        graphTraverseDFS(g);
        System.out.println("Cycle exists in g ? " + cycleExists(g));
        g = new AdjListGraph(8, false);
        g.addEdge(0,1);
        g.addEdge(0,2);
        g.addEdge(1,3);
        g.addEdge(1,4);
        g.addEdge(3,2);
        g.addEdge(3,4);
        g.addEdge(4,5);
        g.addEdge(6,5);
        g.addEdge(5,7);
        g.addEdge(7,6);
        System.out.println(g);
        graphTraverseBFS(g);
        graphTraverseDFS(g);
        System.out.println("Cycle exists in g ? " + cycleExists(g));
        System.out.println("Odd cycle exists in g ? " + oddCycleExists(g));
        g = new AdjListGraph(7, false);
        g.addEdge(0,1);
        g.addEdge(0,2);
        g.addEdge(0,3);
        g.addEdge(2,4);
        g.addEdge(2,5);
        g.addEdge(3,6);
        System.out.println(g);
        System.out.println("Cycle exists in g ? " + cycleExists(g));
        System.out.println("Diameter of g = " + diameter(g));
        assert diameter(g) == 4;
        g = new AdjListGraph(7, false);
        g.addEdge(0,1);
        g.addEdge(1,2);
        g.addEdge(2,3);
        g.addEdge(3,0);
        g.addEdge(3,4);
        g.addEdge(4,5);
        g.addEdge(5,6);
        g.addEdge(6,3);
        System.out.println(g);
        System.out.println("Cycle exists in g ? " + cycleExists(g));
        System.out.println("Odd cycle exists in g ? " + oddCycleExists(g));
    }


}
