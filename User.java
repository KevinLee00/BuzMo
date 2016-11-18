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

}
