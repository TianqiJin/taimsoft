package com.taim.licensegen.model;

import com.taim.licensegen.constant.CryptSession;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Arrays;

/**
 * Created by tjin on 2017-01-24.
 */
public class CryptKey<T> {

    public enum KeyType{
        //AES keys in 256 bits
        SYMMETRIC_KEY,
        //RSA private key
        RSA_PRIVATE_KEY,
        //RSA public key
        RSA_PUBLIC_KEY;
    }

    public static class SymmetricKey{
        private static final int SYMMETRIC_KEY_SIZE = 32;
        private byte[] key;

        public SymmetricKey(byte[] key) throws CryptKeyException{
            if(key.length > SYMMETRIC_KEY_SIZE)
                throw new CryptKeyException("Invalid symmetric key size. It must be " + SYMMETRIC_KEY_SIZE + " bytes");
            else{
                //if the size of input key is less than 32 bytes, we need to append '\0' at the end of the key to ensure the length is exactly 32 b ytes
                this.key = new byte[SYMMETRIC_KEY_SIZE];
                for(int i = 0; i < key.length; i++){
                    this.key[i] = key[i];
                }
                Arrays.fill(this.key, key.length, this.key.length, (byte)'\0');
            }
        }

        public SymmetricKey(String key) throws CryptKeyException {
            if (key.length() > SYMMETRIC_KEY_SIZE)
                throw new CryptKeyException("Invalid symmetric key size. It must be " + SYMMETRIC_KEY_SIZE + " bytes");
            else {
                //if the size of input key is less than 32 bytes, we need to append '\0' at the end of the key to ensure the length is exactly 32 b ytes
                char[] tmp = key.toCharArray();
                this.key = new byte[SYMMETRIC_KEY_SIZE];
                for (int i = 0; i < tmp.length; i++) {
                    this.key[i] = (byte) tmp[i];
                }
                Arrays.fill(this.key, tmp.length, this.key.length, (byte) '\0');
            }
        }

        public byte[] getKey() {
            return key;
        }

        public void setKey(byte[] key) {
            this.key = key;
        }
    }

    public static class CryptKeyException extends Exception {
        public CryptKeyException(String s) {
            super(s);
        }
        public CryptKeyException(String s, Exception x) {
            super(s, x);
        }
    }

    private static final int SHA1_HASH_SIZE = 20;
    private KeyType keyType;
    private T keyData;


    public CryptKey(T keyData, KeyType keyType){
        this.keyData = keyData;
        this.keyType = keyType;
    }

    public CryptKey(){}

    /**
     * Decrypt the private.key file and obtain the real master RSA private key
     * @param privateKeyFile file contains the Taim master RSA private key
     * @return
     * @throws CryptKeyException
     */
    public static CryptKey<PrivateKey> decryptTaimMasterKey(File privateKeyFile, String symmetricKeyPw) throws CryptKeyException{
        try {
            CryptKey<CryptKey.SymmetricKey> cryptKey = new CryptKey<>(new CryptKey.SymmetricKey(symmetricKeyPw), CryptKey.KeyType.SYMMETRIC_KEY);
            byte[] tmpKeyBytes = Files.readAllBytes(privateKeyFile.toPath());

            //salt is stored at the very beginning of the key file and its size is 8 bytes
            byte[] encrypted = Arrays.copyOfRange(tmpKeyBytes, CryptSession.AES_SALT_SIZE, tmpKeyBytes.length);

            //IV is stored in the front of the encrypted private key byte array and its size is 16 bytes
            byte[] initializationVector;
            initializationVector = Arrays.copyOf(encrypted, CryptSession.AES_CIPHER_BLOCK_SIZE);
            encrypted = Arrays.copyOfRange(encrypted, CryptSession.AES_CIPHER_BLOCK_SIZE, encrypted.length);

            IvParameterSpec iv = new IvParameterSpec(initializationVector);
            SecretKeySpec skeySpec = new SecretKeySpec(cryptKey.getKeyData().getKey(), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] decrypted = cipher.doFinal(encrypted);
            PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decrypted));

            return new CryptKey<PrivateKey>(privateKey, CryptKey.KeyType.RSA_PRIVATE_KEY);

        } catch (Exception ex) {
            throw new CryptKeyException(ex.getMessage(), ex);
        }
    }

    public KeyType getKeyType() {
        return keyType;
    }

    public void setKeyType(KeyType keyType) {
        this.keyType = keyType;
    }

    public T getKeyData() {
        return keyData;
    }

    public void setKeyData(T keyData) {
        this.keyData = keyData;
    }
}
