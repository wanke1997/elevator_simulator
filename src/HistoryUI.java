import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;

import javax.swing.JPanel;
import javax.swing.Timer;

public class HistoryUI extends JPanel {
    private int elevatorHeight;
    private int panelHeight;
    private int panelWidth;
    private int nextCT;
    private String s;
 
    public HistoryUI(Scanner scanner) {
        nextCT = 1;
        elevatorHeight = Integer.parseInt(ElevatorSimulation.sysParam.configs.get("ElevatorHeight"));
        panelWidth = 25*elevatorHeight+450;
        panelHeight = 50*elevatorHeight+160;
        int delayTime = 1000;
        if(ElevatorSimulation.sysParam.configs.containsKey("DelayTime")) {
            delayTime = Integer.parseInt(ElevatorSimulation.sysParam.configs.get("DelayTime"));
        }

        // read time=0 state
        s = scanner.nextLine();

        Timer timer = new Timer(delayTime, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // detailed operations
                if(ElevatorSimulation.time < nextCT-1) {
                    setState();
                } else if(ElevatorSimulation.time == nextCT-1) {
                    readState(s);
                    if(scanner.hasNextLine()) {
                        s = scanner.nextLine();
                        nextCT = readNextTime(s);
                    }
                } else {
                    return;
                }
                repaint();
                ElevatorSimulation.time += 1;
            }
        });
        timer.start();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(panelWidth, panelHeight);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        new ScreenSimulation().paintPicture(g, elevatorHeight);
    }

    private void readState(String s) {
        ElevatorSimulation.elevatorA.serveList.head.next = ElevatorSimulation.elevatorA.serveList.tail;
        ElevatorSimulation.elevatorA.serveList.tail.prev = ElevatorSimulation.elevatorA.serveList.head;
        ElevatorSimulation.elevatorA.serveList.nodeNum = 0;
        ElevatorSimulation.elevatorB.serveList.head.next = ElevatorSimulation.elevatorB.serveList.tail;
        ElevatorSimulation.elevatorB.serveList.tail.prev = ElevatorSimulation.elevatorB.serveList.head;
        ElevatorSimulation.elevatorB.serveList.nodeNum = 0;
        ElevatorSimulation.responseList.head.next = ElevatorSimulation.responseList.tail;
        ElevatorSimulation.responseList.tail.prev = ElevatorSimulation.responseList.head;
        ElevatorSimulation.responseList.nodeNum = 0;
        String[] str1 = s.split("\\s*;\\s*");

        for(int i=0;i<str1.length;i++) {
            if(i==0) {
                // 1. set the state of elevators
                String[] str2 = str1[i].split("\\s*,\\s*");
                nextCT = Integer.parseInt(str2[0]);
                ElevatorSimulation.elevatorA.currentFloor = Integer.parseInt(str2[2]);
                ElevatorSimulation.elevatorA.runState = str2[3].charAt(0);
                ElevatorSimulation.elevatorB.currentFloor = Integer.parseInt(str2[5]);
                ElevatorSimulation.elevatorB.runState = str2[6].charAt(0);
            } else {
                // 2. set the ServeListNode and ResponseListNode
                String[] str2 = str1[i].substring(1, str1[i].length()-1).split("\\s*,\\s*");
                int idx = Integer.parseInt(str2[0])-1;
                if(str2[4].equals("A")) {
                    UserCall userCall = ElevatorSimulation.userCallList[idx];
                    ServeListNode node = new ServeListNode(str2[3].charAt(0), userCall, idx);
                    ServeListNode prev = ElevatorSimulation.elevatorA.serveList.tail.prev;
                    prev.next = node;
                    node.prev = prev;
                    node.next = ElevatorSimulation.elevatorA.serveList.tail;
                    ElevatorSimulation.elevatorA.serveList.tail.prev = node;
                    ElevatorSimulation.elevatorA.serveList.nodeNum += 1;
                } else if(str2[4].equals("B")) {
                    UserCall userCall = ElevatorSimulation.userCallList[idx];
                    ServeListNode node = new ServeListNode(str2[3].charAt(0), userCall, idx);
                    ServeListNode prev = ElevatorSimulation.elevatorB.serveList.tail.prev;
                    prev.next = node;
                    node.prev = prev;
                    node.next = ElevatorSimulation.elevatorB.serveList.tail;
                    ElevatorSimulation.elevatorB.serveList.tail.prev = node;
                    ElevatorSimulation.elevatorB.serveList.nodeNum += 1;
                } else if(str2[4].equals("N")) {
                    ResponseListNode node = new ResponseListNode(idx);
                    ResponseListNode prev = ElevatorSimulation.responseList.tail.prev;
                    prev.next = node;
                    node.prev = prev;
                    node.next = ElevatorSimulation.responseList.tail;
                    ElevatorSimulation.responseList.tail.prev = node;
                    ElevatorSimulation.responseList.nodeNum += 1;
                }
            }
        }
    }

    private void setState() {
        // set elevator A
        char state = ElevatorSimulation.elevatorA.runState;
        if(state == 'U') {
            ElevatorSimulation.elevatorA.currentFloor += 1;
        } else if(state == 'D') {
            ElevatorSimulation.elevatorA.currentFloor -= 1;
        }
        // set elevator B
        
        state = ElevatorSimulation.elevatorB.runState;
        if(state == 'U') {
            ElevatorSimulation.elevatorB.currentFloor += 1;
        } else if(state == 'D') {
            ElevatorSimulation.elevatorB.currentFloor -= 1;
        }
    }

    private int readNextTime(String s) {
        String[] str1 = s.split("\\s*;\\s*");
        String[] str2 = str1[0].split("\\s*,\\s*");
        int res = Integer.parseInt(str2[0]);
        return res;
    }
}
