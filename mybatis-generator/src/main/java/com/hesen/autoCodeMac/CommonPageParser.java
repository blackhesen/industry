package com.hesen.autoCodeMac;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Properties;
import java.util.ResourceBundle;


public class CommonPageParser {
	
	private static VelocityEngine ve;
	
	private final static String CONTENT_ENCODING ="UTF-8";
	
	private static final Log log = LogFactory.getLog(CommonPageParser.class);
	
	private static boolean isReplace = true;  //是否可以替换文件 true =可以替换，false =不可以替换

    private static ResourceBundle res = ResourceBundle.getBundle("autoCreateCodeEduMac");
    //获取文件模板根路径
    private static String templateBasePath = res.getString("autoCreate.templatePath");
	
	public static void main(String[] args) {
	}

	static{
		try{
			Properties properties = new Properties();
			properties.setProperty(Velocity.RESOURCE_LOADER,"file");
			properties.setProperty("file.resource.loader.description","Velocity File Resource Loader");
			properties.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH,  templateBasePath);
			properties.setProperty(Velocity.FILE_RESOURCE_LOADER_CACHE, "true");
	        properties.setProperty("file.resource.loader.modificationCheckInterval", "30");
	        properties.setProperty(Velocity.RUNTIME_LOG_LOGSYSTEM_CLASS,  "org.apache.velocity.runtime.log.Log4JLogChute");
	        properties.setProperty("runtime.log.logsystem.log4j.logger", "org.apache.velocity");
	        properties.setProperty("directive.set.null.allowed", "true");
			VelocityEngine velocityEngine = new VelocityEngine();
			velocityEngine.init(properties);
			ve = velocityEngine;
		}catch(Exception e){
			log.error(e);
		}
	}
	
	/**
	 * 生成页面文件
	 * @param context 内容上下文
	 * @param templateName 模板文件路径（相对路径）yyy\\zzz.html
	 * @param targetFile 生成页面文件路径（相对路径） xxx\index_1.html
	 */
	public static void writerPage(VelocityContext context,String templateName,String fileDirPath,String targetFile){
		try{
			File file = new File(fileDirPath+targetFile);
			if(!file.exists()){
				new File(file.getParent()).mkdirs();
			}else{
				if(isReplace){
					//log.info("替换文件"+file.getAbsolutePath());
                    System.out.println("替换文件"+file.getAbsolutePath());
				}else{
				}
			}
			
			Template template = ve.getTemplate(templateName, CONTENT_ENCODING);
			FileOutputStream fos = new FileOutputStream(file);  
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos,CONTENT_ENCODING));
			template.merge(context, writer);
			writer.flush();  
		    writer.close();  
	    	fos.close();
            //log.info("生成成功"+file.getAbsolutePath());
            //System.out.println("生成成功"+file.getAbsolutePath());
		}catch (Exception e) {
			log.error(e);
            e.printStackTrace();
		}
	} 

}
