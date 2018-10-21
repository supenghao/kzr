package com.dhk.api.third;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class LinkFaceHttpClientPost {

      public static final String api_id = "41d6b4ef96fa4d08af6df06f49ebf53e"; 
      public static final String api_secret = "eacf56c1272b48cbbf3b77e0b8a334d7"; 
      public static final String filePath ="";
      public static final String POST_URL = "https://cloudapi.linkface.cn/ocr/idcard";

      public static String HttpClientPost(String postUrl,String filePath) throws ClientProtocolException, IOException {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost post = new HttpPost(postUrl);
            FileBody fileBody = new FileBody(new File(filePath));
            StringBody id = new StringBody(api_id);
            StringBody secret = new StringBody(api_secret);
            //StringBody secret = new StringBody(api_secret);
            MultipartEntity entity = new MultipartEntity();
            entity.addPart("file", fileBody);
            entity.addPart("api_id", id);
            entity.addPart("api_secret", secret);
           // entity.addPart("side","front");
            post.setEntity(entity);
            String line="";
            HttpResponse response = httpclient.execute(post);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entitys = response.getEntity();
                BufferedReader reader = new BufferedReader(
                    new InputStreamReader(entitys.getContent()));
                line = reader.readLine();
                System.out.println(line);
            }else{
                HttpEntity r_entity = response.getEntity();
                String responseString = EntityUtils.toString(r_entity);
                System.out.println("错误码是："+response.getStatusLine().getStatusCode()+"  "+response.getStatusLine().getReasonPhrase());
                System.out.println("出错原因是："+responseString);
                //你需要根据出错的原因判断错误信息，并修改
            }

            httpclient.getConnectionManager().shutdown();
            return line;
      }


    public static void main(String[] args) throws ClientProtocolException, IOException {
        HttpClientPost(POST_URL,filePath);
    }
}
