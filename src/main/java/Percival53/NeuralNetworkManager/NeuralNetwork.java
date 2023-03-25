package Percival53.NeuralNetworkManager;

import java.util.ArrayList;
import java.util.Random;

public class NeuralNetwork {
    float[] inputs;
    Layer[] layers;

    Layer mainLayer = new Layer();

    int width;
    float[] outputs;

    public int outputIndex;

    int height;

    int activeLayer;

    float fitness; // Used for network culling

    float endpoint = 0;

    int[] shape = {0, 2, 4, 2, 0}; // Proportion of network shape

    /**
     * Constructor for Neural Network
     * @param inputAmounts How many inputs the network will be receiving
     * @param layerAmount How many layers the neural network will have [replaced soon]
     * @param outputAmount How many outputs the network will provide
     * @param height How tall the network should be in terms of neurons
     */
    public NeuralNetwork(int inputAmounts, int layerAmount, int outputAmount, int height){
        shape[0] = inputAmounts;
        shape[shape.length - 1] = outputAmount;

        inputs = new float[inputAmounts];
        width = shape.length;
        this.height = height;
        layers = new Layer[shape.length];

        InitLayers(shape.length);
        outputs = new float[outputAmount + 1];
        activeLayer = 0;
        InitNeurons();
        randomizeNeuronConnections();

    }

    public NeuralNetwork(NeuralNetwork nNetwork){
        //Just a deep copy
        this.inputs = new float[nNetwork.inputs.length];
        this.width = nNetwork.width;
        this.height = nNetwork.height;
        layers = new Layer[nNetwork.layers.length];

        this.outputs = new float[nNetwork.outputs.length];
        activeLayer = 0;
        InitLayers(nNetwork.layers.length);
        InitNeurons(nNetwork);
        rollMutate();
    }

    public void randomizeNeuronConnections(){

        for (int i = 0; i < width; i++){
            if (i + 1 < width){
                while (!checkForDisconnect(layers[i], layers[i + 1])) {
                    for (int z = 0; z < shape[i] * height; z++) {
                        layers[i].getNeurons().get(z).addRandomConnection();
                    }
                }
            }
        }
    }

    public void populateLayer(Layer l, int x){
        for (int z = 0; z < shape[x] * height; z++){
            Neuron n = new Neuron(l, this);

            l.addNeuron(n);
        }
    }

    public void rollMutate(){
        Random rndm = new Random();
        for (Layer lf : layers){
            for (Neuron n : lf.getNeurons()){
                float f = rndm.nextFloat(10);

                if (f == 1){
                    n.mutate(-3f, 3f);
                }
            }

        }

    }

    public float[] FeedInput(float[] inputs){
        endpoint = 0;
        Neuron[] sentNeurons = new Neuron[layers[0].getNeurons().size()];

        while (inputs.length > layers[0].getNeurons().size()){
            Neuron newN = new Neuron(layers[0], this);
            layers[0].addNeuron(newN);
        }

        Object[] neuronsF = layers[0].getNeurons().toArray();
        this.inputs = inputs;

        for (int i = 0; i < inputs.length; i++){
            Neuron n = (Neuron) neuronsF[i];
            n.addInput(inputs[i]);
        }

        return this.outputs;
    }

    public void InitNeurons(){
        for (int i = 0; i < width; i++){
            for (int z = 0; z < shape[i] * height; z++){
                Neuron n = new Neuron(layers[i], this);

                layers[i].addNeuron(n);
            }
        }
    }

    public void InitLayers(int layerAmount){
        for (int i = 0; i < layerAmount; i++){
            layers[i] = new Layer();
        }
    }

    public void InitNeurons(NeuralNetwork neuralNetwork){
        shape[0] = neuralNetwork.shape[0];
        shape[shape.length - 1] = neuralNetwork.shape[neuralNetwork.shape.length - 1];

        for (int i = 0; i < shape.length; i++){
            for (int z = 0; z < shape[i] * height; z++){
                Random rndm = new Random();
                Neuron n = new Neuron(layers[i], this).copy(neuralNetwork.layers[i].getNeurons().get(z));

                layers[i].addNeuron(n);
            }
        }
    }

    public void addFitness(float mod){
        fitness += mod;
    }

    public void setFitness(float nFitness){
        fitness = nFitness;
    }

    public float getFitness(){
        return this.fitness;
    }

    public void addOutput(float f){
        endpoint += f;
        outputs[0] = f;
        outputIndex++;
    }

    public void printNeurons(){
        for (int i = 0; i < shape.length; i++){
            System.out.println(" ");
            for(int z = 0; z < shape[i] * height; z++){
                System.out.print(layers[i].getNeurons().get(z).getWeight() + "  ");
            }
        }
    }

    public int topHeight(){
        int x = 0;

        for (int f : shape){
            if (f > x){
                x = f;
            }
        }

        return x;
    }

    public boolean checkForDisconnect(Layer l1, Layer l2){
        ArrayList<Neuron> nArr = new ArrayList<Neuron>();

        int x = 0;
        for (Neuron n : l1.getNeurons()){
            for (Connection c : n.getConnections()){
                if (!nArr.contains(c.endpoint)){
                    nArr.add(c.endpoint);
                    x++;
                }
            }
        }

        int y = 0;
        for (Neuron n : l2.getNeurons()){
            if (n != null){
                y++;
            }
        }

        return (y == x);
    }
}
