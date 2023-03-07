package Percival53.NeuralNetworkManager;

import java.util.Random;

public class Neuron {
    public float weight;

    NeuralNetwork net;

    public Layer layer;

    Connection connection;

    int[][] connectionCoords;

    public float input;
    private float output;

    public Neuron(Layer layer, NeuralNetwork newNet){
        Random rndm = new Random();
        weight = rndm.nextFloat(3);
        this.connection = new Connection();

        connection.origin = this;
        this.layer = layer;
        this.net = newNet;
    }

    public void randomizeConnection(){
        int x = 0;

        for (int i = 0; i < net.layers.length; i++){
            if (net.layers[i] == layer){
                x = i;
            }
        }

        Random rndm = new Random();

        if (x + 1 < net.layers.length){
            Layer nextLayer = net.layers[x + 1];

            int r = rndm.nextInt(net.height);

            if (net.layers[x + 1].getNeurons().size() < 1){
                System.out.println("Empty Layer");
            }

            connection.endpoint = (Neuron) nextLayer.getNeurons().get(r);
        }else{
            connection.endpoint = null;
        }
    }

    public void mutate(float min, float max){
        Random rndm = new Random();

        this.weight += rndm.nextFloat(min, max);
        int x = rndm.nextInt(50);

        if (x == 1){
            randomizeConnection();
        }
    }

    public Neuron setLayer(Layer newLayer){
        this.layer = newLayer;
        return this;
    }

    public Neuron setNetwork(NeuralNetwork net){
        this.net = net;
        return this;
    }

    public Neuron copy(Neuron nOther){
        this.weight = nOther.getWeight();
        this.connectionCoords = nOther.connectionCoords;
        return this;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public void setInput(float input){
        this.input = input;
        this.output = input * weight;

        if (this.connection.endpoint != null){
            this.connection.endpoint.setInput(output);
        }else{
            net.addOutput(this.output);
        }
    }
}
