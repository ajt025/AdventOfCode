import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

public class Day8 {

    int accumulator = 0;
    int currInstruction = 0;
    List<String> instructions;
    HashSet<Integer> visited;

    // Initial CTOR - top layer to solve problem
    public Day8(String filepath) throws FileNotFoundException {
        // Init instructions
        File file = new File(filepath);
        Scanner scanner = new Scanner(file);

        instructions = new ArrayList<>();

        // Each line represents an instruction
        while (scanner.hasNextLine()) {
            instructions.add(scanner.nextLine());
        }
        scanner.close();

        // Init visited hashset
        visited = new HashSet<>();
    }

    // Recursive CTOR - Shallow copy on instructions (readonly) and deep copy on rest
    public Day8(final List<String> instructions, HashSet<Integer> visited,
                int accumulator, int currInstruction) {
        this.instructions = instructions; // readonly, save space by copying only memaddr
        this.accumulator = accumulator;
        this.currInstruction = currInstruction;

        // deep copy on HashSet, we want a new obj to track an individual path's visited
        this.visited = new HashSet<>(visited);
    }

    // Executes our program, returning if a cycle exists.
    public boolean findCycle() {
        // loop until redundant instruction OR end of program
        while (currInstruction < instructions.size() && visited.add(currInstruction)) {
            // args[0] = instruction (ex. acc), args[1] = value (ex. +3, -1)
            String[] args = instructions.get(currInstruction).split(" ");
            int value = Integer.parseInt(args[1]);

            execute(args[0], value);
        }

        return currInstruction < instructions.size();
    }

    // Executes our program, creating a new path branch if a jmp/nop is encountered
    public int breakCycle() {
        // loop until we hit program termination
        while (currInstruction < instructions.size() && visited.add(currInstruction)) {
            String[] args = instructions.get(currInstruction).split(" ");
            int value = Integer.parseInt(args[1]);

            // swap nop/jmp in a new branch
            if (args[0].equals("nop")) {
                Day8 path = new Day8(instructions, visited, accumulator, currInstruction);
                path.execute("jmp", value);
                // attempted swap was the solution! return
                if (!path.findCycle()) {
                    System.out.println("FOUND @ " + currInstruction + " - " + instructions.get(currInstruction));
                    return path.accumulator;
                }
            } else if (args[0].equals("jmp")) {
                Day8 path = new Day8(instructions, visited, accumulator, currInstruction);
                path.execute("nop", value);
                // attempted swap was the solution! return
                if (!path.findCycle()){
                    System.out.println("FOUND @ " + currInstruction + " - " + instructions.get(currInstruction));
                    return path.accumulator;
                }
            }

            // no early return, swap must be found later --> continue execution
            execute(args[0], value);
        }

        // default return - no cycles encountered
        System.out.println("Curr instr: " + currInstruction);
        return accumulator;
    }

    // Helper function to reset program between problem parts
    private void reset() {
        this.accumulator = 0;
        this.currInstruction = 0;
        this.visited.clear();
    }

    // Helper function to execute an instruction
    private void execute(String op, int value) {
        switch (op) {
            case "jmp": // move instruction pointer <value>
                currInstruction += value;
                break;
            case "acc": // add to our accumulator and move to next instruction
                accumulator += value;
                ++currInstruction;
                break;
            case "nop": // ignore, move to next instruction
                ++currInstruction;
                break;
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        Day8 program = new Day8("src/input/Day8.txt");
        program.findCycle();
        System.out.println(program.accumulator);

        program.reset();
        System.out.println("---");

        // PART 2 - path branching -> at every nop/jmp, swap and see if it terminates
        System.out.println(program.breakCycle());
    }
}
