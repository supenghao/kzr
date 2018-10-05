package com.dhk.payment.wpay;

import com.alibaba.fastjson.JSONObject;
import com.dhk.payment.util.ThirdResponseObj;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.*;
import java.util.List;

/**
 * HttpClient工具类
 * @Author Linyb
 * @Date 2016/12/13 14:18
 */
public class HttpClientUtil {
    static Logger logger = Logger.getLogger(HttpClientUtil.class);
    public static String sendForWf(String url) {
        HttpGet get = null;
        CloseableHttpResponse resp = null;
        CloseableHttpClient client = null;
        try {
            client = HttpClients.createDefault();
            get = new HttpGet(url);

            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(5000).setConnectionRequestTimeout(2000)
                    .setSocketTimeout(8000).build();
            get.setConfig(requestConfig);
            resp = client.execute(get);
            int statusCode = resp.getStatusLine().getStatusCode();
            if (statusCode >= 200 && statusCode < 300) {
                HttpEntity entity = resp.getEntity();
                String content = EntityUtils.toString(entity, "utf-8");
                return content;
            }else{
                logger.error("http请求错误："+statusCode);
            }
        } catch (SocketTimeoutException e){
            logger.error(e.getMessage());
        }catch (Exception e) {
            logger.error(e.getMessage());
        }finally {
            try {
                if (resp != null) {
                    resp.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (client != null) {
                    client.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public static ThirdResponseObj sendForYl(String url,List<BasicNameValuePair> nvps) {
        CloseableHttpClient client = null;
        CloseableHttpResponse resp = null;
        try {
            client = HttpClients.createDefault();
            HttpPost post = new HttpPost(url);
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(5000).setConnectionRequestTimeout(2000)
                    .setSocketTimeout(5000).build();
            post.setConfig(requestConfig);
            post.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
            resp = client.execute(post);
            int statusCode = resp.getStatusLine().getStatusCode();
            ThirdResponseObj responseObj = new ThirdResponseObj();
            if (200 == statusCode) {
                responseObj.setCode("success");
                String str = EntityUtils.toString(resp.getEntity(), "UTF-8");
                responseObj.setResponseEntity(str);
                return  responseObj;
            }else{
                logger.error("http请求错误："+statusCode);
            }

        } catch (SocketTimeoutException e){
            logger.error(e.getMessage());
        }catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            try {
                if (client != null)
                    client.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            try {
                if (resp != null)
                    resp.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public static JSONObject sendForGzf(String url, List<BasicNameValuePair> nvps) {
        CloseableHttpClient client = null;
        CloseableHttpResponse resp = null;
        
        try {
            client = HttpClients.createDefault();
            HttpPost post = new HttpPost(url);
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(12000).setConnectionRequestTimeout(2000)
                    .setSocketTimeout(12000).build();
            post.setConfig(requestConfig);
            post.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
            resp = client.execute(post);
            int statusCode = resp.getStatusLine().getStatusCode();
            if (200 == statusCode) {
                String str = EntityUtils.toString(resp.getEntity(), "UTF-8");
                JSONObject jsonRet = JSONObject.parseObject(str);
                return jsonRet;
            }else{
                logger.error("http请求错误："+statusCode);
            }

        } catch (SocketTimeoutException e){
            logger.error(e.getMessage());
        }catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            try {
                if (client != null)
                    client.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            try {
                if (resp != null)
                    resp.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }



    public static String sendForJN(String url, String content) {
        /**
         * 发送请求
         */
        OutputStream outputStream = null;
        OutputStreamWriter outputStreamWriter = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        StringBuffer resultBuffer = new StringBuffer();
        String tempLine = null;
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection) connection;

            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpURLConnection.setRequestProperty("Content-Length", String.valueOf(content.length()));

            outputStream = httpURLConnection.getOutputStream();
            outputStreamWriter = new OutputStreamWriter(outputStream);
            outputStreamWriter.write(content);
            outputStreamWriter.flush();
            inputStream = httpURLConnection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream);
            reader = new BufferedReader(inputStreamReader);
            while ((tempLine = reader.readLine()) != null) {
                resultBuffer.append(tempLine);
            }
            return resultBuffer.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (outputStreamWriter != null) {
                    outputStreamWriter.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
                if (reader != null) {
                    reader.close();
                }
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }





    public static void main(String[]args){
        System.out.println( HttpClientUtil.sendForWf("http://127.0.0.1:8880/api/queryRepayResult"));
    }
}
