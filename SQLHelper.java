import java.sql.*;

public class SQLHelper {
    
    public static void Insert(String table, String[] values) {
	String columns[] = GetColumnNamesForTable(table);
	String sql = "INSERT INTO " + table + "(";
	String v = " VALUES (";
	for (int i = 0; i < columns.length; i++) {
	    String t = i == columns.length - 1 ? ")" : ", ";
	    sql += columns[i] + t;
	    v += "'" + values[i] + "'" + t;
	}

	sql += v;

	SQLHelper.ExecuteSQL(sql);	
    }
    
    public static void DeleteTable(String tableName) {
	SQLHelper.ExecuteSQL("DROP TABLE " + tableName);
    }
    

    private static void ExecuteSQL(String sql) {
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
	    String columns[] = {"name", "phone_num", "email", "pword", "screen_name"};
	    return columns;
	}

	return null;
    }
}
