package me.kunai.mcroleplay;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

class DBManager {

    private Connection sqliteConn;
    private boolean connected;

    private static final String DB_LOCATION = "jdbc:sqlite:%s/mcrp.db";
    private static final String DB_DIR = "plugins/MCRolePlaying";
    private static final String CREATE_TABLE_FORMAT = "CREATE TABLE IF NOT EXISTS %s (%s)";
    private static final String LEVEL_TABLE_NAME = "ClassLevels";
    private static final String LEVEL_TABLE_SCHEMA = "player TEXT PRIMARY KEY NOT NULL, class TEXT NOT NULL, value INT NOT NULL";
    private static final String ALLOW_LEVEL_TABLE_NAME = "AllowLevel";
    private static final String ALLOW_LEVEL_TABLE_SCHEMA = "player TEXT PRIMARY KEY NOT NULL, times INT NOT NULL";

    DBManager() {
        try {
            setupDirectories(DB_DIR);
            sqliteConn = DriverManager.getConnection(String.format(DB_LOCATION, DB_DIR));
            sqliteConn.setAutoCommit(false);
            connected = true;

            setupTables();
        } catch (Exception e) {
            connected = false;
        }
    }

    boolean disconnect() {
        if (connected) {
            try {
                sqliteConn.close();
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        return true;
    }

    /**
     * Marks a player as having an available levelup.
     *
     * @param playerName Player username
     * @return True if successful, false otherwise
     */
    boolean enableLevelUp(String playerName) {
        if (!connected) {
            return false;
        }

        try {
            int availLevels = getAvailableSkillPoints(playerName);
            String addLevel = "INSERT INTO %s (player, times) VALUES (%s, %d)";
            return runSQLUpdate(String.format(addLevel, ALLOW_LEVEL_TABLE_NAME, playerName, availLevels+1));
        } catch (Exception e) {
            return false;
        }
    }

    int getAvailableSkillPoints(String playerName) {
        if (!connected) {
            return -1;
        }

        try {
            // Yes I know there is a possible SQL injection via malicious playnames. Do I care? No. Use a server whitelist.
            String checkCurrent = "SELECT times FROM %s WHERE player='%s'";
            ResultSet rs = runSQLQuery(String.format(checkCurrent, ALLOW_LEVEL_TABLE_NAME, playerName));

            int availLevels = 0;
            if (rs.next()) {
                availLevels = rs.getInt("times");
            }
            rs.close();

            return availLevels;
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * Levels up a player and logs in database.
     *
     * @param playerName Player username
     * @param pClass Class to Level
     * @return New level of that class. -1 indicates that the player was not successfully levelled.
     */
    int levelUp(String playerName, PlayerClass pClass) {
        if (!connected) {
            return -1;
        }

        try {
            int levels = getCurrentLevel(playerName, pClass);

            String addLevel = "INSERT INTO %s (player, class value) VALUES (%s, %s, %d)";
            if (!runSQLUpdate(String.format(addLevel, LEVEL_TABLE_NAME, playerName, pClass.name(), levels+1))) {
                return -1;
            }
            return levels+1;
        } catch (Exception e) {
            return -1;
        }
    }

    int getCurrentLevel(String playerName, PlayerClass pClass) {
        if (!connected) {
            return -1;
        }

        try {
            String checkCurrent = "SELECT value FROM %s WHERE player='%s' AND class='%s'";
            ResultSet rs = runSQLQuery(String.format(checkCurrent, LEVEL_TABLE_NAME, playerName, pClass.name()));

            int levels = 0;
            if (rs.next()) {
                levels  = rs.getInt("value");
            }
            rs.close();

            return levels;
        } catch (Exception e) {
            return -1;
        }
    }

    private boolean setupDirectories(String path) {
        File f = new File(path);
        return f.exists() || f.mkdirs();
    }

    private boolean setupTables() {
        // Set up level table
        if (!runSQLUpdate(String.format(CREATE_TABLE_FORMAT, LEVEL_TABLE_NAME, LEVEL_TABLE_SCHEMA))) {
            return false;
        }
        // Set up allow levelling table
        if (!runSQLUpdate(String.format(CREATE_TABLE_FORMAT, ALLOW_LEVEL_TABLE_NAME, ALLOW_LEVEL_TABLE_SCHEMA))) {
            return false;
        }
        return true;
    }

    private boolean runSQLUpdate(String s) {
        if (!connected) {
            return false;
        }

        try {
            Statement stmt = sqliteConn.createStatement();
            stmt.executeUpdate(s);
            stmt.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private ResultSet runSQLQuery(String s) {
        if (!connected) {
            return null;
        }

        try {
            Statement stmt = sqliteConn.createStatement();
            ResultSet rs = stmt.executeQuery(s);
            stmt.close();
            return rs;
        } catch (Exception e) {
            return null;
        }
    }
}
