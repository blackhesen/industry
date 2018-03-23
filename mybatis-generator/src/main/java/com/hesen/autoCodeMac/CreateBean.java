package com.hesen.autoCodeMac;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by LeonWang on 2015/7/29.
 */
public class CreateBean {
	private Connection connection = null;
    private String uuidColName = null;
	static String url;   
	static String username ;
	static String password ;
	static String rt="\r\t";
	String sqlTables="show tables";
	static{
	try{
		Class.forName("com.mysql.jdbc.Driver");
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void setMysqlInfo(String url,String username,String password){
		   CreateBean.url =url;
		   CreateBean.username =username;
		   CreateBean.password =password;
	}
	public void setConnection(Connection connection) {
		this.connection = connection;
	}
	public Connection getConnection() throws SQLException{
		return DriverManager.getConnection(url, username, password);
	}


    /**
     *  去掉指定字符串的开头和结尾的指定字符
     * @param stream 要处理的字符串
     * @param trimstr 要去掉的字符串
     * @return 处理后的字符串
     */
    public static String sideTrim(String stream, String trimstr) {
        // null或者空字符串的时候不处理
        if (stream == null || stream.length() == 0 || trimstr == null || trimstr.length() == 0) {
            return stream;
        }
        // 结束位置
        int epos = 0;
        // 正规表达式
        String regpattern = "[" + trimstr + "]*+";
        Pattern pattern = Pattern.compile(regpattern, Pattern.CASE_INSENSITIVE);
        // 去掉结尾的指定字符
        StringBuffer buffer = new StringBuffer(stream).reverse();
        Matcher matcher = pattern.matcher(buffer);
        if (matcher.lookingAt()) {
            epos = matcher.end();
            stream = new StringBuffer(buffer.substring(epos)).reverse().toString();
        }
        // 去掉开头的指定字符
        matcher = pattern.matcher(stream);
        if (matcher.lookingAt()) {
            epos = matcher.end();
            stream = stream.substring(epos);
        }
        // 返回处理后的字符串
        return stream;
    }


    private static String[] headcodes = {"S_","N_","D_"};

    /**
     * 去除开头指定字符串
     * @param str
     * @return
     */
    public static String getCutHeadStr(String str) {
        // null或者空字符串的时候不处理
        if (str == null || str.length() == 0 || str == null || str.length() == 0) {
            return str;
        }
        for (int i = 0; i < headcodes.length; i++) {
            if (str.startsWith(headcodes[i])) {
                //name = sideTrim(name,headcodes[i]);
                str = str.replaceFirst("^" + headcodes[i] + "+", "");
            } else if (str.startsWith(headcodes[i].toLowerCase())) {
                //name = sideTrim(name,headcodes[i]);
                str = str.replaceFirst("^" + headcodes[i].toLowerCase() + "+", "");
            }
        }
        str = CamelCaseUtils.toCamelCase(str);
        return str;
    }

    /**
     * 查询表的字段，封装成List
     * @param tableName
     * @return
     * @throws SQLException
     */
    public List<ColumnData> getColumnDatas(String tableName, String schemaName) throws SQLException{
    	String sqlColumns=
                "SELECT distinct COLUMN_NAME, DATA_TYPE, COLUMN_COMMENT,EXTRA,COLUMN_KEY,IS_NULLABLE,CHARACTER_MAXIMUM_LENGTH" +
                " FROM information_schema.columns WHERE table_name =  '"+tableName+"' "+ "and TABLE_SCHEMA = '"+schemaName+"' ";
        Connection con=this.getConnection();
    	PreparedStatement ps=con.prepareStatement(sqlColumns);
    	List<ColumnData>  columnList= new ArrayList<ColumnData>();
        ResultSet rs=ps.executeQuery();
        StringBuffer str = new StringBuffer();
		StringBuffer getset=new StringBuffer();
        while(rs.next()){
        	String name = rs.getString(1);
			String type = rs.getString(2);
			String comment = rs.getString(3);
            String extra = rs.getString(4);
            String isPk = rs.getString(5);
            String isNull = rs.getString(6);
            String maxLength = rs.getString(7);
			type=this.getType(type);

			ColumnData cd= new ColumnData();
			cd.setColumnName(name);
			cd.setDataType(type);
			cd.setColumnComment(comment);
            cd.setExtra(extra);
            cd.setIsPk(isPk);
            cd.setIsNull(isNull);
            cd.setMaxLength(maxLength);
            if (name.indexOf("_UUID")>0) {
                uuidColName = name;
                cd.setPropertyName("uuId");
            } else {
                cd.setPropertyName(getCutHeadStr(name));//实体bean的属性名
            }
			columnList.add(cd);
        }
        argv=str.toString();
        method=getset.toString();
		rs.close();
		ps.close();
		con.close();
		return columnList;
    }

    public String getColumnPk(String tableName, String schemaName) throws SQLException{
        String sqlColumns=
                "SELECT distinct COLUMN_NAME, DATA_TYPE, COLUMN_COMMENT,EXTRA,COLUMN_KEY,IS_NULLABLE,CHARACTER_MAXIMUM_LENGTH" +
                        " FROM information_schema.columns WHERE table_name =  '"+tableName+"' "+ "and TABLE_SCHEMA = '"+schemaName+"' ";
        Connection con=this.getConnection();
        PreparedStatement ps=con.prepareStatement(sqlColumns);
        ResultSet rs=ps.executeQuery();
        while(rs.next()){
            String name = rs.getString(1);
            String extra = rs.getString(4);
            if ("pri".equals(extra) || "PRI".equals(extra)) {
                return getCutHeadStr(name);
            }
        }
        return null;
    }

    private String method;
    private String argv;

    /**
     * 生成实体Bean 的属性和set,get方法
     * @param tableName
     * @return
     * @throws SQLException
     */
    public String getBeanFeilds(String tableName, String schemaName, boolean isModel) throws SQLException{
    	List<ColumnData> dataList = getColumnDatas(tableName, schemaName);
    	StringBuffer str = new StringBuffer();
    	StringBuffer getset = new StringBuffer();
        //int i = 4;
        for(ColumnData d : dataList){
            //baseto里已经由这些属性
             if(d.getColumnName().contains("create_user") ||
                 d.getColumnName().contains("create_time") ||
                 d.getColumnName().contains("version")||
                 d.getColumnName().contains("delete_flg")||
                 d.getColumnName().contains("delete_flag")||
                 d.getColumnName().contains("update_user")||
                 d.getColumnName().contains("update_time")) {
                 continue;
             }
            String name = getCutHeadStr(d.getColumnName());
			String type =  d.getDataType();
			String comment =  d.getColumnComment();
            String isPk = d.getIsPk();
            String isNull = d.getIsNull();
            String maxLength = d.getMaxLength();
			String maxChar=name.substring(0, 1).toUpperCase();
            if (isModel) {
                if ("NO".equals(isNull)) {
                    if ("PRI".equals(isPk) || "pri".equals(isPk)) {
                        str.append("\r\t").append("@NotBlank(message = ")
                                .append("\"" + comment + "为必输" + "\",")
                                .append("groups = " + "{Query.class, Modify.class, Delete.class}" + ")");
                    } else {
                        str.append("\r\t").append("@NotBlank(message = ")
                                .append("\"" + comment + "为必输" + "\",")
                                .append("groups = " + "{Add.class, Modify.class}" + ")");
                    }
                }
                str.append("\r\t").append("@Size(max= " + maxLength).append(", message = ")
                        .append("\"" + comment + "为必输" + "不能超过" + maxLength + "字符" + "\",")
                        .append("groups = " + "{Add.class, Modify.class}" + ")");
            }
			str.append("\r\t").append("private ").append(type+" ").append(name).append(";//   ").append(comment);
			String method=maxChar+name.substring(1, name.length());
			getset.append("\r\t").append("public ").append(type+" ").append("get"+method+"() {\r\t");
			getset.append("    return this.").append(name).append(";\r\t}");
			getset.append("\r\t").append("public void ").append("set"+method+"("+type+" "+name+") {\r\t");
			getset.append("    this."+name+"=").append(name).append(";\r\t}");
            //i++;
        }
        argv=str.toString();
        method=getset.toString();
		return argv+method;
    }

    public String getType(String type){
    	type=type.toLowerCase();
    	if("char".equals(type) || "varchar".equals(type)){
			return "String";
		}else if("int".equals(type) ){
			return "Integer";
		}else if("bigint".equals(type)){
			return "Long";
		}else if("decimal".equals(type)){
            return "Float";
        }else if("timestamp".equals(type) || "date".equals(type)){
			return "java.sql.Timestamp";
		}else if( "datetime".equals(type)){
            return "Date";
        }else if("geometry".equals(type) ) {
            return "String";
        }
    	return "String";
    }

    /**
     * 表名转换成类名 每_首字母大写
     * @param tableName
     * @return
     */
    public String getTablesNameToClassName(String tableName){
    	String [] split=tableName.split("_");
    	if(split.length>1){
    		StringBuffer sb=new StringBuffer();
            for(int i=0;i<split.length;i++){
            	String tempTableName=split[i].substring(0, 1).toUpperCase()+split[i].substring(1, split[i].length());
                sb.append(tempTableName);
            }
            System.out.println(sb.toString());
            return sb.toString();
    	}else{
    		String tempTables=split[0].substring(0, 1).toUpperCase()+split[0].substring(1, split[0].length());
    		return tempTables;
    	}
    }

   /**
    * 生成sql语句
    * @param tableName
    * @throws Exception
    */

    public Map<String,Object> getAutoCreateSql(String tableName,String tableNickName, String schemaName) throws Exception{
   	 	 Map<String,Object> sqlMap=new HashMap<String, Object>();
   	 	 List<ColumnData> columnDatas = getColumnDatas(tableName, schemaName);
	     String columns=this.getColumnSplit(columnDatas);
         String columnsWithNick = this.getColumnSplit(columnDatas,tableNickName);
         String props=this.getPropSplit(columnDatas);//实体所有属性
	     String[] columnList =  getColumnList(columns);  //表所有字段
	     String columnFields =  getColumnFields(columns); //表所有字段 按","隔开
         String columnFieldsWithNick =  getColumnFields(columnsWithNick); //表所有字段 按","隔开，但带有表别名
//         String propFields =  getColumnFields(props); //实体所有属性 按","隔开
         String columnsNoAI=this.getColumnSplitNoAI(columnDatas);//不带自动增长列
         String propsNoAI=this.getPropSplitNoAI(columnDatas);//实体所有属性(不带自动增长列)
	     String insert="insert into "+tableName+"("+columnsNoAI.replaceAll("\\|", ",")+")\n values(#{"+propsNoAI.replaceAll("\\|", "},#{")+"})";
         //insert = insert.replaceFirst("id,","").replace("#{id},","");//id让DB生成
	     String update= getUpdateSql(tableName, columnDatas);
	     String updateTo = getUpdateToSql(tableName, columnDatas);
	     String selectById = getSelectByIdSql(tableName, columnDatas);
         String selectByUUId = getSelectByUUIdSql(tableName, columnDatas);
	     String delete = getDeleteSql(tableName, columnDatas);
	     sqlMap.put("columnList",columnList);
	     sqlMap.put("columnFields",columnFields);
         sqlMap.put("columnFieldsWithNick",columnFieldsWithNick);
	     sqlMap.put("insert", insert//.replace("#{createDt}", "now()").replace("#{updateDt}", "now()")
         );
	     sqlMap.put("update", update//.replace("#{createDt}", "now()").replace("#{updateDt}", "now()")
                 .replace("#{versions}","VERSIONS+1") + " \t\t <!--and VERSIONS = #{versions}-->");
	     sqlMap.put("delete", delete);
	     sqlMap.put("updateTo", updateTo);
	     sqlMap.put("selectById", selectById);
         sqlMap.put("selectByUUId", selectByUUId);
	     return sqlMap;
   }

    /**
     * delete
     * @param tableName
     * @param columnList
     * @return
     * @throws SQLException
     */
    public String getDeleteSql( String tableName,List<ColumnData> columnList)throws SQLException{
   	 StringBuffer sb=new StringBuffer();
   	 sb.append("update ");
	     sb.append("\t  ").append(tableName).append(" set delete_flag='Y' ").append(" where ");
	     sb.append(columnList.get(0).getColumnName()).append(" = #{").append(columnList.get(0).getPropertyName()).append("}");
         //sb.append(" or S_UUID = #{uuid}");
   	return sb.toString();
   }

    /**
     * 根据id查询
     * @param tableName
     * @param columnList
     * @return
     * @throws SQLException
     */
    public String getSelectByUUIdSql( String tableName,List<ColumnData> columnList)throws SQLException{
    	 StringBuffer sb=new StringBuffer();
    	 sb.append("select <include refid=\"base_column_list\" /> \n");
 	     sb.append("\t from ").append(tableName).append(" where ");
         sb.append(uuidColName + " = #{uuId}");
    	return sb.toString();
    }

    /**
     * 根据id查询
     * @param tableName
     * @param columnList
     * @return
     * @throws SQLException
     */
    public String getSelectByIdSql( String tableName,List<ColumnData> columnList)throws SQLException{
        StringBuffer sb=new StringBuffer();
        sb.append("select <include refid=\"base_column_list\" /> \n");
        sb.append("\t from ").append(tableName).append(" where ");
        sb.append(columnList.get(0).getColumnName()).append(" = #{").append(columnList.get(0).getPropertyName()).append("}");
        return sb.toString();
    }

    /**
     * 获取所有的字段，并按","分割
     * @param columns
     * @return
     * @throws SQLException
     */
    public String getColumnFields(String columns)throws SQLException{
    	String fields = columns;
    	if(fields != null && !"".equals(fields)){
    		fields = fields.replaceAll("[|]", ",");
    	}
    	return fields;
    }

    /**
     *
     * @param columns
     * @return
     * @throws SQLException
     */
    public String[] getColumnList(String columns)throws SQLException{
    	 String[] columnList=columns.split("[|]");
	     return columnList;
    }

    /**
     * 修改记录
     * @param tableName
     * @param columnList
     * @return
     * @throws SQLException
     */
    public String getUpdateSql(String tableName,List<ColumnData> columnList)throws SQLException{
    	 StringBuffer sb=new StringBuffer();
        ColumnData cd = columnList.get(0); //获取第一条记录，主键
	     for(int i=1;i<columnList.size();i++){
             ColumnData column=columnList.get(i);
             //不允许更新create信息
             if(columnList.get(i).getColumnName().contains("CREATE_")) {
                 continue;
             }
             //不允许更新UUID
             if(columnList.get(i).getColumnName().contains("_UU")) {
                 continue;
             }

//	    	  if("D_CREATE_DT".equals(column.toUpperCase()))
//	    		  continue;

//	    	  if("D_UPDATE_DT".equals(column.toUpperCase())
//               ||"D_DELETE_DT".equals(column.toUpperCase()))
//	    		  sb.append(column+"=now()");
	    	  else {
                 sb.append("\t");
             }
	    		  sb.append(column.getColumnName()+"=#{"+column.getPropertyName()+"}");
	    	  //最后一个字段不需要","
	    	  if((i+1) < columnList.size()){
	    		  sb.append(",");
                  sb.append("\n");
	    	  }
	     };
        sb.append("\n\t<trim prefix=\"WHERE\" prefixOverrides=\"AND|OR\" >\n");
        sb.append("\t\t<if test=\"" + cd.getPropertyName() + " != null \">\n");
        sb.append("\t\t\t\t and " + cd.getColumnName() + " = #{"+cd.getPropertyName()+"}\n");
        sb.append("\t\t </if>\n");
        sb.append("\t\t<if test=\"" + "uuId" + " != null and " + "uuId" +" != ''\">\n");
        sb.append("\t\t\t\t and " + uuidColName + " = #{uuId}\n");
        sb.append("\t\t </if>\n");
        sb.append("\t</trim>\n");
//    	 String update = "update "+tableName+" set "+sb.toString()+" where ("+columnList.get(0).getColumnName()
//                 +"=#{"+columnList.get(0).getPropertyName()+"}"
//                 + " or S_UUID = #{uuId})";
	     return "update "+tableName+"  \n"+"\t set \n"+sb.toString();
    }

    public String getUpdateToSql(String tableName,List<ColumnData> columnList)throws SQLException{
   	 StringBuffer sb=new StringBuffer();
   	    ColumnData cd = columnList.get(0); //获取第一条记录，主键
   	 	sb.append("\t<trim prefix=\"SET\" suffixOverrides=\",\" >\n");
   	 	 for(int i=1;i<columnList.size();i++){
             //不允许更新create信息
             if(columnList.get(i).getColumnName().contains("create_")) {
                 continue;
             }
             //不允许更新UUID
             if(columnList.get(i).getColumnName().contains("_UU")) {
                 continue;
             }
             //versions必定更新
             if(columnList.get(i).getColumnName().contains("VERSIONS")) {
                 continue;
             }
   	 		 ColumnData data = columnList.get(i);
   	 		 String columnName=data.getColumnName();
   	 		 sb.append("\t<if test=\"").append(getCutHeadStr(columnName)).append(" != null ");
   	 		 //String 还要判断是否为空
   	 		 if("String" == data.getDataType()){
   	 			sb.append(" and ").append(getCutHeadStr(columnName)).append(" != ''");
   	 		 }
   	 		 sb.append(" \">\n\t\t");
   	 		 sb.append(columnName+"=#{"+getCutHeadStr(columnName)+"},\n");
   	 		 sb.append("\t</if>\n");
	     }
         sb.append("\tVERSIONS=VERSIONS+1,\n");
	     sb.append("\t</trim>\n");
         sb.append("\t<trim prefix=\"WHERE\" prefixOverrides=\"AND|OR\" >\n");
         sb.append("\t\t<if test=\"" + cd.getPropertyName() + " != null \">\n");
         sb.append("\t\t\t\t and " + cd.getColumnName() + " = #{"+cd.getPropertyName()+"}\n");
         sb.append("\t\t </if>\n");
         sb.append("\t\t<if test=\"" + "uuId" + " != null and " + "uuId" +" != ''\">\n");
         sb.append("\t\t\t\t and " + uuidColName + " = #{uuId}\n");
         sb.append("\t\t </if>\n");
        sb.append("\t\t<if test=\"" + "versions" + " != null and " + "versions" +" != ''\">\n");
        sb.append("\t\t\t\t and VERSIONS = #{versions}\n");
        sb.append("\t\t </if>\n");
         sb.append("\t</trim>\n");
//	     String update = "update "+tableName+"  \n"+sb.toString()+" where ("+cd.getColumnName()+"=#{"+cd.getPropertyName()+"}"
//                 + " or S_UUID = #{uuId})";
	     return "update "+tableName+"  \n"+sb.toString();
   }



    /**
     * 获取所有列名字
     * @param columnList
     * @return
     * @throws SQLException
     */
    public String getColumnSplit(List<ColumnData> columnList) throws SQLException{
 	     StringBuffer commonColumns=new StringBuffer();
 	     for(ColumnData data : columnList){
 	    	 commonColumns.append(data.getColumnName()+"|");
 	     }
 	     return commonColumns.delete(commonColumns.length()-1, commonColumns.length()).toString();
    }

    public String getColumnSplit(List<ColumnData> columnList,String tableNickName) throws SQLException{
        StringBuffer commonColumns=new StringBuffer();
        for(ColumnData data : columnList){
            commonColumns.append(tableNickName+"."+data.getColumnName()+"|");
        }
        return commonColumns.delete(commonColumns.length()-1, commonColumns.length()).toString();
    }

    public String getColumnSplitNoAI(List<ColumnData> columnList) throws SQLException{
        StringBuffer commonColumns=new StringBuffer();
        for(ColumnData data : columnList){
            if ("auto_increment".equals(data.getExtra())) {
                continue;
            }
            commonColumns.append(data.getColumnName()+"|");
        }
        return commonColumns.delete(commonColumns.length()-1, commonColumns.length()).toString();
    }

    /**
     * 获取所有属性名
     * @param columnList
     * @return
     * @throws SQLException
     */
    public String getPropSplit(List<ColumnData> columnList) throws SQLException{
        StringBuffer commonColumns=new StringBuffer();
        for(ColumnData data : columnList){
            commonColumns.append(data.getPropertyName()+"|");
 	     }
 	     return commonColumns.delete(commonColumns.length()-1, commonColumns.length()).toString();
    }

    public String getPropSplitNoAI(List<ColumnData> columnList) throws SQLException{
        StringBuffer commonColumns=new StringBuffer();
        for(ColumnData data : columnList){
            if ("auto_increment".equals(data.getExtra())) {
                continue;
            }
            commonColumns.append(data.getPropertyName()+"|");
        }
        return commonColumns.delete(commonColumns.length()-1, commonColumns.length()).toString();
    }
    
}
