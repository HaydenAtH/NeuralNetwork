package Percival53.NeuralNetworkManager;

import java.util.ArrayList;

public class Layer {
    ArrayList<Neuron> neurons = new ArrayList<Neuron>();

    public Layer(){

    }

    public ArrayList<Neuron> getNeurons() {
        return neurons;
    }

    public void addNeuron(Neuron n){
        neurons.add(n);
    }
}
