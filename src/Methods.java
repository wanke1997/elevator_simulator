import java.awt.EventQueue;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import javax.swing.JFrame;

public class Methods {
    public static void silenceSimulate() {
        boolean sign = true;
        while(sign) {
            System.out.println("You are in silence simulation module, please select operations. ");
            System.out.println("Press [1] to input the filename and start function. Press [0] to return to main menu.");
            int option = Integer.parseInt(ElevatorSimulation.inputScanner.nextLine());
            if(option == 1) {
                System.out.println("Please enter the file name, including the extension name");
                String fileName = ElevatorSimulation.inputScanner.nextLine();
                File f = new File(System.getProperty("user.dir")+"/UserRequests/"+fileName);
                System.out.println(f.getAbsolutePath());
                DataImport dataImport = new DataImport();
                DataExport dataExport = new DataExport();
                
                dataImport.initSimulation();
                try {
                    dataImport.loadUserCallArray(f);
                } catch(FileNotFoundException e) {
                    System.out.println("File not found, please try again.");
                    e.printStackTrace();
                }
                int res = dataExport.outputSimulationResult(fileName);
                if(res == 1) {
                    sign = false;
                } else if(res == 0) {
                    System.out.println("Simulation failed");
                }
            } else if(option == 0) {
                sign = false;
                return;
            } else {
                System.out.println("ERROR: invalid number, please try again");
            }
        }
    }

    public static void movieSimulate() {
        boolean sign = true;
        while(sign) {
            System.out.println("You are in movie simulation module, please select operations. ");
            System.out.println("Press [1] to input the filename and start function. Press [0] to return to main menu.");
            int option = Integer.parseInt(ElevatorSimulation.inputScanner.nextLine());

            if(option == 1) {
                System.out.println("Please enter the file name, including the extension name");
                String fileName = ElevatorSimulation.inputScanner.nextLine();
                File f = new File(System.getProperty("user.dir")+"/UserRequests/"+fileName);
                System.out.println(f.getAbsolutePath());

                DataImport dataImport = new DataImport();
                dataImport.initSimulation();
                ElevatorSimulation.time = 0;
                try {
                    dataImport.loadUserCallArray(f);
                } catch(FileNotFoundException e) {
                    System.out.println("File not found, please try again.");
                    e.printStackTrace();
                }
                sign = false;
                // call animation to print
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        JFrame frame = new JFrame("Movie Simulation");
                        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        MovieUI ui = new MovieUI();
                        frame.add(ui);
                        frame.pack();
                        frame.setLocationRelativeTo(null);
                        frame.setVisible(true);
                    }
                });
            } else if(option == 0) {
                sign = false;
                return;
            } else {
                System.out.println("ERROR: invalid number, please try again");
            }
        }        
    }

    public static void fullSimulate() {
        boolean sign = true;
        while(sign) {
            System.out.println("You are in full simulation module, please select operations. ");
            System.out.println("Press [1] to input the filename and start function. Press [0] to return to main menu.");
            int option = Integer.parseInt(ElevatorSimulation.inputScanner.nextLine());

            if(option == 1) {
                System.out.println("Please enter the file name, including the extension name");
                String fileName = ElevatorSimulation.inputScanner.nextLine();
                File f = new File(System.getProperty("user.dir")+"/UserRequests/"+fileName);
                System.out.println(f.getAbsolutePath());

                DataImport dataImport = new DataImport();

                dataImport.initSimulation();
                ElevatorSimulation.time = 0;
                try {
                    dataImport.loadUserCallArray(f);
                } catch(FileNotFoundException e) {
                    System.out.println("File not found, please try again.");
                    e.printStackTrace();
                }
                File resultFile = new File(System.getProperty("user.dir")+"/SimulationFiles/"+fileName.substring(0, fileName.length()-4)+"_full.txt");
                try {
                    resultFile.createNewFile();
                    FileWriter writer = new FileWriter(resultFile);
                    new DataExport().importUserCall(writer);
                    new DataExport().importSimulateParam(writer);
                    writer.close();
                    sign = false;
                    EventQueue.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            JFrame frame = new JFrame("Full Simulation");
                            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
            } else if(option == 0) {
                sign = false;
                return;
            } else {
                System.out.println("ERROR: invalid number, please try again");
            }
        }
    }

    public static void historySimulate() {
        boolean sign = true;
        while(sign) {
            System.out.println("You are in history simulation module, please select operations. ");
            System.out.println("Press [1] to input the filename and start function. Press [0] to return to main menu.");
            int option = Integer.parseInt(ElevatorSimulation.inputScanner.nextLine());
            if(option == 1) {
                try {
                    System.out.println("Please enter the file name, including the extension name");
                    String fileName = ElevatorSimulation.inputScanner.nextLine();
                    File f = new File(System.getProperty("user.dir")+"/SimulationFiles/"+fileName);
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
                    // 3. start simulation
                    ElevatorSimulation.time = 0;
                    sign = false;
                    EventQueue.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            JFrame frame = new JFrame("History Simulation");
                            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                            HistoryUI ui = new HistoryUI(scanner);
                            frame.add(ui);
                            frame.pack();
                            frame.setLocationRelativeTo(null);
                            frame.setVisible(true);
                        }
                    });
                } catch(FileNotFoundException e) {
                    System.out.println("File not found, please try again.");
                    e.printStackTrace();
                }

            } else if(option == 0) {
                sign = false;
                return;
            } else {
                System.out.println("ERROR: invalid number, please try again");
            }
        }
    }

    public static void paramConf() {
        boolean sign = true;
        while(sign) {
            System.out.println("You are in parameter configuration module, please select operations. ");
            System.out.println("[1] Configure request file directory");
            System.out.println("[2] Configure simulation file directory");
            System.out.println("[3] Configure elevator height");
            System.out.println("[4] Configure delay time");
            System.out.println("[0] return to main menu");

            int option = Integer.parseInt(ElevatorSimulation.inputScanner.nextLine());
            System.out.println("Your input: "+option);
            switch(option) {
                case 1:
                    String dir = "";
                    if(ElevatorSimulation.sysParam.configs.containsKey("UserRequestFilesPath")) {
                        dir = ElevatorSimulation.sysParam.configs.get("UserRequestFilesPath");
                    }
                    System.out.println("The current request file directory is "+dir);
                    boolean sign2 = true;
                    while(sign2) {
                        System.out.println("Please enter a new directory: ");
                        String newDir = ElevatorSimulation.inputScanner.nextLine();
                        newDir = newDir.trim().replace("\n", "");
                        boolean exists = Files.exists(Paths.get(System.getProperty("user.dir")+"/"+newDir));
                        if(exists) {
                            ElevatorSimulation.sysParam.configs.replace("UserRequestFilesPath", newDir);
                            sign2 = false;
                        } else {
                            System.out.println("ERROR: directory does not exist");
                        }
                    }
                    break;
                case 2:
                    dir = "";
                    if(ElevatorSimulation.sysParam.configs.containsKey("SimulationFilesPath")) {
                        dir = ElevatorSimulation.sysParam.configs.get("SimulationFilesPath");
                    }
                    System.out.println("The current request file directory is "+dir);
                    sign2 = true;
                    while(sign2) {
                        System.out.println("Please enter a new directory: ");
                        String newDir = ElevatorSimulation.inputScanner.nextLine();
                        
                        newDir = newDir.trim().replace("\n", "");
                        boolean exists = Files.exists(Paths.get(System.getProperty("user.dir")+"/"+newDir));
                        if(exists) {
                            ElevatorSimulation.sysParam.configs.replace("SimulationFilesPath", newDir);
                            sign2 = false;
                        } else {
                            System.out.println("ERROR: directory does not exist");
                        }
                    }
                    break;
                case 3:
                    String value = "";
                    if(ElevatorSimulation.sysParam.configs.containsKey("ElevatorHeight")) {
                        value = ElevatorSimulation.sysParam.configs.get("ElevatorHeight");
                    }
                    System.out.println("The current value is "+value);
                    sign2 = true;
                    while(sign2) {
                        System.out.println("Please enter a new value: ");
                        String newValue = ElevatorSimulation.inputScanner.nextLine();
                        try {
                            int val = Integer.parseInt(newValue);
                            if(val > 2) {
                                sign2 = false;
                                ElevatorSimulation.sysParam.configs.replace("ElevatorHeight", newValue);
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("ERROR: Not an Integer");
                        }
                    }
                    break;
                case 4:
                    value = "";
                    if(ElevatorSimulation.sysParam.configs.containsKey("DelayTime")) {
                        value = ElevatorSimulation.sysParam.configs.get("DelayTime");
                    }
                    System.out.println("The current value is "+value);
                    sign2 = true;
                    while(sign2) {
                        System.out.println("Please enter a new value: ");
                        String newValue = ElevatorSimulation.inputScanner.nextLine();
                        try {
                            int val = Integer.parseInt(newValue);
                            if(val > 0) {
                                sign2 = false;
                                ElevatorSimulation.sysParam.configs.replace("DelayTime", newValue);
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("ERROR: Not an Integer");
                        }
                    }
                    break;
                case 0:
                    sign = false;
                    break;
            }
        }
    }
}
