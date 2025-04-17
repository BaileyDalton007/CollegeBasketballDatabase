package commands;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Use case #3 (Alyssa Murphy)
 * a player leaves a team
 */
public class removePlayer implements command {
    @Override
    public String getCMDString() {
        return "removeplayer";
    }

    @Override
    public String helpMessage() {
        return "Remove a player from college basketball and update student count.\n" +
               "Usage: removePlayer <player id>";
    }

    @Override
    public void run(String[] args) {
        if (args.length != 1) {
            System.out.println("Incorrect usage.\n" + helpMessage());
            return;
        }

        int pID = Integer.parseInt(args[0]);
        String callStoredProc = "{call removePlayer(?,?,?,?)}";
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(connectionHandler.connectionUrl);

            try (CallableStatement stmt = connection.prepareCall(callStoredProc)) {
                connection.setAutoCommit(false);

                stmt.setInt(1, pID);
                stmt.registerOutParameter(2, Types.VARCHAR); // @player_name
                stmt.registerOutParameter(3, Types.VARCHAR); // @college_name
                stmt.registerOutParameter(4, Types.INTEGER); // @updated_numStudents

                stmt.execute();

                String playerName = stmt.getString(2);
                String collegeName = stmt.getString(3);
                int remaining = stmt.getInt(4);

                if (playerName == null) {
                    System.out.println("No player found with ID " + pID);
                    throw new SQLException();
                }

                System.out.printf(
                    "Removed player %s from %s. Remaining students: %d%n",
                    playerName, collegeName, remaining
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

