import java.io.File;
import java.io.IOException;
import java.io.FileWriter;

public class DataExport {
    public int outputSimulationResult() {
        // TODO: 遵循流程图补充完整
        File resultFile = new File(System.getProperty("user.dir")+"/SimulationFiles/request1_result.txt");
        ResponseCalculate calculator = new ResponseCalculate();

        try {
            resultFile.createNewFile();
            FileWriter writer = new FileWriter(resultFile);
            importUserCall(writer);
            importSimulateParam(writer);
            ElevatorSimulation.time = 0;
            String initString = ElevatorSimulation.time+",A,"+ElevatorSimulation.elevatorA.currentFloor+","+ElevatorSimulation.elevatorA.runState+",B,"+ElevatorSimulation.elevatorB.currentFloor+","+ElevatorSimulation.elevatorB.runState+"; ";
            writer.write(initString+"\n");

            while(ElevatorSimulation.finishCallNum<ElevatorSimulation.userCallList.length) {
                ElevatorSimulation.statusChangeFlag = false;
                calculator.getNextTimeStatus(ElevatorSimulation.elevatorA);
                calculator.getNextTimeStatus(ElevatorSimulation.elevatorB);
                if(ElevatorSimulation.statusChangeFlag) {
                    String firstOutput = ElevatorSimulation.time+",A,"+ElevatorSimulation.elevatorA.currentFloor+","+ElevatorSimulation.elevatorA.runState+",B,"+ElevatorSimulation.elevatorB.currentFloor+","+ElevatorSimulation.elevatorB.runState+";";
                    writer.write(firstOutput);
                    
                    ServeListNode p = ElevatorSimulation.elevatorA.serveList.head.next;
                    while(p!=ElevatorSimulation.elevatorA.serveList.tail) {
                        String s = "<"+(p.userCallIdx+1)+","+p.userCall.userFloor+","+p.userCall.userTarget+","+p.serveState+","+"A>";
                        writer.write(s+",");
                        p = p.next;
                    }
                    p = ElevatorSimulation.elevatorB.serveList.head.next;
                    while(p!=ElevatorSimulation.elevatorB.serveList.tail) {
                        String s = "<"+(p.userCallIdx+1)+","+p.userCall.userFloor+","+p.userCall.userTarget+","+p.serveState+","+"B>";
                        writer.write(s+",");
                        p = p.next;
                    }
                    ResponseListNode pp = ElevatorSimulation.responseList.head.next;
                    while(pp!=ElevatorSimulation.responseList.tail) {
                        String s = "<"+(pp.userCallIdx+1)+","+ElevatorSimulation.userCallList[pp.userCallIdx].userFloor+","+ElevatorSimulation.userCallList[pp.userCallIdx].userTarget+","+"N,N>";
                        writer.write(s+",");
                        pp = pp.next;
                    }
                    writer.write("\n");
                }
                ElevatorSimulation.time += 1;
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            
            return 0;
        }

        return 1;
    }

    public void importUserCall(FileWriter writer) throws IOException {
        int total = ElevatorSimulation.userCallList.length;
        writer.write(total+"\n");
        for(int i=0;i<total;i++) {
            UserCall call = ElevatorSimulation.userCallList[i];
            String s = call.userFloor+","+call.userTarget+","+call.callTime+"\n";
            writer.write(s);
        }
        writer.write("*********************************************************************************************\n");
    }

    public void importSimulateParam(FileWriter writer) throws IOException {
        int total = ElevatorSimulation.sysParam.configs.size();
        writer.write(total+"\n");
        for(String key:ElevatorSimulation.sysParam.configs.keySet()) {
            String s = key+"="+ElevatorSimulation.sysParam.configs.get(key)+"\n";
            writer.write(s);
        }
        writer.write("*********************************************************************************************\n");
    }
}
