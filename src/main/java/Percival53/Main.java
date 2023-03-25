package Percival53;

import Percival53.NeuralNetworkManager.Dataset;
import Percival53.NeuralNetworkManager.Manager;
import Percival53.NeuralNetworkManager.NeuralNetwork;

import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();
        networkTrainer();
    }

    public static void networkTrainer(){
        Manager manager = new Manager();
        Dataset trainingSet = new Dataset();


        Random rndm = new Random();

        for (int i = 0; i < 500000; i++){
            float n1 = rndm.nextFloat(40);
            float n2 = rndm.nextFloat(40);

            trainingSet.addSet(new float[] {n1, n2}, new float[] {n1 + n2});
        }

        // Meant to simulate a simple addition problem
        NeuralNetwork trainedModel = manager.simulate(trainingSet, 1000, 50, 10);
        trainedModel.printNeurons();

        // Verify Model
        int t = 0;
        int c = 0;

        for (int i = 0; i < 10000; i++){
            Float a = rndm.nextFloat(40);
            Float b = rndm.nextFloat(40);
            Float h = a + b;

            float[] inp = {a, b};
            if (trainedModel.FeedInput(new float[] {a, b})[0] == h){
                c++;
            }
            t++;
        }

        System.out.println("Network Scored: " + c + "/" + t);

        while (true){
            Scanner scnr = new Scanner(System.in);

            System.out.println(" ");
            System.out.println("Model Trained, Provide new Input to Test \n Type 'TRN' to continue training");
            System.out.println("Provide two numbers");

            float num1 = scnr.nextFloat();
            float num2 = scnr.nextFloat();

            float expectedResult = num1 + num2;

            System.out.println("Result: " + trainedModel.FeedInput(new float[] {num1, num2})[0]);
        }
    }
}