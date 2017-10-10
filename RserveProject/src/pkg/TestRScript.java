package pkg;

import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

public class TestRScript {

    public static void main(String a[]) {
        RConnection connection = null;

        try {
            /* Create a connection to Rserve instance running on default port
             * 6311
             */
            connection = new RConnection();

            /* Note four slashes (\\\\) in the path */
            connection.eval("source('C:/Users/priyash.ramawthar/Dropbox/trader/R_scripts/ImportProcessExport.r')");
            //int num1=10;
            //int num2=20;
            //int sum=connection.eval("myAdd("+num1+","+num2+")").asInteger();
            //System.out.println("The sum is=" + sum);
        } catch (RserveException e) {
            e.printStackTrace();
        }
    }
}