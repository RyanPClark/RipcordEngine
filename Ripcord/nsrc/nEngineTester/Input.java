package nEngineTester;

import org.lwjgl.input.Mouse;

public class Input {

	private static boolean wasMouseDown;
	private static boolean wasClicked;
	
	public static void update(){
		boolean mouseDown = Mouse.isButtonDown(0);
		wasClicked = (!mouseDown && wasMouseDown);
		wasMouseDown = mouseDown;
	}
	
	public static boolean wasClicked(){
		return wasClicked;
	}
	
}
