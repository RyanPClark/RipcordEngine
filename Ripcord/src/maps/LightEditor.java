package maps;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import entities.Light;

import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.JTextField;

import org.lwjgl.util.vector.Vector3f;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@SuppressWarnings("serial")
public class LightEditor extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;
	private JTextField textField_6;
	private JTextField textField_7;
	private JTextField textField_8;
	private JTextField textField_9;
	private JTextField textField_10;
	
	private boolean load = false;
	private int ID = 0;
	private int lastID = 1;
	private boolean New = false;

	public LightEditor() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 765);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		JLabel lblLightEditor = new JLabel("LIGHT EDITOR");
		lblLightEditor.setFont(new Font("Tahoma", Font.PLAIN, 45));
		lblLightEditor.setBounds(57, 16, 338, 54);
		contentPane.add(lblLightEditor);
		
		JLabel lblLightId = new JLabel("LIGHT ID");
		lblLightId.setBounds(15, 120, 69, 20);
		contentPane.add(lblLightId);
		
		textField = new JTextField();
		textField.setBounds(90, 120, 116, 26);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JLabel lblPositionx = new JLabel("Position.x");
		lblPositionx.setBounds(15, 208, 69, 20);
		contentPane.add(lblPositionx);
		
		JLabel lblPositiony = new JLabel("Position.y");
		lblPositiony.setBounds(15, 244, 69, 20);
		contentPane.add(lblPositiony);
		
		JLabel lblPositionz = new JLabel("Position.z");
		lblPositionz.setBounds(15, 280, 69, 20);
		contentPane.add(lblPositionz);
		
		textField_1 = new JTextField();
		textField_1.setBounds(90, 204, 116, 28);
		contentPane.add(textField_1);
		textField_1.setColumns(10);
		
		JLabel lblRed = new JLabel("Red");
		lblRed.setBounds(15, 350, 69, 20);
		contentPane.add(lblRed);
		
		JLabel lblGreen = new JLabel("Green");
		lblGreen.setBounds(15, 386, 69, 20);
		contentPane.add(lblGreen);
		
		JLabel lblBlue = new JLabel("Blue");
		lblBlue.setBounds(15, 422, 69, 20);
		contentPane.add(lblBlue);
		
		JLabel lblIntensity = new JLabel("Intensity");
		lblIntensity.setBounds(15, 481, 69, 20);
		contentPane.add(lblIntensity);
		
		JLabel lblAttx = new JLabel("Att.x");
		lblAttx.setBounds(15, 550, 69, 20);
		contentPane.add(lblAttx);
		
		JLabel lblAtty = new JLabel("Att.y");
		lblAtty.setBounds(15, 586, 69, 20);
		contentPane.add(lblAtty);
		
		JLabel lblAttz = new JLabel("Att.z");
		lblAttz.setBounds(15, 626, 69, 20);
		contentPane.add(lblAttz);
		
		textField_2 = new JTextField();
		textField_2.setColumns(10);
		textField_2.setBounds(90, 240, 116, 28);
		contentPane.add(textField_2);
		
		textField_3 = new JTextField();
		textField_3.setColumns(10);
		textField_3.setBounds(90, 276, 116, 28);
		contentPane.add(textField_3);
		
		textField_4 = new JTextField();
		textField_4.setColumns(10);
		textField_4.setBounds(90, 346, 116, 28);
		contentPane.add(textField_4);
		
		textField_5 = new JTextField();
		textField_5.setColumns(10);
		textField_5.setBounds(90, 383, 116, 26);
		contentPane.add(textField_5);
		
		textField_6 = new JTextField();
		textField_6.setColumns(10);
		textField_6.setBounds(90, 418, 116, 28);
		contentPane.add(textField_6);
		
		textField_7 = new JTextField();
		textField_7.setColumns(10);
		textField_7.setBounds(90, 477, 116, 28);
		contentPane.add(textField_7);
		
		textField_8 = new JTextField();
		textField_8.setColumns(10);
		textField_8.setBounds(90, 546, 116, 28);
		contentPane.add(textField_8);
		
		textField_9 = new JTextField();
		textField_9.setColumns(10);
		textField_9.setBounds(90, 586, 116, 28);
		contentPane.add(textField_9);
		
		textField_10 = new JTextField();
		textField_10.setColumns(10);
		textField_10.setBounds(90, 618, 116, 28);
		contentPane.add(textField_10);
		
		JButton btnNew = new JButton("New");
		btnNew.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				New = true;
			}
		});
		
		btnNew.setBounds(280, 116, 115, 29);
		contentPane.add(btnNew);
	}

	protected void modifyLight(Light light){
		
		if (lastID != ID){
			load = true;
		}
		
		lastID = ID;
		
		if (load){
			textField.setText("" + ID);
			textField_1.setText("" + light.getPosition().x);
			textField_2.setText("" + light.getPosition().y);
			textField_3.setText("" + light.getPosition().z);
			textField_4.setText("" + light.getColor().x);
			textField_5.setText("" + light.getColor().y);
			textField_6.setText("" + light.getColor().z);
			textField_7.setText("" + light.getIntensity());
			textField_8.setText("" + light.getAttenuation().x);
			textField_9.setText("" + light.getAttenuation().y);
			textField_10.setText("" + light.getAttenuation().z);
			
			load = false;
		}
		
		if (!textField.getText().isEmpty()){
			ID = Integer.parseInt(textField.getText());
		}
		
		if (!textField_1.getText().isEmpty() && !textField_2.getText().isEmpty() && !textField_3.getText().isEmpty()){
			try{
				float x = Float.parseFloat(textField_1.getText());
				float y = Float.parseFloat(textField_2.getText());
				float z = Float.parseFloat(textField_3.getText());
				light.setPosition(new Vector3f(x, y, z));
			}
			catch (NumberFormatException e){}
		}
		if (!textField_4.getText().isEmpty() && !textField_5.getText().isEmpty() && !textField_6.getText().isEmpty()){
			try{
				float x = Float.parseFloat(textField_4.getText());
				float y = Float.parseFloat(textField_5.getText());
				float z = Float.parseFloat(textField_6.getText());
				light.setColor((new Vector3f(x, y, z)));
			}
			catch (NumberFormatException e){}
		}
		if (!textField_7.getText().isEmpty()){
			try{
				float intensity = Float.parseFloat(textField_7.getText());
				light.setIntensity(intensity);
			}
			catch (NumberFormatException e){}
		}
		if (!textField_8.getText().isEmpty() && !textField_9.getText().isEmpty() && !textField_10.getText().isEmpty()){
			try{
				float x = Float.parseFloat(textField_8.getText());
				float y = Float.parseFloat(textField_9.getText());
				float z = Float.parseFloat(textField_10.getText());
				light.setAttenuation((new Vector3f(x, y, z)));
			}
			catch (NumberFormatException e){}
		}
	}
	
	public int getID(){
		return ID;
	}
	public void setID(int Id){
		ID = Id;
	}
	public boolean isNew(){
		return New;
	}
	public void setNew(boolean isNew){
		New = isNew;
	}
}
