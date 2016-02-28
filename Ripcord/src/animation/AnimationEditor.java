package animation;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.JTextField;
import javax.swing.JSlider;

import javax.swing.JButton;

import entities.Entity;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@SuppressWarnings("serial")
public class AnimationEditor extends JFrame {

	private boolean load = true, play;
	private int lastAnimID, lastPartID, lastKeyframeID;
	
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
	private JTextField textField_11;

	private JSlider slider;
	
	public AnimationEditor() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 765);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		JLabel lblLightEditor = new JLabel("ANIMATION EDITOR");
		lblLightEditor.setFont(new Font("Tahoma", Font.PLAIN, 35));
		lblLightEditor.setBounds(57, 16, 338, 54);
		contentPane.add(lblLightEditor);
		
		JLabel lblAnimid = new JLabel("AnimID");
		lblAnimid.setBounds(15, 112, 69, 20);
		contentPane.add(lblAnimid);
		
		textField = new JTextField("0");
		textField.setBounds(99, 109, 146, 26);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JLabel lblPartid = new JLabel("PartID");
		lblPartid.setBounds(15, 148, 69, 20);
		contentPane.add(lblPartid);
		
		textField_1 = new JTextField("0");
		textField_1.setBounds(99, 151, 146, 26);
		contentPane.add(textField_1);
		textField_1.setColumns(10);
		
		JLabel lblKeyframeid = new JLabel("KeyFrameID");
		lblKeyframeid.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblKeyframeid.setBounds(15, 184, 69, 20);
		contentPane.add(lblKeyframeid);
		
		textField_2 = new JTextField("0");
		textField_2.setBounds(99, 190, 146, 26);
		contentPane.add(textField_2);
		textField_2.setColumns(10);
		
		JLabel lblTranslationx = new JLabel("Translationx");
		lblTranslationx.setBounds(15, 246, 117, 20);
		contentPane.add(lblTranslationx);
		
		JLabel lblTranslationy = new JLabel("Translationy");
		lblTranslationy.setBounds(15, 282, 87, 20);
		contentPane.add(lblTranslationy);
		
		JLabel lblTranslationz = new JLabel("Translationz");
		lblTranslationz.setBounds(15, 318, 87, 20);
		contentPane.add(lblTranslationz);
		
		JLabel lblRotationx = new JLabel("Rotationx");
		lblRotationx.setBounds(15, 354, 69, 20);
		contentPane.add(lblRotationx);
		
		JLabel lblRotationy = new JLabel("Rotationy");
		lblRotationy.setBounds(15, 390, 69, 20);
		contentPane.add(lblRotationy);
		
		JLabel lblRotationz = new JLabel("Rotationz");
		lblRotationz.setBounds(15, 426, 69, 20);
		contentPane.add(lblRotationz);
		
		JLabel lblRotationw = new JLabel("Rotationw");
		lblRotationw.setBounds(15, 462, 87, 20);
		contentPane.add(lblRotationw);
		
		JLabel lblLength = new JLabel("LENGTH");
		lblLength.setBounds(15, 538, 69, 20);
		contentPane.add(lblLength);
		
		textField_3 = new JTextField();
		textField_3.setBounds(147, 243, 146, 26);
		contentPane.add(textField_3);
		textField_3.setColumns(10);
		
		textField_4 = new JTextField();
		textField_4.setBounds(147, 279, 146, 26);
		contentPane.add(textField_4);
		textField_4.setColumns(10);
		
		textField_5 = new JTextField();
		textField_5.setBounds(147, 315, 146, 26);
		contentPane.add(textField_5);
		textField_5.setColumns(10);
		
		textField_6 = new JTextField();
		textField_6.setBounds(147, 351, 146, 26);
		contentPane.add(textField_6);
		textField_6.setColumns(10);
		
		textField_7 = new JTextField();
		textField_7.setBounds(147, 387, 146, 26);
		contentPane.add(textField_7);
		textField_7.setColumns(10);
		
		textField_8 = new JTextField();
		textField_8.setBounds(147, 423, 146, 26);
		contentPane.add(textField_8);
		textField_8.setColumns(10);
		
		textField_9 = new JTextField();
		textField_9.setBounds(147, 459, 146, 26);
		contentPane.add(textField_9);
		textField_9.setColumns(10);
		
		textField_10 = new JTextField();
		textField_10.setBounds(147, 535, 146, 26);
		contentPane.add(textField_10);
		textField_10.setColumns(10);
		
		slider = new JSlider();
		slider.setBounds(93, 645, 200, 26);
		slider.setValue(0);
		contentPane.add(slider);
		
		JLabel lblCounter = new JLabel("Counter");
		lblCounter.setBounds(15, 645, 69, 20);
		contentPane.add(lblCounter);
		
		textField_11 = new JTextField();
		textField_11.setBounds(308, 645, 87, 26);
		contentPane.add(textField_11);
		textField_11.setColumns(10);
		
		JButton btnPlay = new JButton("Play");
		btnPlay.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				play = !play;
			}
		});
		btnPlay.setBounds(267, 112, 117, 50);
		contentPane.add(btnPlay);
	}
	
	public void ModifyAnimation(Entity entity){
		
		try{
			int ID = Integer.parseInt(textField.getText());
			
			Animation animation = MasterAnimation.animations[ID];
			
			if (load){
				
				textField_3.setText("" + animation.translationData.get(lastPartID).x);
				textField_4.setText("" + animation.translationData.get(lastPartID).y);
				textField_5.setText("" + animation.translationData.get(lastPartID).z);
				textField_6.setText("" + animation.rotationData.get(lastPartID)[lastKeyframeID].x);
				textField_7.setText("" + animation.rotationData.get(lastPartID)[lastKeyframeID].y);
				textField_8.setText("" + animation.rotationData.get(lastPartID)[lastKeyframeID].z);
				textField_9.setText("" + animation.rotationData.get(lastPartID)[lastKeyframeID].w);
				textField_10.setText("" + animation.getLength());
				textField_11.setText("0/" + animation.getLength());
				
				load = false;
			}
			else{
				if (!(textField.getText().isEmpty()||textField_1.getText().isEmpty()||textField_2.getText().isEmpty())){
					if (Integer.parseInt(textField.getText()) != lastAnimID || Integer.parseInt(textField_1.getText()) != lastPartID
				|| Integer.parseInt(textField_2.getText()) != lastKeyframeID){
						load = true;
					}
				}
			}
			
			slider.setMaximum(animation.getLength());
			
			if(play){
				slider.setEnabled(false);
				slider.setValue(entity.getAnimCounter());
			}else{
				entity.setAnimCounter(slider.getValue());
				slider.setEnabled(true);
			}
			
			textField_11.setText(""+entity.getAnimCounter()+"/"+animation.getLength());

			animation.setLength(Integer.parseInt(textField_10.getText()));


			animation.translationData.get(lastPartID).x = Float.parseFloat(textField_3.getText());
			animation.translationData.get(lastPartID).y = Float.parseFloat(textField_4.getText());
			animation.translationData.get(lastPartID).z = Float.parseFloat(textField_5.getText());
				
			animation.rotationData.get(lastPartID)[lastKeyframeID].x = Float.parseFloat(textField_6.getText());
			animation.rotationData.get(lastPartID)[lastKeyframeID].y = Float.parseFloat(textField_7.getText());
			animation.rotationData.get(lastPartID)[lastKeyframeID].z = Float.parseFloat(textField_8.getText());
			animation.rotationData.get(lastPartID)[lastKeyframeID].w = Float.parseFloat(textField_9.getText());
			
			lastAnimID = Integer.parseInt(textField.getText());
			lastPartID = Integer.parseInt(textField_1.getText());
			lastKeyframeID = Integer.parseInt(textField_2.getText());
			
		}
		catch(NumberFormatException e){}
		
	}
}
