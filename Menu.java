import java.util.Scanner;

public class Menu {

    private static Scanner scanner = new Scanner(System.in);

    public static final String YesNo[] = {"Yes", "No"};
    public static final String MSG_PROMPT[] = {"Please enter your message"};

    public static int DisplayMenu( String title, String[] items) {
	System.out.println("\n" + title);
	for (int i = 0; i < items.length; i++) {
	    System.out.println( (i + 1) + ". " + items[i] );
	}

	int answer = Integer.parseInt(scanner.nextLine());
	//System.out.print("\033[H\033[2J");
	//System.out.flush();
	
	return answer;
    }


    public static String[] PromptUser( String[] prompts ) {
		String answers[] = new String[prompts.length];
		for (int i = 0; i < prompts.length; i++) {
		    System.out.println( prompts[i] );
		    answers[i] = scanner.nextLine();
		}

		//	System.out.print("\033[H\033[2J");
		//System.out.flush();

		return answers;
		
	    }
}
