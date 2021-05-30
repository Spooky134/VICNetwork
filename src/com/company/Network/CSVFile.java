package com.company.Network;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class CSVFile {

    public CSVFile() {
    }

    public void writeRows(ArrayList<ArrayList<Integer>> array,String path) throws IOException {
        FileWriter writer = new FileWriter(path, false);
        ArrayList<String> buffer = new ArrayList<>();

        StringBuilder minibuffer = new StringBuilder();
        for(int i=0; i<array.size(); i++){
            for(int j=0; j<array.get(0).size(); j++) {
                minibuffer.append(array.get(i).get(j)).append(",");
            }
            buffer.add(String.valueOf(minibuffer));
            minibuffer = new StringBuilder();
        }
        for (String el: buffer){
            writer.write(el);
            writer.write("\n");
        }
        writer.close();
    }

    public void writeRows(int[][] array,String path) throws IOException {
        FileWriter writer = new FileWriter(path, false);
        ArrayList<String> buffer = new ArrayList<>();

        StringBuilder minibuffer = new StringBuilder();
        for(int i=0; i<array.length; i++){
            for(int j=0; j<array[0].length; j++) {
                minibuffer.append(array[i][j]).append(",");
            }
            buffer.add(String.valueOf(minibuffer));
            minibuffer = new StringBuilder();
        }
        for (String el: buffer){
            writer.write(el);
            writer.write("\n");
        }
        writer.close();
    }

    public ArrayList<ArrayList<Integer>> readRows(String path) throws IOException {
        FileReader reader = new FileReader(path);
        Scanner scanner = new Scanner(reader);
        ArrayList<ArrayList<Integer>> data = new ArrayList<>();
        ArrayList<Integer> values = new ArrayList<>();
        String[] str;
        while (scanner.hasNextLine()) {
            str = scanner.nextLine().split(",");
            for(int i=0; i< str.length; i++){
                values.add(Integer.parseInt(str[i]));
            }
            data.add(values);
            values = new ArrayList<>();
        }
        reader.close();
        return data;
    }
}
