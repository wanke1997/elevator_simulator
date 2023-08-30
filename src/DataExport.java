import java.io.File;
import java.io.IOException;

public class DataExport {
    public int outputSimulationResult() {
        // TODO: 遵循流程图补充完整
        File resultFile = new File(System.getProperty("user.dir")+"/SimulationFiles/request1_result.txt");
        ResponseCalculate calculator = new ResponseCalculate();
        try {
            boolean res = resultFile.createNewFile();
            // if(!res) return 0;
            importUserCall(resultFile);
            importSimulateParam(resultFile);
            ElevatorSimulation.time = 0;
            while(ElevatorSimulation.finishCallNum<ElevatorSimulation.userCallList.length) {
                ElevatorSimulation.statusChangeFlag = false;
                calculator.getNextTimeStatus(ElevatorSimulation.elevatorA);
                calculator.getNextTimeStatus(ElevatorSimulation.elevatorB);
                if(ElevatorSimulation.statusChangeFlag) {
                    importSimulateResult(resultFile);
                    System.out.println("At time "+ElevatorSimulation.time);
                    System.out.println("Elevator A: "+ElevatorSimulation.elevatorA.currentFloor+" "+ElevatorSimulation.elevatorA.runState);
                    System.out.println("Elevator B: "+ElevatorSimulation.elevatorB.currentFloor+" "+ElevatorSimulation.elevatorB.runState);
                    System.out.println("About requests: "+ElevatorSimulation.finishCallNum+" of "+ElevatorSimulation.userCallList.length+" has finished");
                    
                    ServeListNode p = ElevatorSimulation.elevatorA.serveList.head.next;
                    while(p!=ElevatorSimulation.elevatorA.serveList.tail) {
                        System.out.println("<"+p.userCall.userFloor+", "+p.userCall.userTarget+", "+p.serveState+", "+"A>");
                        p = p.next;
                    }
                    p = ElevatorSimulation.elevatorB.serveList.head.next;
                    while(p!=ElevatorSimulation.elevatorB.serveList.tail) {
                        System.out.println("<"+p.userCall.userFloor+", "+p.userCall.userTarget+", "+p.serveState+", "+"B>");
                        p = p.next;
                    }
                    ResponseListNode pp = ElevatorSimulation.responseList.head.next;
                    while(pp!=ElevatorSimulation.responseList.tail) {
                        System.out.println("<"+ElevatorSimulation.userCallList[pp.userCallIdx].userFloor+", "+ElevatorSimulation.userCallList[pp.userCallIdx].userTarget+", "+"N, N>");
                        pp = pp.next;
                    }
                }
                ElevatorSimulation.time += 1;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
        System.out.println("Final About requests: "+ElevatorSimulation.finishCallNum+" of "+ElevatorSimulation.userCallList.length);
        
        return 1;
    }

    public void importUserCall(File fp) {

    }

    public void importSimulateParam(File fp) {

    }

    public void importSimulateResult(File fp) {

    }
}
