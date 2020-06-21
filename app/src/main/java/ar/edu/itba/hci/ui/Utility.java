package ar.edu.itba.hci.ui;

public class Utility {
    public static String capitalizeFirstLetter(String str){
        // capitalize first letter
        String output = str.substring(0, 1).toUpperCase() + str.substring(1);

// print the string
        return output;
    }
}
