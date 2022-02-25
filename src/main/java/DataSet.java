import java.util.ArrayList;

public class DataSet {
    private static Data.Params constBasic = new Data.Params(1, 2, 1, 0, 0);
    private static Data.Params linearBasic = new Data.Params(1, 2, 1, 5, 2);
    private static Data.Params non_linearBasic = new Data.Params(1, 2, 1, 24, 2);

    private static Data.Params returnParams(LaunchType launchType, double r){
        return switch (launchType){
            case CONST -> new Data.Params(1, 1 ,1);
            case LINEAR -> new Data.Params(1, r, r * r - 2 / r );
            case NON_LINEAR -> new Data.Params(r * r, r * r, r * r * (r * r - 10));
        };
    }

    public static Data.Params getBasic(){
        return new Data.Params(constBasic);
    }

    public static Data.Params getParams(LaunchType launchType, Double rD){
        double r = 0;
        if(launchType == LaunchType.CONST && rD != null) {
            throw new IllegalArgumentException("If launch type == CONST, you should not initialize dependency 'r'");
        } if(launchType != LaunchType.CONST && rD == null) {
            throw new IllegalArgumentException("If launch type != CONST, you should initialize dependency 'r'");
        } if(rD != null) {
            r = rD.doubleValue();
        }

        return returnParams(launchType, r);

    }

    public static ArrayList<Double> getFactSolutions(LaunchType launchType, int N, Scheme scheme){
        return fillArray(launchType, N, scheme);
    }

    private static ArrayList<Double> fillArray(LaunchType launchType, int N, Scheme scheme){
        ArrayList<Double> u = new ArrayList<Double>();
        ArrayList<Double> r = scheme.getR();
        switch (launchType){
            case CONST:
                for (int i = 0; i <= N; i++) {
                    u.add((double)1);
                }
                break;
            case LINEAR:
                for (int i = 0; i <= N; i++) {
                    u.add(r.get(i));
                }
                break;
            case NON_LINEAR:
                for (int i = 0; i <= N; i++) {
                    u.add(r.get(i) * r.get(i));
                }
                break;
        }
        return u;
    }

}
