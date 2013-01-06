package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;

@Ceylon(major = 3)
@Method
public final class print_ {
	static final process_ p = process_.getProcess$();
    public static void print(@Name("line") @TypeInfo("ceylon.language::Anything") java.lang.Object line){
    	p.writeLine(line==null ? "\u00abnull\u00bb" : line.toString());
    }
    private print_(){}
}
