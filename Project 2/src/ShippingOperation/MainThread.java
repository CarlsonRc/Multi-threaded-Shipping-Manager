package ShippingOperation;

import java.io.*;
import java.util.Scanner;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.StreamSupport;

public class MainThread {

    private static int MAX = 10;
    private static int numStations = 0;
    private static int [] workLoad = new int[MAX];


    public static void main(String[] args) throws IOException {

        System.out.println("\n>>>> Processing Simulation Data <<<<\n");
        scanFile();
        runSimulation();
    }


    public static void scanFile() throws FileNotFoundException {

        int i = 0;
        File file = new File("config.txt");
        Scanner scan = new Scanner(file);

        while (scan.hasNextLine()){
            if(i == 0){// lets us assign first line in .txt to numStations
                numStations = scan.nextInt();
            }
            workLoad[i++] = scan.nextInt();
        }
        System.out.println("Number of Stations: " + numStations);
        for (int j =0;j<numStations;j++){
            System.out.println("Workloads for Station " + j + ": " + workLoad[j]);
        }
        scan.close();// close file
    }

    public static void runSimulation(){

        Conveyor input;
        Conveyor output;

        Conveyor conveyor[] = new Conveyor[numStations];// create array of conveyors equal to amount of stations

        System.out.println("\n>>>> Initializing Conveyors <<<<\n");

        for (int i=0; i<numStations; i++){// loop for initializing conveyors
            //create conveyor objects
            conveyor[i] = new Conveyor(i);

            System.out.println("Conveyor " + conveyor[i].conveyorNum + " Ready");
        }

        System.out.println("\n>>>> Initializing Stations <<<<\n");

        ExecutorService threadExecutor = Executors.newFixedThreadPool(MAX);


        for (int i=0; i<numStations; i++){// loop for initializing stations and their parameters

            try{
                int finalI = i;
                input = conveyor[finalI];
                Conveyor finalInput = input;

                if (i == 0){// lets us assign the left conveyor to first station
                    output = conveyor[numStations-1];
                }
                else{
                    output = conveyor[finalI-1];
                }

                Conveyor finalOutput = output;
                Station thisStation = new Station(finalI, workLoad[finalI], finalInput, finalOutput);// assigning station parameters
                threadExecutor.execute((Runnable) thisStation);// executing threads
            }

            catch (Exception e) {
                e.printStackTrace();
            }
        }

        threadExecutor.shutdown();
        while (!threadExecutor.isTerminated()){
            // Simulation running
        }
        System.out.println("\n>>>> Exiting Simulation <<<<\n");

    }

}
