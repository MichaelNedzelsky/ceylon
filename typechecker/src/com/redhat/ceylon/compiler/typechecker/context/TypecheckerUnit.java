package com.redhat.ceylon.compiler.typechecker.context;

import com.redhat.ceylon.compiler.typechecker.analyzer.ModuleSourceMapper;
import com.redhat.ceylon.model.typechecker.model.Module;
import com.redhat.ceylon.model.typechecker.model.Package;
import com.redhat.ceylon.model.typechecker.model.Unit;

public class TypecheckerUnit extends Unit {

    private Package javaLangPackage;
	private ModuleSourceMapper moduleSourceMapper;

    public TypecheckerUnit(ModuleSourceMapper moduleSourceMapper) {
        this.moduleSourceMapper = moduleSourceMapper;
    }
    
    public TypecheckerUnit(
            String theFilename,
            String theRelativePath,
            String theFullPath,
            Package thePackage) {
        setFilename(theFilename);
        setRelativePath(theRelativePath);
        setFullPath(theFullPath);
        setPackage(thePackage);
    }

    @Override
    public void setJavaLangPackageIfNeeded(Iterable<Module> modules) {
    	if (javaLangPackage != null) return;
    	
        for (Module module: modules) {
            if ("java.base".equals(module.getNameAsString())) {
                javaLangPackage = module.getPackage("java.lang");
                break;
            }
        }
    }

    /** 
     * Override this because it's possible to see java.lang.Iterable 
     * (for example) without a dependency on java.base when importing 
     * a Java modules that uses it. 
     */
    @Override
    protected Package getJavaLangPackage() {
        return javaLangPackage != null ? javaLangPackage : 
            super.getJavaLangPackage();
    }
    
	@Override
	public boolean isJdkPackage(String name) {
	    return moduleSourceMapper!=null ? 
	            moduleSourceMapper.getJdkProvider()
                    .isJDKPackage(name) : 
                super.isJdkPackage(name);
	}
}
