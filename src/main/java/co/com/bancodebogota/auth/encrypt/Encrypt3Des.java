package co.com.bancodebogota.auth.encrypt;

import co.com.bancodebogota.cipher.CipherException;
import co.com.bancodebogota.cipher.TripleDesUtilities;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public class Encrypt3Des {
	
	public static PrivateKey getPrivateKey(String base64PrivateKey) {
		PrivateKey privateKey = null;
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(base64PrivateKey.getBytes()));
		KeyFactory keyFactory = null;
		try {
			keyFactory = KeyFactory.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		try {
			privateKey = keyFactory.generatePrivate(keySpec);
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return privateKey;
	}

	public static String decrypt(byte[] data, PrivateKey privateKey) throws NoSuchPaddingException,
			NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		return new String(cipher.doFinal(data));
	}

	public static String decrypt(String data, String base64PrivateKey) throws IllegalBlockSizeException,
			InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
		return decrypt(Base64.getDecoder().decode(data.getBytes()), getPrivateKey(base64PrivateKey));
	}

	public static String encrypt3Des(String textoClaro, String semilla3Des) {
		String encrypt = "";
		try {
			encrypt = TripleDesUtilities.cipher(textoClaro, semilla3Des);
		} catch (CipherException e) {
			e.printStackTrace();
		}
		return encrypt;
	}

	public static String decryptRsa(String textoCifrado, String privateKey)
			throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException {
		String decryptedString = "";
		try {
			decryptedString = Encrypt3Des.decrypt(textoCifrado, privateKey);
		} catch (NoSuchAlgorithmException e) {
			System.err.println(e.getMessage());
		}
		return decryptedString;
	}
	

}
