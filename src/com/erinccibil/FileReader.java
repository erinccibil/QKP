package com.erinccibil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class FileReader {
    private ArrayList<ProblemInstance> problems;

    public FileReader(){
        this.problems = new ArrayList<>();
        try {
            readProblemData();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readProblemData() throws IOException {
        File folder = new File(new StringBuilder(System.getProperty("user.dir")).append(System.getProperty("file.separator")).append("data").toString());
        ArrayList<File> files = traverseFolders(folder);
        java.io.FileReader reader;
        BufferedReader bufferedReader;
        String line;
        for (File file: files){
            reader = new java.io.FileReader(file);
            bufferedReader = new BufferedReader(reader);
            String name = bufferedReader.readLine();
            int elementSize = Integer.parseInt(bufferedReader.readLine());
            ArrayList<int[]> problemElements = new ArrayList<>();
            int[] elements;
            for (int i = 0; i < elementSize; i++) {
                elements = Arrays.stream(bufferedReader.readLine().trim().split("( )+")).mapToInt(Integer::parseInt).toArray();
                problemElements.add(elements);
            }
            bufferedReader.readLine();
            bufferedReader.readLine();
            int maxCap = Integer.parseInt(bufferedReader.readLine());
            ArrayList<Integer> controlElements = new ArrayList<Integer>();
            int[] controlData = (Arrays.stream(bufferedReader.readLine().trim().split("( )+")).mapToInt(Integer::parseInt).toArray());
            reader.close();
            for (Integer element: controlData) {
                controlElements.add(element);
            }

            String solutionPath = file.getPath().substring(0, file.getPath().length()-4);
            solutionPath = solutionPath + "s.txt";
            //String solutionPath = new StringBuilder(file.getParentFile().getPath()).append(System.getProperty("file.separator")).append(name).append("s.txt").toString();
            reader = new java.io.FileReader(new File(solutionPath));
            bufferedReader = new BufferedReader(reader);
            ArrayList<Integer> solutionIndexes = new ArrayList<>();
            while((line = bufferedReader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    solutionIndexes.add(Integer.parseInt(line.trim()));
                }
            }
            boolean[] solutionArray = new boolean[elementSize];
            Arrays.fill(solutionArray, false);
            for (int i = 0; i < solutionIndexes.size(); i++) {
                solutionArray[solutionIndexes.get(i)] = true;
            }

            reader.close();
            ProblemInstance problemInstance = new ProblemInstance(name, elementSize, problemElements,maxCap,controlElements, solutionArray);
            this.problems.add(problemInstance);
        }
    }

    public ArrayList<File> traverseFolders(File folder){
        ArrayList<File> files = new ArrayList<>();
        for (File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                traverseFolders(fileEntry);
            } else {
                if (!fileEntry.getName().endsWith("s.txt")) {
                    File peoblemFile = fileEntry.getAbsoluteFile();
                    files.add(fileEntry);
                }
            }
        }
        return files;
    }

    public ArrayList<ProblemInstance> getProblems() {
        return problems;
    }
}
