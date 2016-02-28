package internet;

import java.awt.EventQueue;


public class Communication {

	public static NewClient client;
	
	public static void loadClient(final String host){
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				client = new NewClient(host);
				client.run();
			}
		});
	}
	
}
