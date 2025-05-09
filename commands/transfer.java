package commands;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Use case #1 (Bailey Dalton)
 */
public class transfer implements command {
    @Override
    public String getCMDString() {
        return "transfer";
    }

    @Override
    public String helpMessage() {
        return "Transfer a Player to a different team.\n" +
                "Usage: transfer <player id> <new team id>";
    }

    @Override
    public void run(String[] args) {
        // If given the incorrect number of parameters
        if (args.length != 2) {
            System.out.println("Incorrect usage.\n" + helpMessage());
            return;
        }


        int player_id = Integer.parseInt(args[0]);
        int team_id = Integer.parseInt(args[1]);

        String callStoredProc = "{call transferPlayer(?,?,?,?)}";

        Connection connection = null;

        try {
            connection = DriverManager.getConnection(connectionHandler.connectionUrl);

            try(CallableStatement prepsStoredProc = connection.prepareCall(callStoredProc);)
            {
                connection.setAutoCommit(false);
                prepsStoredProc.setInt(1, player_id);
                prepsStoredProc.setInt(2, team_id);
                prepsStoredProc.registerOutParameter(3, java.sql.Types.VARCHAR) ;
                prepsStoredProc.registerOutParameter(4, java.sql.Types.VARCHAR) ;
                prepsStoredProc.execute();
                
                String player_name = prepsStoredProc.getString(3);
                String college_name = prepsStoredProc.getString(4);

                // If the player does not exist in the table
                if (player_name == null) {
                    System.out.println(String.format("A player with the ID of %d does not exist.", player_id));
                    throw new SQLException();
                }

                System.out.println(String.format("Transferred %s to %s.", player_name, college_name));
            
                connection.commit();
            }
        }
        // Handle any errors that may have occurred.
        catch (SQLException e) {
            System.out.println("\nDatabase error: ");
            e.printStackTrace();
            
            // rollback if there is an error.
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
        }
    }
}