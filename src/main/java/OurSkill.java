// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
/**
 * @author Molly Sandler, Andy Duong, Ivan Martinez.
 */
public class OurSkill implements PropertyChangeListener, Runnable {
    private static final Map<String, String> skillMap = new HashMap<>();
    private SerialPort robotConnection;
    private final Queue<String> commandQueue = new LinkedList<>();

    public static String getSkillValue(String skillKey) {
        return (String)skillMap.get(skillKey);
    }

    public void connectBluetooth(){
        robotConnection = SerialPort.getCommPort("COM5"); //port to which robot is communicating
        robotConnection.openPort();
    }

    @Override
    public void run() {
        robotConnection.addDataListener(new SerialPortDataListener() {
            @Override
            public int getListeningEvents() { return SerialPort.LISTENING_EVENT_DATA_AVAILABLE; }
            @Override
            public void serialEvent(SerialPortEvent event)
            {
                if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE) {
                    System.out.println("close");
                    return;
                }
                byte[] newData = new byte[robotConnection.bytesAvailable()];
                int numRead = robotConnection.readBytes(newData, newData.length);
                System.out.println("Read " + numRead + " bytes.");
            }
        });
        List<String> skillSet = new ArrayList<String>(); //array list where the chosen skills are stored

        /////// MUST CHANGE PORT DESCRIPTOR TO THE SPECIFIC OUTGOING SERIAL PORT THE ROBOT IS CONNECTED TO /////////

        for(String command : commandQueue){
            String commandSerial = getSkillValue(command); //gets the serialCommand from each input
            if(command == null){
                continue;
            }

            if (command.equalsIgnoreCase("exit")) { //if an exit is called close the serial port
                robotConnection.closePort();
                return; //loops until an exit is called
            }
//            else if (command.equalsIgnoreCase("camera")){
//                Dog.Camera robotCamera = new Dog.Camera(robotConnection);
//                robotCamera.readCamera();
//            }
            else if (command.equalsIgnoreCase("perform")){ //performs the tasks and clears the skill set
                try {
                    PerformSkills(skillSet, robotConnection);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                commandQueue.clear();
                skillSet.clear();
                printAllSkills();
                WorldData.getWorldData().setGameState(true);
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            else if (commandSerial == null){ //if a command is not found let the user know
                System.out.println("Not a real skill!");
            }
            else{

                skillSet.add(commandSerial); //adds the command serial to the skill set
                System.out.println("Current skill map:"); //prints the current set of instructions
                for (String skill : skillSet) {
                    System.out.print(skill + ", ");
                }
                System.out.println();
            }
        }
    }

    //send all the desired skills in the skillSet to the robot to be performed
    public void PerformSkills(List<String> skillSet, SerialPort robotConnection) throws IOException {

        OutputStream outputStream = robotConnection.getOutputStream(); //creates an output stream to the serial port
        long duration = 5000L;
        long startTime = System.currentTimeMillis();
            while(System.currentTimeMillis() - startTime < duration) {
                for (String currentSkill : skillSet) {
                    outputStream.write(currentSkill.getBytes());
                    System.out.println("task executed");

                    try {
                        Thread.sleep(2000L);
                    } catch (InterruptedException sleepError) {
                        sleepError.printStackTrace();
                    }
                }
                startTime = 5001L;
            }
    }

    public void printAllSkills(){ //prints all the possible skills to choose from
        System.out.println(skillMap.keySet());
    }

    static {    //adds all inputs and code to the skill map
        skillMap.put("stand", "kup");
        skillMap.put("butt_up", "kbuttUp");
        skillMap.put("calibrate_pose", "kcalib");
        skillMap.put("lift_neck", "klifted");
        skillMap.put("landing_pose", "klnd");
        skillMap.put("drop_legs", "kdropped");
        skillMap.put("rest", "krest");
        skillMap.put("sit", "ksit");
        skillMap.put("stretch", "kstr");
        skillMap.put("walk", "kwkF");
        skillMap.put("walk_left", "kwkL");
        skillMap.put("walk_right", "kwkR");
        skillMap.put("crawl", "kcrF");
        skillMap.put("crawl_left", "kcrL");
        skillMap.put("crawl_right", "kcrR");
        skillMap.put("cheers", "kchr");
        skillMap.put("wave", "khi");
        skillMap.put("handstand", "khds");
        skillMap.put("look_around", "kck");
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch ( evt.getPropertyName() ){
            case "commands":
                List<String> commands = (List<String>) evt.getNewValue();
                commandQueue.addAll(commands);
                System.out.println(commandQueue);
                this.run();
        }
    }
}

/*
ALL POSSIBLE INPUTS FROM OPENCAT
['kbalance','kbuttUp','kcalib','kdropped','klifted','klnd','krest','ksit','kstr',
'kup','kzero','kang','kbx','kchr','kck','kcmh','kdg','kfiv','kgdb','khds','khg','khi',
'khsk','khu','kjmp','kkc','kmw','knd','kpd','kpee','kpu','kpu1','krc','kscrh','ksnf','ktbl','kts','kwh','kzz'
 */