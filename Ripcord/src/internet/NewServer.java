package internet;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;

//import maps.Map;


public class NewServer extends JFrame{

	private static final long serialVersionUID = -7458195839040658199L;
	private static final int portNumber = 7730;
	
	private int message;
	//private Map map;
	private boolean connected;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private ServerSocket server;
	private Socket connection;
	
	public NewServer() {
		super("SERVER");
		setVisible(true);
	}
	
	public void run(){
		
		try {
			server = new ServerSocket(portNumber, 80);
			
			while(true){
				try{
					waitForConnection();
					setUpStreams();
					recieveMessage();
					sendMessage(message);
				}
				catch(EOFException e){
					e.printStackTrace();
				}
			}
			
		}
		catch(IOException e){
			e.printStackTrace();
		}
		finally{
			close();
		}
	}
	
	private void waitForConnection(){
		try {
			if(!connected){
				connection = server.accept();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void setUpStreams() throws IOException{
		if(!connected){
			output = new ObjectOutputStream(connection.getOutputStream());
			output.flush();
			input = new ObjectInputStream(connection.getInputStream());
			connected = true;
		}
	}
	
	private void close(){
		try{
			output.close();
			input.close();
			connection.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}

	}
	
	private void sendMessage(Object message){
		//System.out.println("sending");
		try{
			if(output != null){
				//map.update();
				output.writeObject(message);
				output.flush();
			}
			//System.out.println("sent");
		}
		catch(IOException e){
			System.out.println("Can't send Message");
			connected = false;
		}
	}
	
	private void recieveMessage(){
		try {
			//map = (Map) input.readObject();
			message = (int) input.readObject();
		} catch (ClassNotFoundException e) {
		} catch (IOException e) {
			e.printStackTrace();
			connected = false;
		}
	}
	

	public Object getMessage(){
		return message;
	}
	
}
