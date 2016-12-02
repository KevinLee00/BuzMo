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
                        // String sql = "UPDATE CurrentTS SET current_timestamp = (TO_TIMESTAMP('2016-12-01 00:00:00.000', 'YYYY-MM--DD HH24:MI:SS.FF'))";
                        // String sql = "SELECT * FROM (SELECT * FROM (SELECT * FROM Messages M, CurrentTS T WHERE M.timestamp > (T.current_timestamp - interval '7' day)) ORDER BY views DESC) WHERE rownum <= 3 ORDER BY views";
                        // String sql = "SELECT sender, COUNT(M.sender) as AMT FROM Messages M WHERE type <> '0' GROUP BY sender ORDER BY AMT DESC"; 
                        // String sql = "SELECT sender, AMT FROM (SELECT sender, COUNT(M.owner) as AMT FROM Messages M, CurrentTS T WHERE owner = sender AND M.timestamp > (T.current_timestamp - interval '7' day) GROUP BY M.sender ORDER BY AMT DESC) WHERE rownum <= 3 ORDER BY AMT DESC";
                        String sql = "SELECT * FROM (SELECT * FROM (SELECT * FROM Messages M, CurrentTS T WHERE M.timestamp > (T.current_timestamp - interval '7' day) AND M.type = '3') ORDER BY views DESC) WHERE rownum <= 5 ORDER BY views";
                        // String sql = "SELECT * FROM Messages";
                        // String sql = "SELECT sender, COUNT(M.owner) as AMT FROM Messages M WHERE owner = sender GROUP BY M.sender ORDER BY AMT DESC";
                        // String sql = "UPDATE Users SET is_manager = '1' WHERE email = 'kevin2'";
                        ResultSet rs = st.executeQuery(sql);
                        while(rs.next())
                             System.out.println(rs.getString(1)
                                + " " + rs.getString(2).trim()
                                + " " + rs.getString(3).trim()
                                + " " + rs.getString(4).trim()
                                + " " + rs.getString(5).trim()
                                + " " + rs.getString(6).trim()
                                + " " + rs.getString(7).trim()
                                // + " " + rs.getString(8).trim()

                                );
                        con.close();
                }
                catch(Exception e){
                        System.out.println(e);
                }
        }
}