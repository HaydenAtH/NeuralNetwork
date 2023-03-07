package Percival53.NeuralNetworkManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Dataset {
    HashMap<float[], float[]> sets = new HashMap<float[], float[]>();

    public Dataset(){

    }


    public void addSet(float[] input, float[] output){
        sets.put(input, output);
    }

    public float[] getExpectedOutput(float[] input){
        return sets.get(input);
    }

    public float[] getRandomInput(){
        Random rndm = new Random();
        Object[] keysetArr = sets.keySet().toArray();
        return (float[]) keysetArr[rndm.nextInt(keysetArr.length)];
    }
}
