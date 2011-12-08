class OuterTypeArgs() {
	class Foo<T>() given T satisfies Equality {
		shared default class Bar<S>() given S satisfies Equality {
			Bar<S> b0 = Bar<S>();
			@error Bar<S> b1 = Foo<Natural>().Bar<S>();
			Bar<S> b2 = Foo<T>().Bar<S>();
			shared default Bar<S> get() {
				return this;
			}
			shared T getT() {
				throw;
			}
			shared class Qux<R>() {
				shared Bar<S> bar {
					return outer;
				}
				shared Entry<T,S> entry(T t, S s) {
					return t->s;
				}
			}
			void method() {
				class Inner() extends Qux<String>() {}
				Foo<T>.Bar<S>.Qux<String> good = Inner();
				Bar<S>.Qux<String> ok = Inner();
				Qux<String> stillok = Inner();
				@error Foo<String>.Bar<S>.Qux<String> bad = Inner();
				@error Foo<T>.Bar<Integer>.Qux<String> alsobad = Inner();
				Entry<T,S> entry(T t, S s) {
					return Inner().entry(t,s);
				}
			}
		}
	}
	class Baz<F>() extends Foo<F>() given F satisfies Equality {}
	class Fum<T>() extends Foo<T>() given T satisfies Equality {
		shared actual class Bar<S>() extends super.Bar<S>() 
		        given S satisfies Equality {
			shared actual Bar<S> get() {
				return this;
			}
		}
	}
	Baz<String>.Bar<Natural> foobar = Baz<String>().Bar<Natural>();
	@type["OuterTypeArgs.Foo<String>.Bar<Natural>"] value fbg = foobar.get();
	@type["String"] value fbgt = foobar.getT();
	Baz<Float>.Bar<String>.Qux<Object> foobarqux = Baz<Float>().Bar<String>().Qux<Object>();
	@type["OuterTypeArgs.Foo<Float>.Bar<String>"] value fbqg = foobarqux.bar;
	@type["Entry<Float,String>"] foobarqux.entry(1.0, "hello");

	Fum<String>.Bar<Natural> fumbar = Fum<String>().Bar<Natural>();
	@type["OuterTypeArgs.Fum<String>.Bar<Natural>"] value fmbg = fumbar.get();
	Fum<String>.Bar<Natural>.Qux<Integer> fumbarqux = Fum<String>().Bar<Natural>().Qux<Integer>();
	@type["OuterTypeArgs.Foo<String>.Bar<Natural>"] value fmbqg = fumbarqux.bar;
	@type["Entry<String,Natural>"] fumbarqux.entry("given", 30);
	
	Foo<String>.Bar<Natural> fb1 = fmbg;
	Foo<String>.Bar<Natural> fb2 = fbg; 
	Fum<String>.Bar<Natural> fb3 = fmbg;
	@error Fum<String>.Bar<Natural> fb4 = fbg; 
	Baz<String>.Bar<Natural> fb5 = fmbg;
	Baz<String>.Bar<Natural> fb6 = fbg;
	
	Baz<Equality>.Bar<String> bazbarobj = Baz<Equality>().Bar<String>();
	@error Baz<Natural>.Bar<String> bazbarnat = bazbarobj;
	
	class Outer<out T>(T t) {
		shared class Inner<out S>(S s) {
			shared Outer<T>.Inner<S> bar() { return this; }
		}
	}
	
	@type["OuterTypeArgs.Outer<String>.Inner<String>"] Outer("hello").Inner("world");
	@type["OuterTypeArgs.Outer<String>.Inner<String>"] Outer("hello").Inner("world").bar();
	Outer<Object>.Inner<String> oiobj = Outer("hello").Inner("world");
	Outer<String>.Inner<Object> oiobj2 = Outer("hello").Inner("world");
	@error Outer<String>.Inner<String> oistr = oiobj;
	@error Outer<Natural>.Inner<String> oinat = Outer("hello").Inner("world");

	class Consumer<in T>(T t) {
		shared class Inner<out S>(S s) {
			shared S foo(T t) { return s; }
		}
	}
	
	@type["OuterTypeArgs.Consumer<String>.Inner<String>"] Consumer("hello").Inner("world");
	@type["String"] Consumer("hello").Inner("world").foo("hello");
	Consumer<Bottom>.Inner<String> ciobj = Consumer("hello").Inner("world");
	Consumer<String>.Inner<Object> ciobj2 = Consumer("hello").Inner("world");
	@error Consumer<String>.Inner<String> cistr = ciobj;
	@error Consumer<Natural>.Inner<String> cinat = Consumer("hello").Inner("world");

}

