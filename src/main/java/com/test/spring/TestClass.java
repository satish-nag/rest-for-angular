package com.test.spring;

public class TestClass {
    int i = 1;
    int j = 2;
}

class Test1 extends TestClass{
    int j;

    public Test1(){
        super.i = 10;
        super.j = 20;
    }

    public void m1(){
        int a1[] = new int[]{3,5,6};
        int b2[] = fix(a1);
        System.out.print(a1[0]+a1[1]+a1[2]+"");
        System.out.println(b2[0]+b2[1]+b2[2]);
    }

    private int[] fix(int[] b1) {
        b1[1]=10;
        return b1;
    }

    public static void main(String[] args){
        Test1 test1 = new Test1();
        test1.m1();
    }
}