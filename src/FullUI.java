import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JPanel;
import javax.swing.Timer;
 
public class FullUI extends JPanel {
    private int elevatorHeight;
    private int panelHeight;
    private int panelWidth;
 
    public FullUI(String dir) {
        elevatorHeight = Integer.parseInt(ElevatorSimulation.sysParam.configs.get("ElevatorHeight"));
        panelWidth = 25*elevatorHeight+450;
        panelHeight = 50*elevatorHeight+160;
        int delayTime = 1000;
        if(ElevatorSimulation.sysParam.configs.containsKey("DelayTime")) {
            delayTime = Integer.parseInt(ElevatorSimulation.sysParam.configs.get("DelayTime"));
        }
        Timer timer = new Timer(delayTime, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(ElevatorSimulation.finishCallNum<ElevatorSimulation.userCallList.length) {
                    try {
                        FileWriter writer = new FileWriter(dir, true);
                        if(ElevatorSimulation.time==0) {
                            String initString = ElevatorSimulation.time+",A,"+ElevatorSimulation.elevatorA.currentFloor+","+ElevatorSimulation.elevatorA.runState+",B,"+ElevatorSimulation.elevatorB.currentFloor+","+ElevatorSimulation.elevatorB.runState+"; ";
                            writer.write(initString+"\n");
                        }
                        ElevatorSimulation.time += 1;
                        ElevatorSimulation.statusChangeFlag = false;
                        ResponseCalculate calculator = new ResponseCalculate();
                        calculator.getNextTimeStatus(ElevatorSimulation.elevatorA);
                        calculator.getNextTimeStatus(ElevatorSimulation.elevatorB);
                        if(ElevatorSimulation.statusChangeFlag) {
                            new DataExport().importSimulateRequests(writer);
                        }
                        writer.close();
                        repaint();
                    } catch(IOException ex) {
                        ex.printStackTrace();
                    }
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
        new ScreenSimulation().paintPicture(g, elevatorHeight);
    }
}

