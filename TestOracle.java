import java.sql.*;
import java.util.*;

public class TestOracle {

        public static void main(String[] args){
                Timestamp time = new Timestamp(Calendar.getInstance().getTime().getTime());
                // System.out.println(time.toString());
                try{

                        Class.forName("oracle.jdbc.driver.OracleDriver");
                        String url = "jdbc:oracle:thin:@uml.cs.ucsb.edu:1521:xe";
                        String username = "christiandaley";
                        String password = "003";
                        Connection con=DriverManager.getConnection(url,username, password);
                        Statement st = con.createStatement();
                        // String sql = "CREATE TABLE CurrentTS (current_timestamp TIMESTAMP)";
                        // String sql = "INSERT INTO CurrentTS VALUES(to_timestamp('" + time + "', 'YYYY-MM-DD HH24:MI:SS.FF'))";
                        // String sql = "UPDATE CurrentTS SET current_timestamp = (TO_TIMESTAMP('2020-01-01 00:00:00.000', 'YYYY-MM--DD HH24:MI:SS.FF'))";
                        // String sql = "SELECT COUNT(U.email) FROM Users U WHERE U.email NOT IN (SELECT sender FROM Messages)";
                        String sql = "SELECT * From CurrentTS";
                        // String sql = "SELECT sender FROM Messages GROUP BY sender HAVING 4 > COUNT(*)";
                        // String sql = "UPDATE Users SET is_manager = '1' WHERE email = 'kevin2'";
                        ResultSet rs = st.executeQuery(sql);
                        while(rs.next())
                             System.out.println(rs.getTimestamp(1));
                                // + " " + rs.getString(2).trim()
                                // + " " + rs.getString(3).trim()
                                // + " " + rs.getString(4).trim()
                                // + " " + rs.getString(5).trim()
                                // + " " + rs.getString(6).trim()
                                // + " " + rs.getString(7).trim()
                                // + " " + rs.getString(8).trim()

                                // );
                        con.close();
                }
                catch(Exception e){System.out.println(e);}
        }
}