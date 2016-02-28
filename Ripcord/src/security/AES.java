package security;

import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.Cipher;


public class AES {

	private static final String IV = "1A5B71E309DF2A54";
	private static final String ENCRYPTION_KEY = "0123456789abcdef";
 
	public static byte[] encrypt(String plainText) throws Exception {

		Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding", "SunJCE");

		SecretKeySpec key = new SecretKeySpec(ENCRYPTION_KEY.getBytes("UTF-8"), "AES");

		cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(IV.getBytes("UTF-8")));

		return cipher.doFinal(plainText.getBytes("UTF-8"));

	}


	public static String decrypt(byte[] cipherText) throws Exception{

		Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding", "SunJCE");

		SecretKeySpec key = new SecretKeySpec(ENCRYPTION_KEY.getBytes("UTF-8"), "AES");

		cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(IV.getBytes("UTF-8")));

		return new String(cipher.doFinal(cipherText),"UTF-8");

	}

}
