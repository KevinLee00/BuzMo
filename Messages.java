import java.util.*;
import java.sql.*;

public class Messages {
    public static final int PRIVATE_MSG = 0;
    public static final int CIRCLE_MSG = 1;
    public static final int CHATGRP_MSG = 2;

    private static class MSG {
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
	    
	    String friends[] = MyCircle.GetFriends(User.getEmail());
	    String temp[] = new String[friends.length+1];

	    
	    
	    temp[0] = "MyCircle";
	    for (int i = 0; i < friends.length; i++) {
		temp[i+1] = User.NameGivenEmail(friends[i]);
		temp[i+1] += "'s circle";
	    }

	    answer = Menu.DisplayMenu("Who's circle would you like to post a message to?", temp);

	    if (answer == 1) { // self post
		
	    } else {
		PostMessageToCircle(text[0], friends[answer-2], twords);
	    }
	    
	}
	
    }

    public static void CheckInbox() {

	String sql = "SELECT * FROM Messages M WHERE (M.type = 0 AND M.id IN (SELECT id from MessageReceivers MR WHERE MR.email = '" + User.getEmail() + "') AND M.owner = '" + User.getEmail() + "')"; 
	
	ArrayList<MSG> messages = new ArrayList<MSG>();
	
	ResultSet rs = SQLHelper.ExecuteSQL(sql);
	try {
	    while (rs.next()) {
		messages.add(new MSG(rs.getString(1).trim(), rs.getString(2).trim(), rs.getString(3).trim(), rs.getString(4).trim(), User.getEmail(), rs.getString(5).trim(), rs.getInt(6)));
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
	    names[i] = User.NameGivenEmail(m.sender);
	    
	    names[i] = "From: " + names[i];
	    
	    if (m.type == PRIVATE_MSG)
		names[i] += " [PRIVATE]";
	    else if (m.type == CIRCLE_MSG)
		names[i] += " [" + User.NameGivenEmail(m.receiver) + "'s Circle]";
	}


	int answer = Menu.DisplayMenu("Which message would you like to read?", names);
	MSG m = messages.get(answer-1);
	PrintMessage(m);
	
	if (m.owner.equals(User.getEmail())) {
	    answer = Menu.DisplayMenu("Delete message?", Menu.YesNo);
	    if (answer == 1) {
		sql = "DELETE FROM Messages WHERE id = " + m.id + " AND owner = '" + m.owner + "'";
		SQLHelper.ExecuteSQL(sql);
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
	    names[i] = User.NameGivenEmail(m.receiver);
	   
	    names[i] = "To: " + names[i];

	    if (m.type == PRIVATE_MSG)
		names[i] += " [PRIVATE]";
	    
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
	    }
	}
	
	
    }
    
    public static void SendPrivateMessage(String text, String receiver) {
        // message_id, text, timestamp, sender, owner, message_type
	String sql;
	/*sql = "DROP TABLE Messages";
	SQLHelper.ExecuteSQL(sql);
	SQLHelper.Close();
	
	sql = "CREATE TABLE Messages(id INTEGER NOT NULL, text CHAR(1400) NOT NULL, timestamp TIMESTAMP NOT NULL, sender CHAR(20), owner CHAR(20), type INTEGER NOT NULL, FOREIGN KEY(sender) REFERENCES Users(email), FOREIGN KEY(owner) REFERENCES Users(email), PRIMARY KEY(id, owner))";

	SQLHelper.ExecuteSQL(sql);
	SQLHelper.Close();
	*/
	

	int id = GetUniqueMessageID();
	
	Timestamp time = new Timestamp(Calendar.getInstance().getTime().getTime());

	sql = "INSERT INTO Messages VALUES (" + id + ", '" + text + "', to_timestamp('" + time + "', 'YYYY-MM-DD HH24:MI:SS.FF'), '" + User.getEmail() + "', '" + User.getEmail() + "', " + PRIVATE_MSG + ")";
	SQLHelper.ExecuteSQL(sql);
	SQLHelper.Close();


	sql =  "INSERT INTO Messages VALUES (" + id + ", '" + text + "', to_timestamp('" + time + "', 'YYYY-MM-DD HH24:MI:SS.FF'), '" + User.getEmail() + "', '" + receiver + "', " + PRIVATE_MSG + ")";
	SQLHelper.ExecuteSQL(sql);
	SQLHelper.Close();

	sql = "INSERT INTO MessageReceivers VALUES (" + id + ", '" + receiver + "')";
	SQLHelper.ExecuteSQL(sql);
	SQLHelper.Close();
	
	System.out.println("Message sent!");
	
    }

    public static void PostMessageToMyCircle(String text, String[] receivers, int is_public) {
	
	
    }

    public static void PostMessageToCircle(String text, String receiver, String[] twords) {
	int id = GetUniqueMessageID();

	Timestamp time = new Timestamp(Calendar.getInstance().getTime().getTime());

	String friends[] = MyCircle.GetFriends(receiver);
	
		
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
	System.out.println("------------Displaying Message------------\nMessage ID: " + m.id + "\nTimestamp: " + m.time + "\n\n" + m.text + "\n---------------------------------------------\n");
    }
    
    public static void displayPrivateMessages() {
        /*ArrayList<String> friendsList = new ArrayList<String>();
        ArrayList<String> friendsEmailList = new ArrayList<String>();
        ResultSet rs = SQLHelper.ExecuteSQL("SELECT U.name, U.screen_name, U.email FROM Users U");
        while (rs.next) {
            friendsList.add(rs.getString(1));
            friendsEmailList.add(rs.getSTring(3));
        }
        int answer = Menu.DisplayMenu(friendsList.toArray());
        Messages.getPrivateMessagesFrom(friendsEmailList[answer-1]);*/

    }

    public static void getPrivateMessagesFrom(String senderEmail) {
        /*ResultSet rs = SQLHelper.ExecuteSQL("SELECT M.text, M.timestamp, M.sender FROM Messages M WHERE message_type = 0 AND recipient = " + User.getEmail() + " AND sender = " + senderEmail);
        while(rs.next()) {
            System.out.println( currentMessages.getString(1).trim() 
                                + "at " 
                                + currentMessages.getString(2).trim() 
                                + "from " 
                                + currentMessages.getString(3).trim()
                                );
        }
        SQLHelper.Close();*/
    }
}
