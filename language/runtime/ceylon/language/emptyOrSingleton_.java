package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;

@Ceylon(major = 3)
@Method
public class emptyOrSingleton_ {

    private emptyOrSingleton_(){}

    @TypeParameters(@TypeParameter(value="Element", satisfies="ceylon.language::Object"))
    @TypeInfo("ceylon.language::Sequential<Element>")
    public static <Element> Sequential<Element> emptyOrSingleton(
            @Name("element")
            @TypeInfo("ceylon.language::Null|Element")
            Element element) {
        return element==null ? 
                (Sequential)empty_.getEmpty$() : 
                new Singleton<Element>(element);
    }
}
