import java.util.*;
import java.sql.*;


public class Messages {
    public static final int PRIVATE_MSG = 0;
    public static final int CIRCLE_MSG = 1;
    public static final int CHATGRP_MSG = 2;
    public static final int CIRCLE_MSG_PUB = 3;

    
    public static class MSG {
	public String text, id, time, sender, receiver, owner;
	public int type;

	public MSG(String id, String text, String time, String sender, String receiver, String owner, int type) {
	    this.text = text;
	    this.id = id;
	    this.sender = sender;
	    this.receiver = receiver;
	    this.owner = owner;
	    this.type = type;
	    this.time = time;
	}
    }

    public static void ComposeMessage() {
	String text[] = Menu.PromptUser(Menu.MSG_PROMPT);

	String[] items = {"Private", "MyCirlce", "Chatgroup"};
	int answer = Menu.DisplayMenu("What kind of message is this?", items);

	// private message
	if (answer == 1) {
	    String friends[] = MyCircle.GetFriends(User.getEmail());
	    String temp[] = new String[friends.length];
	    
	    for (int i = 0; i < friends.length; i++) {
		temp[i] = User.NameGivenEmail(friends[i]);
	    }
	    
	    
	    answer = Menu.DisplayMenu("Pick a friend to send your message to", temp);
	    SendPrivateMessage(text[0], friends[answer-1]);
	} else if (answer == 2) { // mycircle message
	    String prompt[] = {"Please provide topic words separated by spaces"};
	    String twords[] = Menu.PromptUser(prompt)[0].split(" "); 

	    int ispublic = Menu.DisplayMenu("Should this post be public?", Menu.YesNo);

	    if (ispublic == 1) {
		PostMessageToCircle(text[0], null, twords);
	    } else {
		String friends[] = MyCircle.GetFriends(User.getEmail());
		for (int i = 0; i < friends.length; i++) {
		    System.out.println(i + 1 + ". " + friends[i]);
		}

		String temp[] = {"Which friends should this message be sent to?"};
		String nums[] = Menu.PromptUser(temp)[0].split(" ");
		String receivers[] = new String[nums.length];
		for (int i = 0; i < nums.length; i++) {
		    receivers[i] = friends[Integer.parseInt(nums[i])-1];
		}
		
		PostMessageToCircle(text[0], receivers, twords);
	    }
	    
	} else if (answer == 3) { //ChatGroup message
		ArrayList<String> groups = ChatGroups.getGroups(User.getEmail());
		if (groups.isEmpty()) {
			System.out.println("You are not a member of any group chats.");
		} 
		else {
			int response = Menu.DisplayMenu("Pick a group chat to send a message to.", groups.toArray(new String[0]));
			SendChatGroupMessage(text[0], groups.get(response-1));
			// id, text, timestamp, sender, owner, type

		} 
	}
	
    }

    public static void SendChatGroupMessage(String text, String chatGroup) {
	// First find the members of the chat group
		// ArrayList<String> members = new ArrayList<String>();
		// String sql = "SELECT M.member FROM ChatGroups G, ChatGroupMembers M WHERE G.group_id = M.group_id AND G.name = '" + chatGroup + "'";
		// ResultSet rs = SQLHelper.ExecuteSQL(sql);
		// try {
		// 	members.add(rs.getString(1));
		// }
		// catch(Exception e) { 
		// 	System.out.println(e); 
		// }
		// SQLHelper.Close();

		// Add the message to the Messages table
		int messageId = GetUniqueMessageID();
		Timestamp time = new Timestamp(Calendar.getInstance().getTime().getTime());
		int type = CHATGRP_MSG;
		String sql = "INSERT INTO Messages VALUES('" + messageId + "', '" + text  + "', to_timestamp('" + time + "', 'YYYY-MM-DD HH24:MI:SS.FF'), '" + User.getEmail() + "', '" + User.getEmail() + "', '" + type + "')";
		System.out.println(sql);
		SQLHelper.ExecuteSQL(sql);
		SQLHelper.Close();

		

		// Add the message to ChatGroupMessages table (not MessageRecievers)
		String groupName = ChatGroups.GroupIdGivenName(chatGroup);
		sql = "INSERT INTO ChatGroupMessages VALUES('" + messageId + "', '" + User.getEmail() + "', '" + groupName + "')";
		System.out.println(sql);
		SQLHelper.ExecuteSQL(sql);
		SQLHelper.Close();
		
    }


    public static void CheckInbox() {
	/*
	String s = "DROP TABLE MessageReceivers";
	SQLHelper.ExecuteSQL(s);
	SQLHelper.Close();
	s = "CREATE TABLE MessageReceivers (id INTEGER NOT NULL, email CHAR(20) REFERENCES Users(email), PRIMARY KEY(id, email))";
	SQLHelper.ExecuteSQL(s);
	SQLHelper.Close();
	*/
	/*
	String s = "DELETE FROM Messages WHERE 1=1";
	SQLHelper.ExecuteSQL(s);
	SQLHelper.Close();

	s = "DELETE FROM MessageReceivers WHERE 1=1";
	SQLHelper.ExecuteSQL(s);
	SQLHelper.Close();
	*/
	String sql = "SELECT * FROM Messages M WHERE (M.type = 0 AND M.id IN (SELECT id FROM MessageReceivers MR WHERE MR.email = '" + User.getEmail() + "') AND M.owner = '" + User.getEmail() + "')";
	
	sql += " UNION SELECT * FROM Messages M Where (M.type = 1 AND M.id IN (SELECT id FROM MessageReceivers MR WHERE MR.email = '" + User.getEmail() + "')) OR M.type = 3";
	
	ArrayList<MSG> messages = new ArrayList<MSG>();
	
	ResultSet rs = SQLHelper.ExecuteSQL(sql);
	try {
	    while (rs.next()) {
		messages.add(new MSG(rs.getString(1).trim(), rs.getString(2).trim(), rs.getString(3).trim(), rs.getString(4).trim(), null, rs.getString(5).trim(), rs.getInt(6)));
	    }
	} catch (Exception e) {System.out.println(e);}

	SQLHelper.Close();
	
	if (messages.size() == 0) {
	    System.out.println("No messages");
	    return;
	}

	System.out.println("You have " + messages.size() + " message(s)");

	String names[] = new String[messages.size()];
	for (int i = 0; i < messages.size(); i++) {
	    MSG m = messages.get(i);
	    /*
	    sql = "SELECT email FROM MessageReceivers WHERE id = " + m.id;
	    rs = SQLHelper.ExecuteSQL(sql);

	    try {
		if (rs.next())
		    m.receiver = rs.getString(1).trim();
	    } catch (Exception e) {System.out.println(e);}


	    SQLHelper.Close();

	    */
	    
	    if (m.type == PRIVATE_MSG)
		names[i] = "From: " + User.NameGivenEmail(m.sender) + " [Private]";
	    else if (m.type == CIRCLE_MSG || m.type == CIRCLE_MSG_PUB)
		names[i] = "From: " + User.NameGivenEmail(m.sender) + " [Circle]";
	    
	}


	int answer = Menu.DisplayMenu("Which message would you like to read?", names);
	MSG m = messages.get(answer-1);
	PrintMessage(m);
	
	String message_id = m.id;
	String owner = m.owner;
	sql = "UPDATE Messages SET views = views+1 WHERE id = " + m.id + " AND owner = '" + m.owner + "'";
	SQLHelper.ExecuteSQL(sql);
	SQLHelper.Close();
	
	if (m.owner.equals(User.getEmail())) {
	    answer = Menu.DisplayMenu("Delete message?", Menu.YesNo);
	    if (answer == 1) {
		sql = "DELETE FROM Messages WHERE id = " + m.id + " AND owner = '" + m.owner + "'";
		SQLHelper.ExecuteSQL(sql);
		SQLHelper.Close();

		sql = "DELETE FROM TopicWordsM WHERE id = " + m.id;
		SQLHelper.ExecuteSQL(sql);
		SQLHelper.Close();

		sql = "SELECT * FROM Messages WHERE id = " + m.id;
		rs = SQLHelper.ExecuteSQL(sql);
		try {
		    if (!rs.next()) {
			sql = "DELETE FROM MessageReceivers WHERE id = " + m.id;
			SQLHelper.Close();
			SQLHelper.ExecuteSQL(sql);
		    }

		} catch (Exception e) {System.out.println(e);}

		SQLHelper.Close();
		
		System.out.println("Message deleted");
	    }
	}
    }

    public static void ViewSentMessages() {
	String sql = "SELECT * FROM Messages WHERE sender = '" + User.getEmail() + "' AND owner = '" + User.getEmail() + "'";
	ArrayList<MSG> messages = new ArrayList<MSG>();
	ResultSet rs = SQLHelper.ExecuteSQL(sql);
	
	try {
	    while (rs.next()) {
		messages.add(new MSG(rs.getString(1).trim(), rs.getString(2).trim(), rs.getString(3).trim(), User.getEmail(), null, User.getEmail(), rs.getInt(6)));
		
	    }
	} catch (Exception e) {System.out.println(e);}
	
	
	SQLHelper.Close();
	
	if (messages.size() == 0) {
	    System.out.println("No sent messages");
	    return;
	}
	
	String names[] = new String[messages.size()];

	for (int i = 0; i < messages.size(); i++) {
	    MSG m = messages.get(i);
	    
	    sql = "SELECT email FROM MessageReceivers WHERE id = " + m.id;
	    names[i] = "";
	    
	    //sql = "DELETE FROM Messages WHERE id = " + m.id;
	    //SQLHelper.ExecuteSQL(sql);
	    //System.exit(0);
	    
	    rs = SQLHelper.ExecuteSQL(sql);
	    try {
		while (rs.next()) {
		    names[i] += rs.getString(1).trim() + " ";
		}
	    } catch (Exception e) {System.out.println(e);}

	    SQLHelper.Close();
	    

	   	    
	    if (m.type == PRIVATE_MSG)
		names[i] += " [PRIVATE]";
	    else if (m.type == CIRCLE_MSG || m.type == CIRCLE_MSG_PUB) {
		names[i] += " [Circle]";
	    }
	}

	int answer = Menu.DisplayMenu("Which messages would you like to view?", names);
	MSG m = messages.get(answer-1);
	PrintMessage(m);

	if (m.owner.equals(User.getEmail())) {
	    answer = Menu.DisplayMenu("Would you like to delete this message?", Menu.YesNo);
	    if (answer == 1) {
		sql = "DELETE FROM Messages WHERE id = " + m.id + " AND owner = '" + m.owner + "'";
		SQLHelper.ExecuteSQL(sql);
		SQLHelper.Close();

		sql = "DELETE FROM TopicWordsM WHERE id = " + m.id;
		SQLHelper.ExecuteSQL(sql);
		SQLHelper.Close();

		sql = "SELECT * FROM Messages WHERE id = " + m.id;
		rs = SQLHelper.ExecuteSQL(sql);
		try {
		    if (!rs.next()) {
			sql = "DELETE FROM MessageReceivers WHERE id = " + m.id;
			SQLHelper.Close();
			SQLHelper.ExecuteSQL(sql);
		    }

		} catch (Exception e) {System.out.println(e);}

		SQLHelper.Close();

		System.out.println("Message Deleted");

	    }
	}
	
	
    }
    
    public static void SendPrivateMessage(String text, String receiver) {
        // message_id, text, timestamp, sender, owner, message_type
	String sql;
	/*sql = "DROP TABLE Messages";
	SQLHelper.ExecuteSQL(sql);
	SQLHelper.Close();
	
	sql = "CREATE TABLE Messages(id INTEGER NOT NULL, text CHAR(1400) NOT NULL, timestamp TIMESTAMP NOT NULL, sender CHAR(20), owner CHAR(20), type INTEGER NOT NULL, views INT, FOREIGN KEY(sender) REFERENCES Users(email), FOREIGN KEY(owner) REFERENCES Users(email), PRIMARY KEY(id, owner))";

	SQLHelper.ExecuteSQL(sql);
	SQLHelper.Close();
	*/
	

	int id = GetUniqueMessageID();
	
	Timestamp time = new Timestamp(Calendar.getInstance().getTime().getTime());

	sql = "INSERT INTO Messages VALUES (" + id + ", '" + text + "', to_timestamp('" + time + "', 'YYYY-MM-DD HH24:MI:SS.FF'), '" + User.getEmail() + "', '" + User.getEmail() + "', " + PRIVATE_MSG + ", '0')";
	SQLHelper.ExecuteSQL(sql);
	SQLHelper.Close();


	sql =  "INSERT INTO Messages VALUES (" + id + ", '" + text + "', to_timestamp('" + time + "', 'YYYY-MM-DD HH24:MI:SS.FF'), '" + User.getEmail() + "', '" + receiver + "', " + PRIVATE_MSG + ", '0')";
	SQLHelper.ExecuteSQL(sql);
	SQLHelper.Close();

	sql = "INSERT INTO MessageReceivers VALUES (" + id + ", '" + receiver + "')";
	SQLHelper.ExecuteSQL(sql);
	SQLHelper.Close();
	
	System.out.println("Message sent!");
	
    }

    
    public static void PostMessageToCircle(String text, String[] receivers, String[] twords) {	
	/*
	String s = "CREATE TABLE TopicWordsM (word CHAR(20) NOT NULL, id INTEGER NOT NULL, PRIMARY KEY(word, id))";
	SQLHelper.ExecuteSQL(s);
	SQLHelper.Close();
	*/

	
	
	int id = GetUniqueMessageID();

	Timestamp time = new Timestamp(Calendar.getInstance().getTime().getTime());

	int type = receivers == null ? CIRCLE_MSG_PUB : CIRCLE_MSG;
	
	String sql = "INSERT INTO Messages VALUES (" + id + ", '" + text + "', to_timestamp('" + time + "', 'YYYY-MM-DD HH24:MI:SS.FF'), '" + User.getEmail() + "', '" + User.getEmail() + "', " + type + ", '0')";
	SQLHelper.ExecuteSQL(sql);
	SQLHelper.Close();
	
	ResultSet rs;	
	ArrayList<String> friends = new ArrayList<String>();
	if (receivers == null) {
	    sql = "SELECT email FROM Users WHERE email <> '" + User.getEmail() + "'";
	    rs = SQLHelper.ExecuteSQL(sql);
	    try {
		while (rs.next()) {
		    friends.add(rs.getString(1).trim());
		}
	    } catch (Exception e) {System.out.println(e);}

	} else {
	    for (int i = 0; i < receivers.length; i++) {
		if (!friends.contains(receivers[i]))
		    friends.add(receivers[i]);
		String[] temp = MyCircle.GetFriends(receivers[i]);
		for (int j = 0; j < temp.length; j++) {
		    if (!friends.contains(temp[j]))
			friends.add(temp[j]);
		}
	    }
	}
	
	
	for (int i = 0; i < friends.size(); i++) {
	    sql = "INSERT INTO MessageReceivers VALUES (" + id + ", '" + friends.get(i) + "')";
	    SQLHelper.ExecuteSQL(sql);
	    SQLHelper.Close();
	    
	}


	for (int i = 0; i < twords.length; i++) {
	    sql = "INSERT INTO TopicWordsM VALUES ('" + twords[i] + "', " + id + ")";
	    SQLHelper.ExecuteSQL(sql);
	    SQLHelper.Close();
	}
	

	for (int i = 0; i < friends.size(); i++) {
	    System.out.println(friends.get(i));
	}
	
	/*String sql = "ALTER TABLE MessageReceivers ADD primary INTEGER";
	SQLHelper.ExecuteSQL(sql);
	SQLHelper.Close();*/
	
    }

    private static int GetUniqueMessageID() {
	String sql = "SELECT Max(id) FROM Messages";
        ResultSet rs = SQLHelper.ExecuteSQL(sql);
        int id = 0; // the id will be the max of the current ids plus 1"
	    try {
		while (rs.next()) {
		    id = rs.getInt(1) + 1;
		}
	    } catch (Exception e) {System.out.println(e);}

	SQLHelper.Close();

	return id;
	
    }



    public static void PrintMessage(MSG m) {
	ArrayList<String> twords = new ArrayList<String>();
	String sql = "SELECT word FROM TopicWordsM WHERE id = " + m.id;
	ResultSet rs = SQLHelper.ExecuteSQL(sql);
	try {
	    while (rs.next()) {
		twords.add(rs.getString(1).trim());
	    }
	} catch (Exception e) {System.out.println(e);}

	
	String msg = "------------Displaying Message------------\nMessage ID: " + m.id + "\nTimestamp: " + m.time + "\n\n" + m.text + "\n";

	if (twords.size() != 0) {
	    msg += "Topic Words: ";
	    for (int i = 0; i < twords.size(); i++) {
		msg += twords.get(i) + " ";
	    }
	}

	msg += "\n------------------------------------";


	System.out.println(msg);
    }
}
    
   
