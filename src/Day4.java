import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Day4 {

    // Checks if the current passport map has valid conditions
    public static boolean checkValid(HashMap<String, String> passport) {
        // CASE 1: all 8 entries or missing 1 entry
        if (passport.size() >= 7) {
            // CASE 1.5: missing something other than CID
            if (passport.size() == 7 && passport.containsKey("cid")) return false;

            // *** FIELD VALIDATION *** // - logically, all these values must be present in the passport

            // year verification
            if (Integer.parseInt(passport.get("byr")) < 1920 || Integer.parseInt(passport.get("byr")) > 2002) return false; // birth year
            if (Integer.parseInt(passport.get("iyr")) < 2010 || Integer.parseInt(passport.get("iyr")) > 2020) return false; // issue year
            if (Integer.parseInt(passport.get("eyr")) < 2020 || Integer.parseInt(passport.get("eyr")) > 2030) return false; // expire year

            // height verification
            if (passport.get("hgt").contains("cm")) {
                int cmHeight = Integer.parseInt(passport.get("hgt").substring(0, passport.get("hgt").length() - 2)); // cut off 'cm'
                if (cmHeight < 150 || cmHeight > 193) return false;
            } else if (passport.get("hgt").contains("in")) {
                int inHeight = Integer.parseInt(passport.get("hgt").substring(0, passport.get("hgt").length() - 2)); // cut off 'in'
                if (inHeight < 59 || inHeight > 76) return false;
            } else {
                // missing a valid unit label
                return false;
            }

            // hair color verification
            char[] hairColor = passport.get("hcl").toCharArray();
            if (hairColor[0] != '#' || hairColor.length != 7) return false; // Format: #------
            for (int i = 1; i < hairColor.length; ++i) {
                // if between 0 - 9 OR between 'a' - 'f'--> true - DeMorgan
                // if less than 0 OR greater than 9 AND less than 'a' OR greater than 'f' --> true
                if ((hairColor[i] < '0' || hairColor[i] > '9') && (hairColor[i] < 'a' || hairColor[i] > 'f')) return false;
            }

            // eye color verification
            String eyeColor = passport.get("ecl");
            HashSet<String> validEyeColors = new HashSet<>(Arrays.asList("amb", "blu", "brn", "gry", "grn", "hzl", "oth"));
            if (!validEyeColors.contains(eyeColor)) return false;

            // passport id verification
            String passportId = passport.get("pid");
            if (passportId.length() != 9) return false;
            for (int i = 0; i < passportId.length(); ++i) {
                if (passportId.charAt(i) < '0' || passportId.charAt(i) > '9') return false;
            }

            // passes all above conditions
            return true;
        }

        // CASE 3: missing a vital entry (<= 6)
        return false;
    }

    public static void main(String[] args) throws FileNotFoundException {
        // Input retrieval/parsing
        File file = new File("src/input/Day4.txt");
        Scanner scanner = new Scanner(file);

        HashMap<String, String> passport = new HashMap<>();
        int count = 0; // number of valid passports

        while (scanner.hasNextLine()) {
            String curr = scanner.nextLine();

            // CASE 1: line has content
            if (!curr.isEmpty()) {
                // Get each key:value pair
                String[] splitStr = curr.split(" ");
                // load pairs into the hashmap
                for (String each : splitStr) {
                    String[] pair = each.split(":");
                    passport.put(pair[0], pair[1]);
                }
            } else {
            // CASE 2: line has no content - end of passport entry
//                debug
//                boolean result = checkValid(passport);
//                if (result) ++count;
//                System.out.println(passport + " " + result);
//                passport.clear();
                if (checkValid(passport)) ++count;
                passport.clear(); // ready hashmap for next passport entry
            }
        }

        // check the last entry b/c no newline at EOF
        if (checkValid(passport)) ++count;

        scanner.close();

        System.out.println(count);
    }

}
