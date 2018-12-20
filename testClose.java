package com.swt.test;

import com.swt.segment.common.testComu.CommuteCloseness;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class testClose {
    public static void main(String[] args){
        List<String[]> test = new ArrayList<>();
        String[] edge1 = new String[2];
        edge1[0] = "1324556";
        edge1[1] = "5421321";
        String[] edge2 = new String[2];
        edge2[0] = "1324556";
        edge2[1] = "1233453";
        String[] edge3 = new String[2];
        edge3[0] = "1324556";
        edge3[1] = "987654";
        String[] edge4 = new String[2];
        edge4[0] = "1324556";
        edge4[1] = "5421321";
        String[] edge5 = new String[2];
        edge5[0] = "1324556";
        edge5[1] = "853267956";
        String[] edge6 = new String[2];
        edge6[0] = "1324556";
        edge6[1] = "46789324";
        String[] edge7 = new String[2];
        edge7[0] = "1324556";
        edge7[1] = "7532166";
        String[] edge8 = new String[2];
        edge8[0] = "1324556";
        edge8[1] = "97543274";
        String[] edge9 = new String[2];
        edge9[0] = "987654";
        edge9[1] = "1324556";
        String[] edge10 = new String[2];
        edge10[0] = "97543274";
        edge10[1] = "1324556";
        String[] edge11 = new String[2];
        edge11[0] = "97543274";
        edge11[1] = "1324556";
        test.add(edge1);
        test.add(edge2);
        test.add(edge3);
        test.add(edge4);
        test.add(edge5);
        test.add(edge6);
        test.add(edge7);
        test.add(edge8);
        test.add(edge9);
        test.add(edge10);
        test.add(edge11);
        CommuteCloseness a = new CommuteCloseness();
        Map res = a.closeness(test);
        System.out.println(res.values());

    }
}
