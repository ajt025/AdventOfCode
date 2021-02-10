import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class Day6 {

    // Solves part 1 - unique count of questions answered "yes"
    // Time: O(n * s) n = number of lines, s = length of question string
    // Space: O(n * u) n = number of lines, u = unique questions
    public static int solveOne() throws FileNotFoundException {
        // Input retrieval/parsing
        File file = new File("src/input/Day6.txt");
        Scanner scanner = new Scanner(file);

        HashSet<Character> questions = new HashSet<>();
        int count = 0; // number of questions answered yes

        while (scanner.hasNextLine()) {
            String curr = scanner.nextLine();

            // CASE 1: Line has content
            if (!curr.isEmpty()) {
                // load all characters on line into our questions set
                for (int i = 0; i < curr.length(); ++i) {
                    questions.add(curr.charAt(i));
                }
            } else {
                // CASE 2: Line has no content
                count += questions.size();
                questions.clear();
            }

        }

        // EOF - evalulate final group
        count += questions.size();
        questions.clear();

        return count;
    }

    // Solves part 2 - number of questions that a group all answered yes to
    // Time: O(n * s) n = number of lines, s = length of question string
    // Space: O(n * u) n = number of lines, u = unique questions
    public static int solveTwo() throws FileNotFoundException {
        // Input retrieval/parsing
        File file = new File("src/input/Day6.txt");
        Scanner scanner = new Scanner(file);

        HashMap<Character, Integer> questions = new HashMap<>();
        int groupSize = 0; // counter for how many lines in group
        int count = 0; // number of questions where a group all answered yes

        while (scanner.hasNext()) {
            String curr = scanner.nextLine();

            if (!curr.isEmpty()) {
                ++groupSize; // count current line as part of group
                for (int i = 0; i < curr.length(); ++i) {
                    // count occurrences of questions
                    questions.put(curr.charAt(i), questions.getOrDefault(curr.charAt(i), 0) + 1);
                }
            } else {
                // check if any of the question counts match the group size --> everyone answered yes
                for (Character each : questions.keySet()) {
                    if (questions.get(each) == groupSize) ++count;
                }
                // reset for next group
                groupSize = 0;
                questions.clear();
            }
        }

        // EOF final map check
        for (Integer each : questions.values()) {
            if (each.equals(groupSize)) ++count;
        }

        return count;
    }

    public static void main(String[] args) throws FileNotFoundException {
        System.out.println(solveOne());

        System.out.println(solveTwo());
    }
}
