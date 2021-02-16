import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day12 {

    // Class fields
    private static final char[] DIRECTIONS = {'E', 'S', 'W', 'N'};

    // Member fields
    int currDirection = 0;
    int currX = 0; int currY = 0;

    // Runs a list of instructions onto the ship. PART 1
    public void execute(List<String> instructions) {
        for (String each : instructions) {
            char op = each.charAt(0);
            int value = Integer.parseInt(each.substring(1));

            // Separate turning operations from NESW/F operations
            if (op == 'L' || op == 'R') {
                turn(op, value);
            } else {
                if (op == 'F') // moves forward in currently faced direction
                    move(DIRECTIONS[this.currDirection], value);
                else
                    move(op, value);
            }
        }
    }

    // Runs a list of instructions onto the ship and a given waypoint. PART 2
    public void executeWaypoint(List<String> instructions) {
        // represent waypoint as a another ship (Day12) - values are RELATIVE to ship
        Day12 waypoint = new Day12();
        // waypoint starts at position (10, 1)
        waypoint.currX = 10;
        waypoint.currY = 1;

        for (String each : instructions) {
            char op = each.charAt(0);
            int value = Integer.parseInt(each.substring(1));

            if (op == 'L' || op == 'R')
                waypoint.rotate(op, value); // rotates waypoint around callee ship
            else if (op == 'F')
                this.move(waypoint, value); // moves callee ship towards waypoint
            else
                waypoint.move(op, value); // moves waypoint NESW

//            DEBUG STATEMENT
//            System.out.println("Ship: " + this.currX + " " + this.currY);
//            System.out.println("Waypoint: " + waypoint.currX + " " + waypoint.currY);
//            System.out.println("------------------------");
        }
    }

    // Helpers //

    // Updates the direction the ship is facing. (L/R)
    public void turn(char direction, int value) {
        int turns = value / 90; // how many directional indices we need to move -- assume multiple of 90
        if (direction == 'L') {
            // negative wraparound
            currDirection = (((currDirection - turns) % DIRECTIONS.length) + DIRECTIONS.length) % DIRECTIONS.length;
        } else { // direction == 'R'
            currDirection = (currDirection + turns) % DIRECTIONS.length;
        }
    }

    // Moves forward in the specified direction. (F/NESW)
    public void move(char direction, int value) {
        switch (direction) {
            case 'N':
                currY += value; break;
            case 'E':
                currX += value; break;
            case 'S':
                currY -= value; break;
            case 'W':
                currX -= value; break;
        }
    }

    // Moves the ship towards another ship/waypoint a given number of times.
    public void move(Day12 waypoint, int times) {
        for (int i = 0; i < times; ++i) {
            this.currX += waypoint.currX;
            this.currY += waypoint.currY;
        }
    }

    // Rotates the ship around a given origin (another ship/waypoint).
    public void rotate(char direction, int value) {
        // rotate in 90 degree segments
        for (int i = value; i > 0; i -= 90) {
            if (direction == 'L') { // counter clockwise
                // (x, y) -> (-y, x)
                int temp = -this.currY;
                this.currY = this.currX;
                this.currX = temp;
            } else { // clockwise
                // (x, y) -> (y, -x)
                int temp = -this.currX;
                this.currX = this.currY;
                this.currY = temp;
            }
        }
    }
    // WAYPOINT ROTATION ANALYSIS - similar to turn, but (1) swap values and (2) apply negative
    // RIGHT90 = (10,4) --> (4, -10) --> (-10, -4) --> (-4, 10) --> ...
    // LEFT 90 = (10,4) --> (-4, 10) --> (-10, -4) --> (4, -10) --> ...

    // Returns Manhattan distance of ship.
    public int getL1Norm() {
        return Math.abs(currX) + Math.abs(currY);
    }


    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("src/input/Day12.txt");
        Scanner scanner = new Scanner(file);

        List<String> instructions = new ArrayList<>();
        while (scanner.hasNextLine()) {
            instructions.add(scanner.nextLine());
        }

        // PART 1 - Read instructions and determine L1 distance
        Day12 ship = new Day12();
        ship.execute(instructions);
        System.out.println(ship.getL1Norm());

        // PART 2 - Read instructions (waypoint alternative) and determine L1 distance
        Day12 shipToWaypoint = new Day12();
        shipToWaypoint.executeWaypoint(instructions);
        System.out.println(shipToWaypoint.getL1Norm());
    }
}
