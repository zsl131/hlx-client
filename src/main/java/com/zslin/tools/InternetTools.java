package com.zslin.tools;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * 与微信平台进行交互的网络处理工具类
 * @author 钟述林
 *
 */
public class InternetTools {

	/**
	 * 处理get请求
	 * @param serverName url
	 * @param params 参数
	 * @return 返回结果
	 */
	public static String doGet(String serverName, Map<String, Object> params) {
		String result = null;
		StringBuffer sb = new StringBuffer();
		try {
			URL url = new URL(rebuildUrl(serverName, params));
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(5000);
			conn.connect();
			BufferedReader reader =new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));
			String str;
			while((str=reader.readLine())!=null) {
				sb.append(str).append("\n");
			}

		} catch (Exception e) {
			System.out.println("InternetTools.toGet 出现异常："+e.getMessage());
		}
		result = sb.toString();
		if(result.endsWith("\n")) {result = result.substring(0, result.length()-1);}
		return result;
	}
	
	/**
	 * 重新生成url
	 * @param serverName
	 * @param params
	 * @return
	 */
	private static String rebuildUrl(String serverName, Map<String, Object> params) {
		StringBuffer sb = new StringBuffer(serverName);
		if(serverName.indexOf("?")<0) {
			sb.append("?1=1");
		}
		if(params!=null) {
			for(String key : params.keySet()) {
				sb.append("&").append(key).append("=").append(params.get(key));
			}
		}
		return sb.toString();
	}

	public static Integer post(String url, String postParameter) {
		Integer res = 500; //默认是出错
		HttpClient client = HttpClients.createDefault();
		HttpPost post = new HttpPost(url);
		try {
			post.addHeader("Content-type", "application/json; charset=utf-8");
			post.setHeader("Accept", "application/json");
			post.setEntity(new StringEntity(postParameter, Charset.forName("UTF-8")));
			HttpResponse httpResponse = client.execute(post);

//			HttpEntity entity = httpResponse.getEntity();
			System.out.println("status:" + httpResponse.getStatusLine());
//			System.out.println("response content:" + EntityUtils.toString(entity));
			res = httpResponse.getStatusLine().getStatusCode();
		} catch (UnsupportedEncodingException e1) {
			//e1.printStackTrace();
		} catch (ClientProtocolException e) {
			//e.printStackTrace();
		} catch (IOException e) {
			//e.printStackTrace();
		}
		return res;
	}
}
