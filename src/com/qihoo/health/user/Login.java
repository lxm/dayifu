package com.qihoo.health.user;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class Login {
	String Url = "http://lushifu.changxiaoyuan.com/index/";
	CookieStore cookie = null;
	
	private static Login inst = null;
	
	public static Login getInstance() {
		if (inst == null)
			inst = new Login();
		return inst;
	}

	public boolean GetCode(String PhoneNum) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("phone", PhoneNum));
		String ret = null;
		try {
			ret = this.HttpPost(this.Url + "getcode", params);
		} catch (Exception e) {
			e.printStackTrace();
			Log.v("test", "注册手机失败：" + e);
		}
		if (ret != null && ret.contains("OK")) {
			return true;
		} else {
			return false;
		}
	}

	public boolean CheckCode(String Code, String phoneNum) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("code", Code));
		params.add(new BasicNameValuePair("phone", phoneNum));
		String ret = null;
		try {
			ret = this.HttpPost(this.Url + "checkcode", params);
		} catch (Exception e) {
			Log.v("test", "验证码验证失败：" + e);
			e.printStackTrace();
		}
		Log.v("test", ret);
		if (ret != null && ret.contains("OK")) {
			return true;
		} else {
			return false;
		}
	}

	private String HttpPost(String Url, List<NameValuePair> params)
			throws Exception {
		String result = null;
		HttpPost httppost = new HttpPost(Url);
		httppost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpResponse response = httpclient.execute(httppost);
		if (this.cookie != null) {
			httpclient.setCookieStore(this.cookie);
		}
		this.cookie = httpclient.getCookieStore();
		if (response.getStatusLine().getStatusCode() == 200) {
			result = EntityUtils.toString(response.getEntity());
		}
		return result;
	}

	/*
	 * public String executeHttpPost(String ReqUrl,HashMap<String,String> Param)
	 * { String result = null; URL url = null; HttpURLConnection connection =
	 * null; InputStreamReader in = null; try { url = new URL(ReqUrl);
	 * connection = (HttpURLConnection) url.openConnection();
	 * connection.setDoInput(true); connection.setDoOutput(true);
	 * connection.setRequestMethod("POST");
	 * connection.setRequestProperty("Content-Type",
	 * "application/x-www-form-urlencoded");
	 * connection.setRequestProperty("Charset", "utf-8"); DataOutputStream dop =
	 * new DataOutputStream( connection.getOutputStream()); Iterator<?> it =
	 * Param.entrySet().iterator(); while(it.hasNext()){ Entry<?, ?> entry =
	 * (Entry<?, ?>) it.next(); dop.writeBytes(entry.getKey() + "=" +
	 * entry.getValue()); } dop.flush(); dop.close(); in = new
	 * InputStreamReader(connection.getInputStream()); BufferedReader
	 * bufferedReader = new BufferedReader(in); StringBuffer strBuffer = new
	 * StringBuffer(); String line = null; while ((line =
	 * bufferedReader.readLine()) != null) { strBuffer.append(line); } result =
	 * strBuffer.toString(); } catch (Exception e) { e.printStackTrace(); }
	 * finally { if (connection != null) { connection.disconnect(); } if (in !=
	 * null) { try { in.close(); } catch (IOException e) { e.printStackTrace();
	 * } } } return result; }
	 */
}
