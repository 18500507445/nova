package com.nova.tools.utils.hutool.core.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Console;
import cn.hutool.core.map.MapBuilder;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.XmlUtil;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.xpath.XPathConstants;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * {@link XmlUtil} 工具类
 *
 * @author: Looly
 */
public class XmlUtilTest {

    @Test
    public void parseTest() {
        String result = "<?xml version=\"1.0\" encoding=\"utf-8\" ?>"//
                + "<returnsms>"//
                + "<returnstatus>Success</returnstatus>"//
                + "<message>ok</message>"//
                + "<remainpoint>1490</remainpoint>"//
                + "<taskID>885</taskID>"//
                + "<successCounts>1</successCounts>"//
                + "</returnsms>";
        Document docResult = XmlUtil.parseXml(result);
        String elementText = XmlUtil.elementText(docResult.getDocumentElement(), "returnstatus");
        Assert.equals("Success", elementText);
    }

    @Test
    public void writeTest() {
        String result = "<?xml version=\"1.0\" encoding=\"utf-8\" ?>"//
                + "<returnsms>"//
                + "<returnstatus>Success（成功）</returnstatus>"//
                + "<message>ok</message>"//
                + "<remainpoint>1490</remainpoint>"//
                + "<taskID>885</taskID>"//
                + "<successCounts>1</successCounts>"//
                + "</returnsms>";
        Document docResult = XmlUtil.parseXml(result);
        XmlUtil.toFile(docResult, "e:/aaa.xml", "utf-8");
    }

    @Test
    public void xpathTest() {
        String result = "<?xml version=\"1.0\" encoding=\"utf-8\" ?>"//
                + "<returnsms>"//
                + "<returnstatus>Success（成功）</returnstatus>"//
                + "<message>ok</message>"//
                + "<remainpoint>1490</remainpoint>"//
                + "<taskID>885</taskID>"//
                + "<successCounts>1</successCounts>"//
                + "</returnsms>";
        Document docResult = XmlUtil.parseXml(result);
        Object value = XmlUtil.getByXPath("//returnsms/message", docResult, XPathConstants.STRING);
        Assert.equals("ok", value);
    }

    @Test
    public void xpathTest2() {
        String result = ResourceUtil.readUtf8Str("test.xml");
        Document docResult = XmlUtil.parseXml(result);
        Object value = XmlUtil.getByXPath("//returnsms/message", docResult, XPathConstants.STRING);
        Assert.equals("ok", value);
    }

    @Test
    public void xmlToMapTest() {
        String xml = "<?xml version=\"1.0\" encoding=\"utf-8\" ?>"//
                + "<returnsms>"//
                + "<returnstatus>Success</returnstatus>"//
                + "<message>ok</message>"//
                + "<remainpoint>1490</remainpoint>"//
                + "<taskID>885</taskID>"//
                + "<successCounts>1</successCounts>"//
                + "<newNode><sub>subText</sub></newNode>"//
                + "</returnsms>";
        Map<String, Object> map = XmlUtil.xmlToMap(xml);

        Assert.equals(6, map.size());
        Assert.equals("Success", map.get("returnstatus"));
        Assert.equals("ok", map.get("message"));
        Assert.equals("1490", map.get("remainpoint"));
        Assert.equals("885", map.get("taskID"));
        Assert.equals("1", map.get("successCounts"));
        Assert.equals("subText", ((Map<?, ?>) map.get("newNode")).get("sub"));
    }

    @Test
    public void xmlToMapTest2() {
        String xml = "<root><name>张三</name><name>李四</name></root>";
        Map<String, Object> map = XmlUtil.xmlToMap(xml);

        Assert.equals(1, map.size());
        Assert.equals(CollUtil.newArrayList("张三", "李四"), map.get("name"));
    }

    @Test
    public void mapToXmlTest() {
        Map<String, Object> map = MapBuilder.create(new LinkedHashMap<String, Object>())//
                .put("name", "张三")//
                .put("age", 12)//
                .put("game", MapUtil.builder(new LinkedHashMap<String, Object>()).put("昵称", "Looly").put("level", 14).build())//
                .build();
        Document doc = XmlUtil.mapToXml(map, "user");
        // Console.log(XmlUtil.toStr(doc, false));
        Assert.equals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"//
                        + "<user>"//
                        + "<name>张三</name>"//
                        + "<age>12</age>"//
                        + "<game>"//
                        + "<昵称>Looly</昵称>"//
                        + "<level>14</level>"//
                        + "</game>"//
                        + "</user>", //
                XmlUtil.toStr(doc, false));
    }

    @Test
    public void mapToXmlTest2() {
        // 测试List
        Map<String, Object> map = MapBuilder.create(new LinkedHashMap<String, Object>())
                .put("Town", CollUtil.newArrayList("town1", "town2"))
                .build();

        Document doc = XmlUtil.mapToXml(map, "City");
        Assert.equals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>" +
                        "<City>" +
                        "<Town>town1</Town>" +
                        "<Town>town2</Town>" +
                        "</City>",
                XmlUtil.toStr(doc));
    }

    @Test
    public void readTest() {
        Document doc = XmlUtil.readXML("test.xml");
        Assert.notNull(doc);
    }

    @Test
    public void readBySaxTest() {
        final Set<String> eles = CollUtil.newHashSet(
                "returnsms", "returnstatus", "message", "remainpoint", "taskID", "successCounts");
        XmlUtil.readBySax(ResourceUtil.getStream("test.xml"), new DefaultHandler() {
            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) {
                Assert.isTrue(eles.contains(localName));
            }
        });
    }

    @Test
    public void mapToXmlTestWithOmitXmlDeclaration() {

        Map<String, Object> map = MapBuilder.create(new LinkedHashMap<String, Object>())
                .put("name", "ddatsh")
                .build();
        String xml = XmlUtil.mapToXmlStr(map, true);
        Assert.equals("<xml><name>ddatsh</name></xml>", xml);
    }

    @Test
    public void getByPathTest() {
        String xmlStr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "  <soap:Body>\n" +
                "    <ns2:testResponse xmlns:ns2=\"http://ws.xxx.com/\">\n" +
                "      <return>2020/04/15 21:01:21</return>\n" +
                "    </ns2:testResponse>\n" +
                "  </soap:Body>\n" +
                "</soap:Envelope>\n";

        Document document = XmlUtil.readXML(xmlStr);
        Object value = XmlUtil.getByXPath(
                "//soap:Envelope/soap:Body/ns2:testResponse/return",
                document, XPathConstants.STRING);//
        Assert.equals("2020/04/15 21:01:21", value);
    }

    @Test
    public void beanToXmlIgnoreNullTest() {
        @Data
        class TestBean {
            private String ReqCode;
            private String AccountName;
            private String Operator;
            private String ProjectCode;
            private String BankCode;
        }

        final TestBean testBean = new TestBean();
        testBean.setReqCode("1111");
        testBean.setAccountName("账户名称");
        testBean.setOperator("cz");
        testBean.setProjectCode(null);
        testBean.setBankCode("00001");

        // 不忽略空字段情况下保留自闭标签
        Document doc = XmlUtil.beanToXml(testBean, null, false);
        Assert.notNull(XmlUtil.getElement(doc.getDocumentElement(), "ProjectCode"));

        // 忽略空字段情况下无自闭标签
        doc = XmlUtil.beanToXml(testBean, null, true);
        Assert.isNull(XmlUtil.getElement(doc.getDocumentElement(), "ProjectCode"));
    }

    @Test
    public void xmlToBeanTest() {
        @Data
        class TestBean {
            private String ReqCode;
            private String AccountName;
            private String Operator;
            private String ProjectCode;
            private String BankCode;
        }

        final TestBean testBean = new TestBean();
        testBean.setReqCode("1111");
        testBean.setAccountName("账户名称");
        testBean.setOperator("cz");
        testBean.setProjectCode("123");
        testBean.setBankCode("00001");

        final Document doc = XmlUtil.beanToXml(testBean);
        Assert.equals(TestBean.class.getSimpleName(), doc.getDocumentElement().getTagName());

        final TestBean testBean2 = XmlUtil.xmlToBean(doc, TestBean.class);
        Assert.equals(testBean.getReqCode(), testBean2.getReqCode());
        Assert.equals(testBean.getAccountName(), testBean2.getAccountName());
        Assert.equals(testBean.getOperator(), testBean2.getOperator());
        Assert.equals(testBean.getProjectCode(), testBean2.getProjectCode());
        Assert.equals(testBean.getBankCode(), testBean2.getBankCode());
    }

    @Test
    public void xmlToBeanTest2() {
        @Data
        class SmsRes {
            private String code;
        }

        //issue#1663@Github
        String xmlStr = "<?xml version=\"1.0\" encoding=\"gbk\" ?><response><code>02</code></response>";

        Document doc = XmlUtil.parseXml(xmlStr);

        // 标准方式
        Map<String, Object> map = XmlUtil.xmlToMap(doc.getFirstChild());
        SmsRes res = new SmsRes();
        BeanUtil.fillBeanWithMap(map, res, true);

        // toBean方式
        SmsRes res1 = XmlUtil.xmlToBean(doc.getFirstChild(), SmsRes.class);

        Assert.equals(res.toString(), res1.toString());
    }

    @Test
    public void cleanCommentTest() {
        final String xmlContent = "<info><title>hutool</title><!-- 这是注释 --><lang>java</lang></info>";
        final String ret = XmlUtil.cleanComment(xmlContent);
        Assert.equals("<info><title>hutool</title><lang>java</lang></info>", ret);
    }

    @Test
    public void formatTest() {
        // https://github.com/looly/hutool/pull/1234
        Document xml = XmlUtil.createXml("NODES");
        xml.setXmlStandalone(true);

        NodeList parentNode = xml.getElementsByTagName("NODES");

        Element parent1Node = xml.createElement("NODE");

        Element node1 = xml.createElement("NODENAME");
        node1.setTextContent("走位");
        Element node2 = xml.createElement("STEP");
        node2.setTextContent("1");
        Element node3 = xml.createElement("STATE");
        node3.setTextContent("2");
        Element node4 = xml.createElement("TIMELIMIT");
        node4.setTextContent("");
        Element node5 = xml.createElement("STARTTIME");

        parent1Node.appendChild(node1);
        parent1Node.appendChild(node2);
        parent1Node.appendChild(node3);
        parent1Node.appendChild(node4);
        parent1Node.appendChild(node5);

        parentNode.item(0).appendChild(parent1Node);

        String format = XmlUtil.toStr(xml, "GBK", true);
        Console.log(format);
    }

    @Test
    public void escapeTest() {
        String a = "<>";
        final String escape = XmlUtil.escape(a);
        Console.log(escape);
    }

    @Test
    public void getParamTest() {
        String xml = "<Config name=\"aaaa\">\n" +
                "    <url>222222</url>\n" +
                "</Config>";

        final Document doc = XmlUtil.parseXml(xml);
        final String name = doc.getDocumentElement().getAttribute("name");
        Assert.equals("aaaa", name);
    }
}
