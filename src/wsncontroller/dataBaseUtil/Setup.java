package wsncontroller.dataBaseUtil;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Setup {

    private static Connection con;

    public static void Connect() {
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
        } catch (ClassNotFoundException e) {
            System.out.println("Cannot find  JDBC Driver");
            System.exit(1);
        }
        System.out.println(" JDBC Driver Registered!");
        con = null;
        try {
            con = DriverManager.getConnection("jdbc:derby://localhost:1527/SunSpotDB", "theo", "chris");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        if (con == null) {
            System.out.println("Connection Failed");
            System.exit(1);
        } else {
            System.out.println("Connection Succeeded");
        }
        DropAllTables();
        CreateNodesTable();
        CreateNodesVarValuesTable();
        CreateDataValuesTable();
    }

    public static Statement GetStatement() {
        try {
            return con.createStatement();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static Connection GetConnection() {
        return con;
    }

    public static void DropAllTables() {
        Statement st = GetStatement();

        try {
            DatabaseMetaData dbm = con.getMetaData();
            ResultSet tables = dbm.getTables(null, null, "NODES_VAR_VALUES", null);
            if (tables.next()) {
                String query = "DROP TABLE NODES_VAR_VALUES";
                st.executeUpdate(query);
            }

            tables = dbm.getTables(null, null, "DATA_MESUREMENTS", null);
            if (tables.next()) {
                String query3 = "DROP TABLE DATA_MESUREMENTS";
                st.executeUpdate(query3);
            }
            
            tables = dbm.getTables(null, null, "NODES", null);
            if (tables.next()) {
                String query2 = "DROP TABLE NODES";
                st.executeUpdate(query2);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                st.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void CreateNodesVarValuesTable() {
        Statement st = GetStatement();

        DatabaseMetaData dbm;
        try {
            dbm = con.getMetaData();
            ResultSet tables = dbm.getTables(null, null, "NODES_VAR_VALUES", null);

            if (tables.next()) { //If exists
                //System.out.println("Yparxei to nodes!!!");
            } else {
                String query = "CREATE TABLE  NODES_VAR_VALUES (spot_id varchar(15), frequency int, state varchar(5), FOREIGN KEY (spot_id) REFERENCES NODES(spot_id))";
                st.executeUpdate(query);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                st.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void CreateNodesTable() {
        Statement st = GetStatement();
        DatabaseMetaData dbm;
        try {
            dbm = con.getMetaData();
            ResultSet tables = dbm.getTables(null, null, "NODES", null);

            if (tables.next()) { //If exists
                //System.out.println("Yparxei to nodes!!!");
            } else {
                String query = "CREATE TABLE  NODES (spot_id varchar(15),  posx int, posy int, quadrant int,PRIMARY KEY (spot_id))";
                st.executeUpdate(query);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Setup.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                st.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void CreateDataValuesTable() {
        Statement st = GetStatement();
        DatabaseMetaData dbm;
        try {
            dbm = con.getMetaData();
            ResultSet tables = dbm.getTables(null, null, "DATA_MESUREMENTS", null);

            if (tables.next()) { //If exists
                //System.out.println("Yparxei to nodes!!!");
            } else {
                String query = "CREATE TABLE  DATA_MESUREMENTS (spot_id varchar(15) ,  timestamp varchar(26), light varchar(7), temperature varchar(20), "
                        + " FOREIGN KEY (spot_id) "
                        + "REFERENCES NODES(spot_id)) ";
                st.executeUpdate(query);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Setup.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                st.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
