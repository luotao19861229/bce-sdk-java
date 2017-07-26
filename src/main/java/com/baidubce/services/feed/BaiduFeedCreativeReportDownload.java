package com.baidubce.services.feed;

/**
 * Created by cdluoao1 on 2017/7/25.
 * 下载百度信息流物料层级结构-
 * 输出报表
 */
public class BaiduFeedCreativeReportDownload {
    public static void main(String[] args) {
        if (args.length<2){
            System.out.println("Usage: BaiduFeedCreativeReportDownload accountName  date<YYYY-MM-DD>");
            System.exit(1);
        }
        BaiduFeedDownloadService baiduFeedDownloadService = BaiduFeedDownloadService.getInstance(args);
        baiduFeedDownloadService.downloadReportFeedFile();
    }
}
