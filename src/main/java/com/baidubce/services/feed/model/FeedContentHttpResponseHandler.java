package com.baidubce.services.feed.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baidubce.http.BceHttpResponse;
import com.baidubce.http.handler.HttpResponseHandler;
import com.baidubce.model.AbstractBceResponse;
import com.baidubce.util.JsonUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.StringBuilderWriter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by cdluoao1 on 2017/7/12.
 */
public class FeedContentHttpResponseHandler implements HttpResponseHandler {
    private final Log log = LogFactory.getLog(getClass());
    @Override
    public boolean handle(BceHttpResponse httpResponse, AbstractBceResponse response) throws Exception {
        InputStream in = httpResponse.getContent();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line=null;
        StringBuilder resultBody=new StringBuilder();
        while (null!=(line=reader.readLine())){
            log.info(line);
            resultBody.append(line);
        }
        JSONObject jsonObject= JSONObject.parseObject(resultBody.toString());
        if (jsonObject.getJSONObject("header").getBoolean("succ")){
            String data=jsonObject.getJSONObject("body").getJSONArray("data").get(0).toString();
            if (response instanceof GetFeedResponse){
                GetFeedResponse getFeedResponse = JSON.parseObject(data,GetFeedResponse.class);
                GetFeedResponse returnResponse=(GetFeedResponse)response;
                returnResponse.setFileId(getFeedResponse.getFileId());
                returnResponse.setIsGenerated(getFeedResponse.getIsGenerated());
                returnResponse.setFilePaths(getFeedResponse.getFilePaths());
                returnResponse.setReportId(getFeedResponse.getReportId());
                returnResponse.setReportFilePath(getFeedResponse.getReportFilePath());
            }else if (response instanceof FilePathFeedTypeResponse){
                FilePathFeedTypeResponse filePathFeedTypeResponse=JSON.parseObject(data,FilePathFeedTypeResponse.class);
                FilePathFeedTypeResponse returnResponse=(FilePathFeedTypeResponse)response;
                returnResponse.copyValue(filePathFeedTypeResponse);
            }
        }

        IOUtils.closeQuietly(reader);
        return true;
    }
}
