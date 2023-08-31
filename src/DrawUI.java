import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.Timer;
 
public class DrawUI extends JPanel {
    private int elevatorHeight;
    private int panelHeight;
    private int panelWidth;
 
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
        new ScreenSimulation().paintPicture(g, elevatorHeight);
    }
}
