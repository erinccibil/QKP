package com.erinccibil;

import java.util.Random;

public class BooleanRepresentation implements Comparable<BooleanRepresentation> {
    private boolean[] representation;
    private int fitnessValue;

    public BooleanRepresentation(int size, Random random) {
        this.representation = new boolean[size];
        for (int i = 0; i < size; i++) {
            this.representation[i] = random.nextBoolean();
        }
    }

    public BooleanRepresentation(boolean[] dataArray){
        this.representation = dataArray;
    }

    public boolean[] getRepresentation() {
        return representation;
    }

    public void setRepresentation(boolean[] representation) {
        this.representation = representation;
    }

    public int getFitnessValue() {
        return fitnessValue;
    }

    public void setFitnessValue(int fitneesValue) {
        this.fitnessValue = fitneesValue;
    }

    public BooleanRepresentation clone(){
        boolean[] clone = new boolean[this.representation.length];
        for (int i = 0; i < this.representation.length; i++) {
            clone[i] = new Boolean(this.representation[i]);
        }
        BooleanRepresentation clonedRepresentation = new BooleanRepresentation(clone);
        clonedRepresentation.setFitnessValue(this.fitnessValue);
        return clonedRepresentation;
    }

    @Override
    public int compareTo(BooleanRepresentation o) {
        return o.getFitnessValue() < this.fitnessValue ? -1 : o.getFitnessValue() > this.fitnessValue ? 1 : 0;
    }
}
