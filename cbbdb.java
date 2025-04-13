import java.util.Scanner;
import java.util.Arrays;

import commands.*;

public class cbbdb {
    /**
     * List of all command type objects.
     * When a new command is created (inherits command), add it to this array.
     */
    private static commands.command[] cmdList = {
        new help(),
        new transfer(),
    };

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the College Basketball Database");
        System.out.println("Type 'help' to see available commands.");

        while (true) {
            // Takes a string input from the user.
            System.out.print("> ");
            String input = scanner.nextLine().trim().toLowerCase();

            // Splits the input into a array of the command name and possible parameters.
            // command p1 p2 p3
            String[] inputArr = input.split(" ");
            String commandString = inputArr[0];
            String[] paramArr = Arrays.copyOfRange(inputArr, 1, inputArr.length); // gets all but the first value (the command name)

            // Runs the method for the selected command.
            // Passes in parameters from input string.
            // If no matching command exists, print an error message.
            command commandObj = getCommand(commandString);
            if (commandObj == null) {
                System.out.printf("Command \"%s\" not found, try again. \n", commandString);
            } else {
                commandObj.run(paramArr);
            }

        }
    }

    /**
     * Returns the command object corresponding to the input command string.
     * @param input the string from the user specifying the command
     * @return the command object matching the input
     */
    private static command getCommand(String input) {
        for (command cmd : cmdList) {
            String cmdStr = cmd.getCMDString();

            if (cmdStr.equals(input)) {
                return cmd;
            }
        }

        return null;
    }


}
