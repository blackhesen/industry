package ${coreConditonPkgProj}.${corePkgBusi}.${pkgSub};

import javax.validation.Valid;
import org.hibernate.validator.constraints.NotEmpty;
import java.io.Serializable;
import ${corePkgProj}.${corePkgBusi}.${pkgSub}.${className}To;

/**
 * ${className}To
 * @author ${author}
 * @since ${createDate}
 */
public class ${className}Condition implements Serializable {

    @Valid()
	private ${className}To ${lowerName};

    public ${className}To get${className}() {
        return ${lowerName};
    }

    public void set${className}(${className}To ${lowerName}) {
        this.${lowerName} = ${lowerName};
    }

    private String currentPage;
    public String getCurrentPage() {
        return currentPage;
    }
    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }
}
