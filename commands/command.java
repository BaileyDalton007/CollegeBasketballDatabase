package commands;

public interface command {
    public String getCMDString();

    public void helpMessage();

    public void run(String[] args);
}
