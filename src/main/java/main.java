import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class main {
    final static LaunchType launchType = LaunchType.CONST;
    final static int N = 4;
    final static int stepFactor = 2;
    final static int START_N = 4;
    final static int MAX_N = 2048;

    public static void main(String[] args) {

        Data data = new Data(launchType);
        makeTest(data);
        //showOne(data);
    }


    public static void makeTest(Data data){
        ArrayList<Double> schemeSolutions;
        ArrayList<Double> factSolutions;
        double schemeSol;
        double factSol;
        double maxErr;

        ArrayList<Double> fSols = new ArrayList<>();
        ArrayList<Double> lSols = new ArrayList<>();
        ArrayList<Double> Errs = new ArrayList<>();

        for (int n = START_N; n <= MAX_N ; n*=stepFactor) {
            Scheme scheme = new Scheme(data, n);
            schemeSolutions = new ArrayList<>(scheme.shuttle());
            factSolutions = DataSet.getFactSolutions(launchType, n, scheme);
            maxErr = Math.abs(schemeSolutions.get(0) - factSolutions.get(0));
            schemeSol = schemeSolutions.get(0);
            factSol = factSolutions.get(0);
            for (int i = 1; i < factSolutions.size(); i++) {
                double currentErr = Math.abs(schemeSolutions.get(i) - factSolutions.get(i));
                if (currentErr > maxErr){
                    maxErr = currentErr;
                    schemeSol = schemeSolutions.get(i);
                    factSol = factSolutions.get(i);
                }
            }
            System.out.println(n + " || " + schemeSol + " (V <-> U) " + factSol + " || Err-> " + maxErr + " ||" );
            fSols.add(factSol);
            lSols.add(schemeSol);
            Errs.add(maxErr);
        }

        try(FileWriter file = new FileWriter("src/main/resources/out.txt")) {
            file.write("N:\n");
            for (int n = START_N; n <= MAX_N ; n*=stepFactor){
                file.write(n + "\n");
            }
            file.write("Fact Solutions:\n");
            for (int i = 0; i < fSols.size() ; i++){
                file.write(fSols.get(i) + "\n");
            }
            file.write("Scheme Solutions:\n");
            for (int i = 0; i < lSols.size() ; i++){
                file.write(lSols.get(i) + "\n");
            }
            file.write("Errors:\n");
            for (int i = 0; i < Errs.size() ; i++){
                file.write(Errs.get(i) + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void showOne(Data data) {
        Scheme scheme = new Scheme(data, N);
        ArrayList<Double> schemeSolutions = new ArrayList<>(scheme.shuttle());
        ArrayList<Double> factSolutions = DataSet.getFactSolutions(launchType, N, scheme);

        for (int i = 0; i <= N; i++) {
            System.out.println("N = " + i + " || " + schemeSolutions.get(i) + "  (V <-> U)  " + factSolutions.get(i) + ", where r[i] = " + scheme.getRi(i));
        }
    }

}
