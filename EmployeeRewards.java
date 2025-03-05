//Qno.2 a

public class EmployeeRewards {
        public static int minRewards(int[] ratings) {
            int n = ratings.length;
            int[] rewards = new int[n];
            
            // Initialize each employee with at least 1 reward
            for (int i = 0; i < n; i++) {
                rewards[i] = 1;
            }
            
            // Left-to-Right Pass
            for (int i = 1; i < n; i++) {
                if (ratings[i] > ratings[i - 1]) {
                    rewards[i] = rewards[i - 1] + 1;
                }
            }
            
            // Right-to-Left Pass
            for (int i = n - 2; i >= 0; i--) {
                if (ratings[i] > ratings[i + 1]) {
                    rewards[i] = Math.max(rewards[i], rewards[i + 1] + 1);
                }
            }
            
            // Sum up the rewards
            int totalRewards = 0;
            for (int reward : rewards) {
                totalRewards += reward;
            }
            
            return totalRewards;
        }
    
        public static void main(String[] args) {
            // Example 1
            int[] ratings1 = {1, 0, 2};
            System.out.println("Example 1:");
            System.out.println("Input: ratings = [1, 0, 2]");
            System.out.println("Output: " + minRewards(ratings1)); // Expected Output: 5
    
            // Example 2
            int[] ratings2 = {1, 2, 2};
            System.out.println("\nExample 2:");
            System.out.println("Input: ratings = [1, 2, 2]");
            System.out.println("Output: " + minRewards(ratings2)); // Expected Output: 4
    
            // Additional Test Cases
            int[] ratings3 = {3, 2, 1};
            System.out.println("\nTest Case 3:");
            System.out.println("Input: ratings = [3, 2, 1]");
            System.out.println("Output: " + minRewards(ratings3)); // Expected Output: 6
    
            int[] ratings4 = {1, 3, 2, 1, 4};
            System.out.println("\nTest Case 4:");
            System.out.println("Input: ratings = [1, 3, 2, 1, 4]");
            System.out.println("Output: " + minRewards(ratings4)); // Expected Output: 9
        }
    
}
