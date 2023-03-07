package Percival53.NeuralNetworkManager;

public class Manager {
    NeuralNetwork net;
    int population = 10;

    int generationCount = 5;

    int problemCount = 1;

    int counter;


    NeuralNetwork[] activeNets;
    NeuralNetwork fitNet;

    public NeuralNetwork simulate(Dataset set, int population, int generations){
        counter = 0;
        boolean debug = true;
        this.population = population;
        this.generationCount = generations;
        this.activeNets = new NeuralNetwork[population + 1];

        System.out.print("Training Model");
        int z = 0;

        net = new NeuralNetwork(set.getRandomInput().length, 50, 1, 50);
        fitNet = net;

        populateNets();
        for (int i = 0; i < generationCount; i++) {
            counter++;

            float[] fitnessTracker = new float[problemCount];

            float expectedOutput = -1;
            for (NeuralNetwork n : activeNets) {
                for (int p = 0; p < problemCount; p++){
                    float[] inputSet = set.getRandomInput();

                    float[] output = n.FeedInput(inputSet);
                    fitnessTracker[p] = 1f - (Math.abs(set.getExpectedOutput(inputSet)[0] - output[0]));

                }

                if (problemCount > 1){
                    float average = 0;
                    for (float a : fitnessTracker){
                        average += a;
                    }

                    average /= problemCount;

                    n.setFitness(average);
                }else{
                    n.setFitness(fitnessTracker[0]);
                }



            }
            fitNet = findMostFit(activeNets);

            expectedOutput = set.getExpectedOutput(fitNet.inputs)[0];

            if (debug){
                System.out.println("------------");
                System.out.println("Generation: " + i + " | " + generationCount);
                System.out.println("Output of most Fit Net: " + fitNet.outputs[0] + " | " + expectedOutput + "\nFitness: " + fitNet.fitness + " From: " + problemCount + " problems");
            }
            populateNets(fitNet);

        }

        return fitNet;
    }

    public NeuralNetwork findMostFit(NeuralNetwork[] nets){
        float highestFitness = -999999999;

        NeuralNetwork mostFitNet = null;

        for (NeuralNetwork network : activeNets){
            if (network.getFitness() > highestFitness){
                highestFitness = network.getFitness();
                mostFitNet = network;
            }
        }

        return mostFitNet;
    }
    public void populateNets(){
        for (int i = 0; i < population + 1; i++){
            activeNets[i] = new NeuralNetwork(net);
        }
    }

    public void populateNets(NeuralNetwork modelNetwork){
        for (int i = 0; i < population; i++){
            activeNets[i] = new NeuralNetwork(modelNetwork);
        }
    }
}
