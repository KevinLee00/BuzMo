import java.sql.*;
import java.util.*;

public class MyCircle {

    public static void SendFriendRequest(String name1, String email1, String email2) {

	/*
	String sql = "CREATE TABLE FriendRequests (name1 CHAR(20) NOT NULL, email1 CHAR(20), email2 CHAR(20) NOT NULL, FOREIGN KEY(email1) REFERENCES Users(email), PRIMARY KEY(email1))";
	SQLHelper.ExecuteSQL(sql);
	SQLHelper.Close();
	*/


	if (AreFriends(email1, email2)) {
	    System.out.println("You are already friends with this user");
	    return;
	}
	
	String sql = "INSERT INTO FriendRequests VALUES ('" + name1 + "', '" + email1 + "', '" + email2 + "')";

	SQLHelper.ExecuteSQL(sql);
	SQLHelper.Close();

	System.out.println("Friend request sent");
	
    }

    public static void CheckForFriendRequests() {
	String sql = "SELECT name1, email1 from FriendRequests WHERE email2 = '" + User.getEmail() + "'";
	ResultSet rs = SQLHelper.ExecuteSQL(sql);

	ArrayList<String> names = new ArrayList<String>();
	ArrayList<String> emails = new ArrayList<String>();
	
	try {
	    while (rs.next()) {
		names.add(rs.getString(1).trim());
		emails.add(rs.getString(2).trim());
	    }
	} catch (Exception e) {System.out.println(e);}


	SQLHelper.Close();
	
	if (names.size() == 0)
	    return;


	String items[] = {"Yes", "No"};
	System.out.println("You have " + names.size() + " new friend request(s)!");
	for (int i = 0; i < names.size(); i++) {
	    int answer = Menu.DisplayMenu("Would you like to add " + names.get(i) + " as a friend?", items);
	    if (answer == 1) {
		sql = "INSERT INTO Friends VALUES ('" + User.getEmail() + "', '" + emails.get(i) + "')";
		SQLHelper.ExecuteSQL(sql);
		SQLHelper.Close();
		sql = "INSERT INTO Friends VALUES ('" + emails.get(i) + "', '" + User.getEmail() + "')";
		//sql = "CREATE TABLE Friends (email1 CHAR(20), email2 CHAR(20), FOREIGN KEY(email1) REFERENCES Users(email), FOREIGN KEY(email2) REFERENCES Users(email), PRIMARY KEY(email1, email2))";
		SQLHelper.ExecuteSQL(sql);
		SQLHelper.Close();

		System.out.println("Added " + names.get(i) + " as a friend");
	    }
	}

	sql = "DELETE FROM FriendRequests WHERE email2 = '" + User.getEmail() + "'";
	SQLHelper.ExecuteSQL(sql);
	SQLHelper.Close();
    }

    public static boolean AreFriends(String email1, String email2) {
	String sql = "SELECT * FROM Friends WHERE email1 = '" + email1 + "' AND email2 = '" + email2 + "'";
	ResultSet rs = SQLHelper.ExecuteSQL(sql);
	boolean exists = false;
	try {
	    exists = rs.next();
	} catch (Exception e) {System.out.println(e);}

	SQLHelper.Close();
	
	return exists;
    }


    // returns the email addresses of all friends of the specified user
    public static String[] GetFriends(String email) {
	String sql = "SELECT email2 FROM Friends WHERE email1 = '" + email + "'";
	ArrayList<String> friends = new ArrayList<String>();
	ResultSet rs = SQLHelper.ExecuteSQL(sql);
	
	try {
	    while (rs.next()) {
		friends.add(rs.getString(1).trim());
	    }
	} catch (Exception e) {System.out.println(e);}

	
	SQLHelper.Close();

	String temp[] = new String[friends.size()];

	for (int i = 0; i < friends.size(); i++) {
	    temp[i] = friends.get(i);
	}

	return temp;
    }

    

}
