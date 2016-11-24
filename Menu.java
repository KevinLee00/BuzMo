import java.util.Scanner;

public class Menu {

    private static Scanner scanner = new Scanner(System.in);

    public static int DisplayMenu( String title, String[] items) {
	System.out.println("\n" + title);
	for (int i = 0; i < items.length; i++) {
	    System.out.println( (i + 1) + ". " + items[i] );
	}
	
	
	return Integer.parseInt(scanner.nextLine());
    }


    public static String[] PromptUser( String[] prompts ) {
		String answers[] = new String[prompts.length];
		for (int i = 0; i < prompts.length; i++) {
		    System.out.println( "Please enter: " + prompts[i] );
		    answers[i] = scanner.nextLine();
		}

		return answers;
		
	    }
}
