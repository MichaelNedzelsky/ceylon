package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Annotation;
import com.redhat.ceylon.compiler.java.metadata.Annotations;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.ValueType;

@Ceylon(major = 3)
@Class(extendsType="ceylon.language::Value")
@SatisfiedTypes({"ceylon.language::Comparable<ceylon.language::Character>",
		        "ceylon.language::Ordinal<ceylon.language::Character>"})
@ValueType
public final class Character
        implements Comparable<Character>, Ordinal<Character> {

    public final int codePoint;

    private Character(int codePoint) {
        this.codePoint = codePoint;
    }

    @Ignore
    public static Character instance(int c) {
        return new Character(c);
    }

    @Ignore
    public int intValue() {
        return codePoint;
    }

    public java.lang.String toString() {
        return java.lang.String.valueOf(java.lang.Character.toChars(codePoint));
    }

    @Ignore
    public static java.lang.String toString(int codePoint) {
        return java.lang.String.valueOf(java.lang.Character.toChars(codePoint));
    }

    public boolean getLowercase() {
        return java.lang.Character.isLowerCase(codePoint);
    }

    @Ignore
    public static boolean getLowercase(int codePoint) {
        return java.lang.Character.isLowerCase(codePoint);
    }

    public boolean getUppercase(){
        return java.lang.Character.isUpperCase(codePoint);
    }

    @Ignore
    public static boolean getUppercase(int codePoint) {
        return java.lang.Character.isUpperCase(codePoint);
    }

    public boolean getTitlecase(){
        return java.lang.Character.isTitleCase(codePoint);
    }

    @Ignore
    public static boolean getTitlecase(int codePoint) {
        return java.lang.Character.isTitleCase(codePoint);
    }

    public boolean getDigit(){
        return java.lang.Character.isDigit(codePoint);
    }

    @Ignore
    public static boolean getDigit(int codePoint) {
        return java.lang.Character.isDigit(codePoint);
    }

    public boolean getLetter(){
        return java.lang.Character.isLetter(codePoint);
    }

    @Ignore
    public static boolean getLetter(int codePoint) {
        return java.lang.Character.isLetter(codePoint);
    }

    public boolean getWhitespace(){
        return java.lang.Character.isWhitespace(codePoint);
    }

    @Ignore
    public static boolean getWhitespace(int codePoint) {
        return java.lang.Character.isWhitespace(codePoint);
    }

    public boolean getControl(){
        return java.lang.Character.isISOControl(codePoint);
    }

    @Ignore
    public static boolean getControl(int codePoint) {
        return java.lang.Character.isISOControl(codePoint);
    }

    public ceylon.language.Character getLowercased() {
        return new Character(java.lang.Character.toLowerCase(codePoint));
    }

    @Ignore
    public static int getLowercased(int codePoint) {
        return java.lang.Character.toLowerCase(codePoint);
    }

    public ceylon.language.Character getUppercased() {
        return new Character(java.lang.Character.toUpperCase(codePoint));
    }

    @Ignore
    public static int getUppercased(int codePoint) {
        return java.lang.Character.toUpperCase(codePoint);
    }

    public ceylon.language.Character getTitlecased() {
        return new Character(java.lang.Character.toTitleCase(codePoint));
    }

    @Ignore
    public static int getTitlecased(int codePoint) {
        return java.lang.Character.toTitleCase(codePoint);
    }

    @Override
    public int hashCode() {
        return codePoint;
    }

    @Ignore
    public static int hashCode(int codePoint) {
        return codePoint;
    }

    @Override
    public boolean equals(@Name("that") java.lang.Object obj) {
        if (this == obj)
            return true;
        if (obj instanceof Character) {
            Character other = (Character) obj;
            return codePoint == other.codePoint;
        }
        else {
            return false;
        }
    }

    @Ignore
    public static boolean equals(int codePoint, java.lang.Object obj) {
        if (obj instanceof Character) {
            Character other = (Character) obj;
            return codePoint == other.codePoint;
        }
        else {
            return false;
        }
    }

	@Override
	public Comparison compare(@Name("other") Character other) {
        long x = codePoint;
        long y = other.codePoint;
        return (x < y) ? smaller_.getSmaller$() :
            ((x == y) ? equal_.getEqual$() : larger_.getLarger$());
	}

    @Ignore
    public static Comparison compare(int codePoint, int otherCodePoint) {
        long x = codePoint;
        long y = otherCodePoint;
        return (x < y) ? smaller_.getSmaller$() :
            ((x == y) ? equal_.getEqual$() : larger_.getLarger$());
    }

	/*@Override
	public boolean largerThan(@Name("other") Character other) {
		return codePoint>other.codePoint;
	}

	@Override
	public boolean smallerThan(@Name("other") Character other) {
		return codePoint<other.codePoint;
	}

	@Override
	public boolean asLargeAs(@Name("other") Character other) {
		return codePoint>=other.codePoint;
	}

	@Override
	public boolean asSmallAs(@Name("other") Character other) {
		return codePoint<=other.codePoint;
	}*/

    @Override
    public Character getPredecessor() {
    	return new Character(codePoint-1);
    }

    @Ignore
    public static int getPredecessor(int codePoint) {
        return codePoint-1;
    }

    @Override
    public Character getSuccessor() {
    	return new Character(codePoint+1);
    }

    @Ignore
    public static int getSuccessor(int codePoint) {
        return codePoint+1;
    }

    @Override
    @Annotations(@Annotation("formal"))
    @TypeInfo("ceylon.language::Integer")
    public long distanceFrom(Character other) {
        return codePoint-other.codePoint;
    }
    @Ignore
    public long distanceFrom(int other) {
        return codePoint-other;
    }
    @Ignore
    public static long distanceFrom(int a, int b) {
        return a-b;
    }

    public long getInteger() {
        return codePoint;
    }

    @Ignore
    public static long getInteger(int codePoint) {
        return codePoint;
    }

}
