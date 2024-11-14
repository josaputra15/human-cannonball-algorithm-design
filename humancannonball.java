import java.util.*;
import java.awt.geom.Point2D;

public class humancannonball {

    static class Edge {
        int to;
        double time;
        public Edge(int to, double time) {
            this.to = to;
            this.time = time;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Read start, end points and number of cannons
        Point2D.Double start = new Point2D.Double(scanner.nextDouble(), scanner.nextDouble());
        Point2D.Double end = new Point2D.Double(scanner.nextDouble(), scanner.nextDouble());
        int n = scanner.nextInt();

        // Collect all points (start, destination, cannons)
        List<Point2D.Double> points = new ArrayList<>();
        points.add(start);  // Start point at index 0
        points.add(end);    // Destination point at index 1

        for (int i = 0; i < n; i++) {
            double x = scanner.nextDouble();
            double y = scanner.nextDouble();
            points.add(new Point2D.Double(x, y));
        }

        // Graph represented as adjacency list
        List<List<Edge>> graph = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            graph.add(new ArrayList<>());
            for (int j = 0; j < points.size(); j++) {
                if (i != j) {
                    double time = calculateTime(points.get(i), points.get(j), i == 0);
                    graph.get(i).add(new Edge(j, time));
                }
            }
        }

        // Apply Dijkstra's algorithm from the start point
        double result = dijkstra(graph, points.size());
        System.out.printf("%.6f\n", result);
    }

    static double calculateTime(Point2D.Double p1, Point2D.Double p2, boolean isRunning) {
        double distance = p1.distance(p2);
        if (isRunning) {
            return distance / 5.0; // Run directly to the point
        } else {
            double cannonTravelTime = 2.0; // 2 seconds to launch
            double remainingDistance = Math.max(0, distance - 50.0); // After 50m cannon travel
            return cannonTravelTime + remainingDistance / 5.0;
        }
    }

    static double dijkstra(List<List<Edge>> graph, int numPoints) {
        double[] minTime = new double[numPoints];
        Arrays.fill(minTime, Double.POSITIVE_INFINITY);
        minTime[0] = 0.0;

        PriorityQueue<Edge> pq = new PriorityQueue<>(Comparator.comparingDouble(e -> e.time));
        pq.offer(new Edge(0, 0.0));

        while (!pq.isEmpty()) {
            Edge current = pq.poll();
            int u = current.to;

            if (current.time > minTime[u]) continue;

            for (Edge edge : graph.get(u)) {
                int v = edge.to;
                double timeThroughU = minTime[u] + edge.time;
                if (timeThroughU < minTime[v]) {
                    minTime[v] = timeThroughU;
                    pq.offer(new Edge(v, timeThroughU));
                }
            }
        }
        return minTime[1];
    }
}
