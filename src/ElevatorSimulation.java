public class ElevatorSimulation {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("usage: you should add at least 1 parameter for a command.");
        } else {
            int option = Integer.parseInt(args[0]);
            switch (option) {
                case 1:
                    System.out.println("option 1");
                    // silent simulation
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
                default:
                    // other options
                    break;
            }
        }
    }
}
