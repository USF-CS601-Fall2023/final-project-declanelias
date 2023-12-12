package server.Database;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/** Class for parsing the command line
 *
 */
public class CommandLineParser {

    private Map<String, String> argMap = new HashMap<>();


    /** Command Line Parser for the main function
     * Checks that the command line is valid and parses what the user supplied
     * @param args command line arguments
     * @param validParameters list of valid parameters to be supplied in command line
     * @return hash map with command line parameters as keys and what the user argument for
     *         each parameter as the value
     */
    public void parse(String[] args, Set<String> validParameters) {
        if (args.length == 0) {
            System.err.println("No command line args provided");
            System.exit(1);
        }


        String currentParam = null;
        boolean lastArgWasParam = false;
        for (String arg : args) {
            // arg is a param if it starts with a hyphen
            if (arg.startsWith("-")) {
                currentParam = arg.substring(1);
                // If parameter is invalid, exit with error
                if (!validParameters.contains(currentParam)) {
                    System.err.println("Invalid parameter: " + arg);
                    System.exit(1);
                }
                // If parameter has already been passed, exit with error
                if (argMap.containsKey(currentParam)) {
                    System.err.println("Parameter " + arg + " passed too many times");
                    System.exit(1);
                }
                lastArgWasParam = true;

            } else {
                // If currentParam was not set, then we know command line is invalid
                if (currentParam == null || !lastArgWasParam) {
                    System.err.println("Unexpected argument: " + arg);
                    System.exit(1);
                }

                argMap.put(currentParam, arg);
                lastArgWasParam = false;
                currentParam = null; // reset currentParam to move on to next parameter argument pair
            }
        }

        // Check if a parameter is left without an argument
        if (currentParam != null) {
            System.err.println("Missing argument for parameter: " + currentParam);
            System.exit(1);
        }
    }

    /**
     * Given a command line parameter, returns the argument.
     * @param argName paramter
     * @return string argument given a parameter
     */
    public String getArg(String argName) {
        if (argName.equals("threads")) {
            String threadString = argMap.get(argName);
            if (threadString == null) {
                return "1";
            }
        }
        if (!argMap.containsKey(argName)) {
            return null;
        }
        return argMap.get(argName);
    }
}
