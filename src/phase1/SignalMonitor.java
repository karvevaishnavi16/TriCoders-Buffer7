package phase1;
import java.util.*;
public class SignalMonitor {
    private static final int WINDOW_SIZE=5;//reading per signal per zone
    //separate SW for each signal type
    private HashMap<String,ArrayDeque<Double>> envWindow;
    private HashMap<String,ArrayDeque<Double>> sosWindow;
    private HashMap<String,ArrayDeque<Double>> infraWindow;  

    public SignalMonitor()
    {
        envWindow=new HashMap<>();
        sosWindow=new HashMap<>();
        infraWindow=new HashMap<>();
    }

    //creating empty deque for each signal for this zone
    public void initZone(String zoneName)
    {
        envWindow.put(zoneName,new ArrayDeque<>());
        sosWindow.put(zoneName,new ArrayDeque<>());
        infraWindow.put(zoneName, new ArrayDeque<>());
    }

    public void addReading(String zoneName,double env,int sos,double infra)
    {
        //---Environmental signal window
        ArrayDeque<Double> envDeque=envWindow.get(zoneName);
        envDeque.addLast(env);
        if(envDeque.size()>WINDOW_SIZE)
        {
            envDeque.removeFirst();
        }
        //sos count window
        ArrayDeque<Double> sosDeque=sosWindow.get(zoneName);
        sosDeque.addLast((double)sos);
        if(sosDeque.size()>WINDOW_SIZE)
        {
            sosDeque.removeFirst();
        }
        //infrastree count window
        ArrayDeque<Double> infraDeque=infraWindow.get(zoneName);
        infraDeque.addLast(infra);
        if(infraDeque.size()>WINDOW_SIZE)
        {
            infraDeque.removeFirst();
        }

        //return most recent reading for this zone
        public double getLatestEnv(String zoneName) 
        {
        ArrayDeque<Double> deque = envWindow.get(zoneName);
        if (deque == null || deque.isEmpty()) return 0.0;
        return deque.peekLast();
        }

        public double getLatestSos(String zoneName) 
        {
        ArrayDeque<Double> deque = sosWindow.get(zoneName);
        if (deque == null || deque.isEmpty()) return 0.0;
        return deque.peekLast();
        }

        public double getLatestInfra(String zoneName) 
        {
        ArrayDeque<Double> deque = infraWindow.get(zoneName);
        if (deque == null || deque.isEmpty()) return 0.0;
        return deque.peekLast();
        }

        // current readings in the window for a zone 
        public void printWindow(String zoneName) {
        System.out.println("Window for Zone " + zoneName + ":");
        System.out.println("  Env   : " + envWindow.get(zoneName));
        System.out.println("  SOS   : " + sosWindow.get(zoneName));
        System.out.println("  Infra : " + infraWindow.get(zoneName));
    }



    }

    
}s
