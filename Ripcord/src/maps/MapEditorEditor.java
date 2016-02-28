package maps;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JPasswordField;

import org.lwjgl.util.vector.Vector3f;

import entities.MultiModeledEntity;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@SuppressWarnings("serial")
public class MapEditorEditor extends JFrame {

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
	private JTextField textField_12;
	private JTextField textField_13;
	private JPasswordField passwordField;
	private JTextField textField_15;
	private JTextField textField_16;
	
	private JLabel lblEditor;
	private JLabel lblSelectedEntityId;
	private boolean load = false;
	private boolean newEntity = false;
	private String lastTag = "";
	private int lastID;
	private JTextField textField_17;
	private JTextField textField_18;
	private JTextField textField_19;
	
	public boolean createNewEntity(){
		if(!newEntity){
			return false;
		}else {
			newEntity = false;
			return true;
		}
	}
	
	public String getNewEntityTag(){
		return textField_15.getText();
	}
	
	private void setSelectedEntityLabel(String ID){
		lblSelectedEntityId.setText("Selected Entity ID: " + ID);
	}
	
	public int getParts(){
		return Integer.parseInt(textField_18.getText());
	}
	
	public boolean getB_Model(){
		return Boolean.parseBoolean(textField_19.getText());
	}
	
	public void modifyEntity(MultiModeledEntity entity){
		
		if (!(entity.getTag().equals(lastTag) && entity.getID() == lastID)){
			load = true;
		}
		
		setSelectedEntityLabel(entity.getTag()+" #"+entity.getID());
		setPosition(entity.getPosition());
		setScale(entity.getScale());
		setRotation(entity.getRotX(), entity.getRotY(), entity.getRotZ());
		setRest(entity.getModel().getTexture().getReflectivity(),
				entity.getModel().getTexture().isUseFakeLighting(), entity.getModel().getTexture().isHasTransparency(),
				entity.getModel().getTexture().getShine_damper(), entity.getModel().getRawModel().getVertexCount());
		
		if (load){
			if(entity.isB_model()){
				textField_18.setText("" + entity.getModels().size());
			}else {
				textField_18.setText("" + entity.getModels().size()/2);
			}
			
			textField_19.setText("" + entity.isB_model());
			load = false;
		}
		
		if(!textField.getText().isEmpty() && !textField_1.getText().isEmpty()  && !textField_2.getText().isEmpty() ){
			
			try {
				entity.setPosition(new Vector3f(Float.parseFloat(textField.getText()), Float.parseFloat(textField_1.getText()),
						Float.parseFloat(textField_2.getText())));
			}
			catch (NumberFormatException e){}
		}
		if(!textField_4.getText().isEmpty()){
			try{
				entity.setRotX(Float.parseFloat(textField_4.getText()));
			}
			catch (NumberFormatException e){}
		}
		if(!textField_5.getText().isEmpty()){
			try{
				entity.setRotY(Float.parseFloat(textField_5.getText()));
			}
			catch (NumberFormatException e){}
		}
		if(!textField_10.getText().isEmpty()){
			try{
				entity.setRotZ(Float.parseFloat(textField_10.getText()));
			}
			catch (NumberFormatException e){}
		}
		if(!textField_3.getText().isEmpty() && !textField_6.getText().isEmpty()  && !textField_7.getText().isEmpty() ){
			try{
				entity.setScale(new Vector3f(Float.parseFloat(textField_3.getText()), Float.parseFloat(textField_6.getText()),
						Float.parseFloat(textField_7.getText())));
			}
			catch (NumberFormatException e){}
		}
		if(!textField_12.getText().isEmpty()){
			try{
				entity.getModel().getTexture().setHasTransparency(Boolean.parseBoolean(textField_12.getText()));
			}
			catch (NumberFormatException e){}
		}
		if(!textField_8.getText().isEmpty()){
			try{
				entity.getModel().getTexture().setReflectivity(Float.parseFloat(textField_8.getText()));
			}
			catch (NumberFormatException e){}
		}
		if(!textField_13.getText().isEmpty()){
			try{
				entity.getModel().getTexture().setShine_damper(Float.parseFloat(textField_13.getText()));
			}
			catch (NumberFormatException e){}
		}
		if(!textField_9.getText().isEmpty()){
			try{
				entity.getModel().getTexture().setUseFakeLighting(Boolean.parseBoolean(textField_9.getText()));
			}
			catch (NumberFormatException e){}
		}
		if(!textField_19.getText().isEmpty()){
			try{
				entity.setB_model(Boolean.parseBoolean(textField_19.getText()));
			}
			catch (NumberFormatException e){}
		}
		if(!textField_18.getText().isEmpty()){
			try{
				entity.setNumber_of_parts(Integer.parseInt(textField_18.getText()));
			}
			catch (NumberFormatException e){}
		}
		
		lastTag = entity.getTag();
		lastID = entity.getID();
	}
	
	public void setPosition(Vector3f position){
		
		if(load){
			textField.setText("" + position.x);
			textField_1.setText("" + position.y);
			textField_2.setText("" + position.z);
		}
		
	}
	
	public void setScale(Vector3f scale){
		
		if(load){
			textField_3.setText("" + scale.x);
			textField_6.setText("" + scale.y);
			textField_7.setText("" + scale.z);
		}
	}
	
	public void setRotation(float rotX, float rotY, float rotZ){
		
		if(load){
			textField_4.setText("" + rotX);
			textField_5.setText("" + rotY);
			textField_10.setText("" + rotZ);
		}
	}
	
	public void setRest(float reflectivity, boolean fake_light, boolean transparency, float shine_damper, int vertexCount){
		
		if(load){
			textField_8.setText("" + reflectivity);
			textField_9.setText("" + fake_light);
			textField_12.setText("" + transparency);
			textField_13.setText("" + shine_damper);
			textField_17.setText("" + vertexCount);
		}
	}
	
	public MapEditorEditor() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 849);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		lblEditor = new JLabel("EDITOR");
		lblEditor.setFont(new Font("Tahoma", Font.PLAIN, 45));
		lblEditor.setBounds(190, 16, 184, 59);
		contentPane.add(lblEditor);
		
		lblSelectedEntityId = new JLabel("Selected Entity ID:");
		lblSelectedEntityId.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblSelectedEntityId.setBounds(39, 114, 306, 30);
		contentPane.add(lblSelectedEntityId);
		
		JLabel lblPositionx = new JLabel("Position.x");
		lblPositionx.setBounds(39, 179, 69, 20);
		contentPane.add(lblPositionx);
		
		JLabel lblPositiony = new JLabel("Position.y");
		lblPositiony.setBounds(39, 215, 69, 20);
		contentPane.add(lblPositiony);
		
		JLabel lblPositionz = new JLabel("Position.z");
		lblPositionz.setBounds(39, 251, 69, 20);
		contentPane.add(lblPositionz);
		
		JLabel lblScalex = new JLabel("Scale.x");
		lblScalex.setBounds(39, 287, 69, 20);
		contentPane.add(lblScalex);
		
		JLabel lblScaley = new JLabel("Scale.y");
		lblScaley.setBounds(39, 323, 69, 20);
		contentPane.add(lblScaley);
		
		JLabel lblScalez = new JLabel("Scale.z");
		lblScalez.setBounds(39, 359, 69, 20);
		contentPane.add(lblScalez);
		
		JLabel lblRotx = new JLabel("RotX");
		lblRotx.setBounds(39, 395, 69, 20);
		contentPane.add(lblRotx);
		
		JLabel lblRoty = new JLabel("RotY");
		lblRoty.setBounds(39, 431, 69, 20);
		contentPane.add(lblRoty);
		
		JLabel lblRotz = new JLabel("RotZ");
		lblRotz.setBounds(39, 467, 69, 20);
		contentPane.add(lblRotz);
		
		JLabel lblReflectivity = new JLabel("Reflectivity");
		lblReflectivity.setBounds(39, 503, 78, 20);
		contentPane.add(lblReflectivity);
		
		JLabel lblFakeLighting = new JLabel("Fake Lighting");
		lblFakeLighting.setBounds(39, 539, 100, 20);
		contentPane.add(lblFakeLighting);
		
		JLabel lblTransparent = new JLabel("Transparent");
		lblTransparent.setBounds(39, 575, 85, 20);
		contentPane.add(lblTransparent);
		
		JLabel lblShineDamper = new JLabel("Shine Damper");
		lblShineDamper.setBounds(39, 611, 100, 20);
		contentPane.add(lblShineDamper);
		
		JLabel lblVertexcount = new JLabel("VertexCount");
		lblVertexcount.setBounds(39, 647, 100, 20);
		contentPane.add(lblVertexcount);
		
		JButton btnNew = new JButton("New Entity:");
		btnNew.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				newEntity = true;
			}
		});
		
		btnNew.setBounds(397, 115, 115, 29);
		contentPane.add(btnNew);
		
		textField = new JTextField();
		textField.setBounds(147, 176, 78, 26);
		contentPane.add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setBounds(147, 212, 78, 26);
		contentPane.add(textField_1);
		textField_1.setColumns(10);
		
		textField_2 = new JTextField();
		textField_2.setColumns(10);
		textField_2.setBounds(147, 251, 78, 26);
		contentPane.add(textField_2);
		
		textField_3 = new JTextField();
		textField_3.setColumns(10);
		textField_3.setBounds(147, 287, 78, 26);
		contentPane.add(textField_3);
		
		textField_4 = new JTextField();
		textField_4.setColumns(10);
		textField_4.setBounds(147, 398, 78, 26);
		contentPane.add(textField_4);
		
		textField_5 = new JTextField();
		textField_5.setColumns(10);
		textField_5.setBounds(147, 434, 78, 26);
		contentPane.add(textField_5);
		
		textField_6 = new JTextField();
		textField_6.setColumns(10);
		textField_6.setBounds(147, 323, 78, 26);
		contentPane.add(textField_6);
		
		textField_7 = new JTextField();
		textField_7.setColumns(10);
		textField_7.setBounds(147, 359, 78, 26);
		contentPane.add(textField_7);
		
		textField_8 = new JTextField();
		textField_8.setColumns(10);
		textField_8.setBounds(147, 500, 78, 26);
		contentPane.add(textField_8);
		
		textField_9 = new JTextField();
		textField_9.setColumns(10);
		textField_9.setBounds(147, 536, 78, 26);
		contentPane.add(textField_9);
		
		textField_10 = new JTextField();
		textField_10.setColumns(10);
		textField_10.setBounds(147, 467, 78, 26);
		contentPane.add(textField_10);
		
		textField_12 = new JTextField();
		textField_12.setColumns(10);
		textField_12.setBounds(147, 572, 78, 26);
		contentPane.add(textField_12);
		
		textField_13 = new JTextField();
		textField_13.setColumns(10);
		textField_13.setBounds(147, 611, 78, 26);
		contentPane.add(textField_13);
		
		JButton button = new JButton("+");
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				textField_1.setText("" + (Float.parseFloat(textField_1.getText())+0.5f));
			}
		});
		button.setBounds(240, 215, 45, 29);
		contentPane.add(button);
		
		JButton button_1 = new JButton("-");
		button_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				textField_1.setText("" + (Float.parseFloat(textField_1.getText())-0.5f));
			}
		});
		button_1.setBounds(300, 215, 45, 29);
		contentPane.add(button_1);
		
		JButton button_2 = new JButton("+");
		button_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				textField.setText("" + (Float.parseFloat(textField.getText())+0.5f));
			}
		});
		button_2.setBounds(240, 179, 45, 29);
		contentPane.add(button_2);
		
		JButton button_3 = new JButton("-");
		button_3.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				textField.setText("" + (Float.parseFloat(textField.getText())-0.5f));
			}
		});
		button_3.setBounds(300, 179, 45, 29);
		contentPane.add(button_3);
		
		JButton button_4 = new JButton("+");
		button_4.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				textField_2.setText("" + (Float.parseFloat(textField_2.getText())+0.5f));
			}
		});
		button_4.setBounds(240, 248, 45, 29);
		contentPane.add(button_4);
		
		JButton button_5 = new JButton("-");
		button_5.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				textField_2.setText("" + (Float.parseFloat(textField_2.getText())-0.5f));
			}
		});
		button_5.setBounds(300, 248, 45, 29);
		contentPane.add(button_5);
		
		JButton button_6 = new JButton("+");
		button_6.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				textField_7.setText("" + (Float.parseFloat(textField_7.getText())+0.15f));
			}
		});
		button_6.setBounds(240, 356, 45, 29);
		contentPane.add(button_6);
		
		JButton button_7 = new JButton("-");
		button_7.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				textField_7.setText("" + (Float.parseFloat(textField_7.getText())-0.15f));
			}
		});
		button_7.setBounds(300, 356, 45, 29);
		contentPane.add(button_7);
		
		JButton button_8 = new JButton("+");
		button_8.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				textField_6.setText("" + (Float.parseFloat(textField_6.getText())+0.15f));
			}
		});
		button_8.setBounds(240, 323, 45, 29);
		contentPane.add(button_8);
		
		JButton button_9 = new JButton("-");
		button_9.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				textField_6.setText("" + (Float.parseFloat(textField_6.getText())-0.15f));
			}
		});
		button_9.setBounds(300, 323, 45, 29);
		contentPane.add(button_9);
		
		JButton button_10 = new JButton("-");
		button_10.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				textField_3.setText("" + (Float.parseFloat(textField_3.getText())-0.15f));
			}
		});
		button_10.setBounds(300, 287, 45, 29);
		contentPane.add(button_10);
		
		JButton button_11 = new JButton("+");
		button_11.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				textField_3.setText("" + (Float.parseFloat(textField_3.getText())+0.15f));
			}
		});
		button_11.setBounds(240, 287, 45, 29);
		contentPane.add(button_11);
		
		JButton button_12 = new JButton("+");
		button_12.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				textField_9.setText("" + (!Boolean.parseBoolean(textField_9.getText())));
			}
		});
		button_12.setBounds(240, 535, 105, 29);
		contentPane.add(button_12);
		
		JButton button_15 = new JButton("+");
		button_15.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				textField_10.setText("" + (Float.parseFloat(textField_10.getText())+5));
			}
		});
		button_15.setBounds(240, 467, 45, 29);
		contentPane.add(button_15);
		
		JButton button_16 = new JButton("+");
		button_16.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				textField_5.setText("" + (Float.parseFloat(textField_5.getText())+5));
			}
		});
		button_16.setBounds(240, 434, 45, 29);
		contentPane.add(button_16);
		
		JButton button_17 = new JButton("+");
		button_17.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				textField_4.setText("" + (Float.parseFloat(textField_4.getText())+5));
			}
		});
		button_17.setBounds(240, 398, 45, 29);
		contentPane.add(button_17);
		
		JButton button_18 = new JButton("-");
		button_18.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				textField_4.setText("" + (Float.parseFloat(textField_4.getText())-5));
			}
		});
		button_18.setBounds(300, 398, 45, 29);
		contentPane.add(button_18);
		
		JButton button_19 = new JButton("-");
		button_19.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				textField_5.setText("" + (Float.parseFloat(textField_5.getText())-5));
			}
		});
		button_19.setBounds(300, 434, 45, 29);
		contentPane.add(button_19);
		
		JButton button_20 = new JButton("-");
		button_20.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				textField_10.setText("" + (Float.parseFloat(textField_10.getText())-5));
			}
		});
		button_20.setBounds(300, 467, 45, 29);
		contentPane.add(button_20);
		
		JButton button_24 = new JButton("+");
		button_24.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				textField_12.setText("" + (!Boolean.parseBoolean(textField_12.getText())));
			}
		});
		button_24.setBounds(240, 575, 105, 29);
		contentPane.add(button_24);
		
		JButton button_25 = new JButton("+");
		button_25.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				textField_13.setText("" + (Float.parseFloat(textField_13.getText())+0.15f));
			}
		});
		button_25.setBounds(240, 611, 45, 29);
		contentPane.add(button_25);
		
		JButton button_28 = new JButton("-");
		button_28.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				textField_13.setText("" + (Float.parseFloat(textField_13.getText())-0.15f));
			}
		});
		button_28.setBounds(300, 611, 45, 29);
		contentPane.add(button_28);
		
		passwordField = new JPasswordField();
		passwordField.setText("");
		passwordField.setBounds(397, 692, 115, 26);
		contentPane.add(passwordField);
		
		textField_15 = new JTextField();
		textField_15.setBounds(397, 218, 115, 26);
		contentPane.add(textField_15);
		textField_15.setColumns(10);
		
		JLabel lblTag = new JLabel("TAG");
		lblTag.setBounds(397, 182, 69, 20);
		contentPane.add(lblTag);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(397, 650, 69, 20);
		contentPane.add(lblPassword);
		
		JButton btnNewEmitter = new JButton("New Emitter");
		btnNewEmitter.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//new emitter
			}
		});
		btnNewEmitter.setBounds(397, 350, 134, 29);
		contentPane.add(btnNewEmitter);
		
		JLabel lblType = new JLabel("TYPE");
		lblType.setBounds(397, 395, 69, 20);
		contentPane.add(lblType);
		
		textField_16 = new JTextField();
		textField_16.setBounds(397, 434, 115, 26);
		contentPane.add(textField_16);
		textField_16.setColumns(10);
		
		JLabel label = new JLabel("Frustum");
		label.setBounds(39, 683, 100, 20);
		contentPane.add(label);
		
		textField_17 = new JTextField();
		textField_17.setColumns(10);
		textField_17.setBounds(147, 647, 78, 26);
		contentPane.add(textField_17);
		
		JButton button_13 = new JButton("+");
		button_13.setBounds(240, 499, 45, 29);
		button_13.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				textField_8.setText("" + (Float.parseFloat(textField_8.getText())+0.5f));
			}
		});
		contentPane.add(button_13);
		
		JButton button_22 = new JButton("-");
		button_22.setBounds(300, 499, 45, 29);
		button_22.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				textField_8.setText("" + (Float.parseFloat(textField_8.getText())-0.5f));
			}
		});
		contentPane.add(button_22);
		
		JLabel lblParts = new JLabel("Parts");
		lblParts.setBounds(397, 268, 69, 20);
		contentPane.add(lblParts);
		
		textField_18 = new JTextField();
		textField_18.setBounds(397, 304, 146, 26);
		contentPane.add(textField_18);
		textField_18.setColumns(10);
		
		JLabel lblBmodel = new JLabel("B_Model");
		lblBmodel.setBounds(39, 719, 69, 20);
		contentPane.add(lblBmodel);
		
		textField_19 = new JTextField();
		textField_19.setBounds(147, 722, 78, 26);
		contentPane.add(textField_19);
		textField_19.setColumns(10);
		
	}
}
