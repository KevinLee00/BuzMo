import java.lang.Character;
import java.util.ArrayList;
import java.sql.*;


// "CREATE TABLE ChatGroups(
// 				group_id INT,
//				name VARCHAR(20) UNIQUE, 
// 				duration INT DEFAULT 7, 
// 				owner CHAR(20) NOT NULL, 
// 				PRIMARY KEY(group_id), 
// 				FOREIGN KEY(owner) REFERENCES Users(email))";
// "CREATE TABLE ChatGroupRequests(
// 				email1 CHAR(20), 
// 				email2 CHAR(20),
// 				group_id INT, 
// 				primary key(email1, email2, group_id), 
// 				FOREIGN KEY(email1) REFERENCES USERS(email), 
// 				FOREIGN KEY(email2) REFERENCES Users(email), 
// 				FOREIGN KEY(group_id) REFERENCES ChatGroups(group_id))";
// "CREATE TABLE ChatGroupMembers(
// 				group_id INT,
// 				member CHAR(20), 
// 				PRIMARY KEY(group_id, member), 
// 				FOREIGN KEY(group_id) REFERENCES ChatGroups(group_id), 
// 				FOREIGN KEY(member) REFERENCES Users(email))";



public class ChatGroups {
	
	public static void CreateChatGroup() {
		// name, duration, owner
		String prompts[] = {"Group name", "Duration"};
		String answers[] = Menu.PromptUser(prompts);
		Boolean isValidGroupName = true;
		for (int i=0 ; i<answers[0].length(); i++) {
			char c = answers[0].charAt(i);
			if (!Character.isLetterOrDigit(c) && !Character.isWhitespace(c)) {
				isValidGroupName = false;
			}
		}
		if (isValidGroupName && answers[0].length() <= 20) {
			int uniqueGroupId = GetUniqueGroupID();
			String group_id = Integer.toString(uniqueGroupId);

			String sql = "INSERT INTO ChatGroups VALUES('" + uniqueGroupId + "', '" + answers[0] + "', '" + answers[1] + "', '" + User.getEmail() + "')";
			SQLHelper.ExecuteSQL(sql);
			SQLHelper.Close();

			sql = "INSERT INTO ChatGroupMembers VALUES ('" + uniqueGroupId + "', '" + User.getEmail() + "')";
			SQLHelper.ExecuteSQL(sql);
			SQLHelper.Close();

		} 
		else {
			System.out.println("Invalid Group Name");
		}

		HomeScreen.UserOptions();
	}

	public static void ManageCurrentChatGroups() {
		String sql = "SELECT * FROM ChatGroups WHERE owner = '" + User.getEmail() + "'";
		ResultSet rs = SQLHelper.ExecuteSQL(sql);
		ArrayList<String> groupChatNames = new ArrayList<String>();
		ArrayList<String> groupChatDurations = new ArrayList<String>();
		ArrayList<String> groupChatFullInfo = new ArrayList<String>();
		try {
			while(rs.next()) {
				groupChatNames.add(rs.getString(2));
				groupChatDurations.add(rs.getString(3));
				groupChatFullInfo.add(rs.getString(2) + " | DURATION: " + rs.getString(3) + " days");
			}
		}
		catch(Exception e){
			System.out.println(e);
		}

		if (groupChatNames.size() == 0) {
			System.out.println("You are not the owner of any group chats.");
			return;
		}

		int chatGroupAnswer = Menu.DisplayMenu("You are the owner of the following group chats. Which would you like to edit?", groupChatFullInfo.toArray(new String[0]));
		if (chatGroupAnswer > 0 && chatGroupAnswer <= groupChatFullInfo.size()) {
			String options[] = {"Change the name", "Change the duration", "Add a friend to the group", "Remove user from chat group." ,"Nothing"};
			int optionAnswer = Menu.DisplayMenu("What would you like to do to '" + groupChatNames.get(chatGroupAnswer-1) + "'", options);
			
			if (optionAnswer == 1) {
				String prompts[] = {"What would you like the new name to be?"};
				String newName[] = Menu.PromptUser(prompts);
				sql = "UPDATE ChatGroups SET name = '" + newName[0] + "' WHERE name = '" + groupChatNames.get(chatGroupAnswer-1) + "'";
				SQLHelper.ExecuteSQL(sql);
				SQLHelper.Close();
			}
			
			else if (optionAnswer == 2) {
				String prompts[] = {"What would you like the new duration to be?"};
				String newDuration[] = Menu.PromptUser(prompts);
				sql = "UPDATE ChatGroups SET duration = '" + newDuration[0] + "' WHERE duration = '" + groupChatDurations.get(chatGroupAnswer-1) + "'";
				SQLHelper.ExecuteSQL(sql);
				SQLHelper.Close();
			}
			
			else if (optionAnswer == 3) {
				String friends[] = MyCircle.GetFriends(User.getEmail());
				int answer = Menu.DisplayMenu("Who would you like to invite to the chat group?" , friends);
				String group_id = GroupIdGivenName(groupChatNames.get(chatGroupAnswer-1));
				sql = "INSERT INTO ChatGroupRequests VALUES('" + User.getEmail() + "', '" + friends[answer-1] + "', '" + group_id + "')";
				SQLHelper.ExecuteSQL(sql);
				System.out.println("Chat Group Invite Sent!");
				SQLHelper.Close();
			}

			else if (optionAnswer == 4) {
				// REMOVE USER
			}
			
			else if (optionAnswer == 5) { 
				return; 
			}
		}
		else {
			System.out.println("ERROR: Invalid input");
		}	
	}

	public static ArrayList<String> getGroups(String email) {
		ArrayList<String> groupChats = new ArrayList<String>();
		String sql = "SELECT G.name FROM ChatGroupMembers M, ChatGroups G WHERE member = '" + email + "' AND M.group_id = G.group_id";
		// System.out.println(sql);
		try {	
			ResultSet rs = SQLHelper.ExecuteSQL(sql);
			while(rs.next()) {
				groupChats.add(rs.getString(1));
			}
			SQLHelper.Close();
		} catch(Exception e) { System.out.println(e); }
		return groupChats;
	}


    public static int GetUniqueGroupID() {
    	String sql = "SELECT MAX(group_id) FROM ChatGroups";
    	ResultSet rs = SQLHelper.ExecuteSQL(sql);
    	int id = 0;
    	try {
    		while (rs.next()) {
    			id = rs.getInt(1) + 1;
    		}
    	} 
    	catch (Exception e) {
    		System.out.println(e);
    	}
    	SQLHelper.Close();
    	return id;
    }

    public static String GroupIdGivenName (String group_name) {
    	String sql = "SELECT group_id FROM ChatGroups WHERE name = '" + group_name + "'";
    	String id = null;
    	ResultSet rs = SQLHelper.ExecuteSQL(sql);
    	try {
    		while(rs.next()) {
    			id = rs.getString(1);
    		}
    	} 
    	catch (Exception e) {
    		System.out.println(e);
    	}
    	return id;
    }

    public static void CheckForChatGroupRequests() {

    	String sql = "SELECT G.name, R.email1, R.group_id FROM ChatGroupRequests R, ChatGroups G WHERE R.email2 = '" + User.getEmail() + "' AND " + "R.group_id = G.group_id";
    	ResultSet rs = SQLHelper.ExecuteSQL(sql);
    	ArrayList<String> groupNames = new ArrayList<String>();
    	ArrayList<String> inviters = new ArrayList<String>();
    	ArrayList<String> groupIds = new ArrayList<String>();
    	try {
    		while(rs.next()) {
    			groupNames.add(rs.getString(1));
    			inviters.add(rs.getString(2));
    			groupIds.add(rs.getString(3));
    		}
    	} catch(Exception e) {System.out.println(e);}
    	SQLHelper.Close();

    	if (inviters.size() == 0)
    		return;

    	String items[] = {"Yes", "No"};
    	System.out.println("You have " + inviters.size() + " chat group invitation(s)!");
    	for (int i = 0; i<inviters.size(); i++) {
    		int answer = Menu.DisplayMenu(inviters.get(i).trim() + " invited you to " + groupNames.get(i) + ". Would you like to accept the invitation?", items);
    		if (answer == 1) {
    			sql = "INSERT INTO ChatGroupMembers VALUES('" + groupIds.get(i) + "', '" + User.getEmail() + "')";
    			SQLHelper.ExecuteSQL(sql);
    			SQLHelper.Close();
    			
    			sql = "DELETE FROM ChatGroupRequests WHERE email1 = '" + inviters.get(i) + "' AND email2 = '" + User.getEmail() + "' AND group_id = '" + groupIds.get(i) + "'";
				SQLHelper.ExecuteSQL(sql);
				SQLHelper.Close();    			
    			
    			System.out.println("You have joined " + groupNames.get(i) + ".");
    		}
    	}
    }

    public static void ViewChatGroupMessages() {
    	ArrayList<String> chatGroups = getGroups(User.getEmail());
    	int answer = Menu.DisplayMenu("Which chat group's messages do you want to view?", chatGroups);
    	
    }
}
