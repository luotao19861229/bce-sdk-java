package com.baidubce.services.feed;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

/**
 * Created by cdluoao1 on 2017/7/24.
 */
public class FileDownloadUtil {

    private static int BUFFER_SIZE = 8096; // 缓冲区大小

    public static void getURLResource(String ourputFile, String urlStr)throws Exception {
        FileOutputStream fos = null;
        BufferedInputStream bis = null;
        HttpURLConnection httpUrl = null;
        try {
            URL url = null;
            byte[] buf = new byte[BUFFER_SIZE];
            int size = 0;
            // 建立链接
            url = new URL(urlStr);
            httpUrl = (HttpURLConnection) url.openConnection();
            // 连接指定的资源
            httpUrl.connect();
            // 获取网络输入流
            bis = new BufferedInputStream(httpUrl.getInputStream());
            // 建立文件
            File file = new File(ourputFile);
            if (!file.exists()) {
                file.createNewFile();
            }
            fos = new FileOutputStream(file);
            while ((size = bis.read(buf)) != -1) {
                fos.write(buf, 0, size);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (fos != null) {
                fos.close();
            }
            if (bis != null) {
                bis.close();
            }
            if (httpUrl != null) {
                httpUrl.disconnect();
            }
        }
    }

    public static void wirteFile(String outputStr, String filePath)throws Exception {
        FileOutputStream fos = null;
        try {
            // 建立文件
            File file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            }
            Properties properties = System.getProperties();
            String encodingStr = properties.getProperty("file.encoding");
            fos = new FileOutputStream(file);
            fos.write(outputStr.getBytes("x-UTF-16LE-BOM"));
        } catch (Exception e) {
            throw e;
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
    }
}
