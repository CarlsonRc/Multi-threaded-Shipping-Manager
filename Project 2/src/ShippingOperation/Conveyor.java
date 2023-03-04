package ShippingOperation;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Conveyor {
    // what number conveyor am i?
    // am i locked or unlocked?

    int conveyorNum;
    public Lock lock = new ReentrantLock();

    // conveyor constructor method
    public Conveyor(int conveyorNum){
        this.conveyorNum = conveyorNum;
    }

    public boolean lockConveyor(){
        if (lock.tryLock() == true){
            return true;
        }
        else
        return false;
    }

    public void unlockConveyor(){
        lock.unlock();
    }
}
