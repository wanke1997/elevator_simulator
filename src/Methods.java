import java.awt.EventQueue;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JFrame;

public class Methods {
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
        ElevatorSimulation.time = 0;
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
        System.out.println("fullSimulate() function");
        File f = new File(System.getProperty("user.dir")+"/UserRequests/request1.txt");
        System.out.println(f.getAbsolutePath());
        DataImport dataImport = new DataImport();

        dataImport.initSimulation();
        ElevatorSimulation.time = 0;
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
        try {
            System.out.println("fullSimulate() function");
            File f = new File(System.getProperty("user.dir")+"/SimulationFiles/request1_result.txt");
            System.out.println(f.getAbsolutePath());
            DataImport dataImport = new DataImport();
            dataImport.initSimulation();
            Scanner scanner = new Scanner(f);

            // 1. read 1st part parameters
            int userListLen = Integer.parseInt(scanner.nextLine());
            ElevatorSimulation.userCallList = new UserCall[userListLen];
            int itemNum = 0;
            while(itemNum<userListLen) {
                String s = scanner.nextLine();
                String[] strs = s.split(",");
                int userFloor = Integer.parseInt(strs[0]);
                int userTarget = Integer.parseInt(strs[1]);
                int callTime = Integer.parseInt(strs[2]);
                char callType = (userTarget>userFloor)?'U':'D';
                ElevatorSimulation.userCallList[itemNum] = new UserCall(userFloor, userTarget, callTime, callType);
                itemNum++;
            }
            scanner.nextLine();

            // 2. read 2nd part parameters
            int confNum = Integer.parseInt(scanner.nextLine());
            int paramNum = 0;
            while(paramNum<confNum) {
                String s = scanner.nextLine();
                String[] strs = s.split("=");
                if(strs[0].equals("ElevatorHeight") || strs[0].equals("SimulateSpeed") || strs[0].equals("ElevatorSpeed")) {
                    ElevatorSimulation.sysParam.configs.replace(strs[0], strs[1]);
                }
                paramNum += 1;
            }
            scanner.nextLine();

            System.out.println("Finish the two parts.");

            // 3. start simulation
            ElevatorSimulation.time = 0;

            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    JFrame frame = new JFrame("History Simulation");
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    HistoryUI ui = new HistoryUI(scanner);
                    frame.add(ui);
                    frame.pack();
                    frame.setLocationRelativeTo(null);
                    frame.setVisible(true);
                }
            });
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void paramConf() {

    }
}
