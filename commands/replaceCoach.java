package commands;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Use case #2 (Bailey Dalton)
 */
public class replaceCoach implements command {
    @Override
    public String getCMDString() {
        return "replacecoach";
    }

    @Override
    public String helpMessage() {
        return "Replace a current coach with a new coach.\n" +
                "Usage: replaceCoach <team id> <new coach id>";
    }

    @Override
    public void run(String[] args) {
        // If given the incorrect number of parameters
        if (args.length != 2) {
            System.out.println("Incorrect usage.\n" + helpMessage());
            return;
        }


        int team_id = Integer.parseInt(args[0]);
        int coach_id = Integer.parseInt(args[1]);

        String callStoredProc = "{call replaceCoach(?,?,?,?,?)}";

        Connection connection = null;

        try {
            connection = DriverManager.getConnection(connectionHandler.connectionUrl);
            
            try(CallableStatement prepsStoredProc = connection.prepareCall(callStoredProc);)
            {
                connection.setAutoCommit(false);
                prepsStoredProc.setInt(1, team_id);
                prepsStoredProc.setInt(2, coach_id);
                prepsStoredProc.registerOutParameter(3, java.sql.Types.VARCHAR) ;
                prepsStoredProc.registerOutParameter(4, java.sql.Types.VARCHAR) ;
                prepsStoredProc.registerOutParameter(5, java.sql.Types.VARCHAR) ;
                prepsStoredProc.execute();
                
                String old_coach_name = prepsStoredProc.getString(3);
                String new_coach_name = prepsStoredProc.getString(4);
                String college_name = prepsStoredProc.getString(5);

                if (college_name == null) {
                    System.out.println(String.format("A team with ID %d does not exist", team_id));
                    throw new SQLException();
                }

                if (new_coach_name == null) {
                    System.out.println(String.format("A coach with ID %d does not exist", coach_id));
                    throw new SQLException();
                }

                System.out.println(String.format("Fired coach %s from %s and replaced them with %s",
                                                    old_coach_name, college_name, new_coach_name));

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
