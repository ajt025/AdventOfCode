import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Day1 {

    // Two sum and return values multiplied - O(n), O(1)
    public static int solveTwo(List<Integer> nums, int target) {
        // Sort our list
        Collections.sort(nums);

        // init pointer at beginning and end
        int left = 0; int right = nums.size() - 1;

        while (left < right) {
            int leftNum = nums.get(left);
            int rightNum = nums.get(right);

            // CASE 1: Sum is too large, decrement right
            if (leftNum > target - rightNum) { // overflow caution
                --right;
            // CASE 2: Sum is too small, increment left
            } else if (leftNum < target - rightNum) {
                ++left;
            // CASE 3: Found pair - dereference and return
            } else {
                return nums.get(left) * nums.get(right);
            }
        }

        return -1;
    }

    // Three sum (w/ HashMap) and return values multiplied - O(n^2), O(n^2)
    // * May need to return longs if product gets too large
    public static int solveThree(List<Integer> nums, int target) {
        // choose 1st number as anchor and calculate new target
        for (int i = 0; i < nums.size(); ++i) {
            int subTarget = target - nums.get(i);

            HashMap<Integer, Integer> complements = new HashMap<>();
            // to avoid reusing permutations, all numbers will come after i
            for (int j = i + 1; j < nums.size(); ++j) {
                // save current number in map if complement isn't found
                if (complements.get(subTarget - nums.get(j)) == null) {
                    complements.put(nums.get(j), j);
                } else {
//                    // DEBUG
//                    System.out.println(nums.get(i) + " " + nums.get(j) + " " + (subTarget - nums.get(j)));

                    // if complement has been seen before, return
                    return nums.get(i) * nums.get(j) * (subTarget - nums.get(j));
                }
            }
        }

        return -1;
    }

    // Three sum (w/ two pointers) and return values multiplied

    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("src/input/Day1.txt");
        Scanner scanner = new Scanner(file);

        List<Integer> nums = new ArrayList<>();
        while (scanner.hasNextLine()) {
            nums.add(Integer.parseInt(scanner.nextLine()));
        }

        scanner.close();

    //    System.out.println(solveTwo(nums, 2020));
    //    System.out.println(solveThree(nums, 2020));
    }
}
