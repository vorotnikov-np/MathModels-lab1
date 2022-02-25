import java.util.ArrayList;
import java.util.Collections;

public class Scheme {
    public Scheme(Data data, int N) {
        this.data = data;
        this.N = N;
        buildScheme();
    }

    int N;
    Data data;

    private final ArrayList<Double> r = new ArrayList<>();
    private final ArrayList<Double> rMinus = new ArrayList<>();
    private final ArrayList<Double> rPlus = new ArrayList<>();

    private final ArrayList<Double> k = new ArrayList<>();
    private final ArrayList<Double> kMinus = new ArrayList<>();
    private final ArrayList<Double> kPlus = new ArrayList<>();
    private final ArrayList<Double> q = new ArrayList<>();
    private final ArrayList<Double> f = new ArrayList<>();

    private final ArrayList<Double> h = new ArrayList<>();
    private final ArrayList<Double> h2 = new ArrayList<>();

    private final ArrayList<Double> a = new ArrayList<>();
    private final ArrayList<Double> c = new ArrayList<>();
    private final ArrayList<Double> b = new ArrayList<>();
    private final ArrayList<Double> g = new ArrayList<>();


    public ArrayList<Double> shuttle(){
        if(!checkStabs()){
            System.out.println("Not Stab!");
            System.exit(1);
        }
        final ArrayList<Double> alpha = new ArrayList<>();
        final ArrayList<Double> betta = new ArrayList<>();
        ArrayList<Double> X = new ArrayList<>();
        alpha.add(null);
        betta.add(null);

        alpha.add(-1 * (b.get(0)/c.get(0)));
        betta.add(g.get(0)/c.get(0));
        for (int i = 2; i <= N; i++) {
            double divider = a.get(i-1) * alpha.get(i-1) + c.get(i-1);

            alpha.add(-1 * (b.get(i-1) / divider));
            betta.add((g.get(i-1) - a.get(i-1) * betta.get(i-1)) / divider);
        }

        X.add((g.get(N) - a.get(N) * betta.get(N)) / (a.get(N) * alpha.get(N) + c.get(N)));
        for (int i = (N - 1), j = 0; i >= 0; i--, j++) {
            X.add(alpha.get(i+1) * X.get(j) + betta.get(i+1));
        }
        Collections.reverse(X);
        return X;
    }

    private boolean checkStabs(){
        for (int i = 0; i <= N; i++) {
            if (i == 0 && (Math.abs(c.get(i)) < Math.abs(b.get(i)))){
                return false;
            } else if (i == N && (Math.abs(c.get(i)) < Math.abs(a.get(i)))) {
                return false;
            } else if (i > 0 && i < N && Math.abs(c.get(i)) < (Math.abs(a.get(i)) + Math.abs(b.get(i)))){
                return false;
            }
        }
        return true;
    }


    private void buildScheme(){
        setGrid();
        setMatrix();
    }
    private void setGrid(){
        double step = (double)(data.params.Rr - data.params.Rl) / (double) N;
        for (int i = 0; i <= N; i++) {
            r.add(data.params.Rl + (i * step)); //set r[i]
            if (data.launchType != LaunchType.CONST) {
                setVar(r.get(i)); //set (k,q,f)[i]
            } else {
                setVar();
            }
        }

        for (int i = 0; i <= N; i++) {
        if (i == 0) { //set h[i] for i = 1,2,..,N
            h.add(null);

        } else { //push dw
            h.add(r.get(i) - r.get(i - 1));
        }
    }

        for (int i = 0; i <= N; i++) {
            if (i == 0) {
                rMinus.add(null);
                rPlus.add((r.get(i) + r.get(i + 1)) / 2);
                kMinus.add(null);
                kPlus.add((k.get(i) + k.get(i + 1)) / 2);
            } else if (i == N) {
                rMinus.add((r.get(i) + r.get(i - 1)) / 2);
                rPlus.add(null);
                kMinus.add((k.get(i) + k.get(i - 1)) / 2);
                kPlus.add(null);
            } else {
                rMinus.add((r.get(i) + r.get(i - 1)) / 2);
                rPlus.add((r.get(i) + r.get(i + 1)) / 2);
                kMinus.add((k.get(i) + k.get(i - 1)) / 2);
                kPlus.add((k.get(i) + k.get(i + 1)) / 2);
            }
        }

        for (int i = 0; i <= N; i++) {
            if(i == 0){
                h2.add(h.get(i+1)/2);
            } else if (i == N){
                h2.add(h.get(i)/2);
            } else {
                h2.add((h.get(i) + h.get(i+1))/2);
            }
        }
    }
    private void setMatrix(){
        for (int i = 0; i <= N; i++) {
            double type0 = (r.get(i) * r.get(i));
            double type3 = (h2.get(i) * r.get(i) * r.get(i) * q.get(i));
            double type4 = (h2.get(i) * r.get(i) * r.get(i) * f.get(i));
            double type5 = ( r.get(i) * r.get(i) * data.params.ksi2);
            if(i == 0){
                a.add(null);
                c.add((double)1);
                b.add((double)0);
                g.add(data.params.vi1);

            } else if (i == 1) {
                double type1 = (rPlus.get(i) * rPlus.get(i) * kPlus.get(i) / h.get(i + 1));
                double type2 = (rMinus.get(i) * rMinus.get(i) * kMinus.get(i) / h.get(i));

                a.add((double) 0);
                c.add(type1 + type2 + type3);
                b.add(-1 * type1);
                g.add(type4 - data.params.vi1 * -1 * type2);

            } else if (i == N) {
                double type2 = (rMinus.get(i) * rMinus.get(i) * kMinus.get(i) / h.get(i));

                a.add(-1 * type2);
                c.add(type2 + type3 + type5);
                b.add(null);
                g.add(type4 + type0 * data.params.vi2);

            } else {
                double type1 = (rPlus.get(i) * rPlus.get(i) * kPlus.get(i) / h.get(i + 1));
                double type2 = (rMinus.get(i) * rMinus.get(i) * kMinus.get(i) / h.get(i));

                a.add(-1 * type2);
                c.add(type1 + type2 + type3);
                b.add(-1 * type1);
                g.add(type4);
            }
        }
    }

    private void setVar(double ri){
        data.setData(ri);

        k.add(data.params.k);
        q.add(data.params.q);
        f.add(data.params.f);
    }

    private void setVar(){
        k.add(data.params.k);
        q.add(data.params.q);
        f.add(data.params.f);
    }

    /*private void kMinusAdd(double riMinus){
        data.setData(riMinus);
        kMinus.add(data.params.k);

    }
    private void kPlusAdd(double riPlus){
        data.setData(riPlus);
        kPlus.add(data.params.k);
    }*/


    public ArrayList<Double> getR(){
        return r;
    }

    public double getRi(int index){
        return r.get(index);
    }


};