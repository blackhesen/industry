package com.jcgroup.${module}.controller.${pkg}.${pkgSubAction};

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.opark.${module}.entity.${pkg}.${pkgSub}.${className}To;
import com.opark.${module}.service.${pkg}.${pkgSub}.${className}Service;
import com.opark.${module}.controller.BaseController;
import java.util.*;




/**
 * ${className}Controller
 * @author ${author}
 * @since ${createDate}
 */

@Controller
#if($!appFlag == '0')
@RequestMapping(value = "/api/${apiLv}/${lowerName}")
#end
#if($!appFlag == '1')
@RequestMapping(value = "/${lowerName}")
#end
public class ${className}Controller extends BaseController {
	
	private final static Logger log= Logger.getLogger(${className}Controller.class);

	@Autowired
	private ${className}Service ${lowerName}Service;

    private ModelAndView mav;

	/**
	 * 列表数据(分页)
	 * @param model
	 * @param pageTo
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/dataList")
	public ModelAndView  dataList(${className}To model,PageTo pageTo) throws Exception{
        mav = new ModelAndView();
        if (model == null) {
            model = new ${className}To();
        }
        if (pageTo == null) {
            pageTo = new PageTo();
        }
        Integer rowCount = ${lowerName}Service.queryCount(model);
        pageTo.setTotalResult(rowCount);
        model.setPageTo(pageTo);
        List<${className}To> dataList = ${lowerName}Service.queryListByPager(model);
        mav.addObject("${lowerName}List", dataList);
        mav.addObject("model", model);
        mav.setViewName("${lowerName}/dataList");
        return mav;
	}
	
	
	/**
	 * json数据(分页)
	 * @param model
	 * @param pageTo
	 * @return
	 * @throws Exception 
	 */
    @ResponseBody
	@RequestMapping("/dataJson")
	public HuhooResponse dataJson(${className}To model,PageTo pageTo) throws Exception{
        String startTime = DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:sss");
		HuhooResponse response = new HuhooResponse();
        Map<String, Object> result = new HashMap<>();
        result.put("startTime", startTime);
        if (model == null) {
            model = new ${className}To();
        }
        if (pageTo == null) {
            pageTo = new PageTo();
        }
        List<${className}To> dataJson = null;
        try {
            Integer rowCount = ${lowerName}Service.queryCount(model);
            pageTo.setTotalResult(rowCount);
            model.setPageTo(pageTo);
            dataJson = ${lowerName}Service.queryListByPager(model);
            result.put("${lowerName}Json", dataJson);
            result.put("model", model);
            response.setResult("1");
            response.setMessage("请求成功");
        } catch (Exception e) {
            response.setResult("0");
            response.setMessage(e.getMessage());
        } finally {
            String endTime = DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:sss");
            result.put("endTime", endTime);
            response.setExtendObject(result);
        }
        return response;
	}

    /**
     * 列表数据(全)
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/dataListAll")
    public ModelAndView  dataListAll(${className}To model) throws Exception{
        mav = new ModelAndView();
        if (model == null) {
            model = new ${className}To();
        }
        List<${className}To> dataList = ${lowerName}Service.queryListAll(model);
        mav.addObject("${lowerName}ListAll", dataList);
        mav.setViewName("${lowerName}/dataListAll");
        return mav;
    }


    /**
     * json数据(全)
     * @param model
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/dataJsonAll")
    public HuhooResponse  dataJsonAll(${className}To model) throws Exception{
        String startTime = DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:sss");
        HuhooResponse response = new HuhooResponse();
        Map<String, Object> result = new HashMap<>();
        result.put("startTime", startTime);
        List<${className}To> dataJson = null;
        if (model == null) {
            model = new ${className}To();
        }
        try {
            dataJson = ${lowerName}Service.queryListAll(model);
            result.put("${lowerName}JsonAll", dataJson);
            response.setResult("1");
            response.setMessage("请求成功");
        } catch (Exception e) {
            response.setResult("0");
            response.setMessage(e.getMessage());
        } finally {
            String endTime = DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:sss");
            result.put("endTime", endTime);
            response.setExtendObject(result);
        }
        return response;
    }
	
	/**
	 * 添加或修改数据
	 * @param bean
	 * @return
	 * @throws Exception 
	 */
    @ResponseBody
	@RequestMapping("/save")
	public HuhooResponse save(${className}To bean) throws Exception{
        String startTime = DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:sss");
        HuhooResponse response = new HuhooResponse();
        Map<String, Object> result = new HashMap<>();
        result.put("startTime", startTime);
        String actName = "新增";
        int count = 0;
        try {
            if(bean.getId() == null && StringUtil.isEmpty(bean.getUuId()) ){
                count = ${lowerName}Service.add(bean);
            }else{
                response.setResult("0");
                response.setMessage(actName+"无效");
            }
            if (count > 0) {
                result.put("${lowerName}To", bean);
                response.setResult("1");
                response.setMessage(actName+"成功");
            } else {
                response.setResult("0");
                response.setMessage(actName+"失败");
            }
        } catch (Exception e) {
            response.setResult("0");
            response.setMessage(e.getMessage());
        } finally {
            String endTime = DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:sss");
            result.put("endTime", endTime);
            response.setExtendObject(result);
        }
        return response;
	}

/**
 * 根据参数修改数据
 * @param bean
 * @return
 * @throws Exception
 */
@ResponseBody
@RequestMapping("/updateByTo")
    public HuhooResponse updateByTo(${className}To bean) throws Exception{
        String startTime = DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:sss");
        HuhooResponse response = new HuhooResponse();
        Map<String, Object> result = new HashMap<>();
        result.put("startTime", startTime);
        String actName = "修改";
        int count;
        try {
            if (bean != null && StringUtil.isNotEmpty(bean.getUuId()) ) {
                count = ${lowerName}Service.updateByTo(bean);
                if (count > 0){
                    result.put("${lowerName}To",bean);
                    response.setResult("1");
                    response.setMessage(actName+"成功");
                } else{
                    response.setResult("0");
                    response.setMessage(actName+"无效");
                }
            } else{
                response.setResult("0");
                response.setMessage(actName+"缺失对象");
            }
        } catch (Exception e) {
            response.setResult("0");
            response.setMessage(e.getMessage());
        } finally {
            String endTime = DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:sss");
            result.put("endTime", endTime);
            response.setExtendObject(result);
        }
        return response;
    }

    /**
     * 根据ID获取数据
     * @param id
     * @return
     * @throws Exception
     */
    @ResponseBody
	@RequestMapping("/queryById")
	public HuhooResponse queryById(Integer id) throws Exception{
		String startTime = DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:sss");
        HuhooResponse response = new HuhooResponse();
        Map<String, Object> result = new HashMap<>();
        result.put("startTime", startTime);
        ${className}To bean = null;
        try {
            bean = ${lowerName}Service.queryById(id);
            result.put("${lowerName}To", bean);
            response.setResult("1");
            response.setMessage("获取成功");
        } catch (Exception e) {
            response.setResult("0");
            response.setMessage(e.getMessage());
        } finally {
            String endTime = DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:sss");
            result.put("endTime", endTime);
            response.setExtendObject(result);
        }
        return response;
	}

    /**
     * 根据UUID获取数据
     * @param uuid
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/queryByUUId")
    public HuhooResponse queryByUUId(String uuid) throws Exception{
        String startTime = DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:sss");
        HuhooResponse response = new HuhooResponse();
        Map<String, Object> result = new HashMap<>();
        result.put("startTime", startTime);
        ${className}To bean = null;
        try {
        bean = ${lowerName}Service.queryByUUId(uuid);
        result.put("${lowerName}To", bean);
        response.setResult("1");
        response.setMessage("获取成功");
        } catch (Exception e) {
        response.setResult("0");
        response.setMessage(e.getMessage());
        } finally {
        String endTime = DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:sss");
        result.put("endTime", endTime);
        response.setExtendObject(result);
        }
        return response;
        }

    /**
     * 根据ID删除数据
     * @param id
     * @param httpRequest
     * @return
     * @throws Exception
     */
//    @ResponseBody
//	@RequestMapping("/delete")
//	public HuhooResponse delete(Integer id,HttpServletRequest httpRequest,HttpServletResponse httpResponse) throws Exception{
//		String startTime = DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:sss");
//        HuhooResponse response = new HuhooResponse();
//        Map<String, Object> result = new HashMap<>();
//        result.put("startTime", startTime);
//        try {
//            int count = ${lowerName}Service.delete(id);
//            if (count > 0) {
//                response.setResult("1");
//                response.setMessage("删除成功");
//            } else {
//                response.setResult("1");
//                response.setMessage("删除失败");
//            }
//        } catch (Exception e) {
//            response.setResult("0");
//            response.setMessage(e.getMessage());
//        } finally {
//            String endTime = DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:sss");
//            result.put("endTime", endTime);
//            response.setExtendObject(result);
//        }
//        return response;
//	}

}
