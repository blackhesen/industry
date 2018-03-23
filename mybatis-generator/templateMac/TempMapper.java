package ${logicPkgProj}.dao.${corePkgBusi}.${pkgSub};

import org.apache.ibatis.annotations.Param;
import com.jcgroup.industry.common.core.page.Page;
import com.jcgroup.industry.common.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import ${coreConditonPkgProj}.${corePkgBusi}.${pkgSub}.${className}Condition;
import ${coreDtoPkgProj}.${corePkgBusi}.${pkgSub}.${className}DTO;
import java.util.List;

/**
 * ${className}Mapper
 * @author ${author}
 * @since ${createDate}
 */
@Repository
public interface ${className}Mapper<T> extends BaseMapper<T> {

    public List<${className}DTO> queryDtoListByPager(
        @Param("param")${className}Condition ${lowerName}Condition, Page<${className}DTO> page);

    public List<${className}DTO> queryDtoListAll(
        @Param("param")${className}Condition ${lowerName}Condition);

    public ${className}DTO queryDtoById(Object id);

}
