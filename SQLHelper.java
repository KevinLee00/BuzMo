import java.sql.*;

public class SQLHelper {

    private static Connection con = null;
    
    public static void Insert(String table, String[] values, String[] types) {
	String sql = "INSERT INTO " + table + " VALUES (";
	for (int i = 0; i < values.length; i++) {
	    String t = i == values.length - 1 ? ")" : ",";
	    if (types[i] == "STRING")
		sql += "'" + values[i] + "'";
	    else if (types[i] == "INTEGER")
		sql += values[i];

	    sql += t;
	}

	ExecuteSQL(sql);
	Close();
    }
    
    public static void DeleteTable(String tableName) {
	SQLHelper.ExecuteSQL("DROP TABLE " + tableName);
	Close();
    }

    public static ResultSet ExecuteSQL(String sql) {
	ResultSet rs = null;
		
	try {
	    Class.forName("oracle.jdbc.driver.OracleDriver");
	    String url = "jdbc:oracle:thin:@uml.cs.ucsb.edu:1521:xe";
	    String username = "christiandaley";
	    String password = "003";
	    con=DriverManager.getConnection(url,username, password);
	    Statement st = con.createStatement();
	    rs = st.executeQuery(sql);
	} catch (Exception e) { System.out.println(e); }

	return rs;
	
    }

    public static void Close() {
	try {
	    con.close();
	} catch (Exception e) {System.out.println(e);}
    }
    
    private static String[] GetColumnNamesForTable(String table) {
	if (table == "Users") {	
	    	String columns[] = {"name", "phone_num", "email", "pword", "screen_name, is_manager"};
		return columns;
	}

	if (table == "Messages") {
		String columns[] = {"message_id", "text", "timestamp", "sender", "receiver", "message_type"};
		return columns;
	}

	return null;
    }
}
