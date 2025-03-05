//Q.no.1 a

public class CriticalTemperature {
    
    // Method to find the minimum number of measurements required
    public static int findMinMeasurements(int k, int n) {
        // Create a DP table where dp[m][k] represents the maximum number of temperature levels
        // that can be checked with `m` measurements and `k` samples.
        int[][] dp = new int[n + 1][k + 1];
        
        // Initialize the number of measurements `m` to 0.
        int m = 0;
        
        // Loop until the maximum number of temperature levels that can be checked with `m` measurements
        // and `k` samples is greater than or equal to `n`.
        while (dp[m][k] < n) {
            // Increment the number of measurements.
            m++;
            
            // Fill the DP table for the current number of measurements `m`.
            for (int i = 1; i <= k; i++) {
                // The recurrence relation:
                // dp[m][i] = dp[m-1][i-1] (if the material reacts) + dp[m-1][i] (if the material does not react) + 1 (current temperature level).
                dp[m][i] = dp[m - 1][i - 1] + dp[m - 1][i] + 1;
            }
        }
        
        // Return the minimum number of measurements `m` required.
        return m;
    }

    // Main method to test the function with examples
    public static void main(String[] args) {
        // Example 1: k = 1, n = 2
        System.out.println(findMinMeasurements(1, 2)); // Output: 2
        
        // Example 2: k = 2, n = 6
        System.out.println(findMinMeasurements(2, 6)); // Output: 3
        
        // Example 3: k = 3, n = 14
        System.out.println(findMinMeasurements(3, 14)); // Output: 4
    }
}
