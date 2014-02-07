/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License version 2.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */

package com.redhat.ceylon.compiler.java.codegen;

import com.redhat.ceylon.compiler.typechecker.model.Interface;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.tree.NaturalVisitor;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;

public class CeylonVisitor extends Visitor implements NaturalVisitor {
    protected final CeylonTransformer gen;
    private final ToplevelAttributesDefinitionBuilder topattrBuilder;
    ListBuffer<JCTree> defs;
    ClassDefinitionBuilder classBuilder;
    boolean inInitializer = false;
    
    /** For compilation units */
    public CeylonVisitor(CeylonTransformer ceylonTransformer, ToplevelAttributesDefinitionBuilder topattrBuilder) {
        this.gen = ceylonTransformer;
        this.gen.visitor = this;
        this.defs = new ListBuffer<JCTree>();
        this.topattrBuilder = topattrBuilder;
        this.classBuilder = null;
    }
    
    public void handleException(Exception e, Node that) {
        that.addError(new CodeGenError(that, e.getMessage(), e)); 
    }
    
    /*
     * Compilation Unit
     */
    
    public void visit(Tree.TypeAliasDeclaration decl){
        if(hasErrors(decl))
            return;
        int annots = gen.checkCompilerAnnotations(decl, defs);
        
        if (Decl.withinClassOrInterface(decl)) {
            if (Decl.withinInterface(decl)) {
                classBuilder.getCompanionBuilder((Interface)decl.getDeclarationModel().getContainer()).defs(gen.classGen().transform(decl));
            } else {
                classBuilder.defs(gen.classGen().transform(decl));
            }
        } else {
            appendList(gen.classGen().transform(decl));
        }
        gen.resetCompilerAnnotations(annots);
    }
    
    public void visit(Tree.ImportList that) {
        //append(gen.transform(that));
    }
    
    public void visit(Tree.ClassOrInterface decl) {
        if(hasClassErrors(decl))
            return;
    	if (Decl.isNative(decl) && Decl.isToplevel(decl))
    		return;
        int annots = gen.checkCompilerAnnotations(decl, defs);
        
        if (Decl.withinClassOrInterface(decl)) {
            if (Decl.withinInterface(decl)) {
                classBuilder.getCompanionBuilder((Interface)decl.getDeclarationModel().getContainer()).defs(gen.classGen().transform(decl));
            } else {
                classBuilder.defs(gen.classGen().transform(decl));
            }
        } else {
            appendList(gen.classGen().transform(decl));
        }
        gen.resetCompilerAnnotations(annots);
    }

    private boolean hasClassErrors(Tree.ClassOrInterface decl) {
        ClassErrorVisitor errorVisitor = new ClassErrorVisitor(gen.getContext());
        return errorVisitor.hasErrors(decl);
    }

    boolean hasClassInitialiserErrors(Tree.ClassOrInterface decl) {
        ClassInitialiserErrorVisitor errorVisitor = new ClassInitialiserErrorVisitor(gen.getContext());
        return errorVisitor.hasErrors(decl);
    }

    public void visit(Tree.ObjectDefinition decl) {
        if(hasErrors(decl))
            return;
    	if (Decl.isNative(decl) && Decl.isToplevel(decl))
    		return;
        int annots = gen.checkCompilerAnnotations(decl, defs);
        if (Decl.withinClass(decl)) {
            classBuilder.defs(gen.classGen().transformObjectDefinition(decl, classBuilder));
        } else {
            appendList(gen.classGen().transformObjectDefinition(decl, null));
        }
        gen.resetCompilerAnnotations(annots);
    }
    
    public void visit(Tree.AnyAttribute decl){
        if(hasErrors(decl))
            return;
        int annots = gen.checkCompilerAnnotations(decl, defs);
        if (Decl.withinClass(decl) && !Decl.isLocalToInitializer(decl)) {
            // Class attributes
            gen.valueGen().transformClassAttribute(classBuilder, decl);
        } else if (Decl.withinInterface(decl)) {
            // Interface attributes
            gen.valueGen().transformInterfaceAttribute(classBuilder, decl);
            gen.valueGen().transformCompanionAttribute(classBuilder.getCompanionBuilder((Interface)decl.getDeclarationModel().getContainer()), decl);
        } else if (Decl.isToplevel(decl)) {
            if (!Decl.isNative(decl)) {
                topattrBuilder.add(decl);
            }
        } else {
            // Local
            /*if (decl instanceof Tree.AttributeDeclaration
                    && (Decl.isLocal(decl)) 
                    && ((Decl.isCaptured(decl) && Decl.isVariable((Tree.AttributeDeclaration)decl))
                            || Decl.isTransient((Tree.AttributeDeclaration)decl)
                            || Decl.hasSetter((Tree.AttributeDeclaration)decl))) {
                // Captured local attributes get turned into an inner getter/setter class
                appendList(gen.transform(decl));
            } else*/ {
                // All other local attributes
                appendList(gen.valueGen().transformLocalValue(decl));
            }
        }
        gen.resetCompilerAnnotations(annots);
    }
    
    public void visit(final Tree.AttributeSetterDefinition decl) {
        if(hasErrors(decl))
            return;
        int annots = gen.checkCompilerAnnotations(decl, defs);
        if (Decl.withinClass(decl) && !Decl.isLocalToInitializer(decl)) {
            gen.valueGen().transformClassSetter(classBuilder, decl);
        } else if (Decl.withinInterface(decl)) {
            gen.valueGen().transformInterfaceSetter(classBuilder, decl);
            gen.valueGen().transformCompanionSetter(classBuilder.getCompanionBuilder((Interface)decl.getDeclarationModel().getContainer()), decl);
        } else if (Decl.isToplevel(decl)) {
            if (!Decl.isNative(decl)) {
                topattrBuilder.add(decl);
            }
        } else {
            // Local
            appendList(gen.valueGen().transformLocalSetter(decl));
        }
        gen.resetCompilerAnnotations(annots);
    }

    public void visit(Tree.AnyMethod decl) {
        if(hasErrors(decl))
            return;
    	if (Decl.isNative(decl) && Decl.isToplevel(decl))
    		return;
        int annots = gen.checkCompilerAnnotations(decl, defs);
        if (Decl.withinClassOrInterface(decl)
                && (!Decl.isDeferred(decl) || Decl.isCaptured(decl))) {
            classBuilder.method(decl);
        } else {
            appendList(gen.classGen().transformWrappedMethod(decl));
        }
        gen.resetCompilerAnnotations(annots);
    }
    
    private boolean hasErrors(Node decl) {
        ErrorVisitor errorVisitor = new ErrorVisitor(gen.getContext());
        return errorVisitor.hasErrors(decl);
    }

    /*
     * Class or Interface
     */
    
    // Class Initializer parameter
    public void visit(Tree.Parameter param) {
        // Ignore
    }

    public void visit(Tree.Block b) {
        b.visitChildren(this);
    }

    public void visit(Tree.Annotation ann) {
        // Handled in AbstractTransformer.makeAtAnnotations
    }
    public void visit(Tree.AnonymousAnnotation ann) {
        // Handled in AbstractTransformer.makeAtAnnotations
    }

    // FIXME: also support Tree.SequencedTypeParameter
    public void visit(Tree.TypeParameterDeclaration param) {
        TypeDeclaration container = (TypeDeclaration)param.getDeclarationModel().getContainer();
        classBuilder.typeParameter(param);
        classBuilder.getCompanionBuilder(container).typeParameter(param);
    }

    public void visit(Tree.ExtendedType extendedType) {
        classBuilder.extending(extendedType.getType().getTypeModel());
        gen.expressionGen().transformSuperInvocation(extendedType, classBuilder);
    }

    public void visit(Tree.ClassSpecifier extendedType) {
        // ignore this bit entirely, that's for class aliases and we don't reflect this in the AST,
        // only in type model annotations and that info comes from the model
    }

    // FIXME: implement
    public void visit(Tree.TypeConstraint l) {
    }

    public void visit(Tree.CaseTypes t){
        // FIXME: ignore for now, probably we'll need to add an annotation for it in M2.
        // no need to warn here since the typechecker already warns for M1's unsupported status
        // we do need to avoid visiting its children since that leads to invalid code otherwise as
        // other node types are handled as if they were the body of the class
    }
    
    /*
     * Statements
     */

    public void visit(Tree.Return ret) {
        append(gen.statementGen().transform(ret));
    }

    public void visit(Tree.IfStatement stat) {
        appendList(gen.statementGen().transform(stat));
    }

    public void visit(Tree.WhileStatement stat) {
        appendList(gen.statementGen().transform(stat));
    }

//    public void visit(Tree.DoWhileStatement stat) {
//        append(gen.statementGen().transform(stat));
//    }

    public void visit(Tree.ForStatement stat) {
        appendList(gen.statementGen().transform(stat));
    }

    public void visit(Tree.Break stat) {
        appendList(gen.statementGen().transform(stat));
    }

    public void visit(Tree.Continue stat) {
        append(gen.statementGen().transform(stat));
    }

    public void visit(Tree.SpecifierStatement op) {
        appendList(gen.classGen().transformRefinementSpecifierStatement(op, classBuilder));
    }

    public void visit(Tree.OperatorExpression op) {
        // FIXME: Do we really have operators not handled elsewhere than here?
        append(gen.at(op).Exec(gen.expressionGen().transformExpression(op)));
    }

    public void visit(Tree.Expression tree) {
        // FIXME: Do we really have expressions not handled elsewhere than here?
        append(gen.at(tree).Exec(gen.expressionGen().transformExpression(tree)));
    }

    // FIXME: I think those should just go in transformExpression no?
    public void visit(Tree.PostfixOperatorExpression expr) {
        append(gen.expressionGen().transform(expr));
    }

    public void visit(Tree.PrefixOperatorExpression expr) {
        append(gen.expressionGen().transform(expr));
    }

    public void visit(Tree.ExpressionStatement tree) {
        // this is used for class initializer statements so we want to avoid having errors
        // in them
        if(hasErrors(tree))
            return;
        append(gen.expressionGen().transform(tree));
    }
    
    /*
     * Expression - Invocations
     */
    
    public void visit(Tree.InvocationExpression expr) {
        append(gen.expressionGen().transform(expr));
    }
    
    public void visit(Tree.QualifiedMemberExpression access) {
        append(gen.expressionGen().transform(access));
    }

    public void visit(Tree.BaseMemberExpression access) {
        append(gen.expressionGen().transform(access));
    }
    
    public void visit(Tree.QualifiedTypeExpression access) {
        append(gen.expressionGen().transform(access));
    }
    
    public void visit(Tree.BaseTypeExpression access) {
        append(gen.expressionGen().transform(access));
    }

    /*
     * Expression - Terms
     */
    
    public void visit(Tree.IndexExpression access) {
        append(gen.expressionGen().transform(access));
    }

    public void visit(Tree.This expr) {
        append(gen.expressionGen().transform(expr));
    }

    public void visit(Tree.Super expr) {
        append(gen.expressionGen().transform(expr));
    }

    public void visit(Tree.Outer expr) {
        append(gen.expressionGen().transform(expr));
    }

    public void visit(Tree.Package that) {
        // this is only used as qualifier, and we can consider it a empty qualifier, so we ignore it
    }
    
    public void visit(Tree.IdenticalOp op) {
        append(gen.expressionGen().transform(op));
    }

    // FIXME: port dot operator?
    public void visit(Tree.NotEqualOp op) {
        append(gen.expressionGen().transform(op));
    }

    public void visit(Tree.NotOp op) {
        append(gen.expressionGen().transform(op));
    }

    public void visit(Tree.OfOp op) {
        append(gen.expressionGen().transform(op));
    }

    public void visit(Tree.AssignOp op) {
        append(gen.expressionGen().transform(op));
    }

    public void visit(Tree.IsOp op) {
        append(gen.expressionGen().transform(op));
    }

    public void visit(Tree.InOp op) {
        append(gen.expressionGen().transform(op));
    }

    public void visit(Tree.DefaultOp op) {
        append(gen.expressionGen().transform(op));
    }

    public void visit(Tree.ThenOp op) {
        append(gen.expressionGen().transform(op));
    }

    public void visit(Tree.Nonempty op) {
        append(gen.expressionGen().transform(op));
    }

    public void visit(Tree.Exists op) {
        append(gen.expressionGen().transform(op));
    }

    public void visit(Tree.RangeOp op) {
        append(gen.expressionGen().transform(op));
    }
    
    public void visit(Tree.SegmentOp op) {
        append(gen.expressionGen().transform(op));
    }

    public void visit(Tree.EntryOp op) {
        append(gen.expressionGen().transform(op));
    }

    public void visit(Tree.LogicalOp op) {
        append(gen.expressionGen().transform(op));
    }

    public void visit(Tree.UnaryOperatorExpression op) {
        append(gen.expressionGen().transform(op));
    }

    public void visit(Tree.PositiveOp op) {
        append(gen.expressionGen().transform(op));
    }

    public void visit(Tree.NegativeOp op) {
        append(gen.expressionGen().transform(op));
    }

    public void visit(Tree.BinaryOperatorExpression op) {
        append(gen.expressionGen().transform(op));
    }
    
    public void visit(Tree.ScaleOp op) {
        append(gen.expressionGen().transform(op));
    }
    
    public void visit(Tree.BitwiseOp op) {
    	append(gen.expressionGen().transform(op));
    }
    
    public void visit(Tree.ComparisonOp op) {
        append(gen.expressionGen().transform(op));
    }
    
    public void visit(Tree.CompareOp op) {
        append(gen.expressionGen().transform(op));
    }

    public void visit(Tree.ArithmeticOp op) {
        append(gen.expressionGen().transform(op));
    }
    
    public void visit(Tree.PowerOp op) {
        append(gen.expressionGen().transform(op));
    }
    
    public void visit(Tree.SumOp op) {
        append(gen.expressionGen().transform(op));
    }
    
    public void visit(Tree.DifferenceOp op) {
        append(gen.expressionGen().transform(op));
    }
    
    public void visit(Tree.RemainderOp op) {
        append(gen.expressionGen().transform(op));
    }
    
    public void visit(Tree.WithinOp op) {
        append(gen.expressionGen().transform(op));
    }

    public void visit(Tree.ArithmeticAssignmentOp op){
        append(gen.expressionGen().transform(op));
    }

    public void visit(Tree.BitwiseAssignmentOp op){
        append(gen.expressionGen().transform(op));
    }

    public void visit(Tree.LogicalAssignmentOp op){
        append(gen.expressionGen().transform(op));
    }

    // NB spec 1.3.11 says "There are only two types of numeric
    // literals: literals for Integers and literals for Floats."
    public void visit(Tree.NaturalLiteral lit) {
        append(gen.expressionGen().transform(lit));
    }

    public void visit(Tree.FloatLiteral lit) {
        append(gen.expressionGen().transform(lit));
    }

    public void visit(Tree.CharLiteral lit) {
        append(gen.expressionGen().transform(lit));
    }

    public void visit(Tree.StringLiteral string) {
        append(gen.expressionGen().transform(string));
    }

    public void visit(Tree.QuotedLiteral string) {
        append(gen.expressionGen().transform(string));
    }

    public void visit(Tree.TypeLiteral that) {
        append(gen.expressionGen().transform(that));
    }

    public void visit(Tree.MemberLiteral that) {
        append(gen.expressionGen().transform(that));
    }

    public void visit(Tree.ModuleLiteral that) {
        append(gen.expressionGen().transform(that));
    }

    public void visit(Tree.PackageLiteral that) {
        append(gen.expressionGen().transform(that));
    }

    // FIXME: port TypeName?
    public void visit(Tree.InitializerExpression value) {
        // FIXME: is this even used?
        append(gen.expressionGen().transformExpression(value.getExpression()));
    }

    public void visit(Tree.SequenceEnumeration value) {
        append(gen.expressionGen().transform(value));
    }

    public void visit(Tree.Tuple value) {
        append(gen.expressionGen().transform(value));
    }

    // FIXME: port Null?
    // FIXME: port Condition?
    // FIXME: port Subscript?
    // FIXME: port LowerBoud?
    // FIXME: port EnumList?
    public void visit(Tree.StringTemplate expr) {
        append(gen.expressionGen().transformStringExpression(expr));
    }
    
    public void visit(Tree.Throw throw_) {
        append(gen.statementGen().transform(throw_));
    }
    
    public void visit(Tree.TryCatchStatement t) {
        append(gen.statementGen().transform(t));
    }
    
    public void visit(Tree.SwitchStatement switch_) {
        append(gen.statementGen().transform(switch_));
    }
    
    public void visit(Tree.FunctionArgument fn) {
        append(gen.expressionGen().transform(fn, fn.getTypeModel()));
    }

    public void visit(Tree.ModuleDescriptor that) {
        appendList(gen.transformModuleDescriptor(that));
    }
    public void visit(Tree.PackageDescriptor that) {
        appendList(gen.transformPackageDescriptor(that));
    }

    public void visit(Tree.Assertion that) {
        appendList(gen.statementGen().transform(that));
    }

    public void visit(Tree.Dynamic that) {
        append(gen.makeError(that, "dynamic is not yet supported on this platform"));
    }

    public void visit(Tree.DynamicClause that) {
        append(gen.at(that).Exec(gen.makeError(that, "dynamic is not yet supported on this platform")));
    }

    public void visit(Tree.DynamicStatement that) {
        append(gen.at(that).Exec(gen.makeError(that, "dynamic is not yet supported on this platform")));
    }
    
    public void visit(Tree.CompilationUnit cu) {
        // Figure out all the local ids
        gen.naming.assignNames(cu);
        super.visit(cu);
        String arg = CodegenUtil.getCompilerAnnotationArgument(cu, "die");
        if (arg != null) {
            if (arg.isEmpty()) {
                arg = "java.lang.RuntimeException";
            }
            try {
                java.lang.Class<? extends Throwable> exceptionClass = (java.lang.Class<? extends RuntimeException>)java.lang.Class.forName(arg, true, getClass().getClassLoader());
                Throwable exception = exceptionClass.newInstance();
                if (exception instanceof RuntimeException) {                    
                    throw (RuntimeException)exception;
                } else if (exception instanceof Error) {
                    throw (Error)exception;
                } else {
                    throw new RuntimeException(exception);
                }
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException(e);
            }
        }
    }
    
    public void visit(Tree.CompilerAnnotation ca) {
        // Don't end up visiting the String literal argument of the compiler annotation!
    }

    /**
     * Gets all the results which were appended during the visit
     * @return The results
     * 
     * @see #getSingleResult()
     */
    public ListBuffer<? extends JCTree> getResult() {
        return defs;
    }
    
    /**
     * Returns the single result, or null if there was more than one result
     * @return The result
     * 
     * @see #getResult()
     */
    @SuppressWarnings("unchecked")
    public <K extends JCTree> K getSingleResult() {
        if (defs.size() != 1) {
            return null;
        }
        return (K) defs.first();
    }
    
    public boolean hasResult() {
        return (defs.size() > 0);
    }
    
    void append(JCTree x) {
    	if (inInitializer) {
    		classBuilder.init((JCTree.JCStatement)x);
    	} else {
    		defs.append(x);
    	}
    }

    void appendList(List<? extends JCTree> xs) {
        for (JCTree x : xs) {
            append(x);
        }
    }
}
