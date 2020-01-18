package com.erinccibil;

import java.io.File;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
	// write your code here
        FileReader fr = new FileReader();

        for (int i = 0; i < fr.getProblems().size(); i++) {
            QKP problem = new QKP(fr.getProblems().get(i),1, 100, 0.05f, 3, 0.8f, 500, PenaltyType.REPAIR, new Random(i));
            problem.run();
        }
        System.out.println("Done");

    }
}
