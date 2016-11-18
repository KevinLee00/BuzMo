import java.sql.*;

public class SQLHelper {
    
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
    }
    
    public static void DeleteTable(String tableName) {
	SQLHelper.ExecuteSQL("DROP TABLE " + tableName);
    }

    public static void ExecuteSQL(String sql) {
	try {
	    Class.forName("oracle.jdbc.driver.OracleDriver");
	    String url = "jdbc:oracle:thin:@uml.cs.ucsb.edu:1521:xe";
	    String username = "christiandaley";
	    String password = "003";
	    Connection con=DriverManager.getConnection(url,username, password);
	    Statement st = con.createStatement();
	    st.executeQuery(sql);
	    con.close();
	} catch (Exception e) { System.out.println(e); }
	
    }
    
    private static String[] GetColumnNamesForTable(String table) {
	if (table == "Users") {	
	    	String columns[] = {"name", "phone_num", "email", "pword", "screen_name, is_manager"};
		return columns;
	}

	if (table == "Messages") {
		String columns[] = {"message_id", "text", "timestamp", "sender", "receiver"};
		return columns;
	}

	return null;
    }
}
