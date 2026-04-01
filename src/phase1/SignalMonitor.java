package phase1;
import java.util.*;
public class SignalMonitor {
    private static final int WINDOW_SIZE=5;//reading per signal per zone
    //separate SW for each signal type
    private HashMap<String,ArrayDeque<Double>> envSignalWindow;
    private HashMap<String,ArrayDeque<Double>> sosWindow;
    private HashMap<String,ArrayDeque<Double>> infraWindow;  

    public SignalMonitor()
    {
        envSignalWindow=new HashMap<>();
        sosWindow=new HashMap<>();
        infraWindow=new HashMap<>();
    }

    //creating empty deque for each signal 
    public void initZone(String zoneName)
    {
        envSignalWindow.put(zoneName,new ArrayDeque<>());
        
    }
}
