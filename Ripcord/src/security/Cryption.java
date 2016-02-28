package security;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Cryption {

	public static void encrypt(File file){
		
		FileInputStream fstream = null;
		try {
			fstream = new FileInputStream(file.toPath().toString());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		  
		List<byte[]> bytes = new ArrayList<byte[]>();
		
		String strLine;
		
		try {
			
			while ((strLine = br.readLine()) != null)   {
				  
				String AES_Input = strLine;
				
				while(AES_Input.length()%16 != 0){
					AES_Input += "$";
				}
				byte[] cipher = AES.encrypt(AES_Input);
				
				bytes.add(cipher);
			}
			
			br.close();
						
			BufferedWriter output = new BufferedWriter(new FileWriter(file));

			int size = bytes.size();
			
			for(int i = 0; i < size; i++){
				
				for(int j = 0; j < bytes.get(i).length; j++){
					
					int num = bytes.get(i)[j] + 128;
					int remainder = num%16;
					int quotient = num/16;
					output.write("" + characters[quotient] + characters[remainder]);
				}
				output.write("\n");
			}
			
			output.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	public static void decrypt(File file){

		FileInputStream fstream = null;
		try {
			fstream = new FileInputStream(file.toPath().toString());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		  
		List<String> strings = new ArrayList<String>();
		
		String strLine;
		
		try {
			
			while ((strLine = br.readLine()) != null)   {
				  
				byte[] cipher = new byte[strLine.length()/2];
				String[] textCipher = new String[strLine.length()/2];
				for(int i = 0; i < strLine.length(); i+=2){
					textCipher[i/2] = strLine.substring(i, i+2);
					int sum = 0;
					for(int j = 0; j < 16; j++){
						if(textCipher[i/2].substring(0, 1).equals(characters[j])){
							sum += 16*j;
						}
						if(textCipher[i/2].substring(1, 2).equals(characters[j])){
							sum += j;
						}
					}
					sum -= 128;
					cipher[i/2] = (byte) sum;
				}
				
				String str = AES.decrypt(cipher);
				
				while(str.endsWith("$")){
					str = str.substring(0, str.length()-1);
				}
				
				strings.add(str);
			}
			
			br.close();
						
			BufferedWriter output = new BufferedWriter(new FileWriter(file));
			
			for(int i = 0; i < strings.size(); i++){
				output.write(strings.get(i)+"\n");
			}
			
			output.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	private static final String[] characters = {
		"0", "1", "2", "3", "4", "5", "6", "7",
		"8", "9", "A", "B", "C", "D", "E", "F"
	};
	
}
