public class Data {

    public Data(LaunchType launchType) {
        this.launchType = launchType;
        params.setBasic(DataSet.getBasic());
        if(launchType == LaunchType.CONST){
            setData();
        }
    }

    LaunchType launchType;
    Params params = new Params();


    public void setData(Double r) {
        params.setKQF(DataSet.getParams(launchType, r));
    }

    public void setData() {
        setData(null);
    }

    public static class Params{
        public Params(){
        }

        public Params(double k, double q, double f){
            this.k = k;
            this.q = q;
            this.f = f;
        }

        public Params(int Rl, int Rr, double vi1, double vi2, double ksi2){
            this.Rl = Rl;
            this.Rr = Rr;
            this.vi1 = vi1;
            this.vi2 = vi2;
            this.ksi2 = ksi2;

        }

        public Params(Params other){
            setBasic(other);
        }

        int Rl, Rr;
        double k, q, f;
        double vi1, vi2, ksi2;

        private void setBasic(Params other) {
            this.Rl = other.Rl;
            this.Rr = other.Rr;
            this.vi1 = other.vi1;
            this.vi2 = other.vi2;
            this.ksi2 = other.ksi2;
        }

        private void setKQF(Params other) {
            this.k = other.k;
            this.q = other.q;
            this.f = other.f;
        }

        public int getSize() {
            return (Rr - Rl);
        }
    }

}
