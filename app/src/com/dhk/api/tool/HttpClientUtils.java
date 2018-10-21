package com.dhk.api.tool;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * ClassName: HttpClientUtils 
 * @Description: TODO
 * @author zhangzl
 * @date 2017年6月8日
 */
public class HttpClientUtils {
	private static PoolingHttpClientConnectionManager cm;
	private static String EMPTY_STR = "";
	private static String UTF_8 = "UTF-8";

	private static void init() {
		if (cm == null) {
			cm = new PoolingHttpClientConnectionManager();
			cm.setMaxTotal(50);// 整个连接池最大连接数
			cm.setDefaultMaxPerRoute(5);// 每路由最大连接数，默认值是2
		}
	}

	/**
	 * 通过连接池获取HttpClient
	 * 
	 * @return
	 */
	private static CloseableHttpClient getHttpClient() {
		init();
		return HttpClients.custom().setConnectionManager(cm).build();
	}

	/**
	 * 
	 * @param url
	 * @return
	 */
	public static String httpGetRequest(String url) {
		HttpGet httpGet = new HttpGet(url);
		return getResult(httpGet);
	}

	public static String httpGetRequest(String url, Map<String, String> params) throws URISyntaxException {
		URIBuilder ub = new URIBuilder();
		ub.setPath(url);

		ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
		ub.setParameters(pairs);

		HttpGet httpGet = new HttpGet(ub.build());
		return getResult(httpGet);
	}

	public static String httpGetRequest(String url, Map<String, Object> headers, Map<String, String> params)
			throws URISyntaxException {
		URIBuilder ub = new URIBuilder();
		ub.setPath(url);

		ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
		ub.setParameters(pairs);

		HttpGet httpGet = new HttpGet(ub.build());
		for (Map.Entry<String, Object> param : headers.entrySet()) {
			httpGet.addHeader(param.getKey(), String.valueOf(param.getValue()));
		}
		return getResult(httpGet);
	}

	public static String httpPostRequest(String url) {
		HttpPost httpPost = new HttpPost(url);
		return getResult(httpPost);
	}

	public static String httpPostRequest(String url, Map<String, String> params) throws UnsupportedEncodingException {
		HttpPost httpPost = new HttpPost(url);
		ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
		httpPost.setEntity(new UrlEncodedFormEntity(pairs, UTF_8));
		return getResult(httpPost);
	}

	public static String httpPostRequest(String url, Map<String, Object> headers, Map<String, String> params)
			throws UnsupportedEncodingException {
		HttpPost httpPost = new HttpPost(url);
		for (Map.Entry<String, Object> param : headers.entrySet()) {
			httpPost.addHeader(param.getKey(), String.valueOf(param.getValue()));
		}
		HttpEntity entity = null;
		if(headers.get("Content-type")!=null&&headers.get("Content-type").toString().contains("application/json")){
			if(params!=null){
				entity =  new StringEntity(JSON.toJSONString(params),ContentType.create("text/json", "UTF-8"));
			}else{
				entity =  new StringEntity("",ContentType.create("text/json", "UTF-8"));
			}
			
		}else{
			ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
		    entity = new UrlEncodedFormEntity(pairs,"UTF-8");
		}
		httpPost.setEntity(entity);
		return getResult(httpPost);
	}

	
	public static String httpPutRequest(String url, Map<String, String> params) throws UnsupportedEncodingException {
		HttpPost httpPost = new HttpPost(url);
		ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
		httpPost.setEntity(new UrlEncodedFormEntity(pairs, UTF_8));
		return getResult(httpPost);
	}

	public static String httpPutRequest(String url, Map<String, Object> headers, Map<String, String> params)
			throws UnsupportedEncodingException {
		HttpPut httpPut = new HttpPut(url);
		for (Map.Entry<String, Object> param : headers.entrySet()) {
			httpPut.addHeader(param.getKey(), String.valueOf(param.getValue()));
		}
		HttpEntity entity = null;
		if(headers.get("Content-type")!=null&&headers.get("Content-type").toString().contains("application/json")){
			if(params!=null){
				entity =  new StringEntity(JSON.toJSONString(params),ContentType.create("text/json", "UTF-8"));
			}else{
				entity =  new StringEntity("",ContentType.create("text/json", "UTF-8"));
			}
			
		}else{
			ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
		    entity = new UrlEncodedFormEntity(pairs,"UTF-8");
		}
		httpPut.setEntity(entity);
		return getResult(httpPut);
	}
	
	
	public static String httpDeleteRequest(String url, Map<String, String> params) throws URISyntaxException {
		URIBuilder ub = new URIBuilder();
		ub.setPath(url);

		ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
		ub.setParameters(pairs);

		HttpDelete httpDelete = new HttpDelete(ub.build());
		return getResult(httpDelete);
	}

	public static String httpDeleteRequest(String url, Map<String, Object> headers, Map<String, String> params)
			throws URISyntaxException {
		URIBuilder ub = new URIBuilder();
		ub.setPath(url);

		ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
		ub.setParameters(pairs);

		HttpDelete httpDelete = new HttpDelete(ub.build());
		for (Map.Entry<String, Object> param : headers.entrySet()) {
			httpDelete.addHeader(param.getKey(), String.valueOf(param.getValue()));
		}
		return getResult(httpDelete);
	}
	
	
	private static ArrayList<NameValuePair> covertParams2NVPS(Map<String, String> params) {
		ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
		for (Map.Entry<String, String> param : params.entrySet()) {
			pairs.add(new BasicNameValuePair(param.getKey(), param.getValue()));
		}

		return pairs;
	}

	/**
	 * 处理Http请求
	 * 
	 * @param request
	 * @return
	 */
	private static String getResult(HttpRequestBase request) {
		// CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpClient httpClient = getHttpClient();
		try {
			CloseableHttpResponse response = httpClient.execute(request);
			// response.getStatusLine().getStatusCode();
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				// long len = entity.getContentLength();// -1 表示长度未知
				String result = EntityUtils.toString(entity);
				response.close();
				// httpClient.close();
				return result;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

		}

		return EMPTY_STR;
	}
	//post请求方法
    public static  String sendPost(String url, String data) {
        String response = null;
    
        try {
            CloseableHttpClient httpclient = null;
            CloseableHttpResponse httpresponse = null;
            try {
                httpclient = HttpClients.createDefault();
                HttpPost httppost = new HttpPost(url);
                StringEntity stringentity = new StringEntity(data,
                        ContentType.create("text/json", "UTF-8"));
                httppost.setEntity(stringentity);
                httpresponse = httpclient.execute(httppost);
                response = EntityUtils
                        .toString(httpresponse.getEntity());
            } finally {
                if (httpclient != null) {
                    httpclient.close();
                }
                if (httpresponse != null) {
                    httpresponse.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
	public static void main (String []arg) throws ClientProtocolException, IOException, URISyntaxException{
		
		
		Map<String, Object> headers = new HashMap<>();
		headers.put("Content-type","application/json; charset=utf-8");  
		headers.put("Accept", "application/json");  
		Map<String, Object> params = new HashMap<>();
		
		/*	 String apiUrl = "http://10.6.71.232/K3API/Token/Create/";                             //API地址
		 String authCode = "3836359fedc2f4e75b0f5bfdbf74b0f6268e2a7a1d0d6a0c";  //授权码
		
         params.put("authorityCode", authCode);
         String result = HttpClientUtils.httpGetRequest(apiUrl, headers, params);
	  System.out.println("结果："+result);
	  	  JSONObject jobj = JSON.parseObject(result);
	  if(jobj.get("StatusCode")!=null&&jobj.get("StatusCode").equals(200)){
		  String token = "";
		  if(jobj.get("Data")!=null){
			  JSONObject data = (JSONObject) jobj.get("Data");
			  token = data.get("Token").toString();
		  }
		  
		 
		 
		  Map<String,Object> dataMap = new HashMap<>();
		  dataMap.put("Token", token);
		  params.clear();
		  params.put("Top", "100");
		  params.put("PageSize", "10");
		  params.put("PageIndex", "1");
		  params.put("OrderBy", " ");
		  params.put("SelectPage", "2");
		  params.put("Fields", "*");
		  dataMap.put("Data", params);
	      System.out.println("----------------------------------------");
		  String result1 = HttpClientUtils.httpGetRequest("http://10.6.71.232/K3API/Purchase_Receipt/GetList", headers, dataMap);  
		  System.out.println(result1);
		  }*/
	  

		  
		  Map<String,Object> dataMap = new HashMap<>();
		  
		  params.clear();
//		  dataMap.put("Token", "7D5D733420FA96753A28ABCEB4C0D0E16BA46463CF363B099EC3C4D91F502671154FD73C1C5420BB");
		  params.put("Top", "99999");
		  params.put("PageSize", "999999");
		  params.put("PageIndex", "1");
		//  params.put("Filter", "1=1 and [Fdate] <= '2017-06-30' ");
		  params.put("OrderBy", "[Fdate] asc");
		  params.put("SelectPage", "3");
		  params.put("Fields", "FSupplyID,FStdAmountIncludeTax,FAmountIncludeTax,FStdAmount,FItemID36422,FItemID36436,FBillNo,FAuxQty,Fdate,FUnitID,FAuxTaxPrice");
		  dataMap.put("Data", params);
		//  String result1 = HttpClientUtils.httpPostRequest("http://10.6.71.232/K3API/Bill1000003/GetList?Token=206FC6AB257F47A48E03789B8CBEE6C07C101E4BB0D4E50F71949A58E5621032B5638154F5753699",headers,dataMap);
		//  System.out.println(result1);
		  
		  
/*		  
		  dataMap.clear();
		  
		  params.clear();
//		  dataMap.put("Token", "7D5D733420FA96753A28ABCEB4C0D0E16BA46463CF363B099EC3C4D91F502671154FD73C1C5420BB");
		  params.put("Top", "100");
		  params.put("PageSize", "100");
		  params.put("PageIndex", "1");
		  params.put("Filter", "FNumber=4560");
//		  params.put("OrderBy", "[FBillNo] asc");
		  params.put("SelectPage", "2");
		  params.put("Fields", "FItemID,FName,FAcctID,FModel,FUnitID,FOrderUnitID,FSaleUnitID,FProductUnitID,FStoreUnitID,FFullName,FAlias,FHelpCode,FTaxRate,FProfitRate,FNote");
		  dataMap.put("Data", params);
		  String result2 = HttpClientUtils.httpPostRequest("http://10.6.71.232/K3API/Material/GetList?Token=73EED88B2CBD154942F7A3DF5002DA1F79D38BCEE30C6A7F7CE9082BA8A435D82DCD085F94701954",headers,dataMap);  
		  System.out.println(result2);*/
/*		  dataMap.clear();
		  params.clear();
//		  dataMap.put("Token", "9A4F27D891A921E07AC786880EDFFDCDC817B341C19847EA3352FEAC71F37D984A61DDF21BE98C81");
//		  params.put("Top", "100");
//		  params.put("PageSize", "10");
//		  params.put("PageIndex", "1");
//		  params.put("OrderBy", "[FBillNo] asc");
//		  params.put("SelectPage", "2");
//		  params.put("Fields", "FDeptID,FInterID,Fdate,FKFDate,FSupplyID,FItemID,FUnitID,Fauxqty,Famount,Fauxprice,FStatus,FDCStockID,FEmpID");
		  params.put("FBillNo", "WIN000004");
		  dataMap.put("Data", params);
		  String result2 = HttpClientUtils.httpPostRequest("http://10.6.71.232/K3API/Purchase_Receipt/GetDetail?Token=9A4F27D891A921E07AC786880EDFFDCDC817B341C19847EA3352FEAC71F37D984A61DDF21BE98C81",headers,dataMap);  
		  System.out.println(result2);
		  */
		  
		  		  
		  
		  
		  
	//  3971DD997FBD9CC31CB6F68B6B4997BFB73DCF639173852ED0341109659822C4F909B795C92E4955
//	{""Top"": ""100"", 
//	HttpClient httpClient = HttpClientBuilder.create().build(); 
//	HttpPost httpPost = new HttpPost("http://localhost:8080/clotherdye/api/myTest/create");
//	httpPost.addHeader("content-type", "application/json");
//	httpPost.addHeader("Accept", "application/json");
//	Map<String, Object> params = new HashMap<>();
//    params.put("delivery_date", "2017-04-19");
//	String jsonString = JSON.toJSONString(params);
//	StringEntity entity = new StringEntity(jsonString);
//	httpPost.setEntity(entity);
//	HttpResponse httpResponse = httpClient.execute(httpPost);
//	String response = EntityUtils.toString(httpResponse.getEntity());
//	System.out.println(response);
//	
		
	
	}
}