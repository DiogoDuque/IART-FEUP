package utils;

import java.util.ArrayList;

public class Converter {

    public static double nanosecondsToSeconds(double nanoseconds)
    {
        return nanoseconds/Math.pow(10, 9);
    }

    public static ArrayList<Integer> readArrayFromString(String byteString){

        String trimmedKey = byteString.substring(1, byteString.length()-1);
        String[] tokens = trimmedKey.split(", ");

        //find MVs positions
        ArrayList<Integer> integerArrayList = new ArrayList<Integer>();
        for(int i = 0; i < tokens.length; i++){
                integerArrayList.add(Integer.parseInt(tokens[i]));
        }

        return integerArrayList;
    }

}
