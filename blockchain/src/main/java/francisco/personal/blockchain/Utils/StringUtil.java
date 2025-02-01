package francisco.personal.blockchain.Utils;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

public class StringUtil {
    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    // Overload 1: Hash a String (for transaction IDs, block hashes)
    public static String applySha256(String input) {
        return applySha256(input.getBytes(StandardCharsets.UTF_8));
    }

    // Overload 2: Hash a byte[] (for public keys, raw data)
    public static String applySha256(byte[] input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input);
            return bytesToHex(hash);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder hex = new StringBuilder();
        for (byte b : bytes) {
            hex.append(String.format("%02x", b));
        }
        return hex.toString();
    }

    public static String applyECDSASignature(PrivateKey privateKey, String input) {
        try {
            Signature dsa = Signature.getInstance("SHA256withECDSA");
            dsa.initSign(privateKey);
            dsa.update(input.getBytes());
            byte[] signature = dsa.sign();
            return bytesToHex(signature);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean verifyECDSASignature(PublicKey publicKey, String data, String signature) {
        try {
            Signature ecdsa = Signature.getInstance("SHA256withECDSA");
            ecdsa.initVerify(publicKey);
            ecdsa.update(data.getBytes(StandardCharsets.UTF_8));
            byte[] sigBytes = hexToBytes(signature);
            return ecdsa.verify(sigBytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static byte[] hexToBytes(String hex) {
        int len = hex.length();
        byte[] bytes = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            bytes[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i + 1), 16));
        }
        return bytes;
    }

    public static String getStringFromKey(Key key) {
        return bytesToHex(key.getEncoded());
    }
}
