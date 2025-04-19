package commands;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Use case #7 (Maura Schorr)
 */
public class newSponsor implements command {
    @Override
    public String getCMDString() {
        return "newsponsor"; 
    }

    @Override 
    public String helpMessage() {
        return "New sponsor enters becomes involved and forms relationship with team. \n"+ 
        "Usage: newSponsor <new sponsor id> <contribution> <team id>";
    }

    @Override
    public void run(String args[]) { 
        if (args.length != 3) {
            System.out.println("Incorrect usage.\n" + helpMessage()); 
            return; 
        }

        String sName = args[0];
        int contribution = Integer.parseInt(args[1]); 
        String tName = args[2]; 

        String callStoredProc = "{call newSponsor(?, ?, ?, ?)}";

        Connection connection = null; 

        try { 
            connection = DriverManager.getConnection(connectionHandler.connectionUrl); 
                
            try(CallableStatement stmt = connection.prepareCall(callStoredProc)) { 
                    connection.setAutoCommit(false); 
 
                    stmt.setString(1, sName); //new sponsor name
                    stmt.setInt(2, contribution); //new sponsors contribution
                    stmt.setString(3, tName); //team name
                    stmt.registerOutParameter(4, java.sql.Types.INTEGER);
                    stmt.execute();

                    int sID = stmt.getInt(4);

                    if (tName == null) {
                        System.out.println(String.format("A team with name %s does not exist", tName));
                        throw new SQLException();
                    }

                    if (sName == null) {
                        System.out.println(String.format("New sponsor must have a name"));
                        throw new SQLException();
                    }

                    System.out.println(String.format("New sponsor %s with ID %s has formed a relationship with the team %s, their contribution in the realtionship is %d%n", sName, sID,  tName, contribution));

                    connection.commit();
            }   

        }

        //handle any errors that occur 
        catch (SQLException e) {
            System.out.println("\nDatabase error:");
            e.printStackTrace();
            try {
                if (connection != null) connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
