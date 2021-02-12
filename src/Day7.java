import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day7 {

    public static final String CONTAIN_REGEX = " bags contain ";
    public static final String BAGS_REGEX = "[a-z]+ [a-z]+"; // "no other" edge case is missing; not a problem bc "no other" is never a bag key
    public static final String BAGS_DIGIT_REGEX = "\\d \\w+ \\w+";

    private static HashMap<String, HashSet<String>> bagGraph;
    private static HashMap<String, HashMap<String, Integer>> numBagGraph;

    // Brainstorming a solution

    // Part 1:
    // Each bag is a node with 0-n pointers of other nodes
    // Traverse each possible color's path and see if we eventually hit a shiny bag
        // (a) multiple iterations and build path from end to start
    public static int traverse(String target) {
        int count = 0;
        PriorityQueue<String> pq = new PriorityQueue<>();
        pq.add(target);
        HashSet<String> seen = new HashSet<>(); // avoid redundant paths

        // BFS - build all paths to target by level
        while (!pq.isEmpty()) {
            String curr = pq.poll();

            for (String each : bagGraph.keySet()) {
                // check if valid path to bag from source bag
                if (bagGraph.get(each).contains(curr)) {
                    // if unvisited, count this as a valid path
                    if (seen.add(each)) {
                        ++count;
                        pq.add(each);
                    }
                }
            }
        }

        return count;
    }

    // Part 2:
    // Traverse, but factor in number of bags as well.
    public static int traverseNum(int num, String target) {
        HashMap<String, Integer> curr = numBagGraph.get(target);

        if (curr.size() == 0) { // base case: bag contains no other bags
            return num;
        }

        // recursive call - multiply the subpaths by the number of bags we have for the path
        int count = 0;
        for (String each : curr.keySet()) {
            // Essentially DFS on each subset bag
            count += num * traverseNum(curr.get(each), each);
        }

        // counts the number of bags on this level as well as its sub bags
        return num + count;
    }

    public static void main(String[] args) throws FileNotFoundException {
        bagGraph = new HashMap<>(); // just bag mappings
        numBagGraph = new HashMap<>(); // bag mappings with number of bags

        // Input retrieval/parsing
        File file = new File("src/input/Day7.txt");
        Scanner scanner = new Scanner(file);

        HashSet<Character> questions = new HashSet<>();
        int count = 0; // number of bags with valid paths

        Pattern pBag = Pattern.compile(BAGS_REGEX); // get all bag colors
        Pattern pNum = Pattern.compile(BAGS_DIGIT_REGEX); // get bag colors with #

        // populate our bag graphs
        while (scanner.hasNext()) {
            // Break string into source bag -> target bags
            String[] path = scanner.nextLine().split(CONTAIN_REGEX);

            HashSet<String> containedBags = bagGraph.computeIfAbsent(path[0], k -> new HashSet<>());
            // Break target bags into their individual colors and add to graph
            Matcher mBag = pBag.matcher(path[1]);
            while (mBag.find()) {
                containedBags.add(mBag.group());
            }

            // -- populate num bag graph -- //
            HashMap<String, Integer> bagToNum = numBagGraph.computeIfAbsent(path[0], k -> new HashMap<>());
            Matcher mNum = pNum.matcher(path[1]);
            while (mNum.find()) {
                // break into digit and bag
                String[] numAndBag = mNum.group().split(" ", 2);
                bagToNum.put(numAndBag[1], Integer.parseInt(numAndBag[0]));
            }
        }

        System.out.println(traverse("shiny gold"));
        System.out.println(traverseNum(1, "shiny gold") - 1); // ignore top layer
    }
}
