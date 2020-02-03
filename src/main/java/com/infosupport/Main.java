package com.infosupport;

import com.infosupport.a.ExtensionsKt;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        // try {
        //     StartKt.bar();
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }

        // StartKt.foo();

        try {StartKt.bar();} catch (IOException t) {System.out.println("bats boem"); }

        char c = ExtensionsKt.lastChar("abc");
        String bram = ExtensionsKt.repeat("Bram", 3);
        System.out.println(c);
        System.out.println(bram);

        // foo();
    }

    public static void foo() {
        throw new RuntimeException(new IOException());
    }

}
