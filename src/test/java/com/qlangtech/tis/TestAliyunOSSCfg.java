package com.qlangtech.tis;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * @author: 百岁（baisui@qlangtech.com）
 * @create: 2023-07-27 16:21
 **/
public class TestAliyunOSSCfg {
    @Rule
    public TemporaryFolder tmpFile = new TemporaryFolder();

    private static final String FILE_NAME = "test-mvn-config";

    @Test
    public void testInstanceCreate() throws Exception {

        File userHome = tmpFile.newFolder("userHome");

        try (InputStream stream = TestAliyunOSSCfg.class.getResourceAsStream(FILE_NAME)) {
            Assert.assertNotNull(stream);
            File ossCfgFile = new File(userHome, AliyunOSSCfg.KEY_ALIYUN_OSS_DIR + File.separator + FILE_NAME + ".properties");
            FileUtils.writeByteArrayToFile(ossCfgFile, IOUtils.toByteArray(stream));
        }

        System.setProperty(AliyunOSSCfg.KEY_SYS_USER_HOME, userHome.getAbsolutePath());


        AliyunOSSCfg ossRepo = AliyunOSSCfg.getInstance();
        Assert.assertEquals(1, ossRepo.ossRepo.size());
        OssConfig oss = ossRepo.getOss(FILE_NAME);
        Assert.assertNotNull("cfgName:" + FILE_NAME + " relevant  oss config can not be null", oss);

//        endpoint=http://xxxxx-hangzhou.aliyuncs.com
//        accessKey=test_accessKey
//        secretKey=test_secretKey
//        bucketName=test_bucketName_123

        Assert.assertEquals("test_accessKey", oss.getAccessId());
        Assert.assertEquals("test_secretKey", oss.getAccessSecret());
        Assert.assertEquals("test_bucketName_123", oss.getBucket());
        Assert.assertEquals("http://xxxxx-hangzhou.aliyuncs.com", oss.getEndpoint());
    }
}
