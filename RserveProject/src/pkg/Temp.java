package pkg;
 
 import org.rosuda.REngine.REXPMismatchException;
 import org.rosuda.REngine.Rserve.RConnection;
 import org.rosuda.REngine.Rserve.RserveException;
 
 
 // Don't forget to: http://codophile.com/2015/05/02/how-to-integrate-r-with-java-using-rserve/
 //
 // in R:
 // load the lib -> 	library(Rserve)
 // start the server ->	Rserve()
 public class Temp {
 
     public static void main(String a[]) {
         RConnection connection = null;
 
         try {
             /* Create a connection to Rserve instance running
              * on default port 6311
              */
             connection = new RConnection();
 
             String vector = "c(1,2,3,4)";
             connection.eval("meanVal=mean(" + vector + ")");
             double mean = connection.eval("meanVal").asDouble();
             System.out.println("The mean of given vector is=" + mean);
         } catch (RserveException e) {
             e.printStackTrace();
         } catch (REXPMismatchException e) {
             e.printStackTrace();
         }finally{
             connection.close();
         }
     }
 }