import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Day5 {

    public static final int NUM_OF_ROWS = 127;
    public static final int NUM_OF_COLS = 7;

    // Decodes our seats strings. Returns a list of our unique seat IDs.
    // O(n) per seat, O(1) per seat - ignoring input/output space
    public static List<Integer> decodeSeats(List<String> seats) {
        List<Integer> seatIds = new ArrayList<>();

        for (String seat : seats) {
            // similar to binary search, need to keep track of valid range
            int rowLow = 0; int rowHigh = NUM_OF_ROWS;
            int colLow = 0; int colHigh = NUM_OF_COLS;

            // Decode the current seat's row
            for (int i = 0; i < 7; ++i) {
                int mid = rowLow + (rowHigh - rowLow) / 2; // mid will always favor left
                if (seat.charAt(i) == 'F') { // take lower half
                    rowHigh = mid;
                } else if (seat.charAt(i) == 'B') { // take upper half
                    rowLow = mid + 1;
                }
            }

            // Decode the current seat's col
            for (int i = 7; i < seat.length(); ++i) {
                int mid = colLow + (colHigh - colLow) / 2; // mid will always favor left
                if (seat.charAt(i) == 'L') { // take lower half
                    colHigh = mid;
                } else if (seat.charAt(i) == 'R') { // take upper half
                    colLow = mid + 1;
                }
            }
            // POST-CONDITION: rowLow == rowHigh && colLow == colHigh

            // Hash the seat and save to our list
            seatIds.add(hashSeat(rowLow, colLow));
        }

        return seatIds;
    }

    // Finds the missing seat id, which is a missing value where ID's respectively -1 and +1 are in the list.
    // O(nlogn), O(1)
    public static int findMissingSeat(List<Integer> seats) {
        // ASSUMPTION: We can modify the list in place. If not, we'll need to use O(n) memory to create a copy.
        Collections.sort(seats);

        for (int i = 1; i < seats.size(); ++i) {
        // -1 and +1 means that in a sorted list, we are looking for the two elements that have a difference of 2 in IDs
            if (seats.get(i) - seats.get(i - 1) == 2) { // compare curr to previous element
                return seats.get(i) - 1;
            }
        }

        return -1;
    }

    // Helpers

    // Calculates the unique seat ID from the row and column
    public static int hashSeat(int row, int column) {
        return (row * 8) + column;
    }

    public static void main(String[] args) throws FileNotFoundException {
        // Input retrieval/parsing
        File file = new File("src/input/Day5.txt");
        Scanner scanner = new Scanner(file);

        List<String> seats = new ArrayList<>();

        // Each line represents a binary partitioned seat
        while (scanner.hasNextLine()) {
            seats.add(scanner.nextLine());
        }
        scanner.close();

        List<Integer> seatIds = decodeSeats(seats);
        // PART 1: largest seat ID of all the seats
        System.out.println(Collections.max(seatIds));

        // PART 2: find missing seat ID
        System.out.println(findMissingSeat(seatIds));
    }
}
