@error;
class Operators() {
    
    class X() {}
    class Y() {}
    
    class Z() extends Object() {
        //fake impl
        shared actual String string = "Z()";
    }
    
    @type["String"] value x0 = "Hello" + " " + "World";
    
    @error value x1 = "Hello" + 1;
    
    @type["Float"] value x2 = 1.3 + 2.3;
    
    @type["Integer"] value w9 = 1 - 2;
    
    @type["Natural"] value w5 = 1 * 1;
    
    @type["Float"] value x3 = -2.5;
    
    @type["Float"] value w2 = +2.5;
    
    @type["Integer"] value w0 = -1;
    
    @type["Integer"] value w3 = +1;
    
    @type["Float"] value x4 = 2.4 ** 0.5;
    
    @type["Integer"] value x5 = (1 + 2) - -3;
    
    @type["Float"] value w4 = -3 * 2.5;
    
    @type["Float"]  value x6 = 1.0 * 2.5 ** (-0.5);
    
    @type["Natural"] value x7 = 10 % 3;
    @type["Natural"] value x8 = 10 / 3;
    
    @error value w1 = 4.0 % 2.0;
    
    @type["Float"] value x9 = 1 + 1.0;
    
    @error value x10 = 1.0 * 2.5 ** -0.5;
    
    @type["Boolean"] value x11 = !( true || false ) && true;
    
    @type["Boolean"] value x12 = 1 < 100;
    
    @error value x13 = "foo" < 100;
    
    @type["Boolean"] value x14 = "foo" == "bar";
    @type["Boolean"] value x15 = "foo" != "bar";
    
    @error value x16 = "foo" == Z();
    
    @type["Boolean"] value x17 = Y() === X();
    
    @error value x18 = "foo" === 12;
    
    @type["String"] value x19 = $12.34;
    
    X? nothing = null;
    
    @type["Nothing|Operators.X"] value x20 = nothing ? nothing;
    
    @type["Operators.X"] value x21 = nothing ? X();
    
    @type["Operators.X"] value x22 = nothing ? nothing ? X();
    
    @type["Operators.Y|Operators.X"] value x60 = nothing ? Y();
    
    @error value x23 = X() ? X();
    
    @error value x24 = X() ? nothing;
    
    @type["Range<Natural>"] value x25 = 1..4;
    
    @type["Range<Integer>"] value x252 = -10..+10;
    
    @error value x253 = -1..5;
    
    @error value x26 = 1.2..3.4;
    
    @type["Entry<Natural,Operators.X>"] value x27 = 0->X();
    @type["Operators.X"] value x27item = x27.item;
    @type["Natural"] value x27key = x27.key;
    
    @error value x28 = 0->nothing;
    
    @type["Boolean"] value x29 = nothing exists;
    @type["Boolean"] value x29n = exists nothing;
    
    @error value x30 = 1 exists;
    @error value x30n = exists 1;
    
    @type["Boolean"] value x70 = {} nonempty;
    @type["Boolean"] value x70n = nonempty {};
    @error value x71 = {"hello"} nonempty;
    @error value x71n = nonempty {"hello"};
    
    @type["Boolean"] value x31 = 1 is Natural;
    @type["Boolean"] value x31n = is Natural 1;
    
    @type["Boolean"] value x32 = nothing is Natural;
    @type["Boolean"] value x32n = is Natural nothing;
    
    @type["Boolean"] value x33 = "hello" in "hello world";
    
    object cat satisfies Category {
        shared actual Boolean contains(Equality element) {
            return true;
        }
    }
    
    @type["Boolean"] value x55 = 1 in cat;
    
    Sequence<Natural?> seqopt2 = { null, 1 };    
    @error value x56 = "foo" in seqopt2;
    
    @type["Comparison"] value x34 = 1<=>3;
    @type["Comparison"] value x35 = "foo"<=>"bar";
    @error value x36 = X()<=>X();
    @error value x37 = 1<=>"hello";
    
    X[] sequence = {X(), X()};
    String[]? noSequence = null;
    String[] emp = {};
    
    @type["Nothing|Operators.X"] value x38 = sequence[0];
    @type["Empty|Sequence<Operators.X>"] value x39 = sequence[0..1];
    @type["Empty|Sequence<Operators.X>"] value x40 = sequence[1+1...];
    @type["Nothing|Operators.X"] value x41 = {nothing}[0];
    @type["Nothing|String"] value x42 = noSequence?[0];
    
    @error value x43 = sequence["hello"];
    @error value x44 = sequence["hello"...];
    @error value x45 = sequence[1.."hello"];
    
    @type["Empty|Sequence<String>"] value e45 = emp[].uppercased;
    @type["Sequence<Empty|Sequence<Character>>"] value x46 = {"hello", "world"}[].characters;
    @type["Sequence<String>"] value x47 = {"hello", "world"}[].uppercased;
    @type["Nothing|Empty|Sequence<Character>"] value x48 = {"hello", "world"}[0]?.characters;
    @type["Sequence<Empty|Sequence<Character>>"] value x49 = {"hello", "world"}[].characters;
    @type["Sequence<Ordered<String>>"] value x50 = {"hello", "world"}[].lines;
    @type["Nothing|String"] value x51 = {"hello", "world"}[0]?.normalized;
    @type["Nothing|Ordered<String>"] value x512 = {"hello", "world"}[0]?.split(" ");
    @type["Sequence<String>"] value x52 = {"hello", "world"}[].normalized;
    @type["Sequence<Ordered<String>>"] value x522 = {"hello", "world"}[].split(" ");
    @type["Nothing|String"] value x53 = noSequence?[0]?.normalized;
    @type["Nothing|Ordered<String>"] value x532 = noSequence?[0]?.split(" ");
    @type["Sequence<Operators.X>"] value x54 = {Operators()}[].X();
    
    variable Natural n := 0;
    @type["Natural"] n++;
    @type["Natural"] ++n;
    @type["Natural"] n+=1;
    @type["Natural"] n/=3;
    @type["Natural"] n*=2;
    @error n*=-2;
    @error n+=1.0;
    @error n+=-1;
    @error n-=1;
    
    variable Float f:=0.0;
    @type["Float"] f+=1.0;
    @type["Float"] f+=1;
    @type["Float"] f-=4;
    @type["Float"] f/=4.0;
    @type["Float"] f*=2;
    @type["Float"] f/=-2;
    @type["Float"] f+=-1;
    
    variable Boolean bb := false;
    @type["Boolean"] bb||=false;
    @type["Boolean"] bb&&=true;
    
    @error (n+1) ++;
    @error ++ (n+1);
    
    Natural const = 0;
    @error const++;
    @error --const;
    @error const+=10;
    
    variable Natural nn;
    @error nn+=1;
    @error nn++;
    
    @error X()[].doIt();
    @error X()?.doIt();
    
    @type["Empty"] value es = {};
    
    @type["Nothing"] value nnnn = es[0];
    Nothing nnnn2 = nnnn;
    
    @type["Nothing|String"] value ns = { null, "hello", "world" }[1];
    String? ns2 = ns;
    
    @type["Sequence<Integer|Natural>"] value ins = { -10, -1, 1, 3 };
    @type["Nothing|Integer|Natural"] value ion = ins[2];
    @error String ions = ion.string;
    Sequence<Integer|Natural> ins2 = ins;
    Nothing|Integer|Natural ion2 = ion;
    Integer?|Natural? ion3 = ion;
    Nothing|Integer|Natural ion4 { return ion2; }
    Nothing|Integer|Natural ion5 { return ion3; }
    Integer|Natural defaultIon(Nothing|Integer|Natural x) {
        if (exists x) {
            return x;
        }
        else {
            return 0;
        }
    }
    Integer|Natural defaultIon2(Nothing|Integer|Natural x) {
        return x?0;
    }

    Boolean b1 = true;
    Boolean b2 = false;
    Boolean b3 = b1 && b2;
    variable Boolean b4 := b2 || b3;
    variable Boolean b5 := !b4;
    b4 &&= b3;
    b5 ||= b4;
    
    @type["Entry<String,Float>"] String->Float esf = "hello"->1.0;
    @type["Sequence<Entry<String,Float>>"] Sequence<String->Float> esfs = {esf};
    
    Float x=1.0;
    Float result = x>0.0 then x else 0.0;
    
    String str1 = null ? null ? "hello";
    String str2 = null else null else "hello";
    
}