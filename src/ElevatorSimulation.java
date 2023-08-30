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
                showMenu(menuFlag);
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

    }

    public static void movieSimulate() {

    }

    public static void fullSimulate() {

    }

    public static void historySimulate() {

    }

    public static void paramConf() {

    }
}
