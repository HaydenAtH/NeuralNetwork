package Percival53.NeuralNetworkManager;

import java.util.ArrayList;
import java.util.Random;

public class Neuron {
    public float weight;

    NeuralNetwork net;

    public Layer layer;

    public ArrayList<Connection> connections;

    int[][] connectionsCoords;

    public float input;
    private float output;

    public boolean connected = false;

    public Neuron(Layer layer, NeuralNetwork newNet){
        Random rndm = new Random();
        weight = rndm.nextFloat(3);
        this.connections = new ArrayList<Connection>();

        this.layer = layer;
        this.net = newNet;
    }

    public void mutate(float min, float max){
        Random rndm = new Random();

        this.weight += rndm.nextFloat(min, max);
        int x = rndm.nextInt(30);

        if (x == 1){
            Neuron n = layer.getNeurons().get(rndm.nextInt(layer.getNeurons().size()));
            swapRandomConnection(n);
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
        this.connectionsCoords = nOther.connectionsCoords;
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

        if (this.connections.size() != 0){
            for (Connection c : connections){
                c.endpoint.setInput(output);
            }
        }else{
            net.addOutput(this.output);
        }
    }


    public ArrayList<Connection> getConnections() {
        return connections;
    }

    public void setConnections(ArrayList<Connection> connections) {
        this.connections = connections;
    }

    public void addConnection(Connection c){
        connections.add(c);
    }

    public void addRandomConnection(){
        int x = 0;

        for (int i = 0; i < net.layers.length; i++){
            if (net.layers[i] == layer){
                x = i;
            }
        }

        Random rndm = new Random();

        if (x + 1 < net.layers.length){
            Layer nextLayer = net.layers[x + 1];

            int r;
            while (true){
                r = rndm.nextInt(getNextLayer().getNeurons().size());

                if (nextLayer.getNeurons().get(r) != null){
                    boolean b = false;
                    for (Connection c : connections){
                        if (getNextLayer().getNeurons().size() < layer.getNeurons().size()){
                            if (c.endpoint == (Neuron) nextLayer.getNeurons().get(r)){
                                b = true;
                            }
                        }else{
                            if (c.endpoint == (Neuron) nextLayer.getNeurons().get(r) || ((Neuron) nextLayer.getNeurons().get(r)).connected == true){
                                b = true;
                            }
                        }
                    }

                    if (b){
                        continue;
                    }
                }else{
                    continue;
                }


                break;
            }


            if (net.layers[x + 1].getNeurons().size() < 1){
                System.out.println("Empty Layer");
            }

            Connection c = new Connection();
            c.origin = this;
            c.endpoint = (Neuron) nextLayer.getNeurons().get(r);
            c.endpoint.connected = true;

            addConnection(c);
        }
    }

    public Layer getNextLayer(){
        int i = 0;
        for (Layer l : net.layers){
            if (l == layer){
                return net.layers[i];
            }

            i++;
        }

        return null;
    }

    public void swapRandomConnection(Neuron nf){
        Random r = new Random();

        Connection tC = this.connections.get(r.nextInt(this.connections.size()));
        Connection oC = nf.connections.get(r.nextInt(nf.connections.size()));

        oC.endpoint = tC.endpoint;
        tC.endpoint = oC.endpoint;
    }
}
