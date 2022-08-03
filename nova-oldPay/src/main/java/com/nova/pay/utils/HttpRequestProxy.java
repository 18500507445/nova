package com.nova.pay.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;


public class HttpRequestProxy {
    /**
     * 连接超时
     */
    private static int connectTimeOut = 50000;

    /**
     * 读取数据超时
     */
    private static int readTimeOut = 6000000;

    private static final Log logger = LogFactory.getLog(HttpRequestProxy.class);

    /**
     * 请求编码
     */
    // private static String requestEncoding = "GBK";

    /**
     * <pre>
     * 发送带参数的GET的HTTP请求
     * </pre>
     *
     * @param reqUrl     HTTP请求URL
     * @param parameters 参数映射表
     * @return HTTP响应的字符串
     */
    public static String doGet(String reqUrl, Map<String, String> parameters, String recvEncoding,
                               String requestEncoding) {
        HttpURLConnection url_con = null;
        String responseContent = null;
        InputStream in = null;
        BufferedReader rd = null;
        try {
            StringBuilder params = new StringBuilder();
            for (Entry<String, String> element : parameters.entrySet()) {

                params.append(element.getKey().toString());
                params.append("=");
                params.append(URLEncoder.encode(element.getValue().toString(), requestEncoding));
                params.append("&");
            }

            if (params.length() > 0) {
                params = params.deleteCharAt(params.length() - 1);
            }

            URL url = new URL(reqUrl);
            long time11 = System.currentTimeMillis();
            url_con = (HttpURLConnection) url.openConnection();
            url_con.setRequestMethod("GET");
            System.setProperty("sun.net.client.defaultConnectTimeout", String.valueOf(HttpRequestProxy.connectTimeOut));// （单位：毫秒）jdk1.4换成这个,连接超时
            System.setProperty("sun.net.client.defaultReadTimeout", String.valueOf(HttpRequestProxy.readTimeOut)); // （单位：毫秒）jdk1.4换成这个,读操作超时
            url_con.setConnectTimeout(HttpRequestProxy.connectTimeOut);//（单位：毫秒）jdk
            // 1.5换成这个,连接超时
            url_con.setReadTimeout(HttpRequestProxy.readTimeOut);//（单位：毫秒）jdk 1.5换成这个,读操作超时
            url_con.setDoOutput(true);
            byte[] b = params.toString().getBytes();
            url_con.getOutputStream().write(b, 0, b.length);
            url_con.getOutputStream().flush();
            url_con.getOutputStream().close();

            in = url_con.getInputStream();
            rd = new BufferedReader(new InputStreamReader(in, recvEncoding));
            String tempLine = rd.readLine();
            StringBuilder temp = new StringBuilder();
            String crlf = System.getProperty("line.separator");
            while (tempLine != null) {
                temp.append(tempLine);
                temp.append(crlf);
                tempLine = rd.readLine();
            }
            responseContent = temp.toString();
            rd.close();
            in.close();
            long time22 = System.currentTimeMillis();
            logger.debug(reqUrl + "http-get总耗时=" + (time22 - time11) + "毫秒");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (rd != null) {
                try {
                    rd.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (url_con != null) {
                url_con.disconnect();
            }
        }

        return responseContent;
    }

    /**
     * <pre>
     * 发送不带参数的GET的HTTP请求
     * </pre>
     *
     * @param reqUrl HTTP请求URL
     * @return HTTP响应的字符串
     */
    public static String doGet(String reqUrl, String recvEncoding, String requestEncoding) {
        HttpURLConnection url_con = null;
        String responseContent = null;
        InputStream in = null;
        BufferedReader rd = null;
        try {
            StringBuilder params = new StringBuilder();
            String queryUrl = reqUrl;
            int paramIndex = reqUrl.indexOf("?");

            if (paramIndex > 0) {
                queryUrl = reqUrl.substring(0, paramIndex);
                String parameters = reqUrl.substring(paramIndex + 1, reqUrl.length());
                String[] paramArray = parameters.split("&");
                for (int i = 0; i < paramArray.length; i++) {
                    String string = paramArray[i];
                    int index = string.indexOf("=");
                    if (index > 0) {
                        String parameter = string.substring(0, index);
                        String value = string.substring(index + 1, string.length());
                        params.append(parameter);
                        params.append("=");
                        params.append(URLEncoder.encode(value, requestEncoding));
                        params.append("&");
                    }
                }

                params = params.deleteCharAt(params.length() - 1);
            }

            URL url = new URL(queryUrl);
            url_con = (HttpURLConnection) url.openConnection();
            url_con.setRequestMethod("GET");
            System.setProperty("sun.net.client.defaultConnectTimeout", String.valueOf(HttpRequestProxy.connectTimeOut));// （单位：毫秒）jdk1.4换成这个,连接超时
            System.setProperty("sun.net.client.defaultReadTimeout", String.valueOf(HttpRequestProxy.readTimeOut)); // （单位：毫秒）jdk1.4换成这个,读操作超时
            url_con.setConnectTimeout(HttpRequestProxy.connectTimeOut);//（单位：毫秒）jdk
            // 1.5换成这个,连接超时
            url_con.setReadTimeout(HttpRequestProxy.readTimeOut);//（单位：毫秒）jdk 1.5换成这个,读操作超时
            url_con.setDoOutput(true);
            byte[] b = params.toString().getBytes();
            url_con.getOutputStream().write(b, 0, b.length);
            url_con.getOutputStream().flush();
            url_con.getOutputStream().close();
            in = url_con.getInputStream();
            rd = new BufferedReader(new InputStreamReader(in, recvEncoding));
            String tempLine = rd.readLine();
            StringBuilder temp = new StringBuilder();
            String crlf = System.getProperty("line.separator");
            while (tempLine != null) {
                temp.append(tempLine);
                temp.append(crlf);
                tempLine = rd.readLine();
            }
            responseContent = temp.toString();
            rd.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (rd != null) {
                try {
                    rd.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (url_con != null) {
                url_con.disconnect();
            }
        }

        return responseContent;
    }

    /**
     * <pre>
     * 发送带参数的POST的HTTP请求
     * </pre>
     *
     * @param reqUrl     HTTP请求URL
     * @param parameters 参数映射表
     * @return HTTP响应的字符串
     */
    public static String doPost(String reqUrl, Map<String, String> parameters, String recvEncoding,
                                String requestEncoding) {
        HttpURLConnection url_con = null;
        String responseContent = null;
        InputStream in = null;
        BufferedReader rd = null;
        try {
            StringBuilder params = new StringBuilder();
            for (Entry<String, String> element : parameters.entrySet()) {
                params.append(element.getKey().toString());
                params.append("=");
                params.append(URLEncoder.encode(element.getValue().toString(), requestEncoding));
                params.append("&");
            }

            if (params.length() > 0) {
                params = params.deleteCharAt(params.length() - 1);
            }

            URL url = new URL(reqUrl);
            url_con = (HttpURLConnection) url.openConnection();
            url_con.setRequestMethod("POST");
            System.setProperty("sun.net.client.defaultConnectTimeout", String.valueOf(HttpRequestProxy.connectTimeOut));// （单位：毫秒）jdk1.4换成这个,连接超时
            System.setProperty("sun.net.client.defaultReadTimeout", String.valueOf(HttpRequestProxy.readTimeOut)); // （单位：毫秒）jdk1.4换成这个,读操作超时
            url_con.setConnectTimeout(HttpRequestProxy.connectTimeOut);//（单位：毫秒）jdk
            // 1.5换成这个,连接超时
            url_con.setReadTimeout(HttpRequestProxy.readTimeOut);//（单位：毫秒）jdk 1.5换成这个,读操作超时

            url_con.setDoOutput(true);
            byte[] b = params.toString().getBytes();
            url_con.getOutputStream().write(b, 0, b.length);
            url_con.getOutputStream().flush();
            url_con.getOutputStream().close();

            in = url_con.getInputStream();
            rd = new BufferedReader(new InputStreamReader(in, recvEncoding));
            String tempLine = rd.readLine();
            StringBuilder tempStr = new StringBuilder();
            String crlf = System.getProperty("line.separator");
            while (tempLine != null) {
                tempStr.append(tempLine);
                tempStr.append(crlf);
                tempLine = rd.readLine();
            }
            responseContent = tempStr.toString();
            rd.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (rd != null) {
                try {
                    rd.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (url_con != null) {
                url_con.disconnect();
            }
        }
        return responseContent;
    }

    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            double randNum = Math.random();
            if (null != param && param.length() > 0) {
            } else {
                param = "v=" + randNum;
            }
            String urlNameString = url + "?" + param;

            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
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
     * @return 连接超时(毫秒)
     */
    public static int getConnectTimeOut() {
        return HttpRequestProxy.connectTimeOut;
    }

    /**
     * @return 读取数据超时(毫秒)
     */
    public static int getReadTimeOut() {
        return HttpRequestProxy.readTimeOut;
    }

    /**
     * @param connectTimeOut 连接超时(毫秒)
     */
    public static void setConnectTimeOut(int connectTimeOut) {
        HttpRequestProxy.connectTimeOut = connectTimeOut;
    }

    /**
     * @param readTimeOut 读取数据超时(毫秒)
     * @see #readTimeOut
     */
    public static void setReadTimeOut(int readTimeOut) {
        HttpRequestProxy.readTimeOut = readTimeOut;
    }


    public static void main(String[] args) {


    }
}