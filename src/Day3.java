import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day3 {

    public static final char TREE = '#';

    // Calculates number of trees hit in a 2D map with given vertical/horizontal position deltas
    public static long countTrees(List<List<Character>> map, int vertical, int horizontal) {
        long count = 0; // changed to long bc of overflow in PART 2

        // map dimensions
        int totalRows = map.size();
        int totalCols = map.get(0).size();

        // position pointers
        int currRow = 0;
        int currCol = 0;

        // loop until the bottom of the map is reached
        while (currRow < totalRows) {
            // check if current position is a tree
            if (map.get(currRow).get(currCol) == TREE) {
                ++count;
            }

            // update position based on slope
            currRow += vertical;
            currCol = (currCol + horizontal) % totalCols; // map is wrapped horizontally
        }

        return count;
    }

    public static void main(String[] args) throws FileNotFoundException {
        // Input retrieval/parsing
        File file = new File("src/input/Day3.txt");
        Scanner scanner = new Scanner(file);

        // Load the 2D map into memory
        List<List<Character>> map = new ArrayList<>();

        // Read each character in each line into list of lists
        while (scanner.hasNextLine()) {
            String curr = scanner.nextLine();
            List<Character> currLine = new ArrayList<>();

            for (int i = 0; i < curr.length(); ++i) {
                currLine.add(curr.charAt(i));
            }

            map.add(currLine);
        }

        scanner.close();

        // PART 1 - slope is 1 down, 3 right
        System.out.println(countTrees(map, 1, 3));

        // PART 2 - Multiplying results of several slopes
        System.out.println(countTrees(map, 1, 1)
                            * countTrees(map, 1, 3)
                            * countTrees(map, 1, 5)
                            * countTrees(map, 1, 7)
                            * countTrees(map, 2, 1));
    }
}
