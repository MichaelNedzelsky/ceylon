shared void bug6014() {
	class A<T>(){}
	class B(A<String> l){
		shared void hello(){}
	}
	value func = `B.hello`;
}