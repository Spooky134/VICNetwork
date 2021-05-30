package com.company;
import com.company.Network.Perceptron;

import java.io.IOException;


public class Main {

    public static void main(String[] args) throws IOException {
        Perceptron perceptron = new Perceptron();

        perceptron.trainingNetwork("src/com/company/dataset_training",85);
        perceptron.controlNetwork("src/com/company/dataset_control");

    }
}
