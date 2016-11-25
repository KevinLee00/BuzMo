import java.sql.*;

public class User {
    private static String name, phone_num, email, pword, screen_name; // Used to login
    private static int is_manager;

    public static void setInfo(String name, String phone_num, String email, String pword, String screen_name, int is_manager) {
	User.name = name;
	User.phone_num = phone_num;
	User.email = email;
	User.pword = pword;
	User.screen_name = screen_name;
	User.is_manager = is_manager;

    }
    
    public static String getEmail() {
	return email;
    }

    public static String getName() {
	return name;
    }

    public static String getScreenName() {
	return screen_name;
    }


    public static String NameGivenEmail(String email) {
	String sql = "SELECT name FROM Users WHERE email = '" + email + "'";
	ResultSet rs = SQLHelper.ExecuteSQL(sql);
	String name = null;
	try {
	    while (rs.next()) {
		name = rs.getString(1).trim();
	    }
	} catch (Exception e) {System.out.println(e);}


	SQLHelper.Close();

	return name;

    }


    public static String ScreenNameGivenEmail(String email) {
	String sql = "SELECT screen_name FROM Users WHERE email = '" + email + "'";
	ResultSet rs = SQLHelper.ExecuteSQL(sql);
	String screen_name = null;
	try {
	    while (rs.next()) {
		screen_name = rs.getString(1).trim();
	    }
	} catch (Exception e) {System.out.println(e);}


	SQLHelper.Close();

	return screen_name;

    }
    

}
