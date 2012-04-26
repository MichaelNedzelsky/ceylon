package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Attribute;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;

@Ceylon @Attribute
public class $empty {
    private final static Empty value = new Empty() {

        @Override
        public boolean defines(java.lang.Object key) {
            return Correspondence$impl._defines(this, (Integer)key);
        }

        @Override
        public Category getKeys() {
            return Correspondence$impl._getKeys(this);
        }

        @Override
        public boolean definesEvery(Iterable keys) {
            return Correspondence$impl._definesEvery(this, keys);
        }

        @Override
        public boolean definesAny(Iterable keys) {
            return Correspondence$impl._definesAny(this, keys);
        }

        @Override
        public List<? extends java.lang.Object> items(Iterable keys) {
            return Correspondence$impl._items(this, keys);
        }
        
        @Override
        public boolean containsEvery(ceylon.language.Iterable<?> elements) {
            return Category$impl._containsEvery(this, elements);
        }

        @Override
        public boolean containsAny(ceylon.language.Iterable<?> elements) {
            return Category$impl._containsAny(this, elements);
        }

        @Override
        public long getSize() {
            return Empty$impl._getSize(this);
        }

        @Override
        public boolean getEmpty() {
            return Collection$impl._getEmpty(this);
        }

        @Override
        public Iterator getIterator() {
            return None$impl._getIterator(this);
        }

        @Override
        public java.lang.Object item(Integer key) {
            return Empty$impl._item(this, key);
        }

        @Override
        public java.lang.Object getFirst() {
            return None$impl._getFirst(this);
        }

        @Override
        public java.lang.String toString() {
        	return Empty$impl._toString(this);
        }
        
        @Override
        public Empty span(Integer from, Integer to) {
            return Empty$impl._span(this, from, to);
        }

        @Override
        public Empty segment(Integer from, Integer length) {
            return Empty$impl._segment(this, from, length);
        }

        @Override
        public boolean contains(java.lang.Object element) {
            return Empty$impl._contains(this, element);
        }

        @Override
        public long count(java.lang.Object element) {
            return Empty$impl._count(this, element);
        }

        @Override
        public Empty getClone() {
            return Empty$impl._getClone(this);
        }

        @Override
        public Integer getLastIndex() {
            return Empty$impl._getLastIndex(this);
        }

        @Override
        public boolean defines(Integer key) {
            return Empty$impl._defines(this, key);
        }
        
        public boolean equals(Object that) {
            return that instanceof List ?
                    ((List) that).getEmpty() : false;
        }
        
        @Override
        public int hashCode() {
            return 0;
        }

        @Override
        public java.lang.Object item(java.lang.Object key) {
            return Empty$impl._item(this, key);
        }

        @Override
        public java.lang.Object span(Comparable from, Comparable to) {
            return Empty$impl._span(this, (Integer)from, (Integer)to);
        }

        @Override
        public java.lang.Object segment(Comparable from, Comparable length) {
            return Empty$impl._segment(this, (Integer)from, (Integer)length);
        }


    };
    
    public static Empty getEmpty(){
        return value;
    }
    
}