package com.lfw.chapter01;

public class Hello2 {
    public static void main(String[] args) {
        Hello2$.MOUDLE$.main(args);
    }
}

final class Hello2${
    public static final Hello2$ MOUDLE$;

    //Hello2$(){}
    static {
        MOUDLE$ = new Hello2$();//构造器创建对象
    }

    public void main(String[] args) {
        System.out.println("hello,scala");
    }
}