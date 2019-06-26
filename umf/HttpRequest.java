package umf;

import com.umpay.api.exception.ReqDataException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    /**
     * HTTP请求工具类
     * HttpRequest代码来源于互联网请商户慎重考虑是否使用到真实项目中去
     * 本代码模拟发送 http get和post请求
     * 向指定URL发送GET方法的请求
     * 
     * @param url
     *            发送请求的URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url) {
        String result = "";
        BufferedReader in = null;
        try {
            
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            //connection.setRequestProperty("user-agent",
            //        "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            //connection.setConnectTimeout(60000);
            //connection.setReadTimeout(60000);
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
           // Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
           // for (String key : map.keySet()) {
           //     System.out.println(key + "--->" + map.get(key));
           // }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 向指定 URL 发送POST方法的请求
     * 
     * @param url
     *            发送请求的 URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            //conn.setRequestProperty("user-agent",
                   // "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Accept-Charset", "utf-8");
            conn.setRequestProperty("contentType", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Charset", "utf-8");
           // conn.setConnectTimeout(60000);
            //conn.setReadTimeout(60000);
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }


    public static void main(String[] args) throws ReqDataException {
        Map<String, String> map = new HashMap<String, String>();
        map.put("service","anjuyi_query_pay_result");
        map.put("charset","utf-8");
        map.put("mer_id","6245");  //商户需改为自己的商户号
        //map.put("sign_type","RSA");
        //map.put("notify_url","http://www.xxx.com"); //异步结果回调地址
        //map.put("contract_no","12345678");
        map.put("pay_amt","20000");
        //map.put("trade_type","0");
        map.put("order_id","20190618555634");
        map.put("version","4.0");

        String Result ;

        /*
         * 以下是post请求示例

         * 	*/
        com.umpay.api.common.ReqData reqDataPost = com.umpay.api.paygate.v40.Mer2Plat_v40.makeReqDataByPost(map);
        System.out.println(reqDataPost);
    }

}