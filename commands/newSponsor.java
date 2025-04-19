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
        "Usage: newSponsor <new sponsor id> <team id>";
    }

    @Override
    public void run(String args[]) { 
        if (args.length != 2) {
            System.out.println("Incorrect usage.\n" + helpMessage()); 
            return; 
        }

        Integer sID = Integer.parseInt(args[0]);
        String sName = args[1];
        int contribution = Integer.parseInt(args[3]); 
        String tName = args[4]; 

        String callStoredProc = "{call newSponsor(?, ?, ?, ?)}";

        Connection connection = null; 

        try { 
            connection = DriverManager.getConnection(connectionHandler.connectionUrl); 
                
            try(CallableStatement stmt = connection.prepareCall(callStoredProc)) { 
                    connection.setAutoCommit(false); 
                    stmt.setInt(1, sID); 
                    stmt.registerOutParameter(2, java.sql.Types.VARCHAR); //new sponsor name
                    stmt.registerOutParameter(3, java.sql.Types.INTEGER); //new sponsors contribution
                    stmt.registerOutParameter(4, java.sql.Types.VARCHAR); //team name
                    stmt.execute();

                    if (tName == null) {
                        System.out.println(String.format("A team with name %s does not exist", tName));
                        throw new SQLException();
                    }

                    if (sName == null) {
                        System.out.println(String.format("New sponsor must have a name"));
                        throw new SQLException();
                    }

                    System.out.println(String.format("New sponsor %s has formed a relationship with the team %s, their contribution in the realtionship is %d", sName, tName, contribution));

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
