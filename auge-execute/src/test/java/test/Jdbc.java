package test;

import com.auge.trigger.TriggerStatus;

/**
 * Created by lixun on 2017/6/26.
 */
public class Jdbc {
    public static void main(String[] args) {
        System.out.println(TriggerStatus.READY.getNumVal());
        System.out.println(TriggerStatus.fromInteger(TriggerStatus.READY.getNumVal()));

    }
}
