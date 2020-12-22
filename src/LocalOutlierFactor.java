import java.util.Scanner;

public class LocalOutlierFactor {
    //Memberi index pada proximity matrix
    static void putIndex(int[][] theIdx) {
        for (int i = 0; i < theIdx.length; i++) {
            for (int j = 0; j < theIdx[i].length; j++) {
                theIdx[i][j] = j;
            }
        }
    }
    //Menampilkan Proximity Matrix
    static void proxMatrix(float[] a, float[] b, int n, int k, double[][] tMtx, int[][] theIdx) {
        System.out.println("\nProximity Matrix");
        for (int i = 0; i < n; i++) { 
            for (int j = 0; j < n; j++) {
                tMtx[i][j] = Math.sqrt(Math.pow(a[i] - a[j], 2) + Math.pow(b[i] - b[j], 2));
                System.out.print(String.format("["+ (theIdx[i][j]+1) + "] " + "%.7f", tMtx[i][j]) + "\t");
            }
            System.out.println();
        }
        sortMatrixandIdx(tMtx, theIdx);
    }
    //Melakukan sorting pada proximity matrix dan index masing-masing per baris
    static void sortMatrixandIdx(double[][] tMtx, int[][] theIdx) {
        for (int i = 0; i < tMtx.length; i++) {
            for (int j = 0; j < tMtx[i].length; j++) {
                for (int l = 0; l < tMtx[i].length - j - 1; l++) {
                    if (tMtx[i][l] > tMtx[i][l + 1]) {
                        double t = tMtx[i][l];
                        tMtx[i][l] = tMtx[i][l + 1];
                        tMtx[i][l + 1] = t;

                        int tmp = theIdx[i][l];
                        theIdx[i][l] = theIdx[i][l+1];
                        theIdx[i][l+1] = tmp;
                    }
                }
            }
        }
        System.out.println("\nArray yang Diurutkan dan Indeksnya");
        for (int i = 0; i < tMtx.length; i++) {
            for (int j = 0; j < tMtx[i].length; j++){
                if(j == 0 && tMtx[i][j] == 0){
                    System.out.print("");
                }
                else System.out.print(String.format("["+ (theIdx[i][j]+1) + "] " + "%.7f", tMtx[i][j]) + "\t");
            }
            System.out.println();
        }
    }
    //Menghitung Average Density
    void AverageDensity(double[][] tMtx, int k, double[]averageDens) {
        double t = 0;
        for (int i = 0; i < tMtx.length; i++) {
            for (int j = 0; j <= k; j++) {
                t = t + tMtx[i][j];
                if (j == k)
                    averageDens[i] = k / t;
            }
            t = 0;
        }
        System.out.println("\nAverage Density");
        for (int i = 0; i < averageDens.length; i++) {
            System.out.println(String.format((i + 1) + ".\t" + "%.7f", averageDens[i]));
        }
    }
    //Menghitung Average Relative Density dan menampilkan kesimpulan outlier
    void AverageRelativeDensity(int[][] theIdx, int k, double[] averageDens, double[] averageRdens){
        for (int i=0; i<averageRdens.length; i++){
            double t = 0;
            for (int j=1; j<=k; j++){
                t = t + averageDens[theIdx[i][j]];
                if (j == k)
                    averageRdens[i] = (t/k)/averageDens[i];
            }
            t = 0;
        }
    }

    private static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        LocalOutlierFactor localOutlierFactor = new LocalOutlierFactor();
        System.out.println("===Program Outlier Factor===");
        System.out.print("Masukkan jumlah data Anda\t: ");
        int n = input.nextInt();
        System.out.print("Masukkan k\t\t\t: ");
        int k = input.nextInt();
        System.out.print("Masukkan ambang batas\t\t: ");
        double treshold = input.nextDouble();
        float[] a = new float[n];
        float[] b = new float[n];
        double[][] tMtx = new double[n][n];
        int[][] theIdx = new int[n][n];
        double[] averageDens = new double[n];
        double[] averageRdens = new double[n];
        System.out.println("Inputkan data X dan Y: ");
        for (int i = 0; i < n; i++) {
            a[i] = input.nextFloat();
            b[i] = input.nextFloat();
        }
        putIndex(theIdx);
        proxMatrix(a, b, n, k, tMtx, theIdx);
        localOutlierFactor.AverageDensity(tMtx, k, averageDens);
        localOutlierFactor.AverageRelativeDensity(theIdx, k, averageDens, averageRdens);
        System.out.println("\nAverage Relative Density");
        for (int i = 0; i < averageRdens.length; i++) {
            System.out.print(String.format((i + 1) + ".\t" + "%.7f", averageRdens[i]));
            if(averageRdens[i] > treshold)
                System.out.print(" (Termasuk Outlier tinggi)\n");
            else System.out.print(" (Termasuk Outlier rendah atau Inlier)\n");
        }
    }
}
