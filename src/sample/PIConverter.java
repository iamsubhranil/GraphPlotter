/*
    Created By : iamsubhranil
    Date : 4/3/17
    Time : 5:40 PM
    Package : sample
    Project : GraphPlotter
*/
package sample;

import javafx.util.StringConverter;

public class PIConverter extends StringConverter<Number> {
    private static double coef = 0;

    @Override
    public String toString(Number number) {
        coef += 0.5;
        int [] points = takeForm(coef);
        return points[0]==1?"":points[0]+"PI/"+points[1];
    }

    public static void main(String [] args){
        int [] po = takeForm(1.5);
        System.out.println("Final result : "+po[0]+"/"+po[1]);
    }

    private static int[] takeForm(double point){
        System.out.println(point);
        int comp = (int)(point*100);
        int div = 100;
        for(int i = 2; (i<=comp && i<=div/2);i+=1){
            if(comp%i==0 && div%i==0){
                comp = comp/i;
                div = div/i;
                i = 1;
            }
        }
        return new int[]{comp, div};
    }

    @Override
    public Number fromString(String s) {
        return Double.parseDouble(s.replace("-PI/",""));
    }
}
