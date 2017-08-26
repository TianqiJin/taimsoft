package com.coverity.licensetools.constant;

/**
 * Created by tjin on 2017-01-31.
 */
public class CryptSession {
    // Written to the beginning of every stream that is being encrypted
    // Used to recognize when a correct key has been used to decrypt
    public static final byte[] CIPHER_SESSION_MAGIC = new byte[]{
            0x3f,0x3f,0x3f,0x3f,0x3f,0x3f,0x3f,0x3f,0x3f,0x3f,0x3f,0x3f,0x3f,0x3f,0x3f,0x3f
    };
    //The size of AES cipher block. It must be 16 bytes
    public static final int AES_CIPHER_BLOCK_SIZE = 16;
    //The size of AES salt block. We choose 8 bytes as its size
    public static final int AES_SALT_SIZE = 8;
    // The symmetrically encrypted part is a chain of blocks of this size.
    // At the end of each block, a byte indicates if another block comes
    // after it:
    // - if it's 0, there's another block.
    // - Otherwise, it's the number of encryption blocks that contains any
    // data.
    // In that case, the one-before-last byte indicates how many bytes are
    public static final int CRYPT_SESSION_CHAINED_BLOCK_SIZE = 80;
}
