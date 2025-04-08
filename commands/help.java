package commands;

public class help implements command {

    @Override
    public String getCMDString() {
        return "help";
    }

    @Override
    public void helpMessage() {
        System.out.println("Shows help pages for commands.");
    }

    @Override
    public void run(String[] args) {
        throw new UnsupportedOperationException("Unimplemented method 'run'");
    }
    
}
