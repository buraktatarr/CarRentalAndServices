package Util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordUtil {

    private PasswordUtil(){

    }

    public static String hash(String password){

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = messageDigest.digest(password.getBytes(StandardCharsets.UTF_8));
            return byteToHex(encodedHash);

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

    }

    private static String byteToHex(byte[] encodedHash) {
        StringBuilder builder =new StringBuilder(2*encodedHash.length);

        for (byte b :encodedHash){
            String hex =Integer.toHexString(0xff & b);
            if (hex.length()==1){
                builder.append('0');
            }
            builder.append(hex);
        }
        return builder.toString();
    }
}
