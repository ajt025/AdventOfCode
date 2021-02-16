import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day11 {

    // GOAL - find # of occupied seats after swapping according to rules

    // RULES
    // - L -> #
    //   ###    ###
    // - ### -> #L#
    //   ###    ###

    private static List<List<Character>> seats;
    public static final int[][] DIRECTIONS = {
            {-1, -1},
            {-1,  0},
            {-1,  1},
            { 0, -1},
            { 0,  1},
            { 1, -1},
            { 1,  0},
            { 1,  1},
    };

    // Perform the swapping operation on our seats. Creates a new copy of the seats map every iteration (costly).
    // @param visibility - toggle for line of sight checking (TRUE for PART2)
    public static void updateSeats(boolean visibility) {
        boolean changeFlag = true;

        // continue swapping until there is an iteration that makes no changes
        while (changeFlag) {
            changeFlag = false; // clear this iteration

            // create copy of seats - seat polling occurs only on initial state of copy (i.e. changes from phase 1 do not affect phase 2)
            List<List<Character>> updatedSeats = new ArrayList<>();
            for (List<Character> seat : seats) {
                List<Character> updatedRow = new ArrayList<>(seat);
                updatedSeats.add(updatedRow);
            }

            // perform swaps
            for (int i = 0; i < seats.size(); ++i) {
                for (int j = 0; j < seats.get(0).size(); ++j) {
                    // for every unoccupied seat 'L'... - PHASE 1
                    if (seats.get(i).get(j) == 'L') {

                        // PART 2 - visibility toggle
                        if (visibility) {
                            // becomes occupied if no occupied visible
                            if (countVisible(seats, i, j) == 0) {
                                updatedSeats.get(i).set(j, '#');
                                changeFlag = true;
                            }
                        // PART 1 - adjacency toggle
                        } else {
                            // becomes occupied if no occupied adjacent
                            if (countSurrounded(seats, i, j) == 0) {
                                updatedSeats.get(i).set(j, '#');
                                changeFlag = true;
                            }
                        }
                    }

                    // for every occupied seat '#'... - PHASE 2
                    if (seats.get(i).get(j) == '#') {

                        // PART 2 - visibility toggle
                        if (visibility) {
                            // becomes unoccupied if 5+ visible
                            if (countVisible(seats, i, j) >= 5) {
                                updatedSeats.get(i).set(j, 'L');
                                changeFlag = true;
                            }
                        // PART 1 - adjacency toggle
                        } else {
                            // becomes unoccupied if surrounded by 4+ occupied
                            if (countSurrounded(seats, i, j) >= 4) {
                                updatedSeats.get(i).set(j, 'L');
                                changeFlag = true;
                            }
                        }
                    }
                }
            }

            // update old seats to current updated copy
            seats = updatedSeats;
            printDebug(seats);
        }
    }

    // HELPERS //

    // Return # of occupied seats adjacent (8 spaces) to the given position.
    public static int countSurrounded(List<List<Character>> seats, int row, int col) {
        int rowBoundary = seats.size(); int colBoundary = seats.get(0).size();
        int count = 0;

        for (int[] direction : DIRECTIONS) {
            int targetRow = row + direction[0];
            int targetCol = col + direction[1];

            // check space validity
            if (targetRow < 0 || targetRow >= rowBoundary || targetCol < 0 || targetCol >= colBoundary)
                continue; // skip if OOB

            // check adjacent space
            if (seats.get(targetRow).get(targetCol) == '#') ++count;
        }

        return count;
    }

    // Return # of occupied seats in line of sight (8 directions) to the given position.
    public static int countVisible(List<List<Character>> seats, int row, int col) {
        int rowBoundary = seats.size(); int colBoundary = seats.get(0).size();
        int count = 0;

        for (int[] direction : DIRECTIONS) {
            int multiplier = 1;

            while (true) {
                int targetRow = row + (direction[0] * multiplier);
                int targetCol = col + (direction[1] * multiplier);

                // check space validity
                if (targetRow < 0 || targetRow >= rowBoundary || targetCol < 0 || targetCol >= colBoundary)
                    break; // skip if OOB

                // check adjacent space
                if (seats.get(targetRow).get(targetCol) == '#') {
                    ++count;
                    break;
                } else if (seats.get(targetRow).get(targetCol) == 'L')
                    break; // empty seat breaks line of sight as well

                ++multiplier;
            }
        }

        return count;
    }

    // Returns the number of occupied seats on the airplane.
    public static int getOccupied() {
        int count = 0;

        for (List<Character> row : seats) {
            for (Character each : row) {
                if (each == '#') ++count;
            }
        }

        return count;
    }

    // Debug method - prints the 2D map of seats.
    public static void printDebug(List<List<Character>> seats) {
        for (List<Character> list : seats) {
            System.out.println(list);
        }
        System.out.println("--------------------------------------------------------------------------");
    }

    public static void main(String[] args) throws FileNotFoundException {
        seats = new ArrayList<>();

        File file = new File("src/input/Day11.txt");
        Scanner scanner = new Scanner(file);

        while (scanner.hasNextLine()) {
            String curr = scanner.nextLine();

            List<Character> row = new ArrayList<>();
            for (int i = 0; i < curr.length(); ++i) {
                row.add(curr.charAt(i));
            }
            seats.add(row);
        }

//        updateSeats(false); // PART 1 - checks adjacency
        updateSeats(true); // PART 2 - checks visibility
        System.out.println(getOccupied());
    }
}
