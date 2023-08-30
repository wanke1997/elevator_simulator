public class ResponseCalculate {
    public void getNextTimeStatus(ElevatorState elevator) {
        // add new usercalls to response list
        while(ElevatorSimulation.responseList.nextIdx<ElevatorSimulation.userCallList.length 
        && ElevatorSimulation.userCallList[ElevatorSimulation.responseList.nextIdx].callTime<=ElevatorSimulation.time) {
            ResponseListNode node = new ResponseListNode(ElevatorSimulation.responseList.nextIdx);
            ResponseListNode prev = ElevatorSimulation.responseList.tail.prev;
            node.next = ElevatorSimulation.responseList.tail;
            ElevatorSimulation.responseList.tail.prev = node;
            prev.next = node;
            node.prev = prev;
            ElevatorSimulation.responseList.nodeNum += 1;
            ElevatorSimulation.responseList.nextIdx += 1;
            ElevatorSimulation.statusChangeFlag = true;
        }

        //电梯停止且没有指令，把新指令加到电梯响应队列
        if(elevator.serveList==null) {
            if(ElevatorSimulation.responseList.nodeNum>0) {
                ResponseListNode node = ElevatorSimulation.responseList.head.next;
                node.prev.next = node.next;
                node.next.prev = node.prev;
                ElevatorSimulation.responseList.nodeNum -= 1;
                ElevatorSimulation.statusChangeFlag = true;

                char serveState = 'N';
                if(ElevatorSimulation.userCallList[node.userCallIdx].userFloor==elevator.currentFloor) {
                    serveState = 'E';
                } else {
                    serveState = 'P';
                }
                elevator.serveList = new ServeListNode(serveState, ElevatorSimulation.userCallList[node.userCallIdx]);
                
                char nextRunState = getElevatorDirection(elevator);
                findUserCallCanServe(elevator, nextRunState);
            } 
        } else {
            // 电梯响应队列不为空，可能停止（刚服务完一个user），可能在动（服务中）
            if(elevator.runState=='S') {
                char nextRunState = getElevatorDirection(elevator);
                if(nextRunState!='S') {
                    findUserCallCanServe(elevator, nextRunState);
                }
            } else {
                findUserCallCanServe(elevator, elevator.runState);
            }
        }

        setElevatorState(elevator);
    }

    public void findUserCallCanServe(ElevatorState elevator, char rState) {
        if(ElevatorSimulation.responseList.nodeNum==0) return;

        int uf = elevator.serveList.userCall.userFloor;
        char us = elevator.serveList.userCall.callType;
        char ue = elevator.runState;

        ResponseListNode node = ElevatorSimulation.responseList.head.next;
        int m = elevator.currentFloor;

        while(node!=ElevatorSimulation.responseList.tail) {
            int i = node.userCallIdx;
            int f = ElevatorSimulation.userCallList[i].userFloor;
            int t = ElevatorSimulation.userCallList[i].userTarget;
            char s = ElevatorSimulation.userCallList[i].callType;

            boolean satisfy = false;

            if(us == rState) {
                if(rState=='U'&&s=='U'&&f>m || rState=='D'&&s=='D'&&f<m || ue=='S'&&f==m) {
                    satisfy = true;
                }
            } else {
                if(rState=='U'&&s=='U'&&f>=m&&t<=uf || rState=='D'&&s=='D'&&f<=m&&t>=uf
                    || ue=='S'&&f==m&&rState=='U'&&t<=uf || ue=='S'&&f==m&&rState=='D'&&t>=uf) {
                        satisfy = true;
                    }
            }

            if(satisfy) {
                // delete a node
                ResponseListNode node2 = node;
                node = node.next;
                node2.next.prev = node2.prev;
                node2.prev.next = node2.next;
                ElevatorSimulation.responseList.nodeNum -= 1;
                ElevatorSimulation.statusChangeFlag = true;

                // find the tail of serveList
                ServeListNode node3 = elevator.serveList;
                while(node3.next!=null) {
                    node3 = node3.next;
                }

                // add a node for serveList
                char serveState = (f==m)?'E':'P';
                node3.next = new ServeListNode(serveState, ElevatorSimulation.userCallList[node2.userCallIdx]);
                node3.next.prev = node3;
            } else {
                node = node.next;
            }
        }
    }

    public char getElevatorDirection(ElevatorState elevator) {
        char eStatus = ' ';
        if(elevator.serveList==null) {
            eStatus = 'S';
        } else {
            ServeListNode p = elevator.serveList;
            int m = elevator.currentFloor;
            if(p.serveState=='P') {
                int f = p.userCall.userFloor;
                if(f>m) {
                    eStatus = 'U';
                } else {
                    eStatus = 'D';
                }
            } else if(p.serveState=='E'){
                int t = p.userCall.userTarget;
                if(t>m) {
                    eStatus = 'U';
                } else {
                    eStatus = 'D';
                }
            }
        }
        return eStatus;
    }

    public void setElevatorState(ElevatorState elevator) {

    }
}
