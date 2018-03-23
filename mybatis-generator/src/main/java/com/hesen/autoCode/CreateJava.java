package com.hesen.autoCode;

import com.hesen.common.DateUtil;
import com.hesen.xml.XmlUtil;
import org.apache.velocity.VelocityContext;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;


/**
 * 代码自动生成器，最好再服务停止时运行，不然会有热部署警告
 * Created by LeonWang on 2015/7/29.
 */
public class CreateJava {
    private static ResourceBundle res = ResourceBundle.getBundle("autoCreateCode");
    private static String schemaName = res.getString("autoCreate.schema");//db schema
    private static String url = res.getString("autoCreate.url");//db url
    private static String username = res.getString("autoCreate.username");//db user
    private static String passWord = res.getString("autoCreate.password");//db pwd
    private static String pkg = res.getString("autoCreate.pkg");//mapper、mapperxml、controller、service各层目录下的子包
    private static String pkgSub1 = res.getString("autoCreate.pkgSub1");//一级子包
    private static String pkgSub2 = res.getString("autoCreate.pkgSub2");//二级子包
    private static String apiLv = res.getString("autoCreate.apiLv");//api级别
    private static String module = res.getString("autoCreate.module");//最父工程模块
    private static String pkgSub = pkgSub2;
    private static String pkgSubAction = pkgSub1 + "." + pkgSub2;
    private static String pkgName = pkg + "\\";//子包名
    private static String dirName = module + "/";
    private static String pkgNameSub = pkgSub2 + "\\";//子包的子包名
    private static String dirNameSub = pkgSub2 + "/";
    private static String pkgNameSubAction = pkgSub1 + "\\" + pkgSub2 + "\\";//controller的子包名
    private static String mybatisConfName = res.getString("autoCreate.mybatisConfName");//mybatis的总配置文件名称
    private static String author = res.getString("autoCreate.author");//作者
    private static String logicPath = res.getString("autoCreate.logicPath");//项目logic根路径
    //    private static String webAppPath = res.getString("autoCreate.webAppPath");//项目webApp根路径
    private static String tableName = res.getString("autoCreate.tableName");//表名
    private static String codeName = res.getString("autoCreate.codeName");//中文名或者英文名
    private static String tableNickName = res.getString("autoCreate.tableNickName");//表别名
    private static String createDate = DateUtil.dateToHalfStr(new Date());//创建时间

    public static void main(String[] args) {
        CreateBean createBean = new CreateBean();
        createBean.setMysqlInfo(url, username, passWord);
        String className = createBean.getTablesNameToClassName(tableName.replace("tb_", "").replace("TB_", "").replace("OCORPER_FINANCE_", "").toLowerCase());
        String lowerName = className.substring(0, 1).toLowerCase() + className.substring(1, className.length());

        //根包路径
        String srcPath = logicPath + "src\\main\\java\\com\\jcgroup\\" + module + "\\" + pkgName + "\\";
//        String webSrcPath = webAppPath + "src\\main\\java\\com\\opark\\" + module + "\\";
        //资源文件路径
        String resourcePath = logicPath + "src\\main\\";

        //java,xml文件名称，包含路径
        String modelPath = "model\\" + pkgNameSub + className + "To.java";
        String mapperPath = "dao\\" + pkgNameSub + className + "Mapper.java";
        String servicePath = "service\\" + pkgNameSub + className + "Service.java";
        String controllerPath = "controller\\" + pkgNameSubAction + className + "Controller.java";
        String sqlMapperPath = "resources\\conf\\mybatis\\mappers\\" + module + "\\" + pkgNameSub + className + "Mapper.xml";
        String sqlMapperBuildPath = "conf/mybatis/mappers/" + dirName + dirNameSub + className + "Mapper.xml";

        VelocityContext context = new VelocityContext();
        context.put("className", className); //
        context.put("lowerName", lowerName);
        context.put("codeName", codeName);
        context.put("schemaName", schemaName);
        context.put("tableName", tableName);
        context.put("tableNickName", tableNickName);
        context.put("module", module);
        context.put("pkg", pkg);
        context.put("pkgSub", pkgSub);
        context.put("pkgSubAction", pkgSubAction);
        context.put("author", author);
        context.put("createDate", createDate);
        if (pkgSub1.contains("api")) {
            context.put("appFlag", "0");
            context.put("apiLv", apiLv);
        } else {
            context.put("appFlag", "1");
        }

        /******************************生成bean To*********************************/
        try {
            context.put("feilds", createBean.getBeanFeilds(tableName, schemaName)); //生成bean
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*******************************生成mapper xml**********************************/
        try {
            context.put("pk", createBean.getColumnPk(tableName, schemaName));
            Map<String, Object> sqlMap = createBean.getAutoCreateSql(tableName, tableNickName, schemaName);//sql语句
            context.put("columnDatas", createBean.getColumnDatas(tableName, schemaName)); //resultMap
            context.put("SQL", sqlMap);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        //-------------------生成文件代码---------------------/
        CommonPageParser.writerPage(context, "TempModel.java", srcPath, modelPath); //生成实体Bean
        CommonPageParser.writerPage(context, "TempMapper.java", srcPath, mapperPath); //生成MybatisMapper接口 相当于Dao
        CommonPageParser.writerPage(context, "TempMapper.xml", resourcePath, sqlMapperPath);//生成Mybatis xml配置文件
        CommonPageParser.writerPage(context, "TempService.java", srcPath, servicePath);//生成Service
//        CommonPageParser.WriterPage(context, "TempControllerHuhoo.java",webSrcPath, controllerPath);//生成Controller

        /*************************修改xml文件*****************************/
        XmlUtil xml = new XmlUtil();
        Map<String, String> attrMap = new HashMap<String, String>();
        try {
            /**   引入到mybatis-config下面的XML配置文件 */
            attrMap.clear();
            attrMap.put("resource", sqlMapperBuildPath);
            xml.getAddNode(resourcePath + "resources\\config\\" + mybatisConfName, "/configuration/mappers", "mapper", attrMap, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
