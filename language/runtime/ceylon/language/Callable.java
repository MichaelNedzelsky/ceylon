package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;

@Ceylon(major = 3)
@TypeParameters({
	@TypeParameter(value="Return", variance=Variance.OUT),
	@TypeParameter(value="CallableArgument", satisfies="Sequential<Anything>", variance=Variance.IN)
})
public interface Callable<Return> {
    
    public Return $call();
    
    public Return $call(java.lang.Object arg0);
    
    public Return $call(java.lang.Object arg0, java.lang.Object arg1);
    
    public Return $call(java.lang.Object arg0, java.lang.Object arg1, java.lang.Object arg2);
    
    public Return $call(java.lang.Object... args);

}
