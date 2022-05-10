package ditz.atrops.hedron;

import java.util.Arrays;

public class Stat {

    private int[] stat = new int[6];

    public void add(int i) {
        if(stat.length<=i)
            stat = Arrays.copyOf(stat,3*i/2);
        ++stat[i];
    }

    public void show() {
        for(int i=0; i<stat.length; ++i) {
            if(stat[i]!=0)
                System.out.format("%2d: %3d\n", i, stat[i]);
        }
    }

    public int count() {
        int count = 0;
        for(int i=0; i<stat.length; ++i) {
            if (stat[i] != 0)
                ++count;
        }
        return count;
    }
    public int length() {
        int length = 0;
        for(int i=0; i<stat.length; ++i) {
            if (stat[i] != 0)
                length=i+1;
        }
        return length;
    }

    public void showLine() {
        System.out.print(count());
        System.out.print(":");
        for(int i=3; i<length(); ++i) {
            System.out.print(" ");
            System.out.print(stat[i]);
        }
        System.out.println();
    }
}
