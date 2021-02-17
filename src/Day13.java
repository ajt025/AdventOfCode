import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class Day13 {

    // Finds the bus route with the shortest wait time after the given timestamp.
    // Returns busId and the wait time.
    public static int[] findBus(int timestamp, String[] schedule) {
        int minBusId = -1;
        int minWait = Integer.MAX_VALUE;

        // mod time by all bus IDs while keeping a running minimum of remainder
        for (String each : schedule) {
            if (each.equals("x")) continue; // skip buses not in service

            int busId = Integer.parseInt(each);
            int wait = busId - (timestamp % busId); // wait time is interval - remainder

            // get wait time and id of bus with shortest wait time
            if (wait < minWait) {
                minWait = wait;
                minBusId = busId;
            }
        }

        return new int[] {minBusId, minWait};
    }

    // Finds a timestamp at which the following is satisfied:
    // - in schedule, the bus at index i departs i minutes after the bus at index 0 departs.
    // Returns the timestamp.
    public static long findTimestamp(String[] schedule) {
        long timestamp = Long.parseLong(schedule[0]);
        long step = timestamp;

        // build our timestamp incrementally - finding coinciding values one value at a time
        for (int i = 1; i < schedule.length; ++i) {
            if (!schedule[i].equals("x")) {
                long currBus = Long.parseLong(schedule[i]);

                timestamp = findValidOffset(timestamp, currBus, i, step);
                // safely increment by step; pattern will repeat every (first * second) due to nature of multiplication
                step *= currBus;
            }
        }

        return timestamp;
    }

    // HELPERS //

    // Finds the number in which the second number occurs at a given offset from the first number.
    public static long findValidOffset(long first, long second, long offset, long step) {
        long patternStart = first;
        // continue incrementing by our step until offset condition is satisfied
        while ((patternStart + offset) % second != 0) {
            patternStart += step;
        }
        return patternStart;
    }

    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("src/input/Day13.txt");
        Scanner scanner = new Scanner(file);

        // Text file will always have two lines
        int timestamp = Integer.parseInt(scanner.nextLine());
        String[] schedule = scanner.nextLine().split(",");

        // PART 1 - find bus with shortest waiting time
        System.out.println("-- PART 1 --");
        int[] departure = findBus(timestamp, schedule);
        System.out.println(Arrays.toString(departure));
        System.out.println(departure[0] * departure[1]);

        // PART 2 - find timestamp in which buses leave in consecutive fashion
        System.out.println("-- PART 2 --");
        System.out.println(findTimestamp(schedule));
    }
}
