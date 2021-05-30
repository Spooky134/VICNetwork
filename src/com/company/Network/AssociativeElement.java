package com.company.Network;

public class AssociativeElement {
    public int lamdaA = 1;
    public int lamdaB = 1;
    public int lamdaC = 1;

    public int status = 1;

    public void updateStatus(int[] image,int[] connection) {
        int sum = 0;
        for(int i=0; i<image.length; i++){
           sum += image[i]*connection[i];
        }
        if(sum >=0){
            status = 1;
        }
        else
            status = 0;
    }
}
