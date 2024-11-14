import java.util.*;
import java.io.*;

public class HumanCannonBall1 {

    static class Point {
        double x, y;

        Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        // Euclidean distance
        double euclideanDistanceTo(Point other) {
            return Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y, 2));
        }
    }

    static class Edge {
        int target;
        double time;

        Edge(int target, double time) {
            this.target = target;
            this.time = time;
        }
    }

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        Point start = new Point(sc.nextDouble(), sc.nextDouble());
        Point destination = new Point(sc.nextDouble(), sc.nextDouble());

        
        int n = sc.nextInt();
        Point[] points = new Point[n + 2];
        points[0] = start;
        points[1] = destination;

        for (int i = 0; i < n; i++) {
            points[i + 2] = new Point(sc.nextDouble(), sc.nextDouble());
        }

        // Create graph edges
        List<Edge>[] graph = new List[n + 2];
        for (int i = 0; i < n + 2; i++) {
            graph[i] = new ArrayList<>();
        }

        // Populate the graph
        for (int i = 0; i < n + 2; i++) {
            for (int j = 0; j < n + 2; j++) {
                if (i != j) {
                    double distance = points[i].euclideanDistanceTo(points[j]);
                    double time;

                    if (i == 0) {
                        time = distance / 5.0;
                    } else if (distance <= 50) {
                        time = 2.0;
                    } else {
                        time = 2.0 + (distance - 50) / 5.0;
                    }

                    graph[i].add(new Edge(j, time));
                }
            }
        }

        //Dijkstra's algorithm
        double[] minTime = new double[n + 2];
        Arrays.fill(minTime, Double.MAX_VALUE);
        minTime[0] = 0;

        PriorityQueue<Edge> pq = new PriorityQueue<>(Comparator.comparingDouble(e -> e.time));
        pq.add(new Edge(0, 0));

        while (!pq.isEmpty()) {
            Edge current = pq.poll();
            int u = current.target;

            if (current.time > minTime[u]) continue;

            for (Edge edge : graph[u]) {
                int v = edge.target;
                double newTime = minTime[u] + edge.time;

                if (newTime < minTime[v]) {
                    minTime[v] = newTime;
                    pq.add(new Edge(v, newTime));
                }
            }
        }

        System.out.println(minTime[1]);
    }
}
