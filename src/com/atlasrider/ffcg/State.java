package com.atlasrider.ffcg;

/**
 * Created by billdwyer on 4/2/15.
 */
public class State {
    public int Farmer;
    public int Fox;
    public int Chicken;
    public int Grain;

    public State(int farmer, int fox, int chicken, int grain) {
        Farmer = farmer;
        Fox = fox;
        Chicken = chicken;
        Grain = grain;
    }

    public State(String state) {
        Farmer = Character.getNumericValue(state.toCharArray()[0]);
        Fox = Character.getNumericValue(state.toCharArray()[1]);
        Chicken = Character.getNumericValue(state.toCharArray()[2]);
        Grain = Character.getNumericValue(state.toCharArray()[3]);
    }

    public boolean isValid() {

        // Only 2 Objects can be in the boat at once
        int boatCount = 0;
        boatCount = ((Farmer == 1) ? 1 : 0)
                + ((Fox == 1) ? 1 : 0)
                + ((Chicken == 1) ? 1 : 0)
                + ((Grain == 1) ? 1 : 0);
        if(boatCount > 2) return false;

        // Chicken and fox can't be left alone
        if(Chicken == Fox && Chicken != Farmer) return false;

        // Chicken and grain can't be left alone
        if(Chicken == Grain && Chicken != Farmer) return false;

        // If anything is in the boat, the farmer must be there
        if((Grain == 1 || Fox == 1 || Chicken == 1) && Farmer != 1) return false;

        // Default to true
        return true;
    }

    public boolean isValidNextStep(State nextState) {

        // Objects can only move one spot at a time
        if(Math.abs(Farmer - nextState.Farmer) > 1
            || Math.abs(Fox - nextState.Fox) > 1
            || Math.abs(Chicken - nextState.Chicken) > 1
            || Math.abs(Grain - nextState.Grain) > 1)
            return false;

        // Only the farmer can move the objects.
        if(Fox != nextState.Fox && (Farmer != Fox || nextState.Farmer != nextState.Fox)) return false;
        if(Chicken != nextState.Chicken && (Farmer != Chicken || nextState.Farmer != nextState.Chicken)) return false;
        if(Grain != nextState.Grain && (Farmer != Grain || nextState.Farmer != nextState.Grain)) return false;

        return true;
    }

    public String beingMoved(State nextState) {
        if(Fox != nextState.Fox) return "Fox";
        if(Chicken != nextState.Chicken) return "Chicken";
        if(Grain != nextState.Grain) return "Grain";

        return "";
    }

    public String toString() {
        return Integer.toString(Farmer)
                + Integer.toString(Fox)
                + Integer.toString(Chicken)
                + Integer.toString(Grain);
    }

    public void print() {
        String line = "  %s  |  %s  |  %s  \n";
        String blank = "        ";

        System.out.printf(line, blank, blank, blank);

        printLine(line, "Farmer", Farmer);
        printLine(line, "Fox", Fox);
        printLine(line, "Chicken", Chicken);
        printLine(line, "Grain", Grain);

        System.out.printf(line, blank, blank, blank);
        System.out.printf("--------------------------------------\n");
    }

    private void printLine(String line, String value, int location) {
        String blank = "        ";

        switch(location) {
            case 0:
                System.out.printf(line, String.format("%-8s", value), blank, blank);
                break;
            case 1:
                System.out.printf(line, blank, String.format("%8s", value), blank);
                break;
            case 2:
                System.out.printf(line, blank, blank, String.format("%8s", value));
                break;
        }
    }




}
