import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Day9 {

    public static final int PREAMBLE = 25;

    // Two Sum with preamble in LinkedHashSet
        // after preamble is loaded, pop and queue
        // check if complement exists
    // Why? bc we need the following:
        // maintaining insertion order (Queue-like)
        // quick access (HashMap)

    // Part 1 - find value in which no two values in previous <preamble> values sum to
    public static long findWeakness(Scanner scanner, int preamble) {
        // assumption that there are no duplicates
        LinkedHashSet<Long> set = new LinkedHashSet<>();

        while (scanner.hasNext()) {
            long value = Long.parseLong(scanner.nextLine());

            // fill preamble while there's space
            if (set.size() < preamble) {
                set.add(value);
            } else {
                // check complements - if no possible matches, then we've found the rule-breaking number
                if (!validateSet(set, value)) return value;

                // update our set
                Iterator<Long> itr = set.iterator();
                itr.next(); itr.remove(); // pop first element
                set.add(value); // push last element
            }
        }

        return -1;
    }

    // Part 2 - find contiguous set of # that produce the invalid number from Part 1
    public static long findWeaknessCont(Scanner scanner, long target) {

        // sliding window
        Queue<Long> q = new LinkedList<>();
        long sum = 0;

        while (scanner.hasNext()) {
            // CASE 1: sum < target
            if (sum < target) {
                long value = Long.parseLong(scanner.nextLine());
                q.add(value);
                sum += value;
            } else if (sum > target) {
            // CASE 2: sum > target
                sum -= q.poll();
            } else {
            // CASE 3: sum == target
                break;
            }
        }

        // search queue for min and max
        long min = Long.MAX_VALUE; long max = Long.MIN_VALUE;
        for (Long each : q) {
            min = Math.min(min, each);
            max = Math.max(max, each);
        }
        // * note * wanted to keep track of min and max while reading input, but couldn't guarantee min/max would
        //          not be dropped from queue

        System.out.println("min: " + min + " - max: " + max);
        return min + max;
    }

    // Helpers

    // Validate the set and ensure there is at least one valid pair that sums to the current number.
    public static boolean validateSet(Set<Long> set, long target) {
        // check if there exists 2 values in the set that add up to target
        for (Long each : set) {
            if (set.contains(target - each)) return true;
        }

        return false;
    }

    public static void main(String[] args) throws FileNotFoundException {
        // Input retrieval/parsing
        File file = new File("src/input/Day9.txt");
        Scanner scanner = new Scanner(file);

        // PART 2 target is answer from #1
        long target;

        // Part 1 - pass the scanner bc values are used in read order
        System.out.println(target = findWeakness(scanner, PREAMBLE));

        scanner.close();
        scanner = new Scanner(file);

        // Part 2 - target: 104054607
        System.out.println(findWeaknessCont(scanner, target));
    }
}
