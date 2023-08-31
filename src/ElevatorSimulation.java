import java.awt.EventQueue;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import javax.swing.JFrame;

public class ElevatorSimulation {
    // 两部电梯
    public static ElevatorState elevatorA;
    public static ElevatorState elevatorB;
    // 系统参数
    public static SysParam sysParam;
    // 已完成的指令数量
    public static int finishCallNum;
    // 计时器
    public static int time;
    // 用于记录下一时刻相较于上一时刻电梯状态是否发生了改变
    public static boolean statusChangeFlag;
    // 电梯待响应指令队列（双向链表）
    public static ResponseListHeadNode responseList;
    // 用户指令数组
    public static UserCall[] userCallList;

    public static void main(String[] args) {
        // TODO: add args to the program
        try {
            int param = systemInit();
            if(param!=1) {
                System.out.println("ERROR: return with code "+param);
            } else {
                System.out.println("Initialization succeeded!");
                // if (args.length == 0) {
                //     System.out.println("usage: you should add at least 1 parameter for a command.");
                // }
                // try {
                //     Integer.parseInt(args[0]);
                // } catch (NumberFormatException e) {
                //     System.out.println("ERROR: the first argument should be an Integer to indicate an option");
                // }
                // int menuFlag = Integer.parseInt(args[0]);
                int menuFlag = 0;
                if(menuFlag<0) {
                     System.out.println("ERROR: the first argument Integer should be positive");
                }
                // showMenu(menuFlag);
                // silenceSimulate();
                // movieSimulate();
                fullSimulate();
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public static int systemInit() throws FileNotFoundException {
        int checkResult = 1;

        File sysConf = new File(System.getProperty("user.dir")+"/SysConf");
        if(!sysConf.exists()) {
            checkResult = 0;
            return checkResult;
        }
        File sysParamFile = new File(sysConf, "SysParam.txt");
        if(!sysParamFile.exists()) {
            checkResult = -1;
            return checkResult;
        }

        Scanner scanner = new Scanner(sysParamFile);
        int totalNum = Integer.parseInt(scanner.nextLine());
        String[] keys = new String[totalNum];
        String[] values = new String[totalNum];
        int pt = 0;

        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] strs = line.split("=");
            if(strs[0].equals("UserRequestFilesPath")) {
                File requestFile = new File(System.getProperty("user.dir")+"/"+strs[1]);
                if(!requestFile.exists()) {
                    checkResult = -3;
                    scanner.close();
                    return checkResult;
                }
                if(pt>=totalNum) {
                    checkResult = -7;
                    scanner.close();
                    return checkResult;
                }
                keys[pt] = strs[0];
                values[pt] = requestFile.getAbsolutePath();
                pt++;
            } else if(strs[0].equals("SimulationFilesPath")) {
                File simulationFile = new File(System.getProperty("user.dir")+"/"+strs[1]);
                if(!simulationFile.exists()) {
                    checkResult = -4;
                    scanner.close();
                    return checkResult;
                }
                if(pt>=totalNum) {
                    checkResult = -7;
                    scanner.close();
                    return checkResult;
                }
                keys[pt] = strs[0];
                values[pt] = simulationFile.getAbsolutePath();
                pt++;
            } else if(strs[0].equals("ElevatorHeight")) {
                // judge whether ElevatorHeight is an Integer
                try {
                    Integer.parseInt(strs[1]);
                } catch (NumberFormatException e) {
                    checkResult = -5;
                    scanner.close();
                    return checkResult;
                }
                if(Integer.parseInt(strs[1])<=2) {
                    checkResult = -5;
                    scanner.close();
                    return checkResult;
                } 
                if(pt>=totalNum) {
                    checkResult = -7;
                    scanner.close();
                    return checkResult;
                }
                keys[pt] = strs[0];
                values[pt] = strs[1];
                pt++;
            } else if(strs[0].equals("DelayTime")) {
                try {
                    Integer.parseInt(strs[1]);
                } catch (NumberFormatException e) {
                    checkResult = -6;
                    scanner.close();
                    return checkResult;
                }
                if(Integer.parseInt(strs[1])<=0) {
                    checkResult = -6;
                    scanner.close();
                    return checkResult;
                }
                if(pt>=totalNum) {
                    checkResult = -7;
                    scanner.close();
                    return checkResult;
                }
                keys[pt] = strs[0];
                values[pt] = strs[1];
                pt++;
            } else {
                checkResult = -8;
                scanner.close();
                return checkResult;
            }
        }
        
        if(pt!=totalNum) {
            checkResult = -7;
        }
        scanner.close();
        sysParam = new SysParam(keys, values);
        return checkResult;
    }

    public static void showMenu(int menuFlag) {
        // TODO: finish showMenu function
        switch (menuFlag) {
            case 1:
                System.out.println("option 1");
                // silence simulation
                break;
            case 2:
                System.out.println("option 2");
                // animation simulation 
                break;
            case 3:
                System.out.println("option 3");
                // comprehensive simulation
                break;
            case 4:
                System.out.println("option 4");
                // replay
                break;
            case 5:
                System.out.println("option 5");
                // configuration settings
                break;
            case 0:
                System.out.println("Now exit");
                break;
            default:
                // other options
                break;
        }
    }

    public static void silenceSimulate() {
        // TODO: 后续需要补充完整逻辑
        File f = new File(System.getProperty("user.dir")+"/UserRequests/request1.txt");
        System.out.println(f.getAbsolutePath());
        DataImport dataImport = new DataImport();
        DataExport dataExport = new DataExport();
        
        dataImport.initSimulation();

        try {
            dataImport.loadUserCallArray(f);
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }
        
        dataExport.outputSimulationResult();
    }

    public static void movieSimulate() {
        // TODO: 根据流程图补全流程
        System.out.println("movieSimulate() function");
        File f = new File(System.getProperty("user.dir")+"/UserRequests/request1.txt");
        System.out.println(f.getAbsolutePath());
        DataImport dataImport = new DataImport();

        dataImport.initSimulation();

        try {
            dataImport.loadUserCallArray(f);
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println("Initialization Done");

        // call animation to print
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("Movie Simulation");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                MovieUI ui = new MovieUI();
                frame.add(ui);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }

    public static void fullSimulate() {
        // TODO: 根据流程图补全流程
        System.out.println("movieSimulate() function");
        File f = new File(System.getProperty("user.dir")+"/UserRequests/request1.txt");
        System.out.println(f.getAbsolutePath());
        DataImport dataImport = new DataImport();

        dataImport.initSimulation();

        try {
            dataImport.loadUserCallArray(f);
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("Initialization Done");

        File resultFile = new File(System.getProperty("user.dir")+"/SimulationFiles/request1_result_full.txt");
        try {
            resultFile.createNewFile();
            FileWriter writer = new FileWriter(resultFile);
            new DataExport().importUserCall(writer);
            new DataExport().importSimulateParam(writer);
            writer.close();
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    JFrame frame = new JFrame("Full Simulation");
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    FullUI ui = new FullUI(resultFile.getAbsolutePath());
                    frame.add(ui);
                    frame.pack();
                    frame.setLocationRelativeTo(null);
                    frame.setVisible(true);
                }
            });
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static void historySimulate() {

    }

    public static void paramConf() {

    }
}
