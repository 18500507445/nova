package com.nova.tools.utils.data;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 2019/9/11
 */
public class HttpUtil {

    public static String doHttpGet(String url, Map<String, String> parameterMap) throws Exception {
        String result;
        CloseableHttpClient client = HttpClients.createDefault();
        if (parameterMap != null) {
            StringBuilder urlBuilder = new StringBuilder(url + "?");
            for (Map.Entry<String, String> entry : parameterMap.entrySet()) {
                urlBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
            url = urlBuilder.substring(0, urlBuilder.toString().length() - 1);
        }
        HttpGet get = new HttpGet(url);
        CloseableHttpResponse response = client.execute(get);
        result = EntityUtils.toString(response.getEntity(), getContentCharset(response));
        response.close();
        return result;
    }

    public static String doHttpGet(String url, Map<String, String> parameterMap, Map<String, String> headerMap) throws Exception {
        String result;
        CloseableHttpClient client = HttpClients.createDefault();
        if (parameterMap != null) {
            StringBuilder urlBuilder = new StringBuilder(url + "?");
            for (Map.Entry<String, String> entry : parameterMap.entrySet()) {
                urlBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
            url = urlBuilder.toString().substring(0, urlBuilder.toString().length() - 1);
        }
        HttpGet get = new HttpGet(url);
        if (headerMap != null) {
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                get.setHeader(entry.getKey(), entry.getValue());
            }
        }
        CloseableHttpResponse response = client.execute(get);
        result = EntityUtils.toString(response.getEntity(), getContentCharset(response));
        response.close();
        return result;
    }

    public static File download(String url, String fileName, String suffix, Map<String, String> parameterMap) throws Exception {
        CloseableHttpClient client = HttpClients.createDefault();
        if (parameterMap != null) {
            StringBuilder urlBuilder = new StringBuilder(url + "?");
            for (Map.Entry<String, String> entry : parameterMap.entrySet()) {
                urlBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
            url = urlBuilder.substring(0, urlBuilder.toString().length() - 1);
        }
        HttpGet get = new HttpGet(url);
        CloseableHttpResponse response = client.execute(get);
        byte[] bytes = EntityUtils.toByteArray(response.getEntity());
        File tempFile = File.createTempFile(fileName, suffix);
        FileUtil.doWriterFile(bytes, tempFile);
        response.close();
        return tempFile;
    }

    public static String doHttpPost(String url, Map<String, String> parameterMap) throws Exception {
        String result;
        CloseableHttpClient client = HttpClients.createDefault();
        ArrayList<NameValuePair> parameters = null;
        if (parameterMap != null) {
            parameters = new ArrayList<>();
            for (Map.Entry<String, String> entry : parameterMap.entrySet()) {
                parameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }
        HttpPost post = new HttpPost(url);
        post.setEntity(new UrlEncodedFormEntity(parameters, "utf8"));
        CloseableHttpResponse response = client.execute(post);
        result = EntityUtils.toString(response.getEntity(), getContentCharset(response));
        response.close();
        return result;
    }

    public static String doHttpPost(String url, Map<String, String> parameterMap, Map<String, String> headerMap) throws Exception {
        String result;
        CloseableHttpClient client = HttpClients.createDefault();
        ArrayList<NameValuePair> parameters = null;
        if (parameterMap != null) {
            parameters = new ArrayList<NameValuePair>();
            for (Map.Entry<String, String> entry : parameterMap.entrySet()) {
                parameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }
        HttpPost post = new HttpPost(url);
        post.setEntity(new UrlEncodedFormEntity(parameters, "utf-8"));
        if (headerMap != null) {
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                post.setHeader(entry.getKey(), entry.getValue());
            }
        }
        CloseableHttpResponse response = client.execute(post);
        result = EntityUtils.toString(response.getEntity(), getContentCharset(response));
        response.close();
        return result;
    }

    public static String doHttpPostXML(String url, String xml) throws Exception {
        String result;
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);
        post.setEntity(new StringEntity(xml, ContentType.APPLICATION_XML));
        CloseableHttpResponse response = client.execute(post);
        result = EntityUtils.toString(response.getEntity(), getContentCharset(response));
        response.close();
        return result;
    }

    public static String doHttpPostJson(String url, String json) throws Exception {
        String result;
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);
        post.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));
        CloseableHttpResponse response = client.execute(post);
        result = EntityUtils.toString(response.getEntity(), getContentCharset(response));
        response.close();
        return result;
    }

    public static String doHttpPostJson(String url, String json, Map<String, String> headerMap) throws Exception {
        String result;
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);
        post.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));
        if (headerMap != null) {
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                post.setHeader(entry.getKey(), entry.getValue());
            }
        }
        CloseableHttpResponse response = client.execute(post);
        result = EntityUtils.toString(response.getEntity(), getContentCharset(response));
        response.close();
        return result;
    }

    private static String getContentCharset(HttpResponse response) {
        String charset = "utf-8";
        Header header = response.getEntity().getContentType();
        if (header != null) {
            String s = header.getValue();
            if (matcher(s, "(charset)\\s?=\\s?(utf-?8)")) {
                charset = "utf-8";
            } else if (matcher(s, "(charset)\\s?=\\s?(gbk)")) {
                charset = "gbk";
            } else if (matcher(s, "(charset)\\s?=\\s?(gb2312)")) {
                charset = "gb2312";
            }
        }
        return charset;
    }

    private static boolean matcher(String s, String pattern) {
        Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE + Pattern.UNICODE_CASE);
        Matcher matcher = p.matcher(s);
        return matcher.find();
    }

}
