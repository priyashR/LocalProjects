package pkg;

import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;
// Don't forget to: http://codophile.com/2015/05/02/how-to-integrate-r-with-java-using-rserve/
//
// in R:
// load the lib -> 	library(Rserve)
// start the server ->	Rserve()
public class TestRScript {

    public static void main(String a[]) {
        RConnection connection = null;

        try {
            /* Create a connection to Rserve instance running on default port
             * 6311
             */
            connection = new RConnection();

            /* Note four slashes (\\\\) in the path */
            //connection.eval("source('C:/Users/priyash.ramawthar/Dropbox/trader/R_scripts/ImportProcessExport.r')");
            
            
            
            connection.eval("inFile='C:/Users/priyash.ramawthar/Dropbox/trader/R_scripts/spy_historical_data.txt'");
            //connection.eval("outFile='C:/Users/priyash.ramawthar/Dropbox/trader/R_scripts/spy_with_indicators.csv'");
            connection.eval("outFile='C:/Users/priyash.ramawthar/Dropbox/trader/spy_with_indicators.csv'");
            
            connection.eval("source('C:/Users/priyash.ramawthar/Dropbox/trader/R_scripts/ImportProcessExportParams.r')");
            
            
            //int num1=10;
            //int num2=20;
            //int sum=connection.eval("myAdd("+num1+","+num2+")").asInteger();
            //System.out.println("The sum is=" + sum);
        } catch (RserveException e) {
            e.printStackTrace();
        }
    }
}