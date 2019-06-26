package umf;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * **************************************************
 * <br/>description:使用HttpClient进行请求的发送。
 * @author      zhaohonglin
 * @date        2017-8-24 下午04:25:30
 * **************************************************
 */
public class HttpClientUtil {

    
    private static int timeout = 60000;

    private static String sendEncode = "UTF-8";

    private static int retryConnTimes = 5;

    private static RequestConfig requestConfig = null;

      /**
     * 初化一些提交http请求必要的参数
     */
    static{
        requestConfig = RequestConfig.custom().setSocketTimeout(timeout).setConnectionRequestTimeout(timeout)
                .setConnectTimeout(timeout).build();
    }
    
    /**
     * <br/>description:发送http_post请求，得到返回结果。
     * @param map,url 表示请求参数
     * @return String 联动后台响应的结果
     */
    public static String submit(Map<String,String> map,String url) throws Exception{  	
    	HttpPost method = new HttpPost();
        method.setHeader(HTTP.CONTENT_ENCODING,sendEncode);
        method.setHeader(HTTP.USER_AGENT,"Rich Powered/1.0");
        method.setHeader(HTTP.CONTENT_TYPE,"application/x-www-form-urlencoded");
        method.setConfig(requestConfig);
    
        CloseableHttpClient httpClient = HttpClients.custom().setRetryHandler(new DefaultHttpRequestRetryHandler(retryConnTimes,false)).build();
        method.setURI(new URI(url));
        List<NameValuePair> entity = mapToList(map);
        try{
            method.setEntity(new UrlEncodedFormEntity(entity,sendEncode));
        }catch(Exception e){
            throw new Exception("[UMF SDK] http_post请求参数异常",e);
        }
        int stateCode = 0;
        String result = "";
        CloseableHttpResponse response = null;
        try{
            response = httpClient.execute(method);
            stateCode = response.getStatusLine().getStatusCode();
            HttpEntity respEntity = response.getEntity();
            InputStream stream = respEntity.getContent();
            result = ConvertStreamToString(stream);
            
        }catch(Exception e){
            throw new Exception("[UMF SDK] 发送网络请求失败",e);
        }finally{
            if(response != null)
            {
            	try{
            		response.close();
            		}
            	catch(Exception e){
            	}
            }
        }
        try {
            httpClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String ConvertStreamToString(InputStream is) throws UnsupportedEncodingException {   
    	   BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF-8"));   
	        StringBuilder sb = new StringBuilder();   
	        String line = null;   
	        try {   
	            while ((line = reader.readLine()) != null) {    
	                sb.append(line).append("\n");  
	            }   
	        } catch (IOException e) {   
	            e.printStackTrace();   
	        } finally {   
	            try {   
	                is.close();   
	            } catch (IOException e) {   
	                e.printStackTrace();   
	            }   
	        }   
	    
	        return sb.toString();   
	    }   
  


    /**
     * <br/>description:将请求参数map转换为http请求体
     * @param map
     * @return
     */
    private static List<NameValuePair> mapToList(Map<String,String> map){
        List<NameValuePair> result = new ArrayList<NameValuePair>();
        /*Iterator<Entry<String, String>> iter  = map.entrySet().iterator();
        while (iter.hasNext()) {
        	for (Map.Entry<String, String> entry : map.entrySet()) 
        	
        	Map.Entry entry = iter.next();*/
    	for (Map.Entry<String, String> entry : map.entrySet()) {
        	String key = entry.getKey();
        	String value = entry.getValue();
        	result.add(new BasicNameValuePair(key,value));
        /*Set<String> paramSet = map.keySet();
        for(Iterator<String> iter = paramSet.iterator();iter.hasNext();){
            String key = iter.next();
            String value = map.get(key);
            result.add(new BasicNameValuePair(key,value));*/
        }
        return result;
    }



}