package com.hesen.xml;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class XmlUtil {

    /**
     * @param filePath
     * @param xPath
     * @param newNode
     * @param attrMap
     * @param text
     * @throws Exception
     */
	public void getAddNode(String filePath,String xPath,String newNode,Map<String,String> attrMap,String text) throws Exception{
		if(this.getQueryNode(filePath, xPath, newNode, attrMap, text)<1){
			Document document=this.getPath(filePath, "UTF-8");
			List<?> list=document.selectNodes(xPath);
			System.out.println(xPath);
	    	Element element=(Element) list.get(0);
	    	Element newElement=element.addElement(newNode);
	    	for(Map.Entry<String, String> entry:attrMap.entrySet()){
	    		newElement.addAttribute(entry.getKey(), entry.getValue());
	    	}
	    	if(null!=text && text.trim().length()>0){
	    	    newElement.addText(text);
	    	}
	    	this.getXMLWrite(document, filePath);
	    	System.out.println("修改"+xPath+"成功");
		}else{
			System.out.println("config已添加");
		}
    }
	/**
	 * @param filePath
	 * @param xPath
	 * @param newNode
	 * @param attrMap
	 * @param text
	 * @return
	 * @throws Exception
	 */
	public int getQueryNode(String filePath,String xPath,String newNode,Map<String,String> attrMap,String text) throws Exception{

		int count=0;
		Document document=this.getPath(filePath, "UTF-8");
    	StringBuffer sb=new StringBuffer();
    	for(Map.Entry<String, String> entry:attrMap.entrySet()){
    		sb.append("[@"+entry.getKey()+"='"+entry.getValue()+"']");
    	}
    	xPath=xPath+"/"+newNode+sb.toString();
    	System.out.println("xPath="+xPath);
    	document.selectNodes(xPath);
    	List<?> list=document.selectNodes(xPath);
    	for(int i=0;i<list.size();i++){
    		Element element=(Element) list.get(i);
    		if(element.getText().equals(text)){
    			count++;
    		}
    	}
    	//System.out.println(xPath+"|i="+list.size());
    	//System.out.println("count="+count+"|list.size="+list.size());
    	return count;
    }
	/**
	 * @param document
	 * @param filePath
	 * @throws Exception
	 */
	public void getXMLWrite(Document document,String filePath) throws Exception{
			//FileOutputStream fos = new FileOutputStream(filePath);
	    	OutputFormat of = new OutputFormat(" ", true);
	    	of.setEncoding("UTF-8");
            of.setExpandEmptyElements(true);
            of.setTrimText(true);
            of.setIndent(true);// 设置是否缩进
            of.setNewlines(true);    // 设置是否换行
	    	XMLWriter xw = new XMLWriter(new FileWriter(filePath), of);
	        xw.write(document);
	        xw.close();
	    	System.out.println(document.asXML());
	}

	/**
	 * @param filePath
	 * @param coding
	 * @return
	 */
	public Document getPath(String filePath,String coding){
		 SAXReader saxReader=new SAXReader();

		 Document document = null;
		 try {
			 saxReader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
			BufferedReader read=new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath)),coding));
			document = saxReader.read(read);
		 } catch (Exception e) {
			e.printStackTrace();
		 }
		 return document;
	}
	
}
