package ${coreApiPkgProj}.${corePkgBusi}.${pkgSub};

import ${coreConditonPkgProj}.${corePkgBusi}.${pkgSub}.${className}Condition;
import com.jcgroup.industry.common.core.res.JsonObjectResponse;

/**
 * ${className}Service
 * @author ${author}
 * @since ${createDate}
 */
public interface  ${className}Service {

    public JsonObjectResponse add(${className}Condition condition) throws Exception;

    public JsonObjectResponse update(${className}Condition condition) throws Exception;

    public JsonObjectResponse updateByTo(${className}Condition condition) throws Exception;

    public JsonObjectResponse delete(${className}Condition condition) throws Exception;

    public JsonObjectResponse queryDtoListByPager(${className}Condition ${lowerName}Condition);

    public JsonObjectResponse queryDtoListAll(${className}Condition ${lowerName}Condition);

    public JsonObjectResponse queryDtoById(${className}Condition ${lowerName}Condition);

}
