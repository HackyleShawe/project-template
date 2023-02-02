package com.hackyle.template.common.util;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * ID混淆工具类
 * 注意：
 *  混淆规则不应该轻易改动
 *  由于混淆后的ID可能会用作URL的一部分，需要替换Base64中的不符合URL规则的字符
 */
public class IDUtils {
    private static final String DEFAULT_PW = "blog.hackyle.com:default.pw";

    public static long timestampID() {
        return System.currentTimeMillis();
    }

    public static List<String> encrypt(List<Long> sourceIdList) {
        List<String> encryptedIdList = new ArrayList<>(sourceIdList.size());

        for (Long id : sourceIdList) {
            encryptedIdList.add(encryptByAES(id));
        }

        return encryptedIdList;
    }

    public static List<Long> decrypt(List<String> sourceIdList) {
        List<Long> decryptIdList = new ArrayList<>(sourceIdList.size());

        for (String id : sourceIdList) {
            decryptIdList.add(decryptByAES(id));
        }
        return decryptIdList;
    }


    /**
     * 加密
     * @param sourceObj 加密前的ID为Long
     * @param targetObj 加密后的ID为String
     */
    public static void encrypt(Object sourceObj, Object targetObj) {
        encrypt(sourceObj, "id", targetObj, "setId");
    }

    /**
     * 加密
     * @param sourceObj
     * @param sourceField 指定加密的字段
     * @param targetObj
     * @param targetMethod 指定加密后赋值给targetObj调用的方法
     */
    public static void encrypt(Object sourceObj, String sourceField, Object targetObj, String targetMethod) {
        if(sourceObj == null) {
            throw new RuntimeException("encrypt: source obj can't be null.");
        }
        if(targetObj == null) {
            throw new RuntimeException("encrypt: target obj can't be null.");
        }

        sourceField = StringUtils.isBlank(sourceField) ? "id" : sourceField;
        targetMethod = StringUtils.isBlank(targetMethod) ? "setId" : targetMethod;

        try {
            Field sourceIdField = sourceObj.getClass().getDeclaredField(sourceField);
            sourceIdField.setAccessible(true);
            Object id = sourceIdField.get(sourceObj);
            sourceIdField.setAccessible(false);

            //加密
            String encryptedId = encryptByAES(Long.parseLong(id.toString()));

            Method targetIdField = targetObj.getClass().getMethod(targetMethod, String.class);
            targetIdField.invoke(targetObj, encryptedId);
        } catch (Exception e) {
            throw new RuntimeException("encrypt id has been occurred exception.", e);
        }
    }

    /**
     * 解密
     * @param sourceObj 解密前ID为String
     * @param targetObj 解密后ID为Long
     */
    public static void decrypt(Object sourceObj, Object targetObj) {
        decrypt(sourceObj, "id", targetObj, "setId");
    }

    /**
     * 解密
     * @param sourceObj
     * @param sourceField 指定解密的字段
     * @param targetObj
     * @param targetMethod 指定解密后赋值给targetObj调用的方法
     */
    public static void decrypt(Object sourceObj, String sourceField, Object targetObj, String targetMethod) {
        if(sourceObj == null) {
            throw new RuntimeException("decrypt: source obj can't be null.");
        }
        if(targetObj == null) {
            throw new RuntimeException("decrypt: target obj can't be null.");
        }

        sourceField = StringUtils.isBlank(sourceField) ? "id" : sourceField;
        targetMethod = StringUtils.isBlank(targetMethod) ? "setId" : targetMethod;

        try {
            Field sourceIdField = sourceObj.getClass().getDeclaredField("id");
            sourceIdField.setAccessible(true);
            Object id = sourceIdField.get(sourceObj);
            sourceIdField.setAccessible(false);

            //解密
            long decryptedId = decryptByAES(id.toString());

            Method targetIdField = targetObj.getClass().getMethod("setId", Long.class);
            targetIdField.invoke(targetObj, decryptedId);
        } catch (Exception e) {
            throw new RuntimeException("decrypt id has been occurred exception.", e);
        }
    }

    /**
     * 加密
     * 强制要求: sourceObjList与targetObjList的下标一一对应
     * @param sourceObjList 加密前的ID为Long
     * @param targetObjList 加密后的ID为String
     */
    public static void batchEncrypt(List<?> sourceObjList, List<?> targetObjList) {
        batchEncrypt(sourceObjList, "id", targetObjList, "setId");
    }

    /**
     * 加密
     * @param sourceObjList
     * @param sourceField
     * @param targetObjList
     * @param targetMethod
     */
    public static void batchEncrypt(List<?> sourceObjList, String sourceField, List<?> targetObjList, String targetMethod) {
        if(sourceObjList == null || sourceObjList.size() < 1) {
            return;
        }
        if(targetObjList == null || targetObjList.size() <1) {
            throw new RuntimeException("encrypt batch: targetObjList can't be null or empty.");
        }
        if(sourceObjList.size() != targetObjList.size()) {
            throw new RuntimeException("encrypt batch: sourceObjList and targetObjList size must be equal.");
        }

        sourceField = StringUtils.isBlank(sourceField) ? "id" : sourceField;
        targetMethod = StringUtils.isBlank(targetMethod) ? "setId" : targetMethod;

        for (int i = 0, len=sourceObjList.size(); i < len; i++) {
            Object sourceObj = sourceObjList.get(i);
            Object targetObj = targetObjList.get(i);
            encrypt(sourceObj, sourceField, targetObj, targetMethod);
        }
    }

    /**
     * 解密
     * 强制要求: sourceObjList与targetObjList的下标一一对应
     * @param sourceObjList 解密前ID为String
     * @param targetObjList 解密后ID为Long
     */
    public static void batchDecrypt(List<?> sourceObjList, List<?> targetObjList) {
        batchDecrypt(sourceObjList, "id", targetObjList, "setId");
    }

    /**
     * 解密
     * @param sourceObjList
     * @param sourceField
     * @param targetObjList
     * @param targetMethod
     */
    public static void batchDecrypt(List<?> sourceObjList, String sourceField, List<?> targetObjList, String targetMethod) {
        if(sourceObjList == null || sourceObjList.size() < 1) {
            return;
        }
        if(targetObjList == null || targetObjList.size() <1) {
            throw new RuntimeException("decrypt batch: targetObjList can't be null or empty.");
        }

        if(sourceObjList.size() != targetObjList.size()) {
            throw new RuntimeException("decrypt batch: sourceObjList and targetObjList size must be equal.");
        }

        sourceField = StringUtils.isBlank(sourceField) ? "id" : sourceField;
        targetMethod = StringUtils.isBlank(targetMethod) ? "setId" : targetMethod;

        for (int i = 0, len=sourceObjList.size(); i < len; i++) {
            Object sourceObj = sourceObjList.get(i);
            Object targetObj = targetObjList.get(i);
            decrypt(sourceObj, sourceField, targetObj, targetMethod);
        }
    }

    /**
     * 将ID对称加密加密
     * Base64的定义：用 64 个可打印字符来表示二进制数据，小写字母 a-z、大写字母 A-Z、数字 0-9、符号"+"、"/"（再加上作为垫字的"="，实际上是 65 个字符）。
     */
    public static String encryptByAES(long id) {
        String encrypt = AESUtils.encrypt(String.valueOf(id), DEFAULT_PW);

        //由于返回的是Base64字符串，作为URL时可能存在冲突，所以再进行转换
        encrypt = encrypt.replaceAll("\\+", "-")
                .replaceAll("/", ".")
                .replaceAll("=","_");
        return encrypt;
    }

    /**
     * 将ID对称解密
     */
    public static long decryptByAES(String encryptedId) {
        if(StringUtils.isBlank(encryptedId)) {
            throw new RuntimeException("encryptedId为空");
        }

        encryptedId = encryptedId.replaceAll("-", "+")
                .replaceAll("\\.", "/")
                .replaceAll("_","=");

        String decryptedId = AESUtils.decrypt(encryptedId, DEFAULT_PW);
        return Long.parseLong(decryptedId);
    }


    public static void main(String[] args) throws Exception {
        long start = timestampID();
        testAES();
        System.out.println(timestampID() - start);
    }

    private static void testAES() {
        long id = timestampID();
        System.out.println(id);
        String encryptByAES = encryptByAES(id);
        System.out.println(encryptByAES);

        long decryptByAES = decryptByAES(encryptByAES);
        System.out.println(decryptByAES);
    }
}
