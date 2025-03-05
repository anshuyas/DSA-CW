//Q.no.1 b
import java.util.*;

class InvestmentReturns {
    public static int kthSmallestProduct(int[] returns1, int[] returns2, int k) {
        // Min-heap to store the smallest products
        PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b) -> a[0] - b[0]);
        
        // HashSet to track visited pairs of indices
        Set<String> visited = new HashSet<>();
        
        // Push the product of the first element of returns1 with all elements of returns2
        for (int j = 0; j < returns2.length; j++) {
            int product = returns1[0] * returns2[j];
            minHeap.offer(new int[]{product, 0, j});
            visited.add(0 + "," + j);
        }
        
        // Pop the smallest product k times
        int result = 0;
        while (k > 0 && !minHeap.isEmpty()) {
            int[] current = minHeap.poll();
            result = current[0]; // Current smallest product
            int i = current[1]; // Index in returns1
            int j = current[2]; // Index in returns2
            
            // Push the next possible product by incrementing the index in returns1
            if (i + 1 < returns1.length && !visited.contains((i + 1) + "," + j)) {
                int nextProduct = returns1[i + 1] * returns2[j];
                minHeap.offer(new int[]{nextProduct, i + 1, j});
                visited.add((i + 1) + "," + j);
            }
            
            k--;
        }
        
        return result;
    }

    public static void main(String[] args) {
        // Example Test Cases
        int[] returns1 = {2, 5};
        int[] returns2 = {3, 4};
        int k = 2;
        System.out.println("Example 1:");
        System.out.println("Input: returns1 = [2, 5], returns2 = [3, 4], k = 2");
        System.out.println("Output: " + kthSmallestProduct(returns1, returns2, k)); // Expected Output: 8

        int[] returns3 = {-4, -2, 0, 3};
        int[] returns4 = {2, 4};
        k = 6;
        System.out.println("\nExample 2:");
        System.out.println("Input: returns1 = [-4, -2, 0, 3], returns2 = [2, 4], k = 6");
        System.out.println("Output: " + kthSmallestProduct(returns3, returns4, k)); // Expected Output: 0

        // Additional Test Cases
        int[] returns5 = {1, 3, 5};
        int[] returns6 = {2, 4, 6};
        k = 5;
        System.out.println("\nTest Case 3:");
        System.out.println("Input: returns1 = [1, 3, 5], returns2 = [2, 4, 6], k = 5");
        System.out.println("Output: " + kthSmallestProduct(returns5, returns6, k)); // Expected Output: 12

        int[] returns7 = {-5, -3, -1};
        int[] returns8 = {-2, 0, 2};
        k = 4;
        System.out.println("\nTest Case 4:");
        System.out.println("Input: returns1 = [-5, -3, -1], returns2 = [-2, 0, 2], k = 4");
        System.out.println("Output: " + kthSmallestProduct(returns7, returns8, k)); // Expected Output: 0
    }
}