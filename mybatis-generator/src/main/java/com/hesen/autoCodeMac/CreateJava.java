package com.hesen.autoCodeMac;


import com.hesen.common.DateUtil;
import org.apache.velocity.VelocityContext;

import java.util.Date;
import java.util.Map;
import java.util.ResourceBundle;


/**
 * 代码自动生成器，最好再服务停止时运行，不然会有热部署警告
 * Created by LeonWang on 2015/7/29.
 */
public class CreateJava {
    private static ResourceBundle res = ResourceBundle.getBundle("autoCreateCodeEduMac");
    private static String schemaName= res.getString("autoCreate.schema");//db schema
    private static String url= res.getString("autoCreate.url");//db url
    private static String username =  res.getString("autoCreate.username");//db user
    private static String passWord = res.getString("autoCreate.password");//db pwd
    //private static String pkgSub1 = res.getString("autoCreate.pkgSub1");//一级子包
    private static String pkgSub2 = res.getString("autoCreate.pkgSub2");//二级子包
    private static String pkgSub = pkgSub2;
    private static String pkgSubAction = pkgSub2; //pkgSub1 + "." + pkgSub2;
    private static String pkgName = "/";//子包名
    private static String pkgNameSub = pkgSub2 + "/";//子包的子包名
    private static String author = res.getString("autoCreate.author");//作者
    private static String corePath = res.getString("autoCreate.corePath");//项目core根路径
    //private static String utilsPkg = res.getString("autoCreate.utilsPkg");//项目utils根路径
    //private static String entityPkg = res.getString("autoCreate.entityPkg");//项目entity根路径
    private static String logicPath = res.getString("autoCreate.logicPath");//项目logic根路径
    private static String webAppPath = res.getString("autoCreate.webAppPath");//项目webApp根路径
    private static String tableName = res.getString("autoCreate.tableName");//表名
    private static String codeName = res.getString("autoCreate.codeName");//中文名或者英文名
    private static String tableNickName = res.getString("autoCreate.tableNickName");//表别名
    private static String createDate = DateUtil.dateToHalfStr(new Date());//创建时间

    private static String ignoreTableHead = res.getString("autoCreate.ignoreTableHead");
    private static String corePkgBusi = res.getString("autoCreate.corePkgBusi");
    //private static String mybatisPkg = res.getString("autoCreate.mybatisPkg");//

    private static String controllerPkg = res.getString("autoCreate.controllerPkg");//controllerPkg
    //private static String commonPkg = res.getString("autoCreate.commonPkg");//公共项目路径
    private static String corePkg = res.getString("autoCreate.corePkg");//项目core物理根包路径
    private static String logicPkg = res.getString("autoCreate.logicPkg");//项目logic物理根包路径
    private static String webAppPkg = res.getString("autoCreate.webAppPkg");//项目webApp物理根包路径
    private static String corePkgProj = res.getString("autoCreate.corePkgProj");//项目core根包路径
    private static String coreConditonPkgProj = res.getString("autoCreate.coreConditonPkgProj");//项目core根包路径
    private static String coreDtoPkgProj = res.getString("autoCreate.coreDtoPkgProj");//项目core根包路径
    private static String coreApiPkgProj = res.getString("autoCreate.coreApiPkgProj");//项目coreApi根包路径
    private static String logicPkgProj = res.getString("autoCreate.logicPkgProj");//项目logic根包路径
    private static String webAppPkgProj = res.getString("autoCreate.webAppPkgProj");//项目webApp根包路径


    public static void main(String[] args) {
        CreateBean createBean=new CreateBean();
        createBean.setMysqlInfo(url, username, passWord);
        String className= createBean.getTablesNameToClassName(tableName.replace(ignoreTableHead,"").toLowerCase());
        String lowerName =className.substring(0, 1).toLowerCase()+className.substring(1, className.length());

        //根包路径
        String coreModelPath = corePath + corePkg + corePkgProj + "/"+ corePkgBusi + "/" ;
        String coreConditionPath = corePath + corePkg + coreConditonPkgProj + "/"+ corePkgBusi + "/" ;
        String coreDtoPath = corePath + corePkg + coreDtoPkgProj + "/"+ corePkgBusi + "/" ;
        String coreApiPath = corePath + corePkg + coreApiPkgProj + "/"+ corePkgBusi + "/" ;

        String srcPath = logicPath + logicPkg + logicPkgProj + "/";

        String webSrcPath = webAppPath + webAppPkg + webAppPkgProj + "/" + controllerPkg + "/" ; //+ corePkgBusi + "/";
        //资源文件路径
        String resourcePath = logicPath + "src/main/";

        //java,xml文件名称，包含路径
        String modelPath =  pkgNameSub + className + "To.java";
        String conditionPath =  pkgNameSub + className + "Condition.java";
        String dtoPath =  pkgNameSub + className + "DTO.java";
        String apiServicePath = pkgNameSub + className + "Service.java";

        String mapperPath = "dao/"+ corePkgBusi + "/" +pkgName + pkgNameSub + className + "Mapper.java";
        String servicePath = "impl/"+ corePkgBusi + "/" +pkgName + pkgNameSub + className + "ServiceImpl.java";
        String controllerPath = pkgNameSub + className + "Controller.java";
        String sqlMapperPath = "resources/mapper/"+ pkgName + corePkgBusi + "/"+ pkgNameSub + className + "Mapper.xml";

        VelocityContext context = new VelocityContext();
//        context.put("utilsPkg", utilsPkg.replace("/", "."));
//        context.put("entityPkg", entityPkg.replace("/", "."));
//        context.put("mybatisPkg", mybatisPkg.replace("/", "."));
        context.put("coreApiPkgProj", coreApiPkgProj.replace("/", "."));
//        context.put("commonPkg", commonPkg.replace("/", "."));
        context.put("corePkgProj", corePkgProj.replace("/", "."));
        context.put("coreConditonPkgProj", coreConditonPkgProj.replace("/", "."));
        context.put("coreDtoPkgProj", coreDtoPkgProj.replace("/", "."));
        context.put("logicPkgProj", logicPkgProj.replace("/", "."));
        context.put("webAppPkgProj", webAppPkgProj.replace("/", "."));
        context.put("controllerPkg", controllerPkg);
        context.put("corePkgBusi", corePkgBusi.replace("/", "."));
        context.put("className", className); //
        context.put("lowerName", lowerName);
        context.put("codeName", codeName);
        context.put("tableName", tableName);
        context.put("tableNickName", tableNickName);
        context.put("pkgSub", pkgSub);
        context.put("pkgSubAction", pkgSubAction);
        context.put("author", author);
        context.put("createDate", createDate);

        /******************************生成bean To*********************************/
        try{
            context.put("feilds",createBean.getBeanFeilds(tableName, schemaName, true)); //生成bean
            context.put("dtofeilds",createBean.getBeanFeilds(tableName, schemaName, false)); //生成bean
        }catch(Exception e){
            e.printStackTrace();
        }

        /*******************************生成mapper xml**********************************/
        try{
            //context.put("pk", createBean.getColumnPk(tableName, schemaName));
            Map<String,Object> sqlMap=createBean.getAutoCreateSql(tableName,tableNickName, schemaName);//sql语句
            context.put("columnDatas",createBean.getColumnDatas(tableName, schemaName)); //resultMap
            context.put("SQL",sqlMap);
        }catch(Exception e){
            e.printStackTrace();
            return;
        }

        //-------------------生成文件代码---------------------/
        CommonPageParser.writerPage(context, "TempModel.java",coreModelPath,modelPath); //生成实体Bean
        CommonPageParser.writerPage(context, "TempCondition.java",coreConditionPath,conditionPath); //生成实体Bean
        CommonPageParser.writerPage(context, "TempDTO.java",coreDtoPath,dtoPath); //生成实体Bean
        CommonPageParser.writerPage(context, "TempMapper.xml",resourcePath,sqlMapperPath);//生成Mybatis xml配置文件
        CommonPageParser.writerPage(context, "TempMapper.java", srcPath,mapperPath); //生成MybatisMapper接口 相当于Dao
        CommonPageParser.writerPage(context, "TempServiceImpl.java", srcPath,servicePath);//生成ServiceImpl
        CommonPageParser.writerPage(context, "TempService.java", coreApiPath,apiServicePath);//生成Service
        //CommonPageParser.writerPage(context, "TempControllerNew.java",webSrcPath, controllerPath);//生成Controller

    }

}
