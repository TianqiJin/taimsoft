package com.taim.keygen;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Arrays;

public class KeyGen {
    private static final String PRIVATE_KEY = "taimPrivateKey";
    private static final String PUBLIC_KEY = "taimPublicKey";

    public static void generateRSAKeyPair(String key, String initVector) throws NoSuchAlgorithmException, IOException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair kp = kpg.generateKeyPair();
        PublicKey pub = kp.getPublic();
        PrivateKey pvt = kp.getPrivate();

        Files.write(Paths.get(PRIVATE_KEY), encryptPrivateKey(key, initVector, pvt.getEncoded()));
        Files.write(Paths.get(PUBLIC_KEY), pub.getEncoded());
    }

    private static byte[] encryptPrivateKey(String key, String initVector, byte[] privateKey){
        SecureRandom sr = new SecureRandom();
        byte[] rndBytes = new byte[8];
        sr.nextBytes(rndBytes);
        char[] tmp = key.toCharArray();
        byte[] realKey = new byte[32];
        for (int i = 0; i < tmp.length; i++) {
            realKey[i] = (byte) tmp[i];
        }
        Arrays.fill(realKey, tmp.length, realKey.length, (byte) '\0');
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(realKey, "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(privateKey);
            ByteArrayOutputStream finalArray = new ByteArrayOutputStream();
            finalArray.write(rndBytes);
            finalArray.write(initVector.getBytes("UTF-8"));
            finalArray.write(encrypted);

            return finalArray.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static void decryptPrivateKey(File privateKeyFile, String symmetricKeyPw){
        //if the size of input key is less than 32 bytes, we need to append '\0' at the end of the key to ensure the length is exactly 32 b ytes
        char[] tmp = symmetricKeyPw.toCharArray();
        byte[] key = new byte[32];
        for (int i = 0; i < tmp.length; i++) {
            key[i] = (byte) tmp[i];
        }
        Arrays.fill(key, tmp.length, key.length, (byte) '\0');
        try {
            byte[] tmpKeyBytes = Files.readAllBytes(privateKeyFile.toPath());

            //salt is stored at the very beginning of the key file and its size is 8 bytes
            byte[] encrypted = Arrays.copyOfRange(tmpKeyBytes, 8, tmpKeyBytes.length);

            //IV is stored in the front of the encrypted private key byte array and its size is 16 bytes
            byte[] initializationVector;
            initializationVector = Arrays.copyOf(encrypted, 16);
            encrypted = Arrays.copyOfRange(encrypted, 16, encrypted.length);

            IvParameterSpec iv = new IvParameterSpec(initializationVector);
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] decrypted = cipher.doFinal(encrypted);
            PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decrypted));
            System.out.println(Arrays.toString(privateKey.getEncoded()));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        String key = "TaIMS0ft";
        String initVector = "RandomInitVector";

        generateRSAKeyPair(key, initVector);

        decryptPrivateKey(new File(PRIVATE_KEY), key);
    }
}
