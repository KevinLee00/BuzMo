import java.sql.*;
import java.util.*;

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

    public static int isManager() {
        return is_manager;
    }

    
    public static void updateDatabaseTime() {
        // System.out.println("Please enter a new timestamp for the system. (ex. '2020-01-01 12:30:15.000')");
        String prompts[] = {"Please enter a new timestamp for the system. (ex. '2020-01-01 12:30:15.000')"};
        String answer[] = Menu.PromptUser(prompts);
        String sql = "UPDATE CurrentTS SET current_timestamp = (TO_TIMESTAMP('" + answer[0] + "', 'YYYY-MM--DD HH24:MI:SS.FF'))";
        SQLHelper.ExecuteSQL(sql);
        SQLHelper.Close();
    }
    public static String NameGivenEmail(String email) {
    String sql = "SELECT name, screen_name FROM Users WHERE email = '" + email + "'";
    ResultSet rs = SQLHelper.ExecuteSQL(sql);
    String screen_name = null;
    String name = null;
    try {
        while (rs.next()) {
        name = rs.getString(1);
        screen_name = rs.getString(2);
        }
    } catch (Exception e) {System.out.println(e);}


    SQLHelper.Close();

    if (screen_name != null)
        return screen_name.trim();

    return name.trim();

    }
    public static void EditTopicWords() {
	/*String s = "CREATE TABLE TopicWordsU (word CHAR(20), email CHAR(20) REFERENCES Users(email), PRIMARY KEY(word, email))";
	SQLHelper.ExecuteSQL(s);
	SQLHelper.Close();
	*/
	
	String items[] = {"Add", "Delete"};
	int answer = Menu.DisplayMenu("Would you like to add or delete your topic words?", items);
	if (answer == 1) {
	    String prompts[] = {"Topic words"};
	    String words[] = Menu.PromptUser(prompts)[0].split(" ");

	    for (int i = 0; i < words.length; i++) {
		String sql = "INSERT INTO TopicWordsU VALUES ('" + words[i] + "', '" + User.getEmail() + "')";
		SQLHelper.ExecuteSQL(sql);
		SQLHelper.Close();
	    }

	    System.out.println("Topic words added!");
	} else {
	    String sql = "SELECT word FROM TopicWordsU WHERE email = '" + User.getEmail() + "'";
	    ArrayList<String> twords = new ArrayList<String>();
	    ResultSet rs = SQLHelper.ExecuteSQL(sql);

	    try {
		while (rs.next()) {
		    twords.add(rs.getString(1).trim());
		}

		
	    } catch (Exception e) {System.out.println(e);}

	    SQLHelper.Close();

	    if (twords.size() == 0) {
		System.out.println("No topic words to delete");
		return;
	    }
	    
	    String words[] = new String[twords.size()];
	    for (int i = 0; i < twords.size(); i++) {
		words[i] = twords.get(i);
	    }

	    answer = Menu.DisplayMenu("Which topic word would you like to delete?", words);

	    sql = "DELETE FROM TopicWordsU WHERE word = '" + words[answer-1] + "' AND email = '" + User.getEmail() + "'";
	    SQLHelper.ExecuteSQL(sql);
	    SQLHelper.Close();

	    System.out.println("Topic word deleted!");
	    
	}
    }
    public static void setNewManager() { // NOT TESTED YET
        ArrayList<String> userNames = new ArrayList<String>();
        ArrayList<String> userEmails = new ArrayList<String>();
        String sql = "SELECT * FROM Users WHERE isManager = '0'";
        ResultSet rs = SQLHelper.ExecuteSQL(sql);
        try {
            while (rs.next()) {
                userNames.add(rs.getString(1));
                userEmails.add(rs.getString(3));
            }
        } 
        catch (Exception e) {
            System.out.println(e);
        }

        SQLHelper.Close();

        if (userNames.size() == 0) {
            System.out.println("There are no users that can be promoted to manager.");
        }
        else {
            int answer = Menu.DisplayMenu("Who would you like to promote to manager?", userNames.toArray(new String[0]));
            sql = "UPDATE Users SET isManager = '1' WHERE email = '" + userEmails.get(answer-1) + "'";
            SQLHelper.ExecuteSQL(sql);
            SQLHelper.Close();
        }
    }

    public static void runAnalytics() {
        String items[] = {"Find the active users", "Find top messages", "Show number of inactive users", "Show complete report"};
        int answer = Menu.DisplayMenu("What would you like to do?", items);

        if (answer == 1) { //Find most active users
            findMostActiveUsers();
            // ArrayList<String> senders = new ArrayList<String>();
            // ArrayList<Integer> count = new ArrayList<Integer>();
            // String sql = "SELECT sender, AMT FROM (SELECT sender, COUNT(M.owner) as AMT FROM Messages M, CurrentTS T WHERE owner = sender AND M.timestamp > (T.current_timestamp - interval '7' day) GROUP BY M.sender ORDER BY AMT DESC) WHERE rownum <= 3 ORDER BY AMT DESC";

            // ResultSet rs = SQLHelper.ExecuteSQL(sql);
            // try {
            //     while (rs.next()) {
            //         senders.add(rs.getString(1));
            //         count.add(rs.getInt(2));
            //     }
            // } 
            // catch (Exception e) {
            //     System.out.println(e);
            // }
            // SQLHelper.Close();

            // for (int i=0; i<senders.size(); i++) {
            //     System.out.println(senders.get(i) + " with " + count.get(i) + " messages sent.");
            // }


        }

        else if (answer == 2) { //Find most viewed messages
            findTopMessages();
        //     String sql = "SELECT * FROM (SELECT * FROM (SELECT * FROM Messages M, CurrentTS T WHERE M.timestamp > (T.current_timestamp - interval '7' day) AND M.type = '3') ORDER BY views DESC) WHERE rownum <= 5 ORDER BY views";
        //     ResultSet rs = SQLHelper.ExecuteSQL(sql);
        //     ArrayList<String> output = new ArrayList<String>();
        //     ArrayList<String> timeStamp = new ArrayList<String>();
        //     ArrayList<String> views = new ArrayList<String>();
        //     try {
        //         while(rs.next()) {
        //             output.add(rs.getString(2).trim());
        //             timeStamp.add(rs.getString(3).trim());
        //             views.add(rs.getString(7).trim());
        //             // System.out.println(rs.getString(2).trim());
        //             // System.out.println("Posted On:" + rs.getString(3) + "With " + rs.getString(7) + " views.\n");
        //         }
        //     }
        //     catch (Exception e) {
        //         System.out.println(e);
        //     }
        //     SQLHelper.Close();

        //     for (int i=0; i<output.size(); i++) {
        //         System.out.println("'" + output.get(i) + "'");
        //         System.out.println("Posted on: " + timeStamp.get(i) + " with " + views.get(i) + " views.\n");
        //     }
        }

        else if (answer == 3) { // Find number of inactive users
            findNumOfInactiveUsers();
            // ArrayList<String> usersWithMessages = new ArrayList<String>();
            // ArrayList<Integer> numberOfMessages = new ArrayList<Integer>();
            // String sql = "Select sender, COUNT(Messages.sender) FROM Messages GROUP BY sender HAVING 7 > COUNT(Messages.sender)";
            // ResultSet rs = SQLHelper.ExecuteSQL(sql);
            // try {
            //     while (rs.next()) {
            //         usersWithMessages.add(rs.getString(1));
            //         numberOfMessages.add(rs.getInt(2));
            //     }
            // }


            // catch (Exception e) {
            //     System.out.println(e);
            // }
            // SQLHelper.Close();

            // ArrayList<String> usersWithPrivateMessages = new ArrayList<String>();
            // ArrayList<Integer> numberOfPrivateMessages = new ArrayList<Integer>();
            // sql = "Select sender, COUNT(Messages.sender) FROM Messages WHERE type = '0' GROUP BY sender";
            // rs = SQLHelper.ExecuteSQL(sql);
            // try {
            //     while (rs.next()) {
            //         usersWithPrivateMessages.add(rs.getString(1));
            //         numberOfPrivateMessages.add(rs.getInt(2));
            //     }
            // }
            // catch (Exception e) {
            //     System.out.println(e);
            // }
            // SQLHelper.Close();

            // int numInactiveUsers = 0;
            // sql = "SELECT COUNT(U.email) FROM Users U WHERE U.email NOT IN (SELECT sender FROM Messages)";
            // rs = SQLHelper.ExecuteSQL(sql);
            // try {
            //     while (rs.next()) {
            //         numInactiveUsers = rs.getInt(1);
            //     }
            // }
            // catch (Exception e) {
            //     System.out.println(e);
            // }
            // SQLHelper.Close();

            // // Check for inactive useres who don't post at all
            // for (int i=0; i<usersWithPrivateMessages.size(); i++) {
            //     for (int j=0; j<usersWithMessages.size(); j++) {
            //         if (usersWithPrivateMessages.get(i).equals(usersWithMessages.get(j))) {
            //             int messageAmount = numberOfMessages.get(j);
            //             int pMessageAmount = numberOfPrivateMessages.get(i);
            //             numberOfMessages.set(j, messageAmount - pMessageAmount/2);
            //         }
            //     }
            // }

            // //Check for inactive users who posted more 0 messages but less than 4
            // for (int i=0; i<usersWithMessages.size(); i++) {
            //     if (numberOfMessages.get(i) < 4) {
            //         numInactiveUsers++;
            //     }
            // }

            // System.out.println("There are " + numInactiveUsers + " inactive users on BuzMo.");

        }

        else if (answer == 4) {
            System.out.println("================== BUZMO REPORT ==================");
            numOfNewMessages();
            System.out.println("\n");
            findMostActiveUsers();
            System.out.println("\n");
            
            findTopMessages();
            System.out.println("\n");
            
            findNumOfInactiveUsers();
            System.out.println("================== END OF REPORT ======================");
        }
        else {
            System.out.println("Invalid response");
            
            if (User.isManager() == 1) {
                HomeScreen.ManagerOptions();
            }
            else {
                HomeScreen.UserOptions();
            }
        }
    }

    private static void findMostActiveUsers() {
        System.out.println("Most Active Users:");
        ArrayList<String> senders = new ArrayList<String>();
        ArrayList<Integer> count = new ArrayList<Integer>();
        String sql = "SELECT sender, AMT FROM (SELECT sender, COUNT(M.owner) as AMT FROM Messages M, CurrentTS T WHERE owner = sender AND M.timestamp > (T.current_timestamp - interval '7' day) GROUP BY M.sender ORDER BY AMT DESC) WHERE rownum <= 3 ORDER BY AMT DESC";

        ResultSet rs = SQLHelper.ExecuteSQL(sql);
        try {
            while (rs.next()) {
                senders.add(rs.getString(1));
                count.add(rs.getInt(2));
            }
        } 
        catch (Exception e) {
            System.out.println(e);
        }
        SQLHelper.Close();

        for (int i=0; i<senders.size(); i++) {
            System.out.println(senders.get(i) + " with " + count.get(i) + " messages sent.");
        }
    }

    private static void findTopMessages() {
        System.out.println("Top Messages:");
        String sql = "SELECT * FROM (SELECT * FROM (SELECT * FROM Messages M, CurrentTS T WHERE M.timestamp > (T.current_timestamp - interval '7' day) AND M.type = '3') ORDER BY views DESC) WHERE rownum <= 3 ORDER BY views DESC";
        ResultSet rs = SQLHelper.ExecuteSQL(sql);
        ArrayList<String> output = new ArrayList<String>();
        ArrayList<String> timeStamp = new ArrayList<String>();
        ArrayList<String> authors = new ArrayList<String>();
        ArrayList<String> views = new ArrayList<String>();
        try {
            while(rs.next()) {
                output.add(rs.getString(2).trim());
                timeStamp.add(rs.getString(3).trim());
                authors.add(rs.getString(5).trim());
                views.add(rs.getString(7).trim());
                // System.out.println(rs.getString(2).trim());
                // System.out.println("Posted On:" + rs.getString(3) + "With " + rs.getString(7) + " views.\n");
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
        SQLHelper.Close();

        for (int i=0; i<output.size(); i++) {
            System.out.println("'" + output.get(i) + "' by " + authors.get(i));
            System.out.println("Posted on: " + timeStamp.get(i) + " with " + views.get(i) + " views.\n");
        }
    }

    private static void findNumOfInactiveUsers() {
        System.out.println("Inactive Users:");
        ArrayList<String> usersWithMessages = new ArrayList<String>();
        ArrayList<Integer> numberOfMessages = new ArrayList<Integer>();
        String sql = "Select sender, COUNT(Messages.sender) FROM Messages GROUP BY sender HAVING 7 > COUNT(Messages.sender)";
        ResultSet rs = SQLHelper.ExecuteSQL(sql);
        try {
            while (rs.next()) {
                usersWithMessages.add(rs.getString(1));
                numberOfMessages.add(rs.getInt(2));
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
        SQLHelper.Close();

        ArrayList<String> usersWithPrivateMessages = new ArrayList<String>();
        ArrayList<Integer> numberOfPrivateMessages = new ArrayList<Integer>();
        sql = "Select sender, COUNT(Messages.sender) FROM Messages WHERE type = '0' GROUP BY sender";
        rs = SQLHelper.ExecuteSQL(sql);
        try {
            while (rs.next()) {
                usersWithPrivateMessages.add(rs.getString(1));
                numberOfPrivateMessages.add(rs.getInt(2));
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
        SQLHelper.Close();

        int numInactiveUsers = 0;
        sql = "SELECT COUNT(U.email) FROM Users U WHERE U.email NOT IN (SELECT sender FROM Messages)";
        rs = SQLHelper.ExecuteSQL(sql);
        try {
            while (rs.next()) {
                numInactiveUsers = rs.getInt(1);
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
        SQLHelper.Close();

        // Check for inactive useres who don't post at all
        for (int i=0; i<usersWithPrivateMessages.size(); i++) {
            for (int j=0; j<usersWithMessages.size(); j++) {
                if (usersWithPrivateMessages.get(i).equals(usersWithMessages.get(j))) {
                    int messageAmount = numberOfMessages.get(j);
                    int pMessageAmount = numberOfPrivateMessages.get(i);
                    numberOfMessages.set(j, messageAmount - pMessageAmount/2);
                }
            }
        }

        //Check for inactive users who posted more 0 messages but less than 4
        for (int i=0; i<usersWithMessages.size(); i++) {
            if (numberOfMessages.get(i) < 4) {
                numInactiveUsers++;
            }
        }

        System.out.println("There are " + numInactiveUsers + " inactive users on BuzMo.");
    }

    private static void numOfNewMessages() {
        System.out.println("Number of new messages in the last 7 days:");
        String sql = "SELECT COUNT(*) FROM Messages M, CurrentTS T WHERE sender = owner AND M.timestamp > (T.current_timestamp - interval '7' day)";
        int num = 0;
        ResultSet rs = SQLHelper.ExecuteSQL(sql);
        try {
            while(rs.next()) {
                num = rs.getInt(1);
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
        System.out.println("There have been " + num + " new messages in the past 7 days.");
    }
}
