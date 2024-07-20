import java.security.InvalidAlgorithmParameterException;
import java.io.*;
import java.util.*;

/**
 * Models a weighted graph of latitude-longitude points
 * and supports various distance and routing operations.
 * To do: Add your name(s) as additional authors
 * @author Brandon Fain
 * @author Owen Astrachan modified in Fall 2023
 *
 */
public class GraphProcessor {
    /**
     * Creates and initializes a graph from a source data
     * file in the .graph format. Should be called
     * before any other methods work.
     * @param file a FileInputStream of the .graph file
     * @throws Exception if file not found or error reading
     */

    // include instance variables here

    private static HashMap<Point, HashSet<Point>> adjList = new HashMap<>();
    private static Point[] points;

    /*/public GraphProcessor(){
        // TODO initialize instance variables
        GraphProcessor graff = new GraphProcessor();
        File graphData = new File("/Users/helloamyzhang/Downloads/CS 201/p6-route/data/simple.graph");

        System.out.println(adjList);
        System.out.println(Arrays.toString(points));
        
    }/*/ 

    /**
     * Creates and initializes a graph from a source data
     * file in the .graph format. Should be called
     * before any other methods work.
     * @param file a FileInputStream of the .graph file
     * @throws IOException if file not found or error reading
     */

    public void initialize(FileInputStream file) throws IOException {
        // make a scanner to read from the input stream file
        // if the first character is not an integer, the file format is incorrect and throw an exception

        Scanner readFile = new Scanner(file);
        if (!readFile.hasNextInt()){
            readFile.close();
            file.close();
            throw new IOException("Could not read this .graph file!");
        }

        // store sizes
        int a = readFile.nextInt();
        int b = readFile.nextInt();
        points = new Point[a];

        readFile.nextLine(); // skips blank line in file after sizes

        // add each point to adjList

        for (int i = 0; i < a; i++){
            String [] info = readFile.nextLine().split(" ");
            Point p = new Point(Double.parseDouble(info[1]), Double.parseDouble(info[2]));
            points[i] = p; // for every iteraction of the loop, store the point in an array list
            adjList.put(p, new HashSet<>());
        }

        // add all of the point's neighbors to the graphs using the file's edge data
        // if the point has noints after 9t, then it has no neighbors that we need to add to the adjacency list
        if (readFile.hasNext()){
            while (readFile.hasNextLine()){
                String str = readFile.nextLine();
                String[] info = str.split(" ");
                Point x = points[Integer.parseInt(info[0])];
                Point y = points[Integer.parseInt(info[1])];

                // add end points to both sets of the graph - because it's undirected
                adjList.get(x).add(y);
                adjList.get(y).add(x);
            }
        }

        file.close();
        readFile.close();

    }

    /**
     * Searches for the point in the graph that is closest in
     * straight-line distance to the parameter point p
     * @param p is a point, not necessarily in the graph
     * @return The closest point in the graph to p
     */
    public Point nearestPoint(Point p) {
        // TODO implement nearestPoint
        Double nearest = Double.POSITIVE_INFINITY;
        Point ret = p;

        // loop over all pts
        // call .distance()
        // replace if lower than curr. nearest val
        for (Point q : adjList.keySet()){
            Double distance = q.distance(p);
            if (distance < nearest) {
                nearest = distance;
                ret = q;
            }
        }

        return ret;
    }

    /**
     * Calculates the total distance along the route, summing
     * the distance between the first and the second Points, 
     * the second and the third, ..., the second to last and
     * the last. Distance returned in miles.
     * @param start Beginning point. May or may not be in the graph.
     * @param end Destination point May or may not be in the graph.
     * @return The distance to get from start to end
     */
    public double routeDistance(List<Point> route) {
        double d = 0.0;
        // TODO implement routeDistance
        for (int i = 0; i < route.size() - 1; i++) {
            d += route.get(i).distance(route.get(i+1));
        }
        return d;
    }
    

    /**
     * Checks if input points are part of a connected component
     * in the graph, that is, can one get from one to the other
     * only traversing edges in the graph
     * @param p1 one point
     * @param p2 another point
     * @return true if and onlyu if p2 is reachable from p1 (and vice versa)
     */
    public boolean connected(Point p1, Point p2) {
        // TODO implement connected
        Set<Point> visited = new HashSet<>();
        Stack<Point> stacky = new Stack<>();

        stacky.push(p1);
        Point current = p1;
        visited.add(current);

        while (! visited.contains(p2) && ! stacky.isEmpty()) {
            current = stacky.pop();
            for (Point p: adjList.get(current)){
                if (! visited.contains(p)) {
                    visited.add(p);
                    stacky.push(p);
                }
            }
        }

        return visited.contains(p2);
    }

    /**
     * Returns the shortest path, traversing the graph, that begins at start
     * and terminates at end, including start and end as the first and last
     * points in the returned list. If there is no such route, either because
     * start is not connected to end or because start equals end, throws an
     * exception.
     * @param start Beginning point.
     * @param end Destination point.
     * @return The shortest path [start, ..., end].
     * @throws IllegalArgumentException if there is no such route, 
     * either because start is not connected to end or because start equals end.
     */
    public List<Point> route(Point start, Point end) throws IllegalArgumentException {
        // TODO implement route
        if (start == end || start.distance(end) == 0) {
            throw new IllegalArgumentException("No path between start and end");
        }

        if (! connected(start, end)) {
            throw new IllegalArgumentException("No path between start and end");
        }

        Map<Point, Double> distance = new HashMap<>();
        List<Point> ret = new ArrayList<>();

        // Priority Queue for BFS - prioritize points by distance (value in map)
        Comparator<Point> compare = (a, b) -> Double.compare(distance.get(a), distance.get(b));
        PriorityQueue<Point> toExplore = new PriorityQueue<>(compare);
        Map<Point, Point> previous = new HashMap<>();

        Point current = start;
        toExplore.add(start);
        distance.put(start, 0.0);

        while (! toExplore.isEmpty()){
            current = toExplore.remove();
            for (Point a : adjList.get(current)){
                double dist = current.distance(a);
                if (! distance.containsKey(a) || distance.get(a) > distance.get(current) + dist){
                    distance.put(a, distance.get(current) + dist);
                    previous.put(a, current);
                    toExplore.add(a);
                }
            }
        }
        ret.add(end);
        Point p = previous.get(end);
        ret.add(p);

        for (Point s : previous.keySet()) {
            if (previous.get(p) == null) {
                break;
            }
            p = previous.get(p);
            ret.add(p);
        }

        ArrayList<Point> ret2 = new ArrayList<>();
        for (int i = ret.size() - 1; i >= 0; i--){
            ret2.add(ret.get(i));
        }
        return ret2;
    }
    public static void main(String[] args) throws FileNotFoundException, IOException {
        String name = "data/usa.graph";
        GraphProcessor gp = new GraphProcessor();
        gp.initialize(new FileInputStream(name));
        System.out.println("running GraphProcessor");
    }


    
}
