package internet;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.SwingUtilities;

public class NewClient {

	private static final int portNumber = 7730;
	
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private Object message;
	private String serverIP;
	private Socket connection;
	
	public NewClient(String host){
		serverIP = host;
	}
	
	public void run(){
		
		try{
			connectToServer();
			setUpStreams();
		}
		catch(EOFException e){
			showMessage("Client Terminated Connection\n");
		}
		catch(IOException e){
			e.printStackTrace();
		}
		
	}
	
	public void recieveMessage(){
		try {
			if(input != null)
				message = input.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void showMessage(final String message){
		SwingUtilities.invokeLater(new Runnable(){

			public void run() {
				System.out.println(message);
			}
			
		});
	}
	
	public void sendMessage(Object message){
		try{
			output.writeObject(message);
			output.flush();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	private void setUpStreams() throws IOException{
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input = new ObjectInputStream(connection.getInputStream());
		showMessage("streams are now set up!\n");
	}
	
	private void connectToServer() throws UnknownHostException, IOException{
		showMessage("Attempting Connection\n");
		connection = new Socket(InetAddress.getByName(serverIP), portNumber);
		showMessage("Connected to "+connection.getInetAddress().getHostName());
	}
	
	public void close(){
		showMessage("\n Closing Connections \n");
		try{
			output.close();
			input.close();
			connection.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}

	}
	
	public Object getMessage(){
		return message;
	}
	
}
