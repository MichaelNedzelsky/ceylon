package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.Sequenced;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;

@Ceylon(major = 3)
@Method
public final class tagged_
{
    public static Null tagged(@Name("tags") @Sequenced
            @TypeInfo("ceylon.language::Sequential<ceylon.language::String>")
            final ceylon.language.Sequential<? extends ceylon.language.String> tags) {
        return null;
    }
    @Ignore
    public static Null tagged() {
        return null;
    }
    private tagged_(){}
}
