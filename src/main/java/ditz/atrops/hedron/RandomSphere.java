package ditz.atrops.hedron;

public class RandomSphere extends UnitSphere {

    RandomPoints rand = new RandomPoints();

    RandomSphere(int initial) {
        generate(rand::nextPoint, initial);
    }

    RandomSphere() {
        this(4);
    }

    public void random(int min, int max, int step) {

        int n = points.size();
        int j = rand.nextInt(2*step+1) - step;
        if(j==0)
            j = 1;

        if(n+j < min)
            j = Math.abs(j);

        if(n+j > max)
            j = -Math.abs(j);

        for(int i=0; i<j; i++) {
            addPoint(rand.nextPoint());
        }

        for(int i=0; i<-j; i++) {
            n = points.size();
            n = rand.nextInt(n);
            removePoint(n);
        }
    }
}
