package com.hackyle.template.common.util;

import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class HashUtils {
    /** 默认的盐 */
    public static final String DEFAULT_SALT = "blog.hackyle.com:default.salt";
    private static final String MD5 = "MD5";
    private static final String SHA1 = "SHA-1";
    private static final String SHA256 = "SHA-256";

    private static String hash(String data, String salt, String algorithm) throws NoSuchAlgorithmException {
        if(StringUtils.isBlank(data)) {
            throw new RuntimeException("进行哈希操作的数据不能为空");
        }
        data = StringUtils.isBlank(salt) ? data + DEFAULT_SALT: data + salt;
        String result = "";
        MessageDigest md5 = null;
        md5 = MessageDigest.getInstance(algorithm);
        md5.update(data.getBytes(StandardCharsets.UTF_8));
        result = Base64.getEncoder().encodeToString(md5.digest());
        return result;
    }


    public static String md5(String data) throws NoSuchAlgorithmException {
        return hash(data, null, MD5);
    }
    public static String md5(String data, String salt) throws NoSuchAlgorithmException {
        return hash(data, salt, MD5);
    }

    public static String sha1(String data) throws NoSuchAlgorithmException {
        return hash(data, null, SHA1);
    }
    public static String sha1(String data, String salt) throws NoSuchAlgorithmException {
        return hash(data, salt, SHA1);
    }

    public static String sha256(String data) throws NoSuchAlgorithmException {
        return hash(data, null, SHA256);
    }
    public static String sha256(String data, String salt) throws NoSuchAlgorithmException {
        return hash(data, salt, SHA256);
    }


    public static void main(String[] args) throws NoSuchAlgorithmException {

        String md5 = md5("kyleshawe");
        System.out.println(md5);

        String sha1 = sha1("kyleshawe");
        System.out.println(sha1);

        String sha256 = sha256("kyleshawe");
        System.out.println(sha256);
    }
}
