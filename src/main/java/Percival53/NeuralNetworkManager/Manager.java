package Percival53.NeuralNetworkManager;

public class Manager {
    NeuralNetwork net;
    int population = 10;

    int generationCount = 5;


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

        net = new NeuralNetwork(set.getRandomInput().length, 30, 1, 30);
        fitNet = net;

        populateNets();
        for (int i = 0; i < generationCount; i++) {
            counter++;

            for (NeuralNetwork n : activeNets) {
                float[] inputSet = set.getRandomInput();

                float[] output = n.FeedInput(inputSet);
                n.addFitness(1f - (Math.abs(set.getExpectedOutput(inputSet)[0] - fitNet.endpoint)));
            }
            fitNet = findMostFit(activeNets);

            if (debug){
                System.out.println("------------");
                System.out.println("Generation: " + i + " | " + generationCount);
                System.out.println("Output of most Fit Net: " + fitNet.outputs[0] + "\n Fitness: " + fitNet.fitness);
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

        if (mostFitNet == null){
            System.out.println("Null FitNet");
            mostFitNet = new NeuralNetwork(2,10, 1, 5);
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
