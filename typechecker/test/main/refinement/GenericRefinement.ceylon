class GenericRefinement() {
    
    interface GoodTypes {
        
        class Foo<T,S>() {
            shared default T foo(T t) { return t; }
            shared default S bar(S s) { return s; }
            shared default T t { throw; }
            shared default S s { throw; }
        }
        
        class Bar<Q>() extends Foo<Q,String>() {
            shared actual Q foo(Q t) { return t; }
            shared actual String bar(String s) { return s; }
            shared actual Q t { throw; }
            shared actual String s { throw; }
        }
        
    }
    
    interface NestedTypes {
        
        interface Foo<X> {
            shared formal X x();
        }
        
        interface Bar<X> {
            shared formal Foo<X> foo();
        }
        
        class BarImpl1<Y>(Y xp) satisfies Bar<Y> {
            shared actual Foo<Y> foo() {
                class FooImpl() satisfies Foo<Y> {
                    shared actual Y x() { return xp; }
                }
                return FooImpl();
            }
        }
        class BarImpl2<Y>(Y xp) satisfies Bar<Y> {
            shared actual Foo<Y> foo() {
                object foo satisfies Foo<Y> {
                    shared actual Y x() { return xp; }
                }
                return foo;
            }
        }
        
    }

    interface NestedTypes2 {
        
        interface Foo<X> {
            shared formal X x();
        }
        
        interface Bar<X> {
            shared formal Foo<X> foo();
        }
        
        class BarImpl1<X>(X xp) satisfies Bar<X> {
            shared actual Foo<X> foo() {
                class FooImpl() satisfies Foo<X> {
                    shared actual X x() { return xp; }
                }
                return FooImpl();
            }
        }
        class BarImpl2<X>(X xp) satisfies Bar<X> {
            shared actual Foo<X> foo() {
                object foo satisfies Foo<X> {
                    shared actual X x() { return xp; }
                }
                return foo;
            }
        }
        
    }
    
    interface GenericRefinesConcrete {
    	abstract class WithNumber() {
    		shared formal Number num;
    	}
    	class WithNumeric<T>(Numeric<T> n) 
    	        extends WithNumber() 
    	        given T satisfies Numeric<T> {
    		shared actual default Numeric<T> num = n;
    	}
    	class WithFloat(Float f) 
    	        extends WithNumeric<Float>(f) {
    		shared actual Float num = f;
    	}
    }

}