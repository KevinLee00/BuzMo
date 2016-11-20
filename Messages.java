public class Messages(String[] args) {
    

    public static void sendPrivateMessageToUser(String text, String recipientEmail) {
        // message_id, text, timestamp, owner, sender, receiver, message_type
        int message_id = SQLHelper.ExecuteSQL("SELECT count(*) FROM Messages").getInt(1) + 1;
        SQLHelper.close();
        String timestamp = String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        String senderEmail = User.getEmail();

        // Two one where the sender is the owner and one where the receiver is the owner
        String[] messageCopy1 = [message_id, text, timestamp, senderEmail, senderEmail, recipientEmail, 0];
        String[] messageCopy2 = [message_id, text, timestamp, recipientEmail, senderEmail, recipientEmail, 0];
        SQLHelper.Insert("Messages", messageCopy1);
        SQLHelper.Insert("Messages", messageCopy2);
    }
    
    public static void displayPrivateMessages() {
        ArrayList<String> friendsList = new ArrayList<String>();
        ArrayList<String> friendsEmailList = new ArrayList<String>();
        ResultSet rs = SQLHelper.ExecuteSQL("SELECT U.name, U.screen_name, U.email FROM Users U");
        while (rs.next) {
            friendsList.add(rs.getString(1));
            friendsEmailList.add(rs.getSTring(3));
        }
        int answer = Menu.DisplayMenu(friendsList.toArray());
        Messages.getPrivateMessagesFrom(friendsEmailList[answer-1]);

    }

    public static void getPrivateMessagesFrom(String senderEmail) {
        ResultSet rs = SQLHelper.ExecuteSQL("SELECT M.text, M.timestamp, M.sender FROM Messages M WHERE message_type = 0 AND recipient = " + User.getEmail(); + " AND sender = " + senderEmail);
        while(rs.next()) {
            System.out.println( currentMessages.getString(1).trim() 
                                + "at " 
                                + currentMessages.getString(2).trim() 
                                + "from " 
                                + currentMessages.getString(3).trim()
                                );
        }
        SQLHelper.close();
    }
}
