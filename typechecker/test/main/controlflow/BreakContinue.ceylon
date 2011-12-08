class BreakContinue() {
    void badBreak() {
        @error break;
    }
    void badContinue() {
        @error continue;
    }
    void forBreak() {
        for (n in 0..1) {
            break;
        }
    }
    void forContinue() {
        for (n in 0..1) {
            continue;
        }
    }
    void whileBreak() {
        while (true) {
            break;
        }
    }
    void whileContinue() {
        while (false) {
            continue;
        }
    }
    void forBadBreak() {
        for (n in 0..1) {
            void bad() {
                @error break;
            }
        }
    }
    void forBadContinue() {
        for (n in 0..1) {
            class Bad() {
                @error continue;
            }
        }
    }
    void forBadContinue2() {
        for (n in 0..1) {
            object bad {
                @error continue;
            }
        }
    }
    void whileBadBreak() {
        while (true) {
            class Bad() {
                @error break;
            }
        }
    }
    void whileBadBreak2() {
        while (true) {
            object bad {
                @error break;
            }
        }
    }
    void whileBadContinue() {
        while (false) {
            void bad() {
                @error continue;
            }
        }
    }
    
    class ReturnFromClass() {
        return;
    }
    
    object returnFromObject {
        return;
    }
    
    void returnFromClassInMethod() {
        class Good() {
            return;
        }
        return;
    }
    
    void returnFromObjectInMethod() {
        object good {
            return;
        }
        return;
    }
    
    
    Natural badReturnFromSetter() {
        String bad {
            return "hello";
        }
        assign bad {
            @error return 0;
        }
        return 1;
    }
    
    Natural badReturnFromSetter2() {
        String bad {
            return "hello";
        }
        assign bad {
            @error return "goodbye";
        }
        return 1;
    }
    
    Natural returnFromSetter() {
        String bad {
            return "hello";
        }
        assign bad {
            return;
        }
        return 1;
    }
    
}