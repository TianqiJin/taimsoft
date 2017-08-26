package com.coverity.licensetools.util;

import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

import javax.crypto.SecretKey;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class PKCS5 {
    private static final String ENCODING = "US-ASCII";
    public PKCS5(byte[] salt, String pass, int iteration_count) {
        this.salt = salt;
        this.pass = pass;
        this.iteration_count = iteration_count;
    }
    private final byte[] salt;
    private final String pass;
    private final int iteration_count;

    private void pbkdf2_F(javax.crypto.Mac mac, int idx,
             byte[] output, int offset) {
        mac.update(salt);
        byte[] idx_bytes = new byte[4];
        // Big endian
        for(int i = 0; i < 4; ++i) {
            idx_bytes[3 - i] = (byte)(idx >> (8 * i));
        }

        mac.update(idx_bytes);
        for(int i = 1; i < iteration_count; ++i) {
            byte[] digest = mac.doFinal();
            mac.update(digest);
        }
        try {
            mac.doFinal(output, offset);
        } catch(ShortBufferException e) {
            throw new Error(e);
        }
    }

    private static final int SHA1_HASH_SIZE = 20;

    /**
     * The key derivation function 2 as described in PKCS#5 (uses
     * hmacWithSHA1)
     * See http://tools.ietf.org/html/2898#page-18
     **/
    byte [] pbkdf2(int dkLen) {
        int l = ((dkLen - 1) / SHA1_HASH_SIZE) + 1;
        int r = dkLen % SHA1_HASH_SIZE;
        javax.crypto.Mac mac;
        try {
            mac = javax.crypto.Mac.getInstance("HmacSHA1");
            mac.init(new SecretKeySpec(pass.getBytes(ENCODING), "HMAC-SHA1"));
        } catch(InvalidKeyException e) {
            throw new Error(e);
        } catch(NoSuchAlgorithmException e) {
            throw new Error("Missing algorithm: " + e);
        } catch (UnsupportedEncodingException e) {
            throw new Error("Unsupported encoding: " + e);
        }

        int i;
        byte[] rv = new byte[dkLen];
        int T = 0;
        for(i = 1; i < l; ++i) {
            pbkdf2_F(mac, i, rv, T);
            T += SHA1_HASH_SIZE;
        }
        byte []  tmp_digest = new byte[SHA1_HASH_SIZE];
        pbkdf2_F(mac, i, tmp_digest, 0);
        System.arraycopy(tmp_digest, 0, rv, T, r);
        return rv;
    }

    // Makes a BufferedBlockCipher
    // if mode is true, the cipher is in encryption mode else decryption mode.
    private BufferedBlockCipher makeCipher(boolean mode) {

        //AES/CBC/PKCS5Padding algorithm implemented using BouncyCastleLightWeight API
        //Probably there is a bug in the pbkdf2 implementation -- Detecting it and fixing it
        // is not useful because the encryption scheme has to be maintained for backward
        // compatibility.
        BufferedBlockCipher cipher = new PaddedBufferedBlockCipher(
                new CBCBlockCipher(new AESEngine()));

        //32 accounts for 256 bits
        byte[] data = pbkdf2(32 + cipher.getBlockSize());
        //generates a AES 256 bit key
        SecretKey sk = new SecretKeySpec(data, 0, 32, "AES");

        //initialization vector is used. it is generated from data starting
        // at the 16th byte. the length of the iv is equal to the
        // cipher's block size -- in this case, it is 16.
        KeyParameter keyParam = new KeyParameter(sk.getEncoded());
        ParametersWithIV piv = new ParametersWithIV(keyParam, data, 32, cipher.getBlockSize());


        //initialize the cipher
        cipher.init(mode, piv);

        return cipher;
    }

    // Return an encrypted output stream based on the
    // password parameters (using AES128/CBC) as in PBES2
    private BufferedBlockCipher make_output_pbes2() {
        return makeCipher(true); //true for encrypt;
    }
    // Return an encrypted input stream based on the
    // password parameters (using AES128/CBC) as in PBES2
    private BufferedBlockCipher make_input_pbes2() {
        return makeCipher(false); //false for decrypt;
    }

    /**
     * given a cipher and text, this procedure returns a sequence of
     * bytes corresponding the encrypted/decrypted text.
     */
    private byte[] process(BufferedBlockCipher cipher, byte[] text) throws GeneralSecurityException {
        byte[] result = new byte[cipher.getOutputSize(text.length)];
        int len = cipher.processBytes(text, 0, text.length, result, 0);
        try {
            int last_block_size = cipher.doFinal(result, len);
            byte [] ret = new byte [len + last_block_size];
            System.arraycopy(result, 0, ret, 0, len + last_block_size);
            return ret;
        } catch (CryptoException ce) {
            throw new GeneralSecurityException("Error in PBES_Process -- Check Bouncy Castle Cipher in PKCS5.java", ce);
        }
    }

    public byte[] encrypt(byte[] toEncrypt) throws GeneralSecurityException {
        BufferedBlockCipher cipher = make_output_pbes2();
        return process(cipher, toEncrypt);
    }

    public byte[] decrypt(byte[] toDecrypt) throws GeneralSecurityException {
        BufferedBlockCipher cipher = make_input_pbes2();
        return process(cipher, toDecrypt);
    }

    public static void main(String [] argv) throws Exception {
        byte[] salt = {1, 2, 3, 4, 5, 6, 7, 8};
        String pass = "Hello";
        PKCS5 pk = new PKCS5(salt, pass, 1000);

        String plaintext = "A relatively long text, it should be longer than a block size, to test CBC. AES block size is 16 bytes, this should do.";

        byte[] encrypted = pk.encrypt(plaintext.getBytes(ENCODING));
        System.out.println("Encrypted bytes:");
        for(int i = 0; i < encrypted.length; ++i) {
            System.out.print(", " + encrypted[i]);
        }
        System.out.println("");

        byte[] btr = pk.decrypt(encrypted);
        System.out.println("Decrypted: " + new String(btr, ENCODING));

    }
}