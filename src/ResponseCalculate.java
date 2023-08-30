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
        if(elevator.serveList.nodeNum == 0) {
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
                ServeListNode serveNode = new ServeListNode(serveState, ElevatorSimulation.userCallList[node.userCallIdx], node.userCallIdx);
                ServeListNode prev = elevator.serveList.tail.prev;
                prev.next = serveNode;
                serveNode.prev = prev;
                serveNode.next = elevator.serveList.tail;
                elevator.serveList.tail.prev = serveNode;
                elevator.serveList.nodeNum += 1;
                
                char nextRunState = getElevatorDirection(elevator);
                findUserCallCanServe(elevator, nextRunState);
            } 
        } else if(elevator.serveList.nodeNum > 0) {
            // 电梯响应队列不为空，可能停止（刚服务完一个user），可能在动（服务中）
            if(elevator.runState=='S') {
                char nextRunState = getElevatorDirection(elevator);
                if(nextRunState!='S') {
                    findUserCallCanServe(elevator, nextRunState);
                }
            } else {
                findUserCallCanServe(elevator, elevator.runState);
            }
        } else {
            System.out.println("ERROR: elevator.serveList.nodeNum < 0");
        }

        setElevatorState(elevator);
    }

    private void findUserCallCanServe(ElevatorState elevator, char rState) {
        if(ElevatorSimulation.responseList.nodeNum==0) return;

        int uf = elevator.serveList.head.next.userCall.userFloor;
        char us = elevator.serveList.head.next.userCall.callType;
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

                // add a node for serveList
                char serveState = (f==m)?'E':'P';
                ServeListNode serveNode = new ServeListNode(serveState, ElevatorSimulation.userCallList[node2.userCallIdx], node2.userCallIdx);
                ServeListNode prev = elevator.serveList.tail.prev;
                prev.next = serveNode;
                serveNode.prev = prev;
                elevator.serveList.tail.prev = serveNode;
                serveNode.next = elevator.serveList.tail;
                elevator.serveList.nodeNum += 1;
            } else {
                node = node.next;
            }
        }
    }

    private char getElevatorDirection(ElevatorState elevator) {
        char eStatus = ' ';
        if(elevator.serveList.nodeNum == 0) {
            eStatus = 'S';
        } else {
            ServeListNode p = elevator.serveList.head.next;
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

    private void setElevatorState(ElevatorState elevator) {
        int m = elevator.currentFloor;
        char eRs = elevator.runState;
        int stepFlag = 0;

        if(elevator.serveList.nodeNum != 0) {
            ServeListNode p = elevator.serveList.head.next;
            while(p!=elevator.serveList.tail) {
                int f = p.userCall.userFloor;
                int t = p.userCall.userTarget;
                char s = p.serveState;

                if(eRs=='U') {
                    stepFlag = 1;
                    if(s=='E'&&t==m+1) {
                        elevator.runState = 'S';
                        ElevatorSimulation.statusChangeFlag = true;
                        // delete the node
                        ServeListNode pp = p;
                        p = p.next;
                        pp.prev.next = pp.next;
                        pp.next.prev = pp.prev;
                        elevator.serveList.nodeNum -= 1;

                        ElevatorSimulation.finishCallNum += 1;
                    } else {
                        if(s=='P'&&f==m+1) {
                            elevator.runState = 'S';
                            p.serveState = 'E';
                            ElevatorSimulation.statusChangeFlag = true;
                            p = p.next;
                        } else {
                            p = p.next;
                        }
                    }
                } else if(eRs=='D') { 
                    stepFlag = -1;
                    if(s=='E'&&t==m-1) {
                        elevator.runState = 'S';
                        ElevatorSimulation.statusChangeFlag = true;
                        // delete the node
                        ServeListNode pp = p;
                        p = p.next;
                        pp.prev.next = pp.next;
                        pp.next.prev = pp.prev;
                        elevator.serveList.nodeNum -= 1;

                        ElevatorSimulation.finishCallNum += 1;
                    } else {
                        if(s=='P'&&f==m-1) {
                            elevator.runState = 'S';
                            p.serveState = 'E';
                            ElevatorSimulation.statusChangeFlag = true;
                            p = p.next;
                        } else {
                            p = p.next;
                        }
                    }
                } else {
                    if(s=='P') {
                        if(f>m) {
                            elevator.runState = 'U';
                        } else {
                            elevator.runState = 'D';
                        }
                        ElevatorSimulation.statusChangeFlag = true;
                    } else if(s=='E') {
                        if(t>m) {
                            elevator.runState = 'U';
                        } else {
                            elevator.runState = 'D';
                        }
                        ElevatorSimulation.statusChangeFlag = true;
                    }
                    break;
                }
            }
        }
        elevator.currentFloor = elevator.currentFloor+stepFlag;
    }
}
