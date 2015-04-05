package com.atlasrider.ffcg;

import org.graphstream.algorithm.APSP;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.Path;
import org.graphstream.graph.implementations.SingleGraph;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

    /*
                !                !
                !                !
                !                !
                !                !
    LEFT SIDE   !    RIVER       !    RIGHT SIDE
                !                !
                !                !
                !                !
                !                !
        0               1               2
     */

//  FARMER, FOX, CHICKEN, GRAIN


public class AI {

    private State _startState;
    private State _endState;

    private int _numberOfStates = 3;
    private int _numberOfObjects = 4;
    private int _numberOfPossibilities = (int)Math.pow(_numberOfStates, _numberOfObjects);

    public AI() {
        init();
    }

    public void run() {
        Graph graph = generateDecisionTree();

        Path solution = getSolution(graph);

        System.out.println("Solution: " + solution.toString());

        for(Node node : solution.getNodeSet()) {
            new State(node.getId()).print();

            //new Scanner(System.in).next();
        }

        displayGraph(graph);
    }

    private Graph generateDecisionTree() {
        Graph graph = new SingleGraph("Farmer, Fo, Chicken and the Grain");

        State state;
        int validStates = 0;

        // Iterate through all posibilities
        for(int i=0;i<_numberOfPossibilities;i++) {

            state = new State(addLeadingZeros(Integer.toString(i, _numberOfStates)));
            //System.out.println(state.toString());

            // Only deal with valid states
            if(state.isValid()) {
                validStates++;

                // Add node
                Node node = graph.getNode(state.toString());
                if(node == null) node = graph.addNode(state.toString());

                State nextStep;
                Node nextNode;
                // Iterate through possible next steps
                for(int j=0;j<_numberOfPossibilities;j++) {

                    nextStep = new State(addLeadingZeros(Integer.toString(j, _numberOfStates)));

                    // Only deal with valid next steps
                    if(nextStep.isValid() && state.isValidNextStep(nextStep)) {

                        // Add node
                        nextNode = graph.getNode(nextStep.toString());
                        if(nextNode == null) nextNode = graph.addNode(nextStep.toString());

                        // Add Edge
                        if(!node.hasEdgeBetween(nextNode)) {
                            Edge edge = graph.addEdge(state.toString() + "->" + nextStep.toString(), node, nextNode);
                            edge.addAttribute("ui.label", state.beingMoved(nextStep));
                        }

                    }
                }
            }
        }

        System.out.println("Number of possible states: " + _numberOfPossibilities);
        System.out.println("Number of valid states: " + validStates);

        return graph;
    }

    private Path getSolution(Graph graph) {
        APSP apsp = new APSP();
        apsp.init(graph); // registering apsp as a sink for the graph
        apsp.setDirected(false); // undirected graph
        //apsp.setWeightAttributeName("weight"); // ensure that the attribute name used is "weight"

        apsp.compute(); // the method that actually computes shortest paths

        APSP.APSPInfo info = graph.getNode(_startState.toString()).getAttribute(APSP.APSPInfo.ATTRIBUTE_NAME);
        return info.getShortestPathTo(_endState.toString());
    }


    private void displayGraph(Graph graph) {
        for (Node node : graph) {
            node.addAttribute("ui.label", node.getId());
        }

        graph.setStrict(false);
        graph.setAutoCreate(true);
        graph.display();
    }

    private String addLeadingZeros(String number) {
        while(number.length() < 4) number = "0" + number;
        return number;
    }

    private void init() {
        _startState = new State(0, 0, 0, 0);
        _endState = new State(2, 2, 2, 2);
    }
}
