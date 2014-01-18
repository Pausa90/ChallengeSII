package it.sii.challenge.valand.logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Statistic {
        
        private long startTime;
        
        public Statistic(long startTime){
                this.startTime = startTime;
        }
        
        public void printTemporalInformation(long endTime){
                long toSeconds = 1000000000;
                int minutes = (int)((endTime - this.startTime)/(toSeconds*60));
                int seconds = (int)(((endTime - this.startTime)/toSeconds)-(minutes*60));
                System.out.println("The program took "+ minutes + " minutes and " + seconds + " seconds"); 
        }
        
        public void printMAE(File output, File trueValues, int defaultValueCount){
                try{
                        BufferedReader outputReader = new BufferedReader(new FileReader(output));                
                        BufferedReader trueReader = new BufferedReader(new FileReader(trueValues));
                        String outLine = outputReader.readLine();
                        String trueLine = trueReader.readLine();
                        int n = 0;
                        int tmp = 0;
                        int trueValue = 0;
                        int out = 0;
                        int[] values = {0,0,0,0,0};
                        while (outLine != null){
                                out = Integer.parseInt(outLine);
                                trueValue = Integer.parseInt(trueLine);                                
                                tmp += Math.abs(out-trueValue);        
                                values[out-1]++;
                                n++;
                                outLine = outputReader.readLine();
                                trueLine = trueReader.readLine();
                        }                        
                        outputReader.close();
                        trueReader.close();
                        
                        System.out.println("n = " + n);
                        System.out.print("Number of ");
                        for (int i=0; i<5; i++){
                                System.out.print(i+1 + ":" + values[i] + " ");
                        }
                        System.out.println();
                        
                        double mae = tmp/(double)n;
                        
                        if (n==0) System.out.println("non ci sono file da confrontare");
                        else        System.out.println("MAE:" + mae);
                        System.out.println("Default count:"+defaultValueCount);
                        
                } catch (IOException e){
                        e.printStackTrace();
                }
        }

}