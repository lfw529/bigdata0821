package com.lfw.kafka.producer;

public class Test { //回调解释

    public void aaa(String str,User user){
        System.out.println("调用Test的aaa方法");
        user.say();
    }
    public static void main(String[] args) {
        Test test = new Test();

        test.aaa("hello",new User());
    }
}

class User{
    public void say(){
        System.out.println("回调 say");
    }
}

class Ticket{
    private static int tickets = 100;
    private void sale(){
        System.out.println("卖票："+ --tickets);
    }

    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                new Ticket().sale();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                new Ticket().sale();
            }
        }).start();
    }
}
