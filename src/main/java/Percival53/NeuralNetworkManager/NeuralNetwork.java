package Percival53.NeuralNetworkManager;

import java.util.Random;

public class NeuralNetwork {
    float[] inputs;
    Layer[] layers;
    Neuron[][] neurons;

    Layer mainLayer = new Layer();

    int width;
    float[] outputs;

    public int outputIndex;

    int height;

    int activeLayer;

    float fitness;

    float endpoint = 0;

    public NeuralNetwork(int inputAmounts, int layerAmount, int outputAmount, int height){
        inputs = new float[inputAmounts];
        width = layerAmount;
        this.height = height;
        layers = new Layer[layerAmount];

        neurons = new Neuron[layerAmount][height];

        InitLayers(layerAmount);
        outputs = new float[outputAmount + 1];
        activeLayer = 0;
        InitNeurons();
        verifyLayers();
        randomizeNeuronConnections();

    }

    public NeuralNetwork(NeuralNetwork nNetwork){
        //Just a deep copy
        this.inputs = new float[nNetwork.inputs.length];
        this.width = nNetwork.width;
        this.height = nNetwork.height;
        layers = new Layer[nNetwork.layers.length];
        neurons = new Neuron[nNetwork.layers.length][nNetwork.height];

        this.outputs = new float[nNetwork.outputs.length];
        activeLayer = 0;
        InitLayers(nNetwork.layers.length);
        InitNeurons(nNetwork);
        rollMutate();
    }
    public void verifyLayers(){
        for (int i = 0; i < layers.length; i++){
            if (layers[i].getNeurons().size() != height){
                populateLayer(layers[i], i);
            }
        }
    }

    public void randomizeNeuronConnections(){
        for (int i = 0; i < width; i++){
            for (int z = 0; z < height; z++){
                layers[i].getNeurons().get(z).randomizeConnection();
            }
        }
    }

    public void populateLayer(Layer l, int x){
        for (int z = 0; z < height; z++){
            Neuron n = new Neuron(l, this);

            l.addNeuron(n);
            neurons[x][z] = n;
        }
    }

    public void rollMutate(){
        Random rndm = new Random();
        for (Neuron[] nArr : neurons){
            for (Neuron n : nArr){
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
        Object[] neuronsF = layers[0].getNeurons().toArray();
        this.inputs = inputs;

        for (int i = 0; i < inputs.length; i++){
            Neuron n = (Neuron) neuronsF[0];
            n.setInput(inputs[i]);
        }

        return this.outputs;
    }

    public void InitNeurons(){
        for (int i = 0; i < width; i++){
            for (int z = 0; z < height; z++){
                Neuron n = new Neuron(layers[i], this);

                layers[i].addNeuron(n);
                neurons[i][z] = n;
            }
        }
    }

    public void InitLayers(int layerAmount){
        for (int i = 0; i < layerAmount; i++){
            layers[i] = new Layer();
        }
    }

    public void InitNeurons(NeuralNetwork neuralNetwork){
        for (int i = 0; i < layers.length; i++){
            for (int z = 0; z < height; z++){
                    Random rndm = new Random();
                    Neuron n = new Neuron(layers[i], this).copy(neuralNetwork.neurons[i][z]);

                    layers[i].addNeuron(n);
                    neurons[i][z] = n;
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
        for (int i = 0; i < width; i++){
            System.out.println(" ");
            for(int z = 0; z < height; z++){
                System.out.print(neurons[i][z].getWeight() + "  ");
            }
        }
    }
}
