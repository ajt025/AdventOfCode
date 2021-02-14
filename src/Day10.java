import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Day10 {

    // Counts the differences between each index in a sorted list.
    public static int[] findDifferences(List<Long> joltages) {
        int[] diff = new int[3]; // count diffs of 1, 2, or 3 joltages


        for (int i = 0; i < joltages.size() - 1; ++i) {
            // calculate diff and zero-index
            int index = (int) (joltages.get(i + 1) - joltages.get(i) - 1);
            // assumption there is always a joltage jump to make
            ++diff[index];
        }

        return diff;
    }

    // Counts the number of adapter combinations to get to the largest joltage.
    public static long findCombinations(List<Long> joltages) {
        long[] cache = new long[joltages.size()];

        cache[0] = 1; // one way to get from 0 to 0
        for (int i = 1; i < cache.length; ++i) {
            // offset (1-3 before)
            for (int j = 1; j <= 3; ++j) {
                // given cache index exists and value is within range, add it to combinations
                if (i - j >= 0 && joltages.get(i) - joltages.get(i - j) <= 3) {
                    cache[i] += cache[i - j];
                }
            }
        }

        // last index is our final combination count
        return cache[cache.length - 1];

        // cache[n] = cache[n - 3] + cache[n - 2] + cache[n - 1]
        //            given that n - i is within range of n
    }

    public static void main(String[] args) throws FileNotFoundException {
        // Input retrieval/parsing
        File file = new File("src/input/Day10.txt");
        Scanner scanner = new Scanner(file);

        // create input (outlet joltage + adapter joltages + device joltage)
        List<Long> joltages = new ArrayList<>();
        joltages.add(0L); // outlet joltage
        while (scanner.hasNextLine()) {
            joltages.add(Long.parseLong(scanner.nextLine()));
        }
        joltages.add(Collections.max(joltages) + 3); // device's joltage

        // Sort and print
        Collections.sort(joltages);
        System.out.println("Joltage List:");
        System.out.println(joltages);
        System.out.println("---");

        // PART 1 - 1-diffs * 3-diffs
        int[] differences = findDifferences(joltages);
        System.out.println(differences[0] * differences[2]);

        // PART 2 - DP, combinations
        System.out.println(findCombinations(joltages));
    }
}
