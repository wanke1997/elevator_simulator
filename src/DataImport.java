import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class DataImport {
    public void initSimulation() {
        if(ElevatorSimulation.responseList==null) {
            ElevatorSimulation.responseList = new ResponseListHeadNode();
        } else if(ElevatorSimulation.responseList.nodeNum>0) {
            ElevatorSimulation.responseList.head.next = ElevatorSimulation.responseList.tail;
            ElevatorSimulation.responseList.head.prev = null;
            ElevatorSimulation.responseList.tail.prev = ElevatorSimulation.responseList.head;
            ElevatorSimulation.responseList.tail.next = null;
            ElevatorSimulation.responseList.nodeNum = 0;
        }
        if(ElevatorSimulation.userCallList!=null) {
            ElevatorSimulation.userCallList = null;
        }
        ElevatorSimulation.finishCallNum = 0;
        ElevatorSimulation.responseList.nextIdx = 0;
        initElevator();
    }

    private void initElevator() {
        if(ElevatorSimulation.elevatorA==null) {
            ElevatorSimulation.elevatorA = new ElevatorState(1, 'S');
        } else {
            ElevatorSimulation.elevatorA.currentFloor = 1;
            ElevatorSimulation.elevatorA.runState = 'S';
            ElevatorSimulation.elevatorA.serveList = new ServeListHeadNode();
        }
        if(ElevatorSimulation.elevatorB==null) {
            ElevatorSimulation.elevatorB = new ElevatorState(1, 'S');
        } else {
            ElevatorSimulation.elevatorB.currentFloor = 1;
            ElevatorSimulation.elevatorB.runState = 'S';
            ElevatorSimulation.elevatorB.serveList = new ServeListHeadNode();
        }
    }

    public int loadUserCallArray(File fp) throws FileNotFoundException {
        Scanner scanner = new Scanner(fp);
        int userListLen = Integer.parseInt(scanner.nextLine());
        ElevatorSimulation.userCallList = new UserCall[userListLen];
        int itemNum = 0;
        while(scanner.hasNextLine()) {
            String s = scanner.nextLine();
            String[] strs = s.split(",");
            if(itemNum>=userListLen) {
                scanner.close();
                return 0;
            }
            int userFloor = Integer.parseInt(strs[0]);
            int userTarget = Integer.parseInt(strs[1]);
            int callTime = Integer.parseInt(strs[2]);
            char callType = (userTarget>userFloor)?'U':'D';
            ElevatorSimulation.userCallList[itemNum] = new UserCall(userFloor, userTarget, callTime, callType);
            itemNum++;
        }
        scanner.close();
        if(userListLen!=itemNum) {
            return 0;
        } else {
            return 1;
        }
    }
}
