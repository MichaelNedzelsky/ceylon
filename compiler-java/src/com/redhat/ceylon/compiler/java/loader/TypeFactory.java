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

package com.redhat.ceylon.compiler.java.loader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.redhat.ceylon.compiler.java.tools.LanguageCompiler;
import com.redhat.ceylon.compiler.typechecker.context.Context;
import com.redhat.ceylon.compiler.typechecker.model.IntersectionType;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.model.UnionType;
import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.Unit;
import com.redhat.ceylon.compiler.typechecker.model.UnknownType;
import com.redhat.ceylon.compiler.typechecker.model.Util;

public class TypeFactory extends Unit {
    private Context context;
    private Class arraySequence;

    public static TypeFactory instance(com.sun.tools.javac.util.Context context) {
        TypeFactory instance = context.get(TypeFactory.class);
        if (instance == null) {
            instance = new TypeFactory(LanguageCompiler.getCeylonContextInstance(context));
            context.put(TypeFactory.class, instance);
        }
        return instance;
    }
    
    public TypeFactory(Context context) {
        this.context = context;
    }
    
    public Context getContext() {
        return context;
    }
    
    /**
     * Gets the declaration of {@code ArraySequence}
     * @return The declaration
     */
    public TypeDeclaration getDefaultSequenceDeclaration() {
        // do this lazily so that we don't have to load it from the .class file. We only use it to
        // build a Java type so we don't need to load it entirely, and this way it works with the
        // language module loaded from dump and .car (otherwise it's not in the dump since it doesn't
        // come from ceylon code)
        if(arraySequence == null){
            arraySequence = new Class();
            arraySequence.setName("ArraySequence");
            List<TypeParameter> typeParameters = new ArrayList<TypeParameter>(1);
            TypeParameter typeParameter = new TypeParameter();
            typeParameter.setName("Element");
            typeParameters.add(typeParameter);
            arraySequence.setTypeParameters(typeParameters);
            arraySequence.getSatisfiedTypes().add(getSequenceType(typeParameter.getType()));
            Package languagePackage = context.getModules().getLanguageModule().getPackage("ceylon.language");
            arraySequence.setContainer(languagePackage);
            languagePackage.getMembers().add(arraySequence);
        }
        return arraySequence;
    }

    /**
     * Determines whether the given ProducedType is a union
     * @param pt 
     * @return whether the type is a union type
     */
    public boolean isUnion(ProducedType pt) {
        TypeDeclaration tdecl = pt.getDeclaration();
        return (tdecl instanceof UnionType && tdecl.getCaseTypes().size() > 1);
    }

    /**
     * Determines whether the given ProducedType is an intersection
     * @param pt 
     * @return whether the type is an intersection type
     */
    public boolean isIntersection(ProducedType pt) {
        TypeDeclaration tdecl = pt.getDeclaration();
        return (tdecl instanceof IntersectionType && tdecl.getSatisfiedTypes().size() > 1);
    }

    /**
     * Returns a ProducedType corresponding to {@code ArraySequence<T>}
     * @param et The ProducedType corresponding to {@code T}
     * @return The ProducedType corresponding to {@code ArraySequence<T>}
     */
    public ProducedType getDefaultSequenceType(ProducedType et) {
        return Util.producedType(getDefaultSequenceDeclaration(), et);
    }

    /**
     * Returns a ProducedType corresponding to {@code Array<T>}
     * @param et The ProducedType corresponding to {@code T}
     * @return The ProducedType corresponding to {@code Array<T>}
     */
    public ProducedType getArrayType(ProducedType et) {
        return Util.producedType(getArrayDeclaration(), et);
    }

    public ProducedType getArrayElementType(ProducedType type) {
        ProducedType st = type.getSupertype(getArrayDeclaration());
        if (st!=null && st.getTypeArguments().size()==1) {
            return st.getTypeArgumentList().get(0);
        }
        else {
            return null;
        }
    }
    
    public ProducedType getFixedSizedType(ProducedType pt) {
        return pt.getSupertype(getFixedSizedDeclaration());
    }
    
    public ProducedType getCallableType(java.util.List<ProducedType> typeArgs) {
        return getCallableDeclaration().getProducedType(null, typeArgs);
    }
    
    public ProducedType getCallableType(ProducedType resultType) {
        return getCallableType(Collections.<ProducedType>singletonList(resultType));
    }

    public ProducedType getUnknownType() {
        return new UnknownType(this).getType();
    }
}
