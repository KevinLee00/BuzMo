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
                        String sql = "CREATE TABLE Messages(message_id INTEGER NOT NULL, text CHAR(1400) NOT NULL, timestamp CHAR(10) NOT NULL, sender CHAR(20) NOT NULL, receiver CHAR(20) NOT NULL, PRIMARY KEY(message_id))";
                        ResultSet rs = st.executeQuery(sql);
                        //while(rs.next())
				//MODIFY PRINT TO FIT YOUR QUERY AND ATTRIBUTE TYPES
                        //        System.out.println(rs.getInt(1)+" "+rs.getString(2));
                        con.close();
                }
                catch(Exception e){System.out.println(e);}
        }
}



