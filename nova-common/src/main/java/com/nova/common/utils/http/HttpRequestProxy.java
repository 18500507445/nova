package com.nova.common.utils.http;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import com.nova.common.constant.Constants;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import javax.net.ssl.*;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author wzh
 */
@Slf4j(topic = "HttpRequestProxy")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HttpRequestProxy {

    /**
     * 连接超时
     */
    private static final int CONNECT_TIME_OUT = 50000;

    /**
     * 读取数据超时
     */
    private static final int READ_TIME_OUT = 6000000;

    /**
     * 向指定 URL 发送GET方法的请求
     *
     * @param url 发送请求的 URL
     * @return 所代表远程资源的响应结果
     */
    public static String sendGet(String url) {
        return sendGet(url, "");
    }

    /**
     * 向指定 URL 发送GET方法的请求
     *
     * @param url   发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param) {
        return sendGet(url, param, Constants.UTF8);
    }

    /**
     * 向指定 URL 发送GET方法的请求
     *
     * @param url         发送请求的 URL
     * @param param       请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @param contentType 编码类型
     * @return 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param, String contentType) {
        StringBuilder result = new StringBuilder();
        BufferedReader in;
        try {
            String urlNameString = ObjectUtil.isNotEmpty(param) ? url + "?" + param : url;
            log.info("sendGet - {}", urlNameString);
            URL realUrl = new URL(urlNameString);
            URLConnection connection = realUrl.openConnection();
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.connect();
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), contentType));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            log.info("result - {}", result);
        } catch (ConnectException e) {
            log.error("调用HttpUtils.sendGet ConnectException, url=" + url + ",param=" + param, e);
        } catch (SocketTimeoutException e) {
            log.error("调用HttpUtils.sendGet SocketTimeoutException, url=" + url + ",param=" + param, e);
        } catch (IOException e) {
            log.error("调用HttpUtils.sendGet IOException, url=" + url + ",param=" + param, e);
        } catch (Exception e) {
            log.error("调用HttpsUtil.sendGet Exception, url=" + url + ",param=" + param, e);
        }
        return result.toString();
    }


    /**
     * 向指定 URL 发送GET方法的请求
     *
     * @param url         发送请求的 URL
     * @param param       请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @param headerParam 请求头参数
     * @return 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param, Map<String, Object> headerParam) {
        StringBuilder result = new StringBuilder();
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            log.info("sendGet - {}", urlNameString);
            URL realUrl = new URL(urlNameString);
            URLConnection connection = realUrl.openConnection();
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            for (Map.Entry<String, Object> entry : headerParam.entrySet()) {
                connection.setRequestProperty(entry.getKey(), ObjectUtil.toString(entry.getValue()));
            }
            connection.connect();
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            log.info("result - {}", result);
        } catch (ConnectException e) {
            log.error("调用HttpUtils.sendPost ConnectException, url=" + url + ",param=" + param, e);
        } catch (SocketTimeoutException e) {
            log.error("调用HttpUtils.sendPost SocketTimeoutException, url=" + url + ",param=" + param, e);
        } catch (IOException e) {
            log.error("调用HttpUtils.sendPost IOException, url=" + url + ",param=" + param, e);
        } catch (Exception e) {
            log.error("调用HttpsUtil.sendPost Exception, url=" + url + ",param=" + param, e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception ex) {
                log.error("调用in.close Exception, url=" + url + ",param=" + param, ex);
            }
        }
        return result.toString();
    }

    /**
     * 糊涂工具类
     */
    public static String sendGet(String url, Map<String, String> header, Map<String, Object> param) {
        return HttpUtil.createGet(url)
                .form(param)
                .addHeaders(header)
                .execute()
                .body();
    }

    /**
     *
     */
    public static String sendPost(String url, Map<String, String> header, Map<String, Object> param) {
        return HttpUtil.createPost(url)
                .form(param)
                .addHeaders(header)
                .execute()
                .body();
    }

    /**
     * <pre>
     * 发送带参数的GET的HTTP请求
     * </pre>
     *
     * @param reqUrl     HTTP请求URL
     * @param parameters 参数映射表
     * @return HTTP响应的字符串
     */
    public static String doGet(String reqUrl, Map<String, String> parameters, String recvEncoding, String requestEncoding) {
        HttpURLConnection url_con = null;
        String responseContent = null;
        InputStream in = null;
        BufferedReader rd = null;
        try {
            StringBuilder params = new StringBuilder();
            for (Entry<String, String> element : parameters.entrySet()) {
                params.append(element.getKey());
                params.append("=");
                params.append(URLEncoder.encode(element.getValue(), requestEncoding));
                params.append("&");
            }
            if (params.length() > 0) {
                params = params.deleteCharAt(params.length() - 1);
            }

            URL url = new URL(reqUrl);
            long time11 = System.currentTimeMillis();
            url_con = (HttpURLConnection) url.openConnection();
            url_con.setRequestMethod("GET");
            System.setProperty("sun.net.client.defaultConnectTimeout", String.valueOf(HttpRequestProxy.CONNECT_TIME_OUT));// （单位：毫秒）jdk1.4换成这个,连接超时
            System.setProperty("sun.net.client.defaultReadTimeout", String.valueOf(HttpRequestProxy.READ_TIME_OUT)); // （单位：毫秒）jdk1.4换成这个,读操作超时
            url_con.setConnectTimeout(HttpRequestProxy.CONNECT_TIME_OUT);//（单位：毫秒）jdk
            // 1.5换成这个,连接超时
            url_con.setReadTimeout(HttpRequestProxy.READ_TIME_OUT);//（单位：毫秒）jdk 1.5换成这个,读操作超时
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
            log.debug(reqUrl + "http-get总耗时=" + (time22 - time11) + "毫秒");
        } catch (IOException e) {
            log.error("异常信息:", e);
        } finally {
            if (rd != null) {
                try {
                    rd.close();
                } catch (IOException e) {
                    log.error("异常信息:", e);
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    log.error("异常信息:", e);
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
            System.setProperty("sun.net.client.defaultConnectTimeout", String.valueOf(HttpRequestProxy.CONNECT_TIME_OUT));// （单位：毫秒）jdk1.4换成这个,连接超时
            System.setProperty("sun.net.client.defaultReadTimeout", String.valueOf(HttpRequestProxy.READ_TIME_OUT)); // （单位：毫秒）jdk1.4换成这个,读操作超时
            url_con.setConnectTimeout(HttpRequestProxy.CONNECT_TIME_OUT);//（单位：毫秒）jdk
            // 1.5换成这个,连接超时
            url_con.setReadTimeout(HttpRequestProxy.READ_TIME_OUT);//（单位：毫秒）jdk 1.5换成这个,读操作超时
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
            log.error("异常信息:", e);
        } finally {
            if (rd != null) {
                try {
                    rd.close();
                } catch (IOException e) {
                    log.error("异常信息:", e);
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    log.error("异常信息:", e);
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
                params.append(element.getKey());
                params.append("=");
                params.append(URLEncoder.encode(element.getValue(), requestEncoding));
                params.append("&");
            }
            if (params.length() > 0) {
                params = params.deleteCharAt(params.length() - 1);
            }
            URL url = new URL(reqUrl);
            url_con = (HttpURLConnection) url.openConnection();
            url_con.setRequestMethod("POST");
            // （单位：毫秒）jdk1.4换成这个,连接超时
            System.setProperty("sun.net.client.defaultConnectTimeout", String.valueOf(HttpRequestProxy.CONNECT_TIME_OUT));
            // （单位：毫秒）jdk1.4换成这个,读操作超时
            System.setProperty("sun.net.client.defaultReadTimeout", String.valueOf(HttpRequestProxy.READ_TIME_OUT));
            url_con.setConnectTimeout(HttpRequestProxy.CONNECT_TIME_OUT);//（单位：毫秒）jdk
            // 1.5换成这个,连接超时
            url_con.setReadTimeout(HttpRequestProxy.READ_TIME_OUT);//（单位：毫秒）jdk 1.5换成这个,读操作超时

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
            log.error("异常信息:", e);
        } finally {
            if (rd != null) {
                try {
                    rd.close();
                } catch (IOException e) {
                    log.error("异常信息:", e);
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    log.error("异常信息:", e);
                }
            }
            if (url_con != null) {
                url_con.disconnect();
            }
        }
        return responseContent;
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url   发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        try {
            log.info("sendPost - {}", url);
            URL realUrl = new URL(url);
            URLConnection conn = realUrl.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Accept-Charset", "utf-8");
            conn.setRequestProperty("contentType", "utf-8");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            out = new PrintWriter(conn.getOutputStream());
            out.print(param);
            out.flush();
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            log.info("result - {}", result);
        } catch (ConnectException e) {
            log.error("调用HttpUtils.sendPost ConnectException, url=" + url + ",param=" + param, e);
        } catch (SocketTimeoutException e) {
            log.error("调用HttpUtils.sendPost SocketTimeoutException, url=" + url + ",param=" + param, e);
        } catch (IOException e) {
            log.error("调用HttpUtils.sendPost IOException, url=" + url + ",param=" + param, e);
        } catch (Exception e) {
            log.error("调用HttpsUtil.sendPost Exception, url=" + url + ",param=" + param, e);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                log.error("调用in.close Exception, url=" + url + ",param=" + param, ex);
            }
        }
        return result.toString();
    }

    /**
     * 向指定 URL 发送SSL POST方法的请求
     *
     * @param url   发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendSSLPost(String url, String param) {
        StringBuilder result = new StringBuilder();
        String urlNameString = url + "?" + param;
        try {
            log.info("sendSSLPost - {}", urlNameString);
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, new TrustManager[]{new TrustAnyTrustManager()}, new java.security.SecureRandom());
            URL console = new URL(urlNameString);
            HttpsURLConnection conn = (HttpsURLConnection) console.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Accept-Charset", "utf-8");
            conn.setRequestProperty("contentType", "utf-8");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setSSLSocketFactory(sc.getSocketFactory());
            conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String ret;
            while ((ret = br.readLine()) != null) {
                if (!ret.trim().isEmpty()) {
                    result.append(new String(ret.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8));
                }
            }
            log.info("result - {}", result);
            conn.disconnect();
            br.close();
        } catch (ConnectException e) {
            log.error("调用HttpUtils.sendSSLPost ConnectException, url=" + url + ",param=" + param, e);
        } catch (SocketTimeoutException e) {
            log.error("调用HttpUtils.sendSSLPost SocketTimeoutException, url=" + url + ",param=" + param, e);
        } catch (IOException e) {
            log.error("调用HttpUtils.sendSSLPost IOException, url=" + url + ",param=" + param, e);
        } catch (Exception e) {
            log.error("调用HttpsUtil.sendSSLPost Exception, url=" + url + ",param=" + param, e);
        }
        return result.toString();
    }

    /**
     * 向指定url 发送post方法的请求,讲参数写入包body中
     *
     * @param url   发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPostBody(String url, String param) {
        OutputStreamWriter out = null;
        InputStream is = null;
        String result = "";
        try {
            URL realUrl = new URL(url.replaceAll("\r|\n", ""));
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Accept-Charset", "utf-8");
            conn.setRequestProperty("contentType", "utf-8");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            out = new OutputStreamWriter(conn.getOutputStream());
            out.write(param);
            out.flush();
            out.close();
            is = conn.getInputStream();
            String encoding = conn.getContentEncoding();
            result = IOUtils.toString(is, encoding);
        } catch (ConnectException e) {
            log.error("调用HttpUtils.sendPostBody ConnectException, url=" + url + ",param=" + param, e);
        } catch (SocketTimeoutException e) {
            log.error("调用HttpUtils.sendPostBody SocketTimeoutException, url=" + url + ",param=" + param, e);
        } catch (IOException e) {
            log.error("调用HttpUtils.sendPostBody IOException, url=" + url + ",param=" + param, e);
        } catch (Exception e) {
            log.error("调用HttpsUtil.sendPostBody Exception, url=" + url + ",param=" + param, e);
        }
        return result;
    }

    public static String httpRequest(String requestUrl, String requestMethod, String outputStr) {
        // 创建SSLContext
        StringBuilder buffer = null;
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(requestMethod);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.connect();
            //往服务器端写内容
            if (null != outputStr) {
                OutputStream os = conn.getOutputStream();
                os.write(outputStr.getBytes(StandardCharsets.UTF_8));
                os.close();
            }
            // 读取服务器端返回的内容
            InputStream is = conn.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(isr);
            buffer = new StringBuilder();
            String line = null;
            while ((line = br.readLine()) != null) {
                buffer.append(line);
            }
            br.close();
        } catch (Exception e) {
            log.error("异常信息:", e);
        }
        return buffer.toString();
    }

    private static class TrustAnyTrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }
    }

    private static class TrustAnyHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }


    /**
     * 获取二进制文件
     *
     * @param urlStr
     * @return
     * @throws IOException
     */
    public static byte[] downLoadBinary(String urlStr) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //设置超时间为10秒
        conn.setConnectTimeout(10 * 1000);
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.135 Safari/537.36");
        //得到输入流
        InputStream inputStream = conn.getInputStream();
        //获取数据数组
        return readInputStream(inputStream);
    }

    /**
     * 输入流转二进制
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }

}