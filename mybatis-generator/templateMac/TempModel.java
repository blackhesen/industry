package ${corePkgProj}.${corePkgBusi}.${pkgSub};

import com.jcgroup.industry.common.core.base.BaseTo;
import java.io.Serializable;
import java.util.Date;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;

/**
 * ${className}To
 * @author ${author}
 * @since ${createDate}
 */
public class ${className}To extends BaseTo implements Serializable {

    public interface Add {}
    public interface Modify {}
    public interface Delete{}
    public interface QueryList {}
    public interface Query {}

	${feilds}
	
}
