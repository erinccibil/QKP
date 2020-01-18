package com.erinccibil;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class QKP {
    private ProblemInstance problemInstance;
    private float mutationRate;
    private float crossOverRate;
    private int populationSize;
    private BooleanRepresentation[] population;
    private ArrayList<BooleanRepresentation> newGenerationBasket;
    private PenaltyType penaltyType;
    private int penalty;
    private BooleanRepresentation best;
    private int crossOverPoints;
    private int maxGeneration;
    private long maxDuration;
    private Random random;

    public QKP(ProblemInstance problemInstance, int maxDurationInMinutes, int maxGeneration, float mutationRate, int crossOverPoints, float crossOverRate, int populationSize, PenaltyType penaltyType, Random random) {
        this.problemInstance = problemInstance;
        this.mutationRate = mutationRate;
        this.crossOverRate = crossOverRate;
        this.populationSize = populationSize;
        this.penaltyType = penaltyType;
        this.random = random;
        this.crossOverPoints = crossOverPoints > problemInstance.getElementSize() ? problemInstance.getElementSize() - 1
                : crossOverPoints < 0 ? 1 : crossOverPoints;
        if (PenaltyType.PENALTY == penaltyType) {
            this.penalty = 0;
        }
        this.population = new BooleanRepresentation[populationSize];
        this.newGenerationBasket = new ArrayList<>();
        this.maxGeneration = maxGeneration;
        this.maxDuration = new Integer(maxDurationInMinutes * 60 * 1000).longValue();
        initialize();
    }

    public void run(){
        long start = System.currentTimeMillis();
        long end;
        int generationCnt = 0;
        while (true) {
            newGenerationBasket.clear();
            for (int i = 0; i < population.length/2; i++) {
                if (crossOverRate > random.nextFloat()) {
                    applyCrossOver(population[2 * i], population[2 * i + 1]);
                }
            }
            for (BooleanRepresentation parent: population) {
                newGenerationBasket.add(parent);
            }
            for (BooleanRepresentation ind :newGenerationBasket) {
                if (mutationRate > random.nextFloat()) {
                    applyMutation(ind);
                }
                ind.setFitnessValue(calculateFitness(ind.getRepresentation()));
            }
            Collections.sort(newGenerationBasket);
            for (int i = 0; i < population.length; i++) {
                population[i] = newGenerationBasket.get(i);
            }
            if (population[0].getFitnessValue() > best.getFitnessValue()) {
                best = population[0].clone();
            //    System.out.printf("Problem Name = %s, Best = %d, Optimum = %d, Generation = %d.\n", problemInstance.getName(), best.getFitnessValue(), problemInstance.getOptimum(), generationCnt);

            }
            generationCnt++;
            end = System.currentTimeMillis();
            if (end - start > maxDuration) {
                System.out.printf("Problem Name = %s, Best = %d, Optimum = %d, Generation = %d, Termination Reason : Reached Maximum\n", problemInstance.getName(), best.getFitnessValue(), problemInstance.getOptimum(), generationCnt);
                break;
            } else if (generationCnt >= maxGeneration) {
                System.out.printf("Problem Name = %s, Best = %d, Optimum = %d, Generation = %d, Termination Reason : Maximum Generation\n", problemInstance.getName(), best.getFitnessValue(), problemInstance.getOptimum(), generationCnt);
                break;
            } else if (best.getFitnessValue() >= problemInstance.getOptimum()) {
                System.out.printf("Problem Name = %s, Best = %d, Optimum = %d, Generation = %d, Termination Reason : Optimum solution Found\n", problemInstance.getName(), best.getFitnessValue(), problemInstance.getOptimum(), generationCnt);
                break;
            }
        }
    }

    private void applyCrossOver(BooleanRepresentation ind1, BooleanRepresentation ind2){
        int randRange = problemInstance.getElementSize() / crossOverPoints;
        int crossOverOfset = 0;
        int crossOverLimit;
        int crossOverPosition;
        BooleanRepresentation[] parents = {ind1, ind2};
        boolean[][] childs = {new boolean[problemInstance.getElementSize()],new boolean[problemInstance.getElementSize()]};
        for (int i = 0; i < crossOverPoints; i++) {
            crossOverOfset += i*randRange;
            crossOverPosition = (i + 1 == crossOverPoints)
                    ? crossOverOfset + random.nextInt(problemInstance.getElementSize() - crossOverOfset)
                    : crossOverOfset + random.nextInt(randRange);
            crossOverLimit = (i + 1 == crossOverPoints)
                    ? problemInstance.getElementSize()
                    : randRange * (i + 1);

            for (int j = crossOverOfset; j < crossOverLimit; j++) {
                if (j == crossOverPosition) {
                    parents = swapParents(parents);
                }
                for (int k = 0; k < parents.length; k++) {
                    childs[k][j] = parents[k].getRepresentation()[j];
                }
            }
        }
        if (crossOverPoints % 2 != 0) {
            parents = swapParents(parents);
        }
        for (int i = 0; i < childs.length; i++) {
            newGenerationBasket.add(new BooleanRepresentation(childs[i]));
        }
    }

    private BooleanRepresentation[] swapParents (BooleanRepresentation[] parents) {
        BooleanRepresentation temp = parents[0];
        parents[0] = parents[1];
        parents[1] = temp;
        return parents;
    }

    private void applyMutation(BooleanRepresentation individual) {
        int position = random.nextInt(individual.getRepresentation().length);
        individual.getRepresentation()[position] = individual.getRepresentation()[position] = true ? false : true;
    }

    private void initialize() {
        problemInstance.setOptimum(calculateFitness(problemInstance.getOptimumElements()));
        for (int i = 0; i < populationSize; i++) {
            BooleanRepresentation individual = new BooleanRepresentation(problemInstance.getElementSize(), random);
            individual.setFitnessValue(calculateFitness(individual.getRepresentation()));
            population[i] = individual;
        }
        Arrays.sort(population);
        best = population[0].clone();
    }

    private int calculateFitness(boolean[] representationElements){
        int result = 0;
        for (int i = 0; i < problemInstance.getElementSize(); i++) {
            int[] positionElements = problemInstance.getElements().get(i);
            if ( i - 1 < 0) {
                for (int j = 0; j <positionElements.length; j++) {
                    if (representationElements[i]) {
                        result += positionElements[j];
                    }
                }
            } else {
                for (int j = 0; j < positionElements.length; j++) {
                    if(representationElements[i-1+j] && representationElements[i+j]) {
                        result += positionElements[j];
                    }
                }
            }
        }
        boolean feasible = isfeasible(representationElements);
        return feasible ? result : applyRepairType(representationElements);
    }

    private boolean isfeasible(boolean[] representationElements) {
        int result = 0;
        for (int i = 0; i < representationElements.length; i++) {
            if ( representationElements[i]) {
                result += problemInstance.getControlElements().get(i);
            }
        }
        return problemInstance.getMaxCap() > result ? true : false;
    }


    private int applyRepairType(boolean[] representationElements){
        if (penaltyType == PenaltyType.DEATH_PENALTY) {
            return 0;
        } else if (penaltyType == PenaltyType.PENALTY) {
            return (penalty / 2);
        } else {
            boolean isRepresentationChanged = false;
            while (!isRepresentationChanged){
                int randomPosition = random.nextInt(representationElements.length);
                if (representationElements[randomPosition]) {
                    representationElements[randomPosition] = !representationElements[randomPosition];
                    isRepresentationChanged = !isRepresentationChanged;
                }
            }
            return calculateFitness(representationElements);
        }
    }

}
