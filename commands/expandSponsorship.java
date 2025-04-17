package commands;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Use case #4 (Alyssa Murphy)
 * a sponsor expands their support
 */
public class expandSponsorship implements command {
    @Override
    public String getCMDString() {
        return "expandsponsorship";
    }

    @Override
    public String helpMessage() {
        return "Increase a sponsor’s contribution and link to a new team.\n" +
               "Usage: expandSponsorship <sponsor id> <additional cents> <team id>";
    }

    @Override
    public void run(String[] args) {
        if (args.length != 3) {
            System.out.println("Incorrect usage.\n" + helpMessage());
            return;
        }

        int sID   = Integer.parseInt(args[0]);
        int extra = Integer.parseInt(args[1]);
        int tID   = Integer.parseInt(args[2]);
        String callStoredProc = "{call expandSponsorship(?,?,?,?,?)}";
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(connectionHandler.connectionUrl);

            try (CallableStatement stmt = connection.prepareCall(callStoredProc)) {
                connection.setAutoCommit(false);

                stmt.setInt(1, sID);
                stmt.setInt(2, extra);
                stmt.setInt(3, tID);
                stmt.registerOutParameter(4, Types.VARCHAR); // @sponsor_name
                stmt.registerOutParameter(5, Types.INTEGER); // @new_contribution

                stmt.execute();

                String sponsorName = stmt.getString(4);
                int totalCents     = stmt.getInt(5);

                if (sponsorName == null) {
                    System.out.println("No sponsor found with ID " + sID);
                    throw new SQLException();
                }

                System.out.printf(
                    "Sponsor %s now contributes %d cents and is linked to team %d.%n",
                    sponsorName, totalCents, tID
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