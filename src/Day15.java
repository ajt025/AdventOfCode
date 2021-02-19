import java.util.HashMap;
import java.util.Map;

public class Day15 {

    public static final int[] P1_TEST = {0, 3, 6};
    public static final int[] INPUT = {9,6,0,10,18,2,1};

    // 9 0
    // 6 1
    // 0 2
    // 10 3
    // 18 4
    // 2 5
    // 1 6

    // 0 7
    // 5 8
    // 0 9
    // 2 10
    // 5 11

    public static int solve(int[] input, int n) {
        Map<Integer, Integer> ages = new HashMap<>(); // track number and its last mention
        int i = 0;

        // load input
        for (; i < input.length - 1; ++i) {
            ages.put(input[i], i);
        }
        int lastNum = input[input.length - 1];
        int newNum = lastNum;

        // start count
        for (; i < n; ++i) {
            lastNum = newNum; // update the number we're working with

            // CASE 1: last number has not been said before
            if (ages.get(lastNum) == null) {
                newNum = 0;
            } else {
            // CASE 2: last number has been said before
                newNum = i - ages.get(lastNum);
            }

            ages.put(lastNum, i); // save last index of the prev number
//            System.out.println(lastNum);
        }

        return lastNum;
    }

    public static void main(String[] args) {
//        System.out.println(partOne(P1_TEST, 10));
        System.out.println(solve(INPUT, 2020));
        System.out.println(solve(INPUT, 30000000));
    }
}
