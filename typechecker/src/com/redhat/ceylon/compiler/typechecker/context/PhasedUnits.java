package com.redhat.ceylon.compiler.typechecker.context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonToken;
import org.antlr.runtime.CommonTokenStream;

import com.redhat.ceylon.compiler.typechecker.analyzer.ModuleManager;
import com.redhat.ceylon.compiler.typechecker.io.VirtualFile;
import com.redhat.ceylon.compiler.typechecker.parser.CeylonLexer;
import com.redhat.ceylon.compiler.typechecker.parser.CeylonParser;
import com.redhat.ceylon.compiler.typechecker.parser.LexError;
import com.redhat.ceylon.compiler.typechecker.parser.ParseError;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;

/**
 * Contains phased units
 *
 * @author Emmanuel Bernard <emmanuel@hibernate.org>
 */
public class PhasedUnits {
    private Map<VirtualFile, PhasedUnit> phasedUnitPerFile = new LinkedHashMap<VirtualFile, PhasedUnit>();
    private Map<String, PhasedUnit> phasedUnitPerRelativePath = new HashMap<String, PhasedUnit>();
    private final Context context;
    private final ModuleManager moduleBuilder;

    public PhasedUnits(Context context) {
        this.context = context;
        this.moduleBuilder = new ModuleManager(context);
        this.moduleBuilder.initCoreModules();
    }

    public PhasedUnits(Context context, ModuleManager moduleBuilder) {
        this.context = context;
        this.moduleBuilder = moduleBuilder;
    }
    
    public void addPhasedUnit(VirtualFile unitFile, PhasedUnit phasedUnit) {
        this.phasedUnitPerFile.put(unitFile, phasedUnit);
        this.phasedUnitPerRelativePath.put(phasedUnit.getPathRelativeToSrcDir(), phasedUnit);
    }

    public void removePhasedUnitForRelativePath(String relativePath) {
	PhasedUnit phasedUnit = this.phasedUnitPerRelativePath.get(relativePath);
        this.phasedUnitPerRelativePath.remove(relativePath);
        this.phasedUnitPerFile.remove(phasedUnit.getUnitFile());
    }

    public ModuleManager getModuleBuilder() {
        return moduleBuilder;
    }

    public List<PhasedUnit> getPhasedUnits() {
        List<PhasedUnit> list = new ArrayList<PhasedUnit>();
        list.addAll(phasedUnitPerFile.values());
        return list;
    }

    public PhasedUnit getPhasedUnit(VirtualFile file) {
        return phasedUnitPerFile.get(file);
    }

    public PhasedUnit getPhasedUnitFromRelativePath(String relativePath) {
        if (relativePath.startsWith("/")) {
            relativePath = relativePath.substring(1);
        }
        return phasedUnitPerRelativePath.get(relativePath);
    }

    public void parseUnits(List<VirtualFile> srcDirectories) {
        for (VirtualFile file : srcDirectories) {
            parseUnit(file, file);
        }
    }

    public void parseUnit(VirtualFile srcDir) {
        parseUnit(srcDir, srcDir);
    }

    public void parseUnit(VirtualFile file, VirtualFile srcDir) {
        try {
            if (file.isFolder()) {
                //root directory is the src dir => start from here
                for (VirtualFile subfile : file.getChildren()) {
                    parseFileOrDirectory(subfile, srcDir);
                }
            }
            else {
                //simple file compilation
                //TODO is that really valid?
                parseFileOrDirectory(file, srcDir);
            }
        }
        catch (RuntimeException e) {
            //let it go
            throw e;
        }
        catch (Exception e) {
            throw new RuntimeException("Error while parsing the source directory: " + file.toString(), e);
        }
    }

    private void parseFile(VirtualFile file, VirtualFile srcDir) throws Exception {
        if (file.getName().endsWith(".ceylon")) {

            //System.out.println("Parsing " + file.getName());
            CeylonLexer lexer = new CeylonLexer(new ANTLRInputStream(file.getInputStream()));
            CommonTokenStream tokenStream = new CommonTokenStream(lexer);
            CeylonParser parser = new CeylonParser(tokenStream);
            Tree.CompilationUnit cu = parser.compilationUnit();
            List<CommonToken> tokens = new ArrayList<CommonToken>(tokenStream.getTokens().size()); 
            tokens.addAll(tokenStream.getTokens());
            PhasedUnit phasedUnit = new PhasedUnit(file, srcDir, cu, 
                    moduleBuilder.getCurrentPackage(), moduleBuilder, 
                    context, tokens);
            addPhasedUnit(file, phasedUnit);

            List<LexError> lexerErrors = lexer.getErrors();
            for (LexError le : lexerErrors) {
                //System.out.println("Lexer error in " + file.getName() + ": " + le.getMessage());
                cu.addLexError(le);
            }
            lexerErrors.clear();

            List<ParseError> parserErrors = parser.getErrors();
            for (ParseError pe : parserErrors) {
                //System.out.println("Parser error in " + file.getName() + ": " + pe.getMessage());
                cu.addParseError(pe);
            }
            parserErrors.clear();

        }
    }

    private void parseFileOrDirectory(VirtualFile file, VirtualFile srcDir) throws Exception {
        if (file.isFolder()) {
            processDirectory(file, srcDir);
        }
        else {
            parseFile(file, srcDir);
        }
    }

    private void processDirectory(VirtualFile dir, VirtualFile srcDir) throws Exception {
        moduleBuilder.push(dir.getName());
        final List<VirtualFile> files = dir.getChildren();
        for (VirtualFile file : files) {
            if (ModuleManager.MODULE_FILE.equals(file.getName())) {
                moduleBuilder.visitModuleFile();
            }
        }
        for (VirtualFile file : files) {
            parseFileOrDirectory(file, srcDir);
        }
        moduleBuilder.pop();
    }
}
