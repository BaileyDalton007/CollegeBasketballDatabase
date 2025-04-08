package commands;

public interface command {
    /**
     * Gets the string version of the command that the user will type to execute it.
     * @return string version of the command
     */
    public String getCMDString();

    /**
     * Returns the help message for this command. 
     * Should include all use cases with parameters etc.
     * @return the string form of the help message for this command
     */
    public String helpMessage();

    /**
     * Runs the command.
     * @param args optional arguements passed into the command from the command line interface.
     */
    public void run(String[] args);
}
