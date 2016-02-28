package internet;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Client extends JFrame{

	private static final long serialVersionUID = 1581324465255627399L;
	private static final int portNumber = 7730;
	
	private JTextArea chatWindow;
	private JTextField userText;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private String message = "";
	private String serverIP;
	private Socket connection;
	
	public Client(String host){
		super("Client");
		serverIP = host;
		userText = new JTextField();
		userText.setEditable(false);
		userText.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				sendMessage(e.getActionCommand());
				userText.setText("");
			}
			
		});
		add(userText, BorderLayout.NORTH);
		chatWindow = new JTextArea();
		add(new JScrollPane(chatWindow));
		setSize(450, 225);
		setVisible(true);
	}
	
	public void run(){
		
		try{
			connectToServer();
			setUpStreams();
			whileChatting();
		}
		catch(EOFException e){
			showMessage("Client Terminated Connection\n");
		}
		catch(IOException e){
			e.printStackTrace();
		}finally{
			close();
		}
		
	}
	
	
	private void whileChatting() throws IOException{
		ableToType(true);
		do {
			try{
				message = (String) input.readObject();
				showMessage("\n"+message);
			}
			catch(ClassNotFoundException e){
				showMessage("I nodent nkow that type of class\n");
			}
		}
		while(!message.equals("END"));
	}
	
	private void ableToType(final boolean ToF){
		SwingUtilities.invokeLater(new Runnable(){

			public void run() {
				userText.setEditable(ToF);
			}
			
		});
	}
	
	private void showMessage(final String message){
		SwingUtilities.invokeLater(new Runnable(){

			public void run() {
				chatWindow.append(message);
			}
			
		});
	}
	
	private void sendMessage(String message){
		try{
			output.writeObject(message);
			output.flush();
			showMessage("\n" + message);
		}
		catch(IOException e){
			chatWindow.append(e.getMessage());
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
	
	private void close(){
		showMessage("\n Closing Connections \n");
		ableToType(false);
		try{
			output.close();
			input.close();
			connection.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}

	}
	
}
