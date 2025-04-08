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
public class help implements command {

    @Override
    public String getCMDString() {
        return "help";
    }

    @Override
    public String helpMessage() {
        return "Shows help pages for commands.";
    }

    @Override
    public void run(String[] args) {
        throw new UnsupportedOperationException("Unimplemented method 'run'");
    }
    
}
