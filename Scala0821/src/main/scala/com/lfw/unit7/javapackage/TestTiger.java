package com.lfw.unit7.javapackage;

public class TestTiger {
    public static void main(String[] args) {
        //使用 xm 的 Tiger
        com.lfw.unit7.javapackage.xm.Tiger tiger01 = new com.lfw.unit7.javapackage.xm.Tiger();
        com.lfw.unit7.javapackage.xh.Tiger tiger02 = new com.lfw.unit7.javapackage.xh.Tiger();
        System.out.println("tiger01=" + tiger01);
        System.out.println("tiger02=" + tiger02);
    }
}
