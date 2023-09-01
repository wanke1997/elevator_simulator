import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class ScreenSimulation {
    public void paintPicture(Graphics g, int elevatorHeight) {
        Graphics2D g2d = (Graphics2D) g;
        // building
        g2d.drawRect(80, 120, 200, elevatorHeight*50);
        g2d.drawString("building", 155,105);
        for(int i=0;i<elevatorHeight;i++) {
            if(i<elevatorHeight-1) {
                g2d.drawLine(80, (i+1)*50+120, 280, (i+1)*50+120);
            }
            g2d.drawString(Integer.toString(elevatorHeight-i),50,50*i+150);
        }
        // elevator A information
        g2d.drawRoundRect(350, 120, (int)(12.5*elevatorHeight-17.5), (int)(25.0*elevatorHeight-35.0), 20, 20);
        g2d.drawString("elevatorA status", 360, 105);
        int startX = 360;
        int startY = 135;
        g2d.drawString("Floor: "+ElevatorSimulation.elevatorA.currentFloor, startX, startY);
        startY += 15;
        g2d.drawString("Status: "+convert(ElevatorSimulation.elevatorA.runState), startX, startY);
        startY += 15;
        g2d.drawString("Commands: ", startX, startY);
        startY += 15;
        ServeListNode node = ElevatorSimulation.elevatorA.serveList.head.next;
        while(node!=ElevatorSimulation.elevatorA.serveList.tail) {
            String s1 = (node.userCallIdx+1)+".user floor: "+node.userCall.userFloor;
            g2d.drawString(s1, startX, startY);
            startY += 15;
            String s2 = "  target floor: "+node.userCall.userTarget;
            g2d.drawString(s2, startX, startY);
            startY += 15;
            String s3 = "  status: "+convert(node.userCall.callType);
            g2d.drawString(s3, startX, startY);
            startY += 15;
            node = node.next;
        }
        // elevator B information
        g2d.drawRoundRect((int)(12.5*elevatorHeight+382.5), 120, (int)(12.5*elevatorHeight-17.5), (int)(25.0*elevatorHeight-35.0), 20, 20);
        g2d.drawString("elevatorB status", (int)(12.5*elevatorHeight+392.5), 105);
        startX = (int)(12.5*elevatorHeight+392.5);
        startY = 135;
        g2d.drawString("Floor: "+ElevatorSimulation.elevatorB.currentFloor, startX, startY);
        startY += 15;
        g2d.drawString("Status: "+convert(ElevatorSimulation.elevatorB.runState), startX, startY);
        startY += 15;
        g2d.drawString("Commands: ", startX, startY);
        startY += 15;
        node = ElevatorSimulation.elevatorB.serveList.head.next;
        while(node!=ElevatorSimulation.elevatorB.serveList.tail) {
            String s1 = (node.userCallIdx+1)+".user floor: "+node.userCall.userFloor;
            g2d.drawString(s1, startX, startY);
            startY += 15;
            String s2 = "  target floor: "+node.userCall.userTarget;
            g2d.drawString(s2, startX, startY);
            startY += 15;
            String s3 = "  status: "+convert(node.userCall.callType);
            g2d.drawString(s3, startX, startY);
            startY += 15;
            node = node.next;
        }
        // bottom information
        g2d.drawRoundRect(350, 25*elevatorHeight+135, 25*elevatorHeight+15, 25*elevatorHeight-35, 20, 20);
        g2d.drawString("Simulation Basic Information", 370, 25*elevatorHeight+120);
        startX = 360;
        startY = 25*elevatorHeight+150;
        g2d.drawString("Time: "+ElevatorSimulation.time, startX, startY);
        startY += 15;
        g2d.drawString("TODO Commands: ", startX, startY);
        startY += 15;
        ResponseListNode node2 = ElevatorSimulation.responseList.head.next;
        while(node2!=ElevatorSimulation.responseList.tail) {
            String s1 = (node2.userCallIdx+1)+".user floor: "+ElevatorSimulation.userCallList[node2.userCallIdx].userFloor;
            g2d.drawString(s1, startX, startY);
            startY += 15;
            String s2 = "  target floor: "+ElevatorSimulation.userCallList[node2.userCallIdx].userTarget;
            g2d.drawString(s2, startX, startY);
            startY += 15;
            String s3 = "  type: "+convert(ElevatorSimulation.userCallList[node2.userCallIdx].callType);
            g2d.drawString(s3, startX, startY);
            startY += 15;
            node2 = node2.next;
        }
        // elevator A & B
        int elevatorAHeight = ElevatorSimulation.elevatorA.currentFloor;
        int elevatorBHeight = ElevatorSimulation.elevatorB.currentFloor;
        g2d.setColor(Color.green);
        g2d.drawRect(105, 50*(elevatorHeight-elevatorAHeight)+130, 60, 30);
        g2d.fillRect(105, 50*(elevatorHeight-elevatorAHeight)+130, 60, 30);
        g2d.drawRect(195, 50*(elevatorHeight-elevatorBHeight)+130, 60, 30);
        g2d.fillRect(195, 50*(elevatorHeight-elevatorBHeight)+130, 60, 30);
        g2d.setColor(Color.black);
        g2d.drawString("A", 130, 50*(elevatorHeight-elevatorAHeight)+150);
        g2d.drawString("B", 220, 50*(elevatorHeight-elevatorBHeight)+150);
        g2d.dispose();
    }

    private String convert(char ch) {
        if(ch=='U') return "up";
        else if(ch=='D') return "down";
        else if(ch=='S') return "stop";
        else if(ch=='P') return "waiting";
        else if(ch=='E') return "serving";
        else if(ch=='N') return "N/A";
        return "";
    }
}
