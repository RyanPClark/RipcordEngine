package internet;

import javax.swing.JFrame;

public class ServerMain {

	public static void main(String[] args){
	
		NewServer server = new NewServer();
		server.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		server.run();
	}
	
}
