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

import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.Value;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.MethodDeclaration;

/**
 * Utility functions telling you about code generation strategies
 * @see Decl
 */
class Strategy {
    private Strategy() {}
    
    public static boolean defaultParameterMethodTakesThis(Tree.Declaration decl) {
        return defaultParameterMethodTakesThis(decl.getDeclarationModel());
    }
    
    public static boolean defaultParameterMethodTakesThis(Declaration decl) {
        return decl instanceof Method 
                && decl.isToplevel();
    }
    
    public static boolean defaultParameterMethodStatic(Tree.Declaration decl) {
        // Only top-level methods and top-level class initializers 
        // have static default value methods
        return defaultParameterMethodStatic(decl.getDeclarationModel());
    }
    
    public static boolean defaultParameterMethodStatic(Declaration decl) {
        if (decl instanceof Parameter) {
            decl = ((Parameter) decl).getDeclaration();
        }
        // Only top-level methods have static default value methods
        return (decl instanceof Method || decl instanceof Class) 
                && decl.isToplevel();
    }
    
    public static boolean defaultParameterMethodOnSelf(Tree.Declaration decl) {
        return defaultParameterMethodOnSelf(decl.getDeclarationModel());
    }
    
    public static boolean defaultParameterMethodOnSelf(Declaration decl) {
        return decl instanceof Method 
                && !Decl.withinInterface(decl);
    }
    
    /**
     * Determines whether the given Class def should have a {@code main()} method generated.
     * I.e. it's a concrete top level Class without initializer parameters
     * @param def
     */
    public static boolean generateMain(Tree.ClassOrInterface def) {
        return def instanceof Tree.AnyClass 
                && Decl.isToplevel(def) 
                && !Decl.isAbstract(def)
                && ((Class)def.getDeclarationModel()).getParameterList().getParameters().isEmpty();
    }
    
    /**
     * Determines whether the given Method def should have a {@code main()} method generated.
     * I.e. it's a top level method without parameters
     * @param def
     */
    public static boolean generateMain(Tree.AnyMethod def) {
        return  Decl.isToplevel(def) 
                && !def.getParameterLists().isEmpty() 
                && def.getParameterLists().get(0).getParameters().isEmpty();
    }
    
    public static boolean generateThisDelegates(Tree.AnyMethod def) {
        return Decl.withinInterface(def.getDeclarationModel())
            && def instanceof MethodDeclaration
            && ((MethodDeclaration) def).getSpecifierExpression() == null;
    }

    public static boolean needsOuterMethodInCompanion(ClassOrInterface model) {
        return Decl.withinClassOrInterface(model);
    }
    
    public static boolean useField(Value attr) {
        return Decl.isCaptured(attr);
    }
    
    
    public static boolean createField(Parameter p, Value v) {
        return (p == null) || (useField(v) && !p.isCaptured());
    }
    
    /**
     * Non-{@code shared} concrete interface members are 
     * only defined/declared on the companion class, not on the transformed 
     * interface itself.
     */
    public static boolean onlyOnCompanion(Declaration model) {
        return Decl.withinInterface(model)
                && (model instanceof ClassOrInterface
                        || !Decl.isShared(model));
    }
    
    static boolean generateInstantiator(Declaration model) {
        return model instanceof Class 
                && model.isMember()
                && !model.isAnonymous()
                && !((Class)model).isAbstract()
                && Decl.isCeylon((Class)model);
    }
    
    /** 
     * Determines whether a {@code void} Ceylon method should be declared to 
     * return {@code void} or {@code java.lang.Object} (the erasure of 
     * {@code ceylon.language.Void}) in Java. 
     * If the method can be refined, 
     * (but was not itself refined from a Java {@code void} method), or is 
     * {@code actual} then {@code java.lang.Object} should be used.
     */
    static boolean useBoxedVoid(Method m) {
        return m.isMember() &&
            (m.isDefault() || m.isFormal() || m.isActual()) &&
            m.getUnit().getVoidDeclaration().equals(m.getType().getDeclaration()) &&
            Decl.isCeylon((TypeDeclaration)m.getRefinedDeclaration().getContainer());
    }
    
}
