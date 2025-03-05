//Q.no.3 a

import java.util.*;

public class NetworkDevices {

    // Union-Find data structure
    static class UnionFind {
        int[] parent;
        int[] rank;

        public UnionFind(int size) {
            parent = new int[size];
            rank = new int[size];
            for (int i = 0; i < size; i++) {
                parent[i] = i;
                rank[i] = 1;
            }
        }

        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]); // Path compression
            }
            return parent[x];
        }

        public boolean union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            if (rootX == rootY) return false; // Already connected

            // Union by rank
            if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX;
            } else if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
            } else {
                parent[rootY] = rootX;
                rank[rootX]++;
            }
            return true;
        }
    }

    // Function to find the minimum total cost to connect all devices
    public static int minCostToConnectDevices(int n, int[] modules, int[][] connections) {
        // Create a list to store all edges (connections + module options)
        List<int[]> edges = new ArrayList<>();

        // Add edges for connections
        for (int[] connection : connections) {
            int device1 = connection[0];
            int device2 = connection[1];
            int cost = connection[2];
            edges.add(new int[]{device1, device2, cost});
        }

        // Add edges for module options (virtual node 0)
        for (int i = 1; i <= n; i++) {
            edges.add(new int[]{0, i, modules[i - 1]});
        }

        // Sort edges by cost
        edges.sort((a, b) -> a[2] - b[2]);

        // Initialize Union-Find
        UnionFind uf = new UnionFind(n + 1); // n devices + virtual node 0

        int totalCost = 0;
        int edgesUsed = 0;

        // Kruskal's algorithm to find MST
        for (int[] edge : edges) {
            int u = edge[0];
            int v = edge[1];
            int cost = edge[2];

            if (uf.union(u, v)) {
                totalCost += cost;
                edgesUsed++;
                if (edgesUsed == n) break; // All devices are connected
            }
        }

        return totalCost;
    }

    public static void main(String[] args) {
        // Test Case 1
        int n1 = 3;
        int[] modules1 = {1, 2, 2};
        int[][] connections1 = {{1, 2, 1}, {2, 3, 1}};
        System.out.println("Test Case 1:");
        System.out.println("Input: n = 3, modules = [1, 2, 2], connections = [[1, 2, 1], [2, 3, 1]]");
        System.out.println("Output: " + minCostToConnectDevices(n1, modules1, connections1)); // Expected Output: 3


        // Test Case 4
        int n4 = 6;
        int[] modules4 = {1, 1, 1, 1, 1, 1};
        int[][] connections4 = {{1, 2, 2}, {2, 3, 2}, {3, 4, 2}, {4, 5, 2}, {5, 6, 2}};
        System.out.println("\nTest Case 4:");
        System.out.println("Input: n = 6, modules = [1, 1, 1, 1, 1, 1], connections = [[1, 2, 2], [2, 3, 2], [3, 4, 2], [4, 5, 2], [5, 6, 2]]");
        System.out.println("Output: " + minCostToConnectDevices(n4, modules4, connections4)); // Expected Output: 6

    }
}