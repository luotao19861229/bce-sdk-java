package com.baidubce.services.feed;

import com.baidubce.AbstractBceClient;
import com.baidubce.BceClientConfiguration;
import com.baidubce.BceClientException;
import com.baidubce.auth.BceV1Signer;
import com.baidubce.auth.DefaultBceCredentials;
import com.baidubce.auth.SignOptions;
import com.baidubce.http.Headers;
import com.baidubce.http.HttpMethodName;
import com.baidubce.http.handler.BceMetadataResponseHandler;
import com.baidubce.http.handler.HttpResponseHandler;
import com.baidubce.internal.InternalRequest;
import com.baidubce.internal.RestartableInputStream;
import com.baidubce.model.AbstractBceResponse;
import com.baidubce.services.feed.model.*;
import com.baidubce.services.sms.SmsConstant;
import com.baidubce.util.HttpUtils;
import com.baidubce.util.JsonUtils;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.util.*;
import java.util.zip.GZIPInputStream;

/**
 * Created by cdluoao1 on 2017/7/12.
 */
public class BaiduFeedDownloadService extends AbstractBceClient {

    private  final  Log log = LogFactory.getLog(BaiduFeedDownloadService.class);

    protected static final HttpResponseHandler[] FEED_HANDLERS = new HttpResponseHandler[] {
            new BceMetadataResponseHandler(), new FeedContentHttpResponseHandler()};
    private static String ACCOUNT_HIRACHY_FILE="baidu-feed-creative-id-map.tsv";
    private static String CREATIVE_REPORT_FILE="baidu-feed-creative-report-daily.tsv";

    private  DefaultBceCredentials bceCredentials;

    private  FeedAccount superAccount;

    private String saveFilePath;

    private String accountName;

    private String reportDate;



    public BaiduFeedDownloadService(BceClientConfiguration config) {
        super(config,FEED_HANDLERS );
    }

    public static BaiduFeedDownloadService getInstance(String[] args){
        BceClientConfiguration config=new BceClientConfiguration();
        config.setEndpoint("http://sem.baidubce.com/");
        return new BaiduFeedDownloadService(config).init(args);
    }

    //初始化
    public  BaiduFeedDownloadService init(String[] args){
        accountName=args[0];
        reportDate=args[1];
        Map<String,String> feedConfKeyValue=getFeedConfKeyValueFromConfigFile();
        bceCredentials=new DefaultBceCredentials(feedConfKeyValue.get("accessKeyId"),feedConfKeyValue.get("secretKey"));
        superAccount=new FeedAccount(feedConfKeyValue.get("SuperAccountName"),feedConfKeyValue.get("SuperPassword"),
                feedConfKeyValue.get("SuperBceUser"));
        saveFilePath=feedConfKeyValue.get("saveFilePath");
        Preconditions.checkNotNull(saveFilePath,"saveFilePath can not be null");
        return this;
    }

    protected Map<String, String> getFeedConfKeyValueFromConfigFile() {
        Map<String, String> feedConfKeyValue  = Maps.newHashMap();
        Properties prop = new Properties();
        BufferedReader reader=null;
        try {
            InputStream in =  BaiduFeedDownloadService.class.getResourceAsStream("/feed_conf.properties");
            reader = new BufferedReader(new InputStreamReader(in,"utf-8"));
            prop.load(reader);     ///加载属性列表
            Iterator<String> it=prop.stringPropertyNames().iterator();
            while(it.hasNext()){
                String key=it.next();
                feedConfKeyValue.put(key,prop.getProperty(key));
            }
            in.close();
        }catch (Exception e){
            log.error("read properties error ",e);
        }finally {
            IOUtils.closeQuietly(reader);
        }
        return feedConfKeyValue;
    }


    public void downloadAccount(){
        String fileId = this.getFeedFileId(accountName);
        // 如果报告没有准备好，重试1小時
        boolean isReady = false;
        for (int i = 0; i < 360; i++) {
            isReady ="3".equals(getFeedFileState(accountName, fileId));
            if (isReady) {
                break;
            }
            try {
                Thread.sleep(10000L);
            } catch (InterruptedException e) {
                log.error("BaiduFeedDownloadService downloadAccount", e);
            }
        }
        if (!isReady) {
            log.info("wait one hour,file not ready,");
            System.exit(1);
        }
        FilePathFeedTypeResponse filePathFeedTypeResponse=getFeedFilePath(accountName,fileId);
        dealFilePathFeedTypeResponse(filePathFeedTypeResponse);
    }

    private void dealFilePathFeedTypeResponse(FilePathFeedTypeResponse filePathFeedTypeResponse){
//        downloadReportFile(filePathFeedTypeResponse.getFeedAccountFilePath(),"account.gz");
        downloadReportFile(filePathFeedTypeResponse.getFeedCampaignFilePath(),"campaign.gz");
        downloadReportFile(filePathFeedTypeResponse.getFeedAdgroupFilePath(),"adgroup.gz");
        downloadReportFile(filePathFeedTypeResponse.getFeedCreativeFilePath(),"creative.gz");
        BufferedReader creativeReader=null;
        BufferedReader adgroupReader=null;
        BufferedReader campaignReader=null;
        BufferedWriter accountCreativeHierachyWriter=null;
        String line=null;
        try {
            campaignReader = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(getSaveFileFullPath("campaign.gz"))), "gbk"));
            campaignReader.readLine();//ignore tsv head  campaignFeedId	campaignFeedName
            Map<String,String> campaignIdToNameMap=Maps.newHashMap();
            while (null!=(line=campaignReader.readLine())){
                String[] lineInfos = line.split("\t");
                campaignIdToNameMap.put(lineInfos[0],lineInfos[1].replaceAll("^\"|\"$", ""));
            }
            adgroupReader = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(getSaveFileFullPath("adgroup.gz"))), "gbk"));
            adgroupReader.readLine();// ignore tsv head adgroupFeedId	campaignFeedId	adgroupFeedName
            Map<String,String> adgroupIdToNameMap=Maps.newHashMap();
            while (null!=(line=adgroupReader.readLine())){
                String[] lineInfos = line.split("\t");
                adgroupIdToNameMap.put(lineInfos[0],campaignIdToNameMap.get(lineInfos[1])+"\t"+lineInfos[2].replaceAll("^\"|\"$", ""));
            }
            creativeReader = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(getSaveFileFullPath("creative.gz"))), "gbk"));
            accountCreativeHierachyWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(getSaveFileFullPath(ACCOUNT_HIRACHY_FILE)), "gbk"));
            accountCreativeHierachyWriter.write("account_name\tcampaign_name\tgroup_name\tad_position\tcreative_id");accountCreativeHierachyWriter.newLine();
            creativeReader.readLine();// ignore tsv head  creativeFeedId	adgroupFeedId	creativeFeedName
            while (null!=(line=creativeReader.readLine())){
                String[] lineInfos = line.split("\t");
                accountCreativeHierachyWriter.write(accountName);
                accountCreativeHierachyWriter.write("\t");
                accountCreativeHierachyWriter.write(adgroupIdToNameMap.get(lineInfos[1]));
                accountCreativeHierachyWriter.write("\t");
                accountCreativeHierachyWriter.write(lineInfos[2].replaceAll("^\"|\"$", ""));
                accountCreativeHierachyWriter.write("\t");
                accountCreativeHierachyWriter.write(lineInfos[0]);
                accountCreativeHierachyWriter.newLine();
            }
        } catch (IOException e) {
            log.error("dealFilePathFeedTypeResponse error",e);
        }finally {
            IOUtils.closeQuietly(campaignReader);
            IOUtils.closeQuietly(adgroupReader);
            IOUtils.closeQuietly(creativeReader);
            IOUtils.closeQuietly(accountCreativeHierachyWriter);
        }
        new File(getSaveFileFullPath("campaign.gz")).deleteOnExit();
        new File(getSaveFileFullPath("adgroup.gz")).deleteOnExit();
        new File(getSaveFileFullPath("creative.gz")).deleteOnExit();
    }


    private String getSaveFileFullPath(String fileName){
        //下载文件到本地;
        String path=saveFilePath;
        File dirFile=new File(path);
        if (!dirFile.exists()){
            dirFile.mkdirs();
        }
        return String.format("%s/%s_%s_%s",path, reportDate,accountName,fileName);
    }

    public String getFeedFileId(String accountName){
        GetFeedResponse response = requestFeedApi(accountName,getAllFeedObjectsRequest(),"BulkJobFeedService","getAllFeedObjects");
        Preconditions.checkNotNull(response.getFileId(),"fileId can not be null");
        return response.getFileId();
    }

    public String getFeedFileState(String accountName,String fileId){
        Preconditions.checkNotNull(fileId,"fileId can not be null");
        GetFeedResponse body=new GetFeedResponse();
        body.setFileId(fileId);
        GetFeedResponse response = requestFeedApi(accountName,body,"BulkJobFeedService","getFileStatus");
        Preconditions.checkNotNull(response.getIsGenerated(),"isGenerated can not be null");
        return response.getIsGenerated();
    }

    public FilePathFeedTypeResponse getFeedFilePath(String accountName,String fileId){
        Preconditions.checkNotNull(fileId,"reportId can not be null");
        GetFeedResponse body=new GetFeedResponse();
        body.setFileId(fileId);
        FilePathFeedTypeResponse response = requestFeedApi(accountName,body,"BulkJobFeedService","getFilePath",FilePathFeedTypeResponse.class);
        Preconditions.checkNotNull(response.getFeedAccountFilePath(),"filePaths can not be null");
        return response;
    }

    private GetAllFeedObjectsRequest getAllFeedObjectsRequest(){
        GetAllFeedObjectsRequest request=new GetAllFeedObjectsRequest();
        List<String> allFields = ImmutableList.of("all");
        request.setFeedAccountFields(allFields);
        request.setFeedCampaignFields(ImmutableList.of("campaignFeedId","campaignFeedName"));
        request.setFeedAdgroupFields(ImmutableList.of("adgroupFeedId","campaignFeedId","adgroupFeedName"));
        request.setFeedCreativeFields(ImmutableList.of("creativeFeedId","adgroupFeedId","creativeFeedName"));
        return request;
    }

    /**
     * 下载创意维度报表
     */
    public void downloadReportFeedFile(){
        if (new File(getSaveFileFullPath("report.tsv")).exists()){
            log.info("file exits "+getSaveFileFullPath("report.tsv"));
            return;
        }
        String reportId = this.getReportFeedId(accountName, reportDate);
        // 如果报告没有准备好，重试1小時
        boolean isReady = false;
        for (int i = 0; i < 360; i++) {
            isReady ="3".equals(getReportFeedState(accountName, reportId));
            if (isReady) {
                break;
            }
            try {
                Thread.sleep(10000L);
            } catch (InterruptedException e) {
                log.error("BaiduFeedDownloadService downloadReportFeedFile", e);
            }
        }
        if (!isReady) {
            log.info("wait one hour,file not ready,");
            System.exit(1);
        }
         downloadReportFile(getReportFeedFileUrl(accountName,reportId),CREATIVE_REPORT_FILE);
    }

    public String getReportFeedId(String accountName, String startDate){
        GetReportFeedIdRequest getReportFeedIdRequest=new GetReportFeedIdRequest();
        getReportFeedIdRequest.setReportRequestType(getReportRequestType(startDate));
        GetFeedResponse response = requestFeedApi(accountName,getReportFeedIdRequest,"ReportFeedService","getReportFeedId");
        Preconditions.checkNotNull(response.getReportId(),"reportId can not be null");
        return response.getReportId();
    }

    public String getReportFeedState(String accountName,String reportId){
        Preconditions.checkNotNull(reportId,"reportId can not be null");
        GetReportFeedStateRequest getReportFeedtateRequest=new GetReportFeedStateRequest(reportId);
        GetFeedResponse response = requestFeedApi(accountName,getReportFeedtateRequest,"ReportFeedService","getReportFeedState");
        Preconditions.checkNotNull(response.getIsGenerated(),"isGenerated can not be null");
        return response.getIsGenerated();
    }

    public String getReportFeedFileUrl(String accountName,String reportId){
        Preconditions.checkNotNull(reportId,"reportId can not be null");
        GetFeedResponse response = requestFeedApi(accountName,new GetReportFeedFileUrlRequest(reportId),"ReportFeedService","getReportFeedFileUrl");
        Preconditions.checkNotNull(response.getReportFilePath(),"feedAccountFilePath can not be null");
        return response.getReportFilePath();
    }

    public  GetFeedResponse requestFeedApi(String accountName, Object body, String serviceName, String methodName){
        return this.requestFeedApi(accountName, body,serviceName,methodName,GetFeedResponse.class);
    }

    protected  <T extends AbstractBceResponse> T requestFeedApi(String accountName, Object body, String serviceName, String methodName,Class<T> responseClass){
        GetFeedRequest request=new GetFeedRequest();
        request.setOpUsername(superAccount.getAccountName());
        request.setOpPassword(superAccount.getPassword());
        request.setTgSubname(accountName);
        request.setBceUser(superAccount.getBceUser());
        Map<String,Object> requestContent= ImmutableMap.of("header",request,"body",body);
        InternalRequest internalRequest =createRequest("/feed/cloud/", HttpMethodName.POST,serviceName,methodName);
        internalRequest = fillRequestPayload(internalRequest, JsonUtils.toJsonString(requestContent));
        return this.invokeHttpClient(internalRequest, responseClass);
    }

    protected InternalRequest createRequest(String resourceKey,HttpMethodName httpMethod, String... pathVariables) {
        List<String> pathComponents = new ArrayList<String>();
        pathComponents.add(URL_PREFIX);
        assertStringNotNullOrEmpty(resourceKey, "String resourceKey should not be null or empty");
        pathComponents.add(resourceKey);
        if (pathVariables != null) {
            for (String pathVariable : pathVariables) {
                pathComponents.add(pathVariable);
            }
        }
        // get a InternalRequest instance
        InternalRequest request =
                new InternalRequest(httpMethod, HttpUtils.appendUri(this.getEndpoint(),
                        pathComponents.toArray(new String[pathComponents.size()])));
        request.setCredentials(bceCredentials);

        // set headersToSign
        SignOptions options = SignOptions.DEFAULT;
        Set<String> headersToSign = new HashSet<String>();
        // headersToSign.add("content-type");
        headersToSign.add("host");
        headersToSign.add("x-bce-date");
        headersToSign.add("x-bce-request-id");
        options.setHeadersToSign(headersToSign);

        new BceV1Signer().sign(request, request.getCredentials());

        return request;
    }

    protected InternalRequest fillRequestPayload(InternalRequest internalRequest, String strJson) {

        log.info(String.format("payload [%s]",strJson));
        byte[] requestJson = null;
        try {
            requestJson = strJson.getBytes(DEFAULT_ENCODING);
        } catch (UnsupportedEncodingException e) {
            throw new BceClientException("Unsupported encode.", e);
        }

        internalRequest.addHeader(Headers.CONTENT_LENGTH, String.valueOf(requestJson.length));
        internalRequest.addHeader(Headers.CONTENT_TYPE, SmsConstant.CONTENT_TYPE);
        internalRequest.setContent(RestartableInputStream.wrap(requestJson));

        return internalRequest;
    }

    protected void assertStringNotNullOrEmpty(String parameterValue, String errorMessage) {
        if (parameterValue == null) {
            throw new IllegalArgumentException(errorMessage);
        }
        if (parameterValue.isEmpty() || parameterValue.trim().isEmpty()) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    private ReportRequestFeedType getReportRequestType(String startDate){
        ReportRequestFeedType type =new ReportRequestFeedType();
        //build ReportRequestType differ from reportType
        type.setStartDate(startDate);
        type.setEndDate(startDate);
        //目前关键词和创意报表所传递的指标字段一样，暂时无需分别
        List<String> column = new ArrayList<String>();
        column.add("impression");
        column.add("click");
        column.add("cost");
        column.add("ctr");
        column.add("cpc");
        column.add("cpm");
        type.setPerformanceData(column.toArray(new String[column.size()]));
        type.setLevelOfDetails(7);
        type.setReportType(703);
        type.setStatRange(7);
        type.setUnitOfTime(5);
//        type.setDevice(0);
        type.setMaterialStyle(0);
        return type;
    }

    /***
     * 下载报表文件到本地
     * @param reportFileUrl
     * @return
     */
    protected String downloadReportFile(String reportFileUrl,String fileName){
        String localFile = getSaveFileFullPath(fileName);
        try {
            FileDownloadUtil.getURLResource(localFile, reportFileUrl);
        } catch (Exception e) {
            log.error("download baidu feed error,url:"+reportFileUrl,e);
        }
        return localFile;
    }
}
