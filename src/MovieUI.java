import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.Timer;
 
public class MovieUI extends JPanel {
    private int elevatorHeight;
    private int panelHeight;
    private int panelWidth;
 
    public MovieUI() {
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
                    ElevatorSimulation.time += 1;
                    ResponseCalculate calculator = new ResponseCalculate();
                    calculator.getNextTimeStatus(ElevatorSimulation.elevatorA);
                    calculator.getNextTimeStatus(ElevatorSimulation.elevatorB);
                    repaint();
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
