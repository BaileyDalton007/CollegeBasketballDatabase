package commands;

/**
 * The help command.
 * Gives more information on available commands.
 * Displays the list of all commands, or in-depth usage for a specific command.
 * 
 * usage:
 * > help
 * > help <command>
 * 
 */
public class help {

    public String getCMDString() {
        return "help";
    }

    public String helpMessage() {
        return "Shows help pages for commands.\n" +
                "Usage: help\n" +
                "Usage: help <command>\n";
    }

    public void run(String[] args, commands.command[] commands) {
        if (args.length == 0) {
            // If no arguements are given, list all possible commands.
            String output = "List of Commands:\n"+
                            "Use help <command name> for detailed information\n";

            for (commands.command cmd : commands) {
                output += cmd.getCMDString() + "\n";
            }

            System.out.print(output);

        } else if (args.length == 1) {
            // If an arguement is given, give its help message.
            String input = args[0];
            String output = String.format("Help Page for %s:\n", input);
            boolean cmdFound = false;

            for (commands.command cmd : commands) {
                if (cmd.getCMDString().equals(input)) {
                    output += cmd.helpMessage() + "\n";
                    cmdFound = true;
                }

            }

            if (cmdFound) {
                System.out.print(output);
            } else {
                System.out.print(String.format("Command %s not found \n" + helpMessage(), input));
            }

        } else {
            // If given the incorrect number of parameters
            System.out.println("Incorrect usage.\n" + helpMessage());
            return;
        }
    }
    
}
