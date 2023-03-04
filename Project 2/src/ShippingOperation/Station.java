package ShippingOperation;

import java.util.Random;

public class Station implements Runnable{

    protected Random gen = new Random();
    protected int stationNum;
    protected int workload;
    protected Conveyor inputConveyor;
    protected Conveyor outputConveyor;
    protected boolean bothLocks = false;
    protected int workLoadCounter;

   // Station constructor method
    public Station(int stationNum, int workload, Conveyor inputConveyor, Conveyor outputConveyor){
        this.stationNum = stationNum;
        this.workload = workload;
        this.inputConveyor = inputConveyor;
        this.outputConveyor = outputConveyor;
        workLoadCounter = workload;
    }



    public void goToSleep(){// pause thread
        try{
            Thread.sleep(gen.nextInt(500)); // sleep random amount of time up to 500ms
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void working(){// simulate working
        System.out.println("\n>>>> Station " + stationNum + " Moving Packages <<<<\n");
        System.out.println("Station " + stationNum + " has moved packages into station on input conveyor " + inputConveyor.conveyorNum);
        System.out.println("Station " + stationNum + " has moved packages out of station on output conveyor " + outputConveyor.conveyorNum);
        workLoadCounter--;// decrement workload
        System.out.println("Station " + stationNum + " has " + workLoadCounter + " workloads left\n\n");

        goToSleep();

        if (workLoadCounter == 0){
            System.out.println("\n***** Station " + stationNum + " All Workloads Complete - Station " + stationNum + " Preparing Shutdown *****\n");
            System.out.println("\n***** Station " + stationNum + " Offline *****\n");
        }
    }

    @Override
    public void run() {

        System.out.println("Station " + stationNum + " Assigned to Input " + inputConveyor.conveyorNum + " and Output " + outputConveyor.conveyorNum + " with Workload of " + workload);
        for (int i=0; i < workload; i++){
            System.out.println("Station " + stationNum + " Beginning Lock Acquisition Phase");

            bothLocks = false;
            while (!bothLocks){
                if (inputConveyor.lockConveyor()){//get lock for input conveyor
                    System.out.println("Station " + stationNum + " holds lock for input Conveyor " + inputConveyor.conveyorNum);
                    if (outputConveyor.lockConveyor()){//get lock for output conveyor
                        System.out.println("Station " + stationNum + " holds lock for output Conveyor " + outputConveyor.conveyorNum);

                        bothLocks = true;// once we have both locks set true

                        System.out.println("\n>>>> Station " + stationNum + " holds both locks on input Conveyor " + inputConveyor.conveyorNum + " and output Conveyor " + outputConveyor.conveyorNum + " <<<<\n");

                        working();//beginning workload
                        outputConveyor.unlockConveyor();// release both locks after workload is complete
                        inputConveyor.unlockConveyor();
                        System.out.println("Station " + stationNum + " releasing output lock " + outputConveyor.conveyorNum);
                        System.out.println("Station " + stationNum + " releasing input lock " + inputConveyor.conveyorNum);
                    }
                    else {
                        inputConveyor.unlockConveyor();// release input lock in order to not starve the other stations
                        System.out.println("\n!!!! Station " + stationNum + " was not granted output Conveyor " + outputConveyor.conveyorNum + " - releasing input Conveyor " + inputConveyor.conveyorNum + " !!!!\n");
                        goToSleep();// sleep before next attempt, allows for another thread to ask for locks preventing deadlock
                    }
                }
            }
        }

    }
}
