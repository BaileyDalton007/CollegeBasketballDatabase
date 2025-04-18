package commands;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Use case #10 (Maura Schorr)
 */
public class followersSoldOut implements command {
    @Override
    public String getCMDString() {
        return "followerssoldout"; 
    }

    @Override 
    public String helpMessage() {
        return "Team gains followers and sells out game. \n"+ 
        "Usage: followersSoldOut <team id> <new followers> <new sold out> ";
    }

    @Override
    public void run(String args[]) { 
        if (args.length != 11) {
            System.out.println("Incorrect usage.\n" + helpMessage()); 
            return; 
        }

        int tID = Integer.parseInt(args[0]);
        int new_followers = Integer.parseInt(args[1]);
        Date purDate = Date.valueOf(args[2]); 
        int section1 = Integer.parseInt(args[3]); 
        int seatNum1 = Integer.parseInt(args[4]);
        int rowNum1 = Integer.parseInt(args[5]); 
        int price1 = Integer.parseInt(args[6]); 
        int section2 = Integer.parseInt(args[7]); 
        int seatNum2 = Integer.parseInt(args[8]);
        int rowNum2 = Integer.parseInt(args[9]);
        int price2 = Integer.parseInt(args[10]); 

        String calledStoredProc = "{call followersSoldOut(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";

        Connection connection = null; 

        try {
            connection = 
        DriverManager.getConnection(connectionHandler.connectionUrl);
            
            try(CallableStatement stmt = connection.prepareCall(calledStoredProc);)
            {
                connection.setAutoCommit(false);

                stmt.setInt(1, tID); 
                stmt.registerOutParameter(2, java.sql.Types.INTEGER); //new followers
                stmt.setDate(3, purDate); //new sponsors contribution
                stmt.setInt(4, section1); //ticket 1 section
                stmt.setInt(5, seatNum1); //ticket 1 seat number
                stmt.setInt(6, rowNum1); //ticket 1 row number
                stmt.setInt(7, price1); //ticket 1 price
                stmt.setInt(8, section2); //ticket 2 section
                stmt.setInt(9, seatNum2); //ticket 2 seat number
                stmt.setInt(10, rowNum2); //ticket 3 row number
                stmt.setInt(11, price2); //ticket 2 price
                stmt.registerOutParameter(12, java.sql.Types.INTEGER); //new number of sold out games

                stmt.execute();

                int followers = stmt.getInt(2); 
                int numSoldOut = stmt.getInt(12);       

                if (stmt.wasNull()) { 
                    System.out.println("Must add all information for each ticket");
                    throw new SQLException();
                }

                System.out.printf(
                    "Updated team %d followers to %d, now have %d sold out games%n",
                    tID, followers, numSoldOut
                );

                connection.commit();
            }
        }

        //handle any errors
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

