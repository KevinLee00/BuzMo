public class Messages(String[] args) {
    

    public static sendMessageToUser(String text, String recipientEmail) {
        // message_id, text, Timestamp, Sender, Reciever, MessageType
        int message_id = Integer.ParseInt(SQLHelper.ExecuteSQL("SELECT count(*) FROM Messages")) + 1;
        String timestamp = String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        String senderEmail = User.getEmail();
        String recipientEmail;

        String[] values = [message_id, text, timestamp, senderEmail, recipientEmail, 0];
        SQLHelper.Insert("Messages", values);
    }
    
    public static displayListOfFriends

    public static getCurrentMessagesFrom(String senderEmail) {
        ResultSet currentMessagesRS = SQLHelper.ExecuteSQL("SELECT text, timestamp, sender FROM Messages WHERE message_type = 0 AND recipient = " + User.getEmail(); + " AND sender = " + senderEmail);
        while(currentMessagesRS.next()) {
            System.out.println(currentMessages.getString(1) + "At " + currentMessages.getString(2) + "From " + currentMessages.getString(3));
  
        }
        SQLHelper.close();
    }
}
