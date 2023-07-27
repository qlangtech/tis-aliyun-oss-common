package com.qlangtech.tis;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

/**
 * @author: 百岁（baisui@qlangtech.com）
 * @create: 2023-07-27 14:47
 **/
public class AliyunOSSCfg {

    public static final String KEY_SYS_USER_HOME = "user.home";
    public static final String KEY_ALIYUN_OSS_DIR = "aliyun-oss";

    private static AliyunOSSCfg instance;

    Map<String, OssConfig> ossRepo;

    private AliyunOSSCfg() {

    }

    public OssConfig getOss(String cfgName) {
        return Objects.requireNonNull(this.ossRepo.get(cfgName)
                , "cfgName:" + cfgName + " relevant OssConfig can not be null");
    }

    public static AliyunOSSCfg getInstance() {
        if (instance == null) {
            synchronized (AliyunOSSCfg.class) {
                if (instance == null) {

                    instance = new AliyunOSSCfg();
                    Map<String, OssConfig> ossRepo
                            = instance.ossRepo = new HashMap<>();

                    //  String mvnOssConfigName = "aliyun-oss/mvn-config.properties";

                    File ossCfgRoot = new File(System.getProperty(KEY_SYS_USER_HOME), KEY_ALIYUN_OSS_DIR);

                    //  File cfgFile = new File(System.getProperty("user.home"), mvnOssConfigName);
                    Collection<File> cfgs = FileUtils.listFiles(ossCfgRoot, new String[]{"properties"}, false);
                    for (File cfg : cfgs) {
                        OssConfig oss = createOssConfig(cfg);
                        ossRepo.put(oss.cfgName, oss);
                    }
                    instance.ossRepo = Collections.unmodifiableMap(ossRepo);
                }
            }
        }
        return instance;
    }

    private static OssConfig createOssConfig(File cfgFile) {
//        String mvnOssConfigName = "aliyun-oss/mvn-config.properties";
//        File cfgFile = new File(System.getProperty("user.home"), mvnOssConfigName);
        if (!cfgFile.exists()) {
            throw new IllegalStateException("oss config file is not exist:" + cfgFile.getAbsoluteFile() + "\n config.properties template is ");
        }
        Properties props = new Properties();
        try {
            try (InputStream reader = FileUtils.openInputStream(cfgFile)) {
                props.load(reader);
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }

        String endpoint = getProp(props, "endpoint");
        String accessKeyId = getProp(props, "accessKey");
        String secretKey = getProp(props, "secretKey");
        String bucketName = getProp(props, "bucketName");

        final OssConfig oss = new OssConfig(StringUtils.substringBefore(cfgFile.getName(), "."));
        oss.setAccessId(accessKeyId);
        oss.setBucket(bucketName);
        oss.setEndpoint(endpoint);
        oss.setAccessSecret(secretKey);

        oss.validate();
        return oss;
    }

    private static String getProp(Properties props, String key) {
        String value = props.getProperty(key);
        if (StringUtils.isEmpty(value)) {
            throw new IllegalArgumentException("key:" + key + " relevant value can not be null");
        }
        return value;
    }

}
