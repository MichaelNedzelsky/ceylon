class LanguageModule() {
    
    shared N plus<X,Y,N>(X x, Y y)
            given N of X|Y satisfies Numeric<N>
            given X satisfies Castable<N> & Numeric<X>
            given Y satisfies Castable<N> & Numeric<Y> {
        return x.castTo<N>().plus(y.castTo<N>());
    }

    @type["Natural"] plus<Natural, Natural, Natural>(1, 2);
    @type["Integer"] plus<Natural, Integer, Integer>(1, -2);
    @type["Float"] plus<Natural, Float, Float>(1, 2.0);
    @error plus<Natural, Integer, Natural>(1, -2);
    
    //Put these back in once we have enumerated type bounds
    //@error plus<Natural, Integer, Float>(1, -2);
    //@error plus<Natural, Natural, Integer>(1, 2);

    @type["Empty|Sequence<Entry<Natural,String>>"] entries("hello", "world");
    @type["Empty|Sequence<Entry<Natural,String>>"] entries({"hello", "world"}...);
    for (Natural i->String s in entries("hello", "world", "!")) {}

}