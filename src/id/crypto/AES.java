package id.crypto;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Fatia Kusuma Dewi on 4/16/2016.
 */
public class AES {

    public AES() {

    }

    public byte[] AESencryption(byte[] q, byte[] plaintext) throws Exception{
        SecretKey key = new SecretKeySpec(q, "AES");

        Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, key);

        return cipher.doFinal(plaintext);
    }

    public byte[] AESdecryption(byte[] q, byte[] ciphertext) throws Exception{
        SecretKey key = new SecretKeySpec(q, "AES");

        Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, key);

        return cipher.doFinal(ciphertext);
    }
}
