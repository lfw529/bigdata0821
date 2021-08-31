package com.lfw.chapter02;

public class DataTypeConversion {
    public static void main(String[] args) {
        byte b = 10;
        test(b);
    }
    //四个方法都在，执行 byte 版本
   /* public static void test(byte b){
        System.out.println("bbbb");
    }*/
    //如果没有 byte 类型，会就近自动转换成 short 版本
    /*public static void test(short s){
        System.out.println("ssss");
    }*/
    //如果也没有 short 类型，也不会转换成 char 版本，因为与 char 没有自动转换关系
    public static void test(char c){
        System.out.println("cccc");
    }
    //如果也没有 short 类型，会自动转换成 int 版本
    public static void test(int i){
        System.out.println("iiii");
    }
}
