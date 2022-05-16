package ditz.atrops.hedron;

import java.util.Arrays;

public class Stat implements Comparable<Stat> {

    private int[] stat = new int[6];

    public void add(int i) {
        while(stat.length<=i)
            stat = Arrays.copyOf(stat,3*stat.length/2);
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
            if(i<10)
                System.out.print(" ");    
            System.out.print(stat[i]);
        }
        System.out.println();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Stat stat1 = (Stat) o;

        return Arrays.equals(stat, stat1.stat);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(stat);
    }

    @Override
    public int compareTo(Stat o) {
        int result = Integer.compare(stat.length, o.stat.length);

        if(result==0) {
            for(int i=0; i<stat.length; ++i) {
                int j = stat.length-i-1;
                result = Integer.compare(stat[j], o.stat[j]);
                if(result!=0)
                    break;
            }
        }

        return result;
    }
}
