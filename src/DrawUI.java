import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
 
public class DrawUI extends JPanel {
    private int elevatorHeight;
    private int panelHeight;
    private int panelWidth;
    public static void main(String[] args) {
        try {
            ElevatorSimulation.systemInit();
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } 
                
        File f = new File(System.getProperty("user.dir")+"/UserRequests/request1.txt");
        System.out.println(f.getAbsolutePath());
        DataImport dataImport = new DataImport();
        dataImport.initSimulation();
        try {
            dataImport.loadUserCallArray(f);
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(ElevatorSimulation.elevatorA.runState+" "+ElevatorSimulation.elevatorB.runState);
        ElevatorSimulation.time = 0;
        System.out.println("Initialization Done");
        
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("Testing");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                DrawUI ui = new DrawUI();
                frame.add(ui);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
 
    public DrawUI() {
        elevatorHeight = Integer.parseInt(ElevatorSimulation.sysParam.configs.get("ElevatorHeight"));
        panelWidth = 25*elevatorHeight+450;
        panelHeight = 50*elevatorHeight+160;

        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(ElevatorSimulation.finishCallNum<ElevatorSimulation.userCallList.length) {
                    ResponseCalculate calculator = new ResponseCalculate();
                    calculator.getNextTimeStatus(ElevatorSimulation.elevatorA);
                    calculator.getNextTimeStatus(ElevatorSimulation.elevatorB);
                    System.out.println("Time: "+ElevatorSimulation.time);
                    System.out.println("next status: "+ElevatorSimulation.elevatorA.currentFloor+" "+ElevatorSimulation.elevatorB.currentFloor);
                    repaint();
                    ElevatorSimulation.time += 1;
                } else {
                    return;
                }
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
        draw(g);
    }

    void draw(Graphics g) {
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
