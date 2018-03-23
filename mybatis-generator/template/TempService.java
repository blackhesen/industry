package com.jcgroup.${module}.${pkg}.service.${pkgSub};

import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.jcgroup.${module}.${pkg}.dao.${pkgSub}.${className}Mapper;
import com.jcgroup.${module}.${pkg}.model.${pkgSub}.${className}To;


/**
 * ${className}Service
 * @author ${author}
 * @since ${createDate}
 */
public class ${className}Service {
	private final static Logger log= LoggerFactory.getLogger(${className}Service.class);

	@Autowired
    private ${className}Mapper<${className}To> ${lowerName}mapper;

    public int add(${className}To to) throws Exception{
        return ${lowerName}mapper.add(to);
    }

    public int update(${className}To to) throws Exception{
        return ${lowerName}mapper.update(to);
    }

    public int updateByTo(${className}To to) throws Exception{
        return ${lowerName}mapper.updateByTo(to);
    }

    public int delete(Object id) throws Exception{
        return ${lowerName}mapper.delete(id);
    }

    public int queryCount(${className}To model) throws Exception{
        return ${lowerName}mapper.queryCount(model);
    }

    public int queryCountMap(Map params) throws Exception{
        return ${lowerName}mapper.queryCountMap(params);
    }

    public List<${className}To> queryListByPager(${className}To model) throws Exception{
        return ${lowerName}mapper.queryListByPager(model);
     }

    public List<${className}To> queryListByPagerMap(Map params) throws Exception{
        return ${lowerName}mapper.queryListByPagerMap(params);
    }

    public ${className}To queryById(Object id) throws Exception{
        return ${lowerName}mapper.queryById(id);
    }

    public ${className}To queryByUUId(Object uuid) throws Exception{
        return ${lowerName}mapper.queryByUUId(uuid);
    }

    public List<${className}To> queryListAll(${className}To model) throws Exception{
        return ${lowerName}mapper.queryListAll(model);
    }

}
