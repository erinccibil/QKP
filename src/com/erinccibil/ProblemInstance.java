package com.erinccibil;

import java.util.ArrayList;

public class ProblemInstance {
    private String name;
    private int elementSize;
    private ArrayList<int[]> elements;
    private int maxCap;
    private ArrayList<Integer> controlElements;
    private int optimum;
    private boolean[] optimumElements;

    public ProblemInstance(String name, int elementSize, ArrayList<int[]> elements, int maxCap, ArrayList<Integer> controlElements, boolean[] optimumElements){
        this.name = name;
        this.elementSize = elementSize;
        this.elements = elements;
        this.maxCap = maxCap;
        this.controlElements = controlElements;
        this.optimumElements = optimumElements;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getElementSize() {
        return elementSize;
    }

    public void setElementSize(int elementSize) {
        this.elementSize = elementSize;
    }

    public ArrayList<int[]> getElements() {
        return elements;
    }

    public void setElements(ArrayList<int[]> elements) {
        this.elements = elements;
    }

    public int getMaxCap() {
        return maxCap;
    }

    public void setMaxCap(int maxCap) {
        this.maxCap = maxCap;
    }

    public ArrayList<Integer> getControlElements() {
        return controlElements;
    }

    public void setControlElements(ArrayList<Integer> controlElements) {
        this.controlElements = controlElements;
    }

    public int getOptimum() {
        return optimum;
    }

    public void setOptimum(int optimum) {
        this.optimum = optimum;
    }

    public boolean[] getOptimumElements() {
        return optimumElements;
    }

    public void setOptimumElements(boolean[] optimumElements) {
        this.optimumElements = optimumElements;
    }
}
