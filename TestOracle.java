import java.sql.*;

public class TestOracle {

        public static void main(String[] args){

                try{

                        Class.forName("oracle.jdbc.driver.OracleDriver");
                        String url = "jdbc:oracle:thin:@uml.cs.ucsb.edu:1521:xe";
                        String username = "christiandaley";
                        String password = "003";
                        Connection con=DriverManager.getConnection(url,username, password);
                        Statement st = con.createStatement();
                        String sql = "SELECT sender FROM Messages GROUP BY sender HAVING 4 > COUNT(*)";
                        // String sql = "SELECT * from messages";
                        ResultSet rs = st.executeQuery(sql);
                        while(rs.next())
                             System.out.println(rs.getString(1).trim() 
                                // + " " + rs.getString(2).trim()
                                // + " " + rs.getString(3).trim()
                                // + " " + rs.getString(4).trim()
                                // + " " + rs.getString(5).trim()
                                );
                        con.close();
                }
                catch(Exception e){System.out.println(e);}
        }
}



