package com.hackyle.template.common.pojo;

import java.util.Map;

public class JwtPayload {
    /** 编码 */
    private String id;
    /** 主体 */
    private String subject;
    /** 受众 */
    private String[] audience;
    /** 签发人 */
    private String issuer;
    /** JWT的需要携带的其他数据: 可以放置任何数据（例如敏感数据） */
    private Map<String, Object> payloadExtendMap;

    public JwtPayload(String id, String subject, String issuer) {
        this.id = id;
        this.subject = subject;
        this.issuer = issuer;
    }

    public JwtPayload(String id, String subject, String issuer, Map<String, Object> payloadExtendMap) {
        this.id = id;
        this.subject = subject;
        this.issuer = issuer;
        this.payloadExtendMap = payloadExtendMap;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String[] getAudience() {
        return audience;
    }

    public void setAudience(String[] audience) {
        this.audience = audience;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public Map<String, Object> getPayloadExtendMap() {
        return payloadExtendMap;
    }

    public void setPayloadExtendMap(Map<String, Object> payloadExtendMap) {
        this.payloadExtendMap = payloadExtendMap;
    }
}
