package com.qlangtech.tis;


import org.apache.commons.lang3.StringUtils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;

public class OssConfig {
    public final String cfgName;

    public OssConfig(String cfgName) {
        this.cfgName = cfgName;
    }

    private String endpoint;
    private String accessId;
    private String accessSecret;
    private String bucket;

    private OSS oss;

    public String getAccessSecret() {
        return accessSecret;
    }

    public void setAccessSecret(String accessSecret) {
        this.accessSecret = accessSecret;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getAccessId() {
        return accessId;
    }

    public void setAccessId(String accessId) {
        this.accessId = accessId;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public void validate() {
        if (StringUtils.isEmpty(this.accessId) || StringUtils.isEmpty(this.endpoint) || StringUtils.isEmpty(this.bucket)
                || StringUtils.isEmpty(this.accessSecret)) {
            throw new IllegalStateException("illegal " + this.toString());
        }
    }


    public OSS getOSS() {
        if (oss == null) {
            oss = new OSSClientBuilder().build(this.getEndpoint(), this.getAccessId(), this.getAccessSecret());
        }
        return oss;
    }

    @Override
    public String toString() {
        return "OssConfig{" +
                "endpoint='" + endpoint + '\'' +
                ", accessId='" + accessId + '\'' +
                ", accessSecret='" + accessSecret + '\'' +
                ", bucket='" + bucket + '\'' +
                '}';
    }
}
