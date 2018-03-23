package ${logicPkgProj}.impl.${corePkgBusi}.${pkgSub};

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.stereotype.Component;
import com.jcgroup.industry.common.core.service.BaseService;
import ${logicPkgProj}.dao.${corePkgBusi}.${pkgSub}.${className}Mapper;
import ${corePkgProj}.${corePkgBusi}.${pkgSub}.${className}To;
import ${coreConditonPkgProj}.${corePkgBusi}.${pkgSub}.${className}Condition;
import ${coreApiPkgProj}.${corePkgBusi}.${pkgSub}.${className}Service;
import ${coreDtoPkgProj}.${corePkgBusi}.${pkgSub}.${className}DTO;
import com.jcgroup.industry.common.core.res.JsonObjectResponse;
import com.jcgroup.industry.biz.exception.ErrorEnum;
import com.jcgroup.industry.common.core.page.Page;
import com.jcgroup.industry.common.core.dto.PageDTO;
import com.jcgroup.industry.common.core.util.StringUtil;

/**
 * ${className}ServiceImpl
 * @author ${author}
 * @since ${createDate}
 */
@Service(version = "1.0.0", provider = "dubboProvider")
@Transactional
public class ${className}ServiceImpl extends BaseService implements ${className}Service {
	private final static Logger log= LoggerFactory.getLogger(${className}ServiceImpl.class);

	@Autowired
    private ${className}Mapper<${className}To> ${lowerName}Mapper;

    public JsonObjectResponse add(${className}Condition condition) throws Exception{
        ${lowerName}Mapper.add(condition.get${className}());
        return new JsonObjectResponse(ErrorEnum.RES_CODE_SUCCESS.getErrorCode(), ErrorEnum.RES_CODE_SUCCESS.getErrorMessage());
    }

    public JsonObjectResponse update(${className}Condition condition) throws Exception{
        if (${lowerName}Mapper.update(condition.get${className}()) == 1) {
            return new JsonObjectResponse(ErrorEnum.RES_CODE_SUCCESS.getErrorCode(), ErrorEnum.RES_CODE_SUCCESS.getErrorMessage());
        } else {
            return new JsonObjectResponse(ErrorEnum.RES_CODE_FAIL.getErrorCode(), ErrorEnum.RES_CODE_FAIL.getErrorMessage());
        }
    }

    public JsonObjectResponse updateByTo(${className}Condition condition) throws Exception{
        if (${lowerName}Mapper.updateByTo(condition.get${className}()) == 1) {
            return new JsonObjectResponse(ErrorEnum.RES_CODE_SUCCESS.getErrorCode(), ErrorEnum.RES_CODE_SUCCESS.getErrorMessage());
        } else {
            return new JsonObjectResponse(ErrorEnum.RES_CODE_FAIL.getErrorCode(), ErrorEnum.RES_CODE_FAIL.getErrorMessage());
        }
    }

    public JsonObjectResponse delete(${className}Condition condition) throws Exception{
        if (${lowerName}Mapper.delete(condition.get${className}().getId()) == 1) {
            return new JsonObjectResponse(ErrorEnum.RES_CODE_SUCCESS.getErrorCode(), ErrorEnum.RES_CODE_SUCCESS.getErrorMessage());
        } else {
            return new JsonObjectResponse(ErrorEnum.RES_CODE_FAIL.getErrorCode(), ErrorEnum.RES_CODE_FAIL.getErrorMessage());
        }
    }

    public JsonObjectResponse queryDtoListByPager(${className}Condition condition) {
        Page<${className}DTO> page = new Page<${className}DTO>();
        if(StringUtil.isNotEmpty(condition.getCurrentPage())) {
            page.setCurrentPage(Integer.parseInt(condition.getCurrentPage()));
        }
        page.setPageSize(20);
        List<${className}DTO> result = ${lowerName}Mapper.queryDtoListByPager(condition, page);
        PageDTO pageDTO = new PageDTO();
        pageDTO.setPage(page);
        pageDTO.setResult(result);
        return new JsonObjectResponse(
            ErrorEnum.RES_CODE_SUCCESS.getErrorCode(),
            ErrorEnum.RES_CODE_SUCCESS.getErrorMessage(), result);
    }

    public JsonObjectResponse queryDtoListAll(${className}Condition condition) {
        List<${className}DTO> result = ${lowerName}Mapper.queryDtoListAll(condition);
        return new JsonObjectResponse(
            ErrorEnum.RES_CODE_SUCCESS.getErrorCode(),
            ErrorEnum.RES_CODE_SUCCESS.getErrorMessage(), result);
    }

    public JsonObjectResponse queryDtoById(${className}Condition condition) {
        ${className}DTO ${lowerName}DTO = ${lowerName}Mapper.queryDtoById(condition.get${className}().getId());
        if(${lowerName}DTO == null) {
            return new JsonObjectResponse(
            ErrorEnum.RES_CODE_NOT_EXIST.getErrorCode(),
            ErrorEnum.RES_CODE_NOT_EXIST.getErrorMessage());
        }
        return new JsonObjectResponse(
        ErrorEnum.RES_CODE_SUCCESS.getErrorCode(),
        ErrorEnum.RES_CODE_SUCCESS.getErrorMessage(), ${lowerName}DTO);
    }

}
