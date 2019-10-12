package xyz.kazuthecat.coffeebot.databases;

import javax.swing.plaf.nimbus.State;
import java.sql.*;

public class DBHandler {
    private final String dbAddr;

    public DBHandler(String dbAddr, String dbUser, String dbPass) {
        this.dbAddr = "jdbc:mysql://" + dbAddr + "/coffeedb?user=" + dbUser + "&password=" + dbPass;
    }

    private Connection getDBConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        return DriverManager.getConnection(dbAddr);
    }

    /**
     * Execute an arbitrary number of SQL statements.
     * @param sqls The statements to be executed.
     * @return A DBEnum indicating if the operation was successful.
     */
    public DBEnum execute(String[] sqls) {
        Connection conn = null;
        Statement stmt = null;
        DBEnum dbEnum;
        try {
            conn = getDBConnection();
            stmt = conn.createStatement();
            for (String sql : sqls) {
                stmt.addBatch(sql);
            }
            stmt.executeBatch();
            dbEnum = DBEnum.SUCCESS;
        } catch (SQLException | ClassNotFoundException e) {
            dbEnum = executeException(e);
        } finally {
            try { if (stmt != null) stmt.close(); } catch (Exception e) { executeException(e); }
            try { if (conn != null) conn.close(); } catch (Exception e) { executeException(e); }
        }
        return dbEnum;
    }

    /**
     * Execute a PreparedStatement an arbitrary number of times (with different keys).
     * For different statements the function needs to be called multiple times.
     * @param sql The statements to be executed.
     * @param keys The keys to be filled in for each statement.
     * @return A DBEnum indicating if the operation was successful.
     */
    public DBEnum execute(String sql, String[][] keys) {
        Connection conn = null;
        PreparedStatement stmt = null;
        DBEnum dbEnum;
        try {
            conn = getDBConnection();
            stmt = conn.prepareStatement(sql);
            for (String[] keyset : keys) {
                for (int i = 0; i < keyset.length; i++) {
                    stmt.setString(i + 1, keyset[i]);
                }
                stmt.execute();
                stmt.clearParameters();
            }
            dbEnum = DBEnum.SUCCESS;
        } catch (SQLException | ClassNotFoundException e) {
            dbEnum = executeException(e);
        } finally {
            try { if (stmt != null) stmt.close(); } catch (Exception e) { executeException(e); }
            try { if (conn != null) conn.close(); } catch (Exception e) { executeException(e); }
        }
        return dbEnum;
    }

    private DBEnum executeException(Exception e) {
        // Idea is that we might expand this to give more specific Enums later on.
        if (e instanceof SQLException) {
            System.out.println("ERROR: Encountered an SQLException when trying to execute batch statement.");
        } else if (e instanceof ClassNotFoundException) {
            System.out.println("ERROR: Could not find MySQL Database Driver.");
        }
        e.printStackTrace();
        return DBEnum.ERROR;
    }

}
