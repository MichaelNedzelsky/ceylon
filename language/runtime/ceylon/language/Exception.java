package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.Defaulted;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;

@Ceylon(major = 3)
@Class(extendsType="ceylon.language::IdentifiableObject")
public class Exception extends RuntimeException {

	private static final long serialVersionUID = -1790691559137471641L;

	private String description;
    
    public Exception(
            @TypeInfo("ceylon.language::String|ceylon.language::Null")
            @Name("description")
            @Defaulted
            String description,
            @TypeInfo("ceylon.language::Exception|ceylon.language::Null")
            @Name("cause")
            @Defaulted
            java.lang.Throwable cause) {
        super(description==null ? null : description.toString(), cause);
        this.description = description;
    }
    
    @Ignore
    public Exception(String description) {
        this(description, $init$cause(description));
    }
    
    @Ignore
    public Exception() {
        this($init$description());
    }
        
    @TypeInfo("ceylon.language::Exception|ceylon.language::Null")
    public java.lang.Throwable getCause() {
        return super.getCause();
    }
    
    @TypeInfo("ceylon.language::String")
    public java.lang.String getMessage() {
        if (description != null
                && description != null) {
            return description.toString();
        } 
        else if (getCause() != null 
                && getCause().getMessage() != null) {
            return getCause().getMessage();
        }
        return "";
    }

    @TypeInfo("ceylon.language::String")
    public java.lang.String toString() {
        return className_.className(this) + " \"" + getMessage() +"\""; 
    }
    
    @Override
    public void printStackTrace() {
    	super.printStackTrace();
    }

    @Ignore
    public static ceylon.language.String $init$description(){
        return null;
    }
    @Ignore
    public static java.lang.Throwable $init$cause(ceylon.language.String description){
        return null;
    }
}
