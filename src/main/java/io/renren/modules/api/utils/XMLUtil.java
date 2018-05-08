package io.renren.modules.api.utils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.*;

/**
 * 将一个对象转换为xml形式
 */
public class XMLUtil {

    /**
     * 将xml文本转换为map
     * @param xmlText
     * @return
     */
    public static HashMap<String,String> xmlToMap(String xmlText){
        InputStream in = new ByteArrayInputStream(xmlText.getBytes());
        return xmlToMap(in);
    }

    /**
     * 将xml数据流转换为map
     * @param inputStream
     * @return
     */
    public static HashMap<String,String> xmlToMap(InputStream inputStream) {
        HashMap<String,String> map=new HashMap<>();
        // 通过IO获得Document
        try {
            SAXReader reader = new SAXReader();
            Document doc = reader.read(inputStream);
            //得到xml的根节点
            Element root=doc.getRootElement();
            recursiveParseXML(root,map);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 递归遍历节点
     * @param root
     * @param map
     */
    private static void recursiveParseXML(Element root,HashMap<String,String> map){
        //得到根节点的子节点列表
        List<Element> elementList = root.elements();
        //判断有没有子元素列表
        if(elementList.size()==0){
            map.put(root.getName(), root.getTextTrim());
        }
        else{
            //遍历
            for(Element e:elementList){
                recursiveParseXML(e,map);
            }
        }
    }

    private static XStream xstream = new XStream(new XppDriver() {
        public HierarchicalStreamWriter createWriter(Writer out) {
            return new PrettyPrintWriter(out) {
                // 对所有xml节点都增加CDATA标记
                boolean cdata = true;
                public void startNode(String name, Class clazz) {
                    super.startNode(name, clazz);
                }
                protected void writeText(QuickWriter writer, String text) {
                    if (cdata) {
                        writer.write("<![CDATA[");
                        writer.write(text);
                        writer.write("]]>");
                    } else {
                        writer.write(text);
                    }
                }
            };
        }
    });

    /**
     * 将javaBean对象转换为xml文本
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> String objToXML(T obj){
        xstream.alias("xml",obj.getClass());
        return xstream.toXML(obj).replaceAll("__","_");
    }

    /**
     * 将一个map对象转换为javaBean
     * @param map
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> T mapToObj(HashMap<String, String> map,T obj){
        Class<?> aClass = obj.getClass();
        Field[] fields = aClass.getDeclaredFields();
        try {
            for (Field f : fields) {
                f.setAccessible(true);
                String val = map.get(f.getName());
                if(f.getType() == Integer.class){
                    if(val == null){
                        f.set(obj,null);
                    }else{
                        f.set(obj,Integer.parseInt(val));
                    }
                }else if(f.getType() == String.class){
                    f.set(obj, val);
                }else{
                    LoggerFactory.getLogger(XMLUtil.class).error("有新属性类型，快处理。");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LoggerFactory.getLogger(XMLUtil.class).error("map转JavaBean时出了问题");
        }
        return obj;
    }

    /**
     * map转xml
     * @param map
     * @return
     */
    public static  StringBuffer mapToXML(Map map) {
        StringBuffer sb=new StringBuffer();
        sb.append("<xml>");
        Set set = map.keySet();
        for (Iterator it = set.iterator(); it.hasNext(); ) {
            String key = (String) it.next();
            Object value = map.get(key);
            if (null == value) {
                value = "";
            }else {
                sb.append("<" + key + ">" + value + "</" + key + ">");
            }
        }
        sb.append("</xml>");
        return sb;
    }

}