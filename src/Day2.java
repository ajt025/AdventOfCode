import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day2 {

    // Per password: O(n), O(1) -- ignoring input
    // Count valid passwords that have target occurrences within min - max
    public static int countValid(List<Integer> mins,
                                 List<Integer> maxs,
                                 List<Character> targets,
                                 List<String> passwords) {
        int validPasswords = 0;

        // checking each password
        for (int i = 0; i < passwords.size(); ++i) {
            String currPassword = passwords.get(i);
            int currMin = mins.get(i);
            int currMax = maxs.get(i);
            Character currTarget = targets.get(i);

            int charCount = 0;
            // count number of occurrences of target
            for (int j = 0; j < currPassword.length(); ++j) {
                if (currTarget == currPassword.charAt(j)) ++charCount;
            }

            // requisite check - is char count within our range?
            if (charCount >= currMin && charCount <= currMax) {
                ++validPasswords;
            }
        }

        return validPasswords;
    }

    // Per password: O(1), O(1)
    // Count valid passwords where target occurs once at firstPos OR secondPos
    public static int countValidAlt(List<Integer> firstPos,
                                    List<Integer> secondPos,
                                    List<Character> targets,
                                    List<String> passwords) {
        int validPasswords = 0;

        // checking each password
        for (int i = 0; i < passwords.size(); ++i) {
            String currPassword = passwords.get(i);
            int currFirst = firstPos.get(i) - 1; // adjust for zero indexing
            int currSecond = secondPos.get(i) - 1;
            Character currTarget = targets.get(i);

            // only one of the indices can have the target char
            if (currPassword.charAt(currFirst) == currTarget ^ currPassword.charAt(currSecond) == currTarget)
                ++validPasswords;
        }

        return validPasswords;
    }

    public static void main(String[] args) throws FileNotFoundException {
        // Input retrieval/parsing
        File file = new File("src/input/Day2.txt");
        Scanner scanner = new Scanner(file);

        List<Integer> min = new ArrayList<>();
        List<Integer> max = new ArrayList<>();
        List<Character> target = new ArrayList<>();
        List<String> passwords = new ArrayList<>();

        while (scanner.hasNextLine()) {
            String[] curr = scanner.nextLine().split(" ");

            // range parsing
            String[] range = curr[0].split("-");
            min.add(Integer.parseInt(range[0]));
            max.add(Integer.parseInt(range[1]));

            // target parsing
            target.add(curr[1].charAt(0));

            // string parsing
            passwords.add(curr[2]);
        }
        scanner.close();

        System.out.println(countValid(min, max, target, passwords));
        System.out.println(countValidAlt(min, max, target, passwords));
    }
}
