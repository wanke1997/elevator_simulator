import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class ScreenSimulation {
    
    public void paintPicture(Graphics g, int elevatorHeight) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawRect(80, 120, 200, elevatorHeight*50);
        g2d.drawString("building", 155,105);

        // elevator A information
        g2d.drawRoundRect(350, 120, (int)(12.5*elevatorHeight-17.5), (int)(25.0*elevatorHeight-35.0), 20, 20);
        g2d.drawString("elevatorA status", 360, 105);
        // elevator B information
        g2d.drawRoundRect((int)(12.5*elevatorHeight+382.5), 120, (int)(12.5*elevatorHeight-17.5), (int)(25.0*elevatorHeight-35.0), 20, 20);
        g2d.drawString("elevatorB status", (int)(12.5*elevatorHeight+392.5), 105);

        // bottom information
        g2d.drawRoundRect(350, 25*elevatorHeight+135, 25*elevatorHeight+15, 25*elevatorHeight-35, 20, 20);
        g2d.drawString("Simulation basic information", 370, 25*elevatorHeight+120);

        for(int i=0;i<elevatorHeight;i++) {
            if(i<elevatorHeight-1) {
                g2d.drawLine(80, (i+1)*50+120, 280, (i+1)*50+120);
            }
            g2d.drawString(Integer.toString(elevatorHeight-i),50,50*i+150);
        }

        // elevator A & B
        int elevatorAHeight = ElevatorSimulation.elevatorA.currentFloor;
        int elevatorBHeight = ElevatorSimulation.elevatorB.currentFloor;
        // int elevatorAHeight = ElevatorSimulation.elevatorA.currentFloor;
        // int elevatorBHeight = ElevatorSimulation.elevatorB.currentFloor;

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
}
