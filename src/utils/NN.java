package utils;

import reader.ArrfReader;

public class NN {

    public static byte[] getMissingValues(double[] input) {
        byte[] res = new byte[input.length];    // it is initialized with all zeroes
        for(int i = 0; i < input.length; i++)
            if(input[i] == ArrfReader.NULLVAL)
                res[i] = 1;
        return res;
    }
}
