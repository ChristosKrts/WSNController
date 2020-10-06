package wsncontroller.dataBaseUtil;

import java.util.logging.Level;
import java.util.logging.Logger;
import wsncontroller.node.Node;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Queries {

    public static void InsertNode(String spot_id, int frequency, String state, int posx, int posy, int quadrant) {
        boolean flag1 = false, flag2 = false;

        String query = "INSERT INTO nodes (spot_id,posx,posy,quadrant) VALUES(?,?,?,?) ";
        PreparedStatement stmt = null;
        do {
            try {
                flag1 = false;
                stmt = Setup.GetConnection().prepareStatement(query);
                stmt.setString(1, spot_id);
                stmt.setInt(2, posx);
                stmt.setInt(3, posy);
                stmt.setInt(4, quadrant);
                stmt.executeUpdate();
            } catch (SQLException ex) {
                flag1 = true;
                System.err.println("Problem setting on nodes");
                ex.printStackTrace();
            } finally {
                closePreparedStatement(stmt);
            }
        } while (flag1 == true);

        String query2 = "INSERT INTO nodes_var_values ( spot_id,frequency, state) VALUES (?,?,?)";
        PreparedStatement stmt2 = null;
        do {
            try {
                flag2 = false;
                stmt2 = Setup.GetConnection().prepareStatement(query2);
                stmt2.setString(1, spot_id);
                stmt2.setInt(2, frequency);
                stmt2.setString(3, state);
                stmt2.executeUpdate();
            } catch (SQLException ex) {
                flag2 = true;
                System.err.println("Problem setting on nodes_static_values");
                Logger.getLogger(Queries.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                closePreparedStatement(stmt2);
            }
        } while (flag2 == true);
    }

    public static void InsertData(String spot_id, String timestamp, String temperature, String light) {
        String query = "INSERT INTO data_mesurements ( spot_id,timestamp, light, temperature) VALUES (?,?, ?, ?)";
        PreparedStatement stmt = null;
        try {
            stmt = Setup.GetConnection().prepareStatement(query);
            stmt.setString(1, spot_id);
            stmt.setString(2, timestamp);
            stmt.setString(3, light);
            stmt.setString(4, temperature);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("Problem inserting values into data_mesurements!!!!");
            Logger.getLogger(Queries.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closePreparedStatement(stmt);
        }

    }

    //checks if record from table nodes exists
    public static boolean checkIfExists(String spot_id) {
        ResultSet rs = null;
        boolean flag = false;
        PreparedStatement stmt = null;
        String query = "SELECT * FROM nodes WHERE spot_id = ?";
        try {
            stmt = Setup.GetConnection().prepareStatement(query);
            stmt.setString(1, spot_id);
            rs = stmt.executeQuery();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    while (rs.next()) {
                        flag = true;
                        break;
                    }
                } catch (SQLException e) {
                    System.out.println("Cannot get data(quantity) From productstor");
                    System.exit(1);
                } finally {
                    closeResultSet(rs);
                }
            }
            closePreparedStatement(stmt);
        }

        return flag;
    }

    public static void UpdateFreq(String spot_id, int frequency) {
        String query = "UPDATE nodes_var_values SET frequency = ?  WHERE spot_id = ?";
        PreparedStatement stmt = null;
        try {
            stmt = Setup.GetConnection().prepareStatement(query);
            stmt.setInt(1, frequency);
            stmt.setString(2, spot_id);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            closePreparedStatement(stmt);
        }
    }

    public static void PrintAllRecords() {
        Statement st = Setup.GetStatement();
        ResultSet rs = null;
        String query = "SELECT * FROM nodes_var_values";
        try {
            rs = st.executeQuery(query);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        if (rs != null) {
            try {
                while (rs.next()) {
                    System.out.println(rs.getString("spot_id") + "" + rs.getString("frequency") + "" + rs.getString("state"));
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        closeStatement(st);
        closeResultSet(rs);
    }

    public static void UpdateState(String spot_id, String state) {
        String query = "UPDATE nodes_var_values SET state = ?  WHERE spot_id = ?";
        PreparedStatement stmt = null;
        try {
            stmt = Setup.GetConnection().prepareStatement(query);
            stmt.setString(1, state);
            stmt.setString(2, spot_id);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            closePreparedStatement(stmt);
        }
    }

    public static List<Node> ReturnNodesQ(int quadrant) {
        ResultSet rs = null;
        String query = "SELECT * FROM nodes WHERE quadrant = ?";
        PreparedStatement stmt = null;
        try {
            stmt = Setup.GetConnection().prepareStatement(query);
            stmt.setInt(1, quadrant);
            rs = stmt.executeQuery();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            //closePreparedStatement(stmt);
        }

        if (rs != null) {
            List<Node> list = new ArrayList<Node>();
            try {
                while (rs.next()) {
                    ResultSet rs1 = null;
                    String query1 = "SELECT * FROM nodes_var_values WHERE  spot_id= ?";
                    PreparedStatement stmt1;
                    stmt1 = Setup.GetConnection().prepareStatement(query1);
                    stmt1.setString(1, rs.getString("spot_id"));
                    rs1 = stmt1.executeQuery();

                    if (rs1 != null) {
                        while (rs1.next()) {
                            Node node = new Node(rs.getString("spot_id"), rs1.getInt("frequency"), Boolean.parseBoolean(rs1.getString("state")), rs.getInt("posx"), rs.getInt("posy"), rs.getInt("quadrant"));
                            list.add(node);
                        }
                    }
                    closeResultSet(rs1);
                    closePreparedStatement(stmt1);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                closeResultSet(rs);
                return list;
            }
        }

        closeResultSet(rs);
        return null;
    }

    public static int getNodeFrequency(String spot_id) {
        ResultSet rs = null;
        String query = "SELECT * FROM nodes_var_values WHERE spot_id = ?";
        PreparedStatement stmt;
        try {
            stmt = Setup.GetConnection().prepareStatement(query);
            stmt.setString(1, spot_id);
            rs = stmt.executeQuery();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        try {
            while (rs.next()) {
                return rs.getInt("frequency");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Queries.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeResultSet(rs);
        }

        return 0; //In case something "bad" happens, return 0 
    }

    public static void deleteNodeRecord(String spot_id) {
        boolean flag1 = false, flag2 = false;
        String query = "DELETE FROM nodes_var_values WHERE  spot_id=(?)";
        PreparedStatement stmt = null;
        do {
            try {
                flag1 = false;
                stmt = Setup.GetConnection().prepareStatement(query);
                stmt.setString(1, spot_id);
                stmt.executeUpdate();
            } catch (SQLException ex) {
                flag1 = true;
                System.err.println("Problem deleting record from nodes_var_values");
                ex.printStackTrace();
            } finally {
                closePreparedStatement(stmt);
            }
        } while (flag1 == true);

        String query2 = "DELETE FROM nodes WHERE  spot_id=(?)";
        PreparedStatement stmt2 = null;
        do {
            try {
                flag2 = false;
                stmt2 = Setup.GetConnection().prepareStatement(query2);
                stmt2.setString(1, spot_id);
                stmt2.executeUpdate();
            } catch (SQLException ex) {
                flag2 = true;
                System.err.println("Problem deleting record from nodes");
                Logger.getLogger(Queries.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                closePreparedStatement(stmt2);
            }
        } while (flag2 == true);
    }

    public static void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                System.err.println("The result set cannot be closed.");
            }
        }
    }

    public static void closeStatement(Statement st) {
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                System.err.println("The statement cannot be closed.");
            }
        }
    }

    public static void closePreparedStatement(PreparedStatement st) {
        if (st != null) {
            try {
                st.close();
            } catch (SQLException ex) {
                Logger.getLogger(Queries.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
