import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day14 {

    public static final int BIT_COUNT = 36;
    public static final int MASK_START_INDEX = 7;
    public static final String MEMORY_REGEX = "\\d+";

    // Solves part one - summing all values in memory, values are masked
    public static long partOne(String src) throws FileNotFoundException {
        File file = new File(src);
        Scanner scanner = new Scanner(file);
        Pattern p = Pattern.compile(MEMORY_REGEX);

        Map<Integer, char[]> memspace = new HashMap<>();
        char[] mask = new char[36];

        // read instructions
        while (scanner.hasNextLine()) {
            String curr = scanner.nextLine();

            // CASE 1: mask update
            if (curr.contains("mask")) {
                mask = curr.substring(MASK_START_INDEX).toCharArray();
            } else {
            // CASE 2: memory change
                Matcher m = p.matcher(curr);
                int key = 0; long value = 0;
                // [0] = addr, [1] = value
                if (m.find()) key = Integer.parseInt(m.group());
                if (m.find()) value = Long.parseLong(m.group());

                // apply mask to the value
                char[] maskedValue = applyMask(longToBits(value), mask);

                // update memory space
                memspace.put(key, maskedValue);
            }
        }

        long sum = 0;
        // sum the values in memory
        for (Integer addr : memspace.keySet()) {
            sum += bitsToLong(memspace.get(addr));
        }

        return sum;
    }

    // Solves part two - summing all values in memory, memory is masked
    public static long partTwo(String src) throws FileNotFoundException {
        File file = new File(src);
        Scanner scanner = new Scanner(file);
        Pattern p = Pattern.compile(MEMORY_REGEX);

        Map<Long, char[]> memspace = new HashMap<>();
        char[] mask = new char[36];

        while (scanner.hasNextLine()) {
            String curr = scanner.nextLine();

            // CASE 1: mask update
            if (curr.contains("mask")) {
                mask = curr.substring(MASK_START_INDEX).toCharArray();
            } else {
                // CASE 2: memory change
                Matcher m = p.matcher(curr);
                long key = 0; long value = 0;

                // [0] = addr, [1] = value
                if (m.find()) key = Long.parseLong(m.group());
                if (m.find()) value = Long.parseLong(m.group());

                // apply mask to the key
                char[] maskedKey = applyMaskX(longToBits(key), mask);

                // update values on all permutations of the masked address
                permutate(memspace, maskedKey, longToBits(value));
            }
        }

        long sum = 0;
        // sum the values in memory
        for (Long addr : memspace.keySet()) {
            sum += bitsToLong(memspace.get(addr));
        }

        return sum;
    }

    // HELPERS //

    // Converts a number into its bit form.
    // - ASSUMES - value fits in 36 bits
    public static char[] longToBits(long value) {
        char[] bits = new char[BIT_COUNT];
        int i = BIT_COUNT - 1;

        // convert number into bit form
        while (value > 0) {
            bits[i] = (char) ('0' + (value % 2)); // '0' or '1'
            value /= 2;
            --i; // move ptr
        }

        return bits;
    }

    // Converts an array of bits into a long form.
    public static long bitsToLong(char[] bits) {
        long sum = 0;
        for (int i = 0; i < BIT_COUNT; ++i) {
            // add respective bit value if bit is active
            if (bits[BIT_COUNT - i - 1] == '1') sum += (1L << i);
        }
        return sum;
    }

    // Applies a mask to the first parameter, a char array representing a value. X is a don't care.
    public static char[] applyMask(char[] value, char[] mask) {
        for (int i = 0; i < mask.length; ++i) {
            // overwrite our value if the current mask bit is not a "don't care" or empty [TODO be explicit about this]
            if (mask[i] == '1' || mask[i] == '0') {
                value[i] = mask[i];
            }
        }

        return value;
    }

    // Applies a mask; 0 is unchanged, 1 is overwritten, X becomes floating (PART2)
    public static char[] applyMaskX(char[] value, char[] mask) {
        for (int i = 0; i < mask.length; ++i) {
            // overwrite our value if the current mask bit is not a "don't care" or empty [TODO be explicit about this]
            if (mask[i] == '1' || mask[i] == 'X') {
                value[i] = mask[i];
            }
        }

        return value;
    }

    // Recursively creates permutations with backtracking
    public static void permutate(Map<Long, char[]> memspace, char[] addr, char[] value) {
        // BASE CASE: created a permutation
        if (!contains(addr, 'X')) {
            memspace.put(bitsToLong(addr), value);
            return;
        }

        // if not, we'll create more permutations - find first 'X' and stop
        for (int i = 0; i < addr.length; ++i) {
            if (addr[i] == 'X') {
                addr[i] = '0';
                permutate(memspace, addr, value);
                addr[i] = '1';
                permutate(memspace, addr, value);
                addr[i] = 'X'; // reset
                break;
            }
        }
    }

    // Check if char array has a given char. (mostly used to check for leftover Xs)
    public static boolean contains(char[] addr, char target) {
        for (char c : addr) {
            if (c == target) return true;
        }
        return false;
    }

    public static void main(String[] args) throws FileNotFoundException {
        // PART 1
        System.out.println(partOne("src/input/Day14.txt"));
        // PART 2
        System.out.println(partTwo("src/input/Day14.txt"));
    }
}
