package com.hackyle.template.common.util;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * AES加密解密工具类，并进行Base64编解码
 */
public class AESUtils {
    /** 加密算法 */
    private static final String ENCRYPT_ALGORITHM = "AES";
    /** 算法名称/加密模式/数据填充方式，默认：AES/ECB/PKCS5Padding */
    private static final String CIPHER_NAME = "AES/ECB/PKCS5Padding";
    private static final String KEY_SPEC = "AES";
    private static final String SECRET_RANDOM_ALGORITHM = "SHA1PRNG";

    /**
     * 加密后转为Base64串
     * @param plaintext 待加密的数据
     * @param password 密码
     * @return 加密后的Base64
     */
    public static String encrypt(String plaintext, String password) {
        byte[] passwordKeyBytes = passwordKey(password);
        byte[] plaintextBytes = plaintext.getBytes(StandardCharsets.UTF_8);
        byte[] encryptedBytes = doFinal(Cipher.ENCRYPT_MODE, plaintextBytes, passwordKeyBytes);
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    /**
     * 解密前解码Base64
     * @param ciphertext 待解密的数据
     * @param password 密码
     * @return 解密后的字节数组
     */
    public static String decrypt(String ciphertext, String password) {
        byte[] ciphertextBytes = Base64.getDecoder().decode(ciphertext);
        byte[] passwordKeyBytes = passwordKey(password);
        byte[] decryptedBytes = doFinal(Cipher.DECRYPT_MODE, ciphertextBytes, passwordKeyBytes);
        return new String(decryptedBytes);
    }

    /**
     * 根据password生成AES所需的128、192、256位的密码
     * @param password 密码
     */
    private static byte[] passwordKey(String password) {
        /*
         * AES有三种加密类型：AES-128，AES-192，AES-256
         * 其中的数字指的是加密秘钥的长度。例如，128：表明加密或解密的秘钥长度
         * 注意：加密或解密时所用的秘钥长度必须一致
         *
         * keySize的长度必须是128、192、256的其中之一，否则报错：java.security.InvalidKeyException: Invalid AES key length
         */
        KeyGenerator keyGenerator = null;
        byte[] passwordKeyBytes = null;
        try {
            //利用用户密码作为种子进行初始化
            SecureRandom secureRandom = SecureRandom.getInstance(SECRET_RANDOM_ALGORITHM);
            secureRandom.setSeed(password.getBytes(StandardCharsets.UTF_8));

            keyGenerator = KeyGenerator.getInstance(ENCRYPT_ALGORITHM);
            keyGenerator.init(128, secureRandom);
            SecretKey secretKey = keyGenerator.generateKey();//生成key
            passwordKeyBytes = secretKey.getEncoded();//得到key的字节数组
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("根据password生成AES所需的128、192、256位的密码出错", e);
        }
        return passwordKeyBytes;
    }

    /**
     * 执行加密或解密
     * @param mode 模式：解密模式、加密模式
     * @param data 待加/解密的数据
     * @param key 密码
     */
    private static byte[] doFinal(int mode, byte[] data, byte[] key) {
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_NAME);
            SecretKeySpec secretKey = new SecretKeySpec(key, KEY_SPEC);
            cipher.init(mode, secretKey);
            return cipher.doFinal(data);
        } catch (Exception e) {
            throw new RuntimeException("执行加密或解密", e);
        }
    }

    public static void main(String[] args) {
        String data = "1658814571488";
        String password = "kyle";

        String encrypt = encrypt(data, password);
        System.out.println(encrypt);

        String decrypt = decrypt(encrypt, password);
        System.out.println(decrypt);
    }
}
