class Optional() {
    
    class X() {}
    class Y() {}
    X? x = X();
    X? y = null;
    X? z = x;
    X? w = y;
    
    void xx(X x) {}
    
    if (exists x) {
        xx(x);
    }
    
    if (exists X xxx = x) {
        xx(xxx);
    }
    
    if (exists @error Y xxx = x) {}
    
    value sx = { X() };
    value sxn = { X(), null };
    value sy = { Y() };
    value syn = { Y(), null };
    value sxy = { X(), Y() };
    variable value ss := { X(), Y(), null };
    ss:=sx;
    ss:=sy;
    ss:=sxy;
    ss:=sxn;
    ss:=syn;
    value bs = { X(), "foo" };
    @error ss:=bs;
    
    class Foo<T>() {
        shared T? optional = null;
        shared T definite { throw; }
        shared T[]? optionalList { throw; }
        shared T[] list { throw; }
    }
    
    String? optional = Foo<String>().optional;
    String definite = Foo<String>().definite;
    String[]? optionalList = Foo<String>().optionalList;
    String[] list = Foo<String>().list;
    
    @error String sssss = list.first;
    @error Natural nnnn = list.lastIndex;
    
    if (nonempty list) {
        String s = list.first;
        Natural li = list.lastIndex;
    }
    
    if (nonempty Sequence<String> strings = optionalList) {
        String s = strings.first;
        Natural li = strings.lastIndex;
    }
    
    Sequence<String> stuff = { "foo" };
    Character[][] chars = stuff[].characters;

    String[] nostuff = {};
    Character[][] nochars = nostuff[].characters;
    
    String? maybestuff = null;
    Character[]? maybechars = maybestuff?.characters;
    
    Character[] somechars = {};
    Natural scs = somechars.size;
    value sci = somechars.iterator;
    
    @type["Nothing|String|Integer|Sequence<Object>"] String? | String | String? | Integer | Sequence<Object> foobar1 = -1;
    @type["Nothing|Empty|Sequence<String>|Integer"] String[]? | String[] | Sequence<String> | Integer foobar2 = 1.integer;
    
    @type["Sequence<Nothing|String|Integer|Sequence<Object>|Empty>"] value xyz = { foobar1, foobar2 };
    
    //TODO: I think the type parameter X does
    //      not hide the X defined above - it
    //      should!
    /*shared void entries<X>(X... sequence) 
            given X satisfies Equality {
        if (nonempty sequence) {
            entries<X>(sequence.clone);
        }
    }*/
    
    class WithOptional<T>(T? val) {
        shared T? val = val;
        shared T[]? seq;
        if (exists val) {
            seq = {val};
        }
        else {
            seq = null;
        }
    }
    
    if (exists s = WithOptional<String>("hello").val) {
        @type["String"] value es = s;
    }
    
    if (exists seq = WithOptional<String>("goodbye").seq) {
        @type["Empty|Sequence<String>"] value sseq = seq;
    }
    
    if (nonempty seq = WithOptional<String>("hello again").seq) {
        @type["Sequence<String>"] value sseq = seq;
    }
    
    class WithOptionalString(String? val)
            extends WithOptional<String>(val) {
        
        if (exists val) {
            @type["String"] value ss = val;
        }
        
        void method() {
        
            if (exists seq = WithOptionalString("hello").seq) {
                @type["Empty|Sequence<String>"] value sseq = seq;
            }
            
            if (nonempty seq = WithOptionalString("hello").seq) {
                @type["Sequence<String>"] value sseq = seq;
            }
            
        }
        
    }
    
    
    if (exists seq = WithOptionalString("hello").seq) {
        @type["Empty|Sequence<String>"] value sseq = seq;
    }
    
    if (nonempty seq = WithOptionalString("hello").seq) {
        @type["Sequence<String>"] value sseq = seq;
    }
    
}