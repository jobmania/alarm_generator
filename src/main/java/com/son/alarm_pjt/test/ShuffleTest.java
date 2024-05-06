package com.son.alarm_pjt.test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ShuffleTest {
    public static void main(String[] args) {
        List<String>  abc =Arrays.asList("엄", "준", "식","문","뵹","평","택");

        Collections.shuffle(abc);

        for (String s : abc) {
            System.out.println("s = " + s);
        }
    }
}
