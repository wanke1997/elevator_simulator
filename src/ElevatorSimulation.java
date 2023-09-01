import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

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
    public static Scanner inputScanner;
    public static void main(String[] args) {
        try {
            inputScanner = new Scanner(System.in);
            int param = systemInit();
            if(param!=1) {
                System.out.println("ERROR: return with code "+param);
            } else {
                System.out.println("Initialization succeeded!");
                showMenu();
            }
            inputScanner.close();
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

    public static void showMenu() {
        boolean sign = true;
        while(sign) {
            System.out.println("The program is an elevator simulator. Please select the function that you want. ");
            System.out.println("[1] Silence simulation");
            System.out.println("[2] Movie simulation (only execute once)");
            System.out.println("[3] Full simulation (only execute once)");
            System.out.println("[4] History replay (only execute once)");
            System.out.println("[5] Modify system configuration");
            System.out.println("[0] Exit the simulator");
            System.out.println("Now enter your option: ");

            int menuFlag = Integer.parseInt(inputScanner.nextLine());
            switch (menuFlag) {
                case 1:
                    System.out.println("Welcome to silence simulation");
                    Methods.silenceSimulate();
                    break;
                case 2:
                    System.out.println("Welcome to movie simulation");
                    Methods.movieSimulate();
                    sign = false;
                    break;
                case 3:
                    System.out.println("Welcome to full simulation");
                    Methods.fullSimulate();
                    sign = false;
                    break;
                case 4:
                    System.out.println("Welcome to history replay");
                    Methods.historySimulate();
                    sign = false;
                    break;
                case 5:
                    System.out.println("Welcome to system configuration modification");
                    Methods.paramConf();
                    break;
                case 0:
                    System.out.println("Now exit, have a good day");
                    sign = false;
                    break;
                default:
                    // other options
                    break;
            }
        }
    }
}
