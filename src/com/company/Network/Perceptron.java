package com.company.Network;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;


public class Perceptron {
    public Random rnd = new Random();

    public AssociativeElement[] associativeElements;
    public int[][] connections;
    public int percentTraing = 0;

    public Map<String,Integer> summators = new HashMap<>();
    public Map<String,String> answer = new HashMap<>();

    public ArrayList<String> paths = new ArrayList<>();

    public ArrayList<ArrayList<Integer>> lamdaA = new ArrayList<>();
    public ArrayList<ArrayList<Integer>> lamdaB = new ArrayList<>();
    public ArrayList<ArrayList<Integer>> lamdaC = new ArrayList<>();

    public Perceptron() {
        connections = new int[1225][450];
        createTable();

        associativeElements = new AssociativeElement[450];

        for (int i = 0; i < 450; i++) {
            associativeElements[i] = new AssociativeElement();
        }

        summators.put("A", 0);
        summators.put("B", 0);
        summators.put("C", 0);

        answer.put("A", "R");
        answer.put("B", "Б");
        answer.put("C", "Omega");

        saveTactLamda();
    }

    //создает таблицу подключений
    public void createTable(){
        //все нули
        for(int i=0; i<1225; i++){
            for(int j=0; j<450; j++){
                connections[i][j] = 0;
            }
        }
        int[] arr = {1,-1};
        //1 сектор
//        for(int i=0; i<225; i++){
//            connections[i][rnd.nextInt(450-255-1)+225] = arr[rnd.nextInt(2)];
//        }
        //2 сектор
        for(int i=225; i<1000; i++){
            connections[i][rnd.nextInt(450-1)] = arr[rnd.nextInt(2)];
        }
        //3 сектор
//        for(int i=1000; i<1225; i++){
//            connections[i][rnd.nextInt(225-1)] = arr[rnd.nextInt(2)];
//        }
        //диагональ 1 сектор
        int j = 0;
        for(int i=225-1; i>-1; i--){
            connections[i][j] = arr[rnd.nextInt(2)];
            j +=1;
        }
        //диагональ 2 сектор
        j=225;
        for(int i=1225-1; i>1000-1; i--){
            connections[i][j] = arr[rnd.nextInt(2)];
            j +=1;
        }
    }

    //активирует ассоциативные элементы
    public void updateStatusAssociativeElement(int[] image){
        int[] buffer = new int[1225];
        for(int i=0; i<450; i++){
            for(int j=0; j<1225; j++){
                buffer[j] = connections[j][i];
            }
            associativeElements[i].updateStatus(image,buffer);
        }
    }

    //суммирует элементы
    public void sumElements(){
        int sumA = 0;
        int sumB = 0;
        int sumC = 0;
        for(int i=0; i<450; i++){
            sumA += associativeElements[i].lamdaA * associativeElements[i].status;
            sumB += associativeElements[i].lamdaB * associativeElements[i].status;
            sumC += associativeElements[i].lamdaC * associativeElements[i].status;
        }

        summators.put("A",sumA);
        summators.put("B",sumB);
        summators.put("C",sumC);
    }

    //обновляет значения весов/лямд
    public void updateValueLamda(String pathToPicture){
        String key = getMaxValueDictionaryKey(summators);

        if(!(pathToPicture.contains(key))){
            switch (key) {
                case "A":
                    for (int i = 0; i < 450; i++) {
                        if (associativeElements[i].status == 1) {
                            associativeElements[i].lamdaA -= 1;
                            associativeElements[i].lamdaC += 1;
                            associativeElements[i].lamdaB += 1;
                        }
                    }
                    break;
                case "B":
                    for (int i = 0; i < 450; i++) {
                        if (associativeElements[i].status == 1) {
                            associativeElements[i].lamdaB -= 1;
                            associativeElements[i].lamdaC += 1;
                            associativeElements[i].lamdaA += 1;
                        }
                    }
                    break;
                case "C":
                    for (int i = 0; i < 450; i++) {
                        if (associativeElements[i].status == 1) {
                            associativeElements[i].lamdaC -= 1;
                            associativeElements[i].lamdaA += 1;
                            associativeElements[i].lamdaB += 1;
                        }
                    }
                    break;
            }
            //подумать
            if(pathToPicture.contains("A")){
                for (int i = 0; i < 450; i++) {
                    if (associativeElements[i].status == 1) {
                        associativeElements[i].lamdaA += 1;
                    }
                }
            }
            else if(pathToPicture.contains("B")){
                for (int i = 0; i < 450; i++) {
                    if (associativeElements[i].status == 1) {
                        associativeElements[i].lamdaB += 1;
                    }
                }
            }
            else if(pathToPicture.contains("C")){
                for (int i = 0; i < 450; i++) {
                    if (associativeElements[i].status == 1) {
                        associativeElements[i].lamdaC += 1;
                    }
                }
            }
            saveTactLamda();
        }
    }

    //тренеровка сети
    public void trainingNetwork(String pathToDataSet,int percent) throws IOException {
        int correctAnswer = 0;
        int countAnswer = 0;

        createPathsPictures(pathToDataSet);
        while (true) {
            Collections.shuffle(paths);
            for(int i=0; i<paths.size(); i++){
                updateStatusAssociativeElement(createPictureArray(paths.get(i)));
                sumElements();
                updateValueLamda(paths.get(i));
                String key = getMaxValueDictionaryKey(summators);
                System.out.println(key);
                System.out.println(summators);
                System.out.println(paths.get(i));

                if(paths.get(i).contains(key))
                    correctAnswer += 1;
                countAnswer+=1;
            }

            percentTraining(correctAnswer,countAnswer);
            if (percentTraing >=  percent){
                break;
            }
            System.out.println(percentTraing);
        }
        saveNetwork();
        System.out.println(percentTraing);
    }

    //проверка сети
    public void controlNetwork(String pathToControlDataSet) throws IOException {
        loadNetwork();
        createPathsPictures(pathToControlDataSet);
        String key;
        for(int i=0; i<paths.size(); i++){
            updateStatusAssociativeElement(createPictureArray(paths.get(i)));
            sumElements();
            key = getMaxValueDictionaryKey(summators);
            System.out.println(answer.get(key)+'-'+summators);
            System.out.println(paths.get(i));
        }
    }

    //записывает лямды каждый такт их изменения
    public void saveTactLamda(){
        ArrayList<Integer> buffer = new ArrayList<>();
        for(int i=0; i<450; i++){
            buffer.add(associativeElements[i].lamdaA);
        }
        lamdaA.add(buffer);
        buffer = new ArrayList<>();
        for(int i=0; i<450; i++){
            buffer.add(associativeElements[i].lamdaB);
        }
        lamdaB.add(buffer);
        buffer = new ArrayList<>();
        for(int i=0; i<450; i++){
            buffer.add(associativeElements[i].lamdaC);
        }
        lamdaC.add(buffer);
    }

    //сохраняет сеть
    public void saveNetwork() throws IOException {
        CSVFile csvWrite = new CSVFile();
        csvWrite.writeRows(lamdaA,"src/com/company/data/lamdaAClass.csv");
        csvWrite.writeRows(lamdaB,"src/com/company/data/lamdaBClass.csv");
        csvWrite.writeRows(lamdaC,"src/com/company/data/lamdaCClass.csv");
        csvWrite.writeRows(connections,"src/com/company/data/connection.csv");
    }

    //загружает сеть
    public void loadNetwork() throws IOException {
        ArrayList<ArrayList<Integer>> buffer;
        CSVFile csvReader = new CSVFile();

        buffer = csvReader.readRows("src/com/company/data/connection.csv");
        updateNetworkConnectionsData(buffer);

        buffer = csvReader.readRows("src/com/company/data/lamdaAClass.csv");
        updateNetworkLamdasAClassData(buffer);

        buffer = csvReader.readRows("src/com/company/data/lamdaBClass.csv");
        updateNetworkLamdasBClassData(buffer);

        buffer = csvReader.readRows("src/com/company/data/lamdaCClass.csv");
        updateNetworkLamdasCClassData(buffer);
    }

    //возвращает изображение в виде массива
    public int[] createPictureArray(String pathToPicture) throws IOException {
        File file = new File(pathToPicture);

        BufferedImage image = ImageIO.read(file);

        int blue;
        int red;
        int green;

        int[][] buffer = new int[image.getHeight()][image.getWidth()];
        int[] result = new int[image.getHeight()*image.getWidth()];

        for(int i=0; i<image.getHeight(); i++){
            for(int j=0; j<image.getWidth(); j++){
                Color color = new Color(image.getRGB(i, j));

                blue = color.getBlue();
                red = color.getRed();
                green = color.getGreen();

                if((blue+red+green)/3>0)
                    buffer[i][j]=0;
                else
                    buffer[i][j]=1;
            }
        }
        int k=0;
        for(int i=0; i<image.getHeight(); i++){
            for (int j=0; j<image.getWidth(); j++){
                result[k] = buffer[i][j];
                k++;
            }
        }

        return result;
    }

    //создает список путей к изображениям
    public void createPathsPictures(String directory){
        paths.clear();
        File dir = new File(directory);
        if(dir.isDirectory())
        {
            for(String item : dir.list()) {
                if (!item.contains("DS"))
                    paths.add(directory+'/'+item);
            }
        }
    }

    //находит максимальное значение в словаре
    private String getMaxValueDictionaryKey(Map<String, Integer> map){
        Optional<Map.Entry<String, Integer>> maxEntry = map.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue()
                );

        return maxEntry.get().getKey();
    }

    //вычисляет процент тренерованности сети
    public void percentTraining(int correctAnswer,int countAnswer){
        percentTraing = (correctAnswer*100)/countAnswer;
    }

    //обновление значений таблицы подключений после загрузки сети
    public void updateNetworkConnectionsData(ArrayList<ArrayList<Integer>> connect){
        for (int i=0; i<connections.length; i++){
            for (int j=0; j<connections[i].length; j++){
                connections[i][j] = connect.get(i).get(j);
            }
        }
    }

    //обновление значений лямд A класса после подгрузки сети
    public void updateNetworkLamdasAClassData(ArrayList<ArrayList<Integer>> lamdasA){
        for(int i=0; i<associativeElements.length; i++){
            associativeElements[i].lamdaA = lamdasA.get(lamdasA.size()-1).get(i);
        }
    }

    //обновление значений лямд B класса после подгрузки сети
    public void updateNetworkLamdasBClassData(ArrayList<ArrayList<Integer>> lamdasB){
        for(int i=0; i<associativeElements.length; i++){
            associativeElements[i].lamdaB = lamdasB.get(lamdasB.size()-1).get(i);
        }
    }

    //обновление значений лямд C класса после подгрузки сети
    public void updateNetworkLamdasCClassData(ArrayList<ArrayList<Integer>> lamdasC){
        for(int i=0; i<associativeElements.length; i++){
            associativeElements[i].lamdaC = lamdasC.get(lamdasC.size()-1).get(i);
        }
    }
}

