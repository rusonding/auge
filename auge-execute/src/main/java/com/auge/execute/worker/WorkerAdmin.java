package com.auge.execute.worker;

import io.netty.channel.Channel;

import java.util.*;

/**
 * Created by lixun on 2017/6/23.
 */
public class WorkerAdmin {
    public static Worker getFreeWorker(Map<String, Worker> workers) {
        if (workers.size() > 0) {
            Map<String, Worker> workerMap = sortWorkerByExecutorNum(workers);
            Worker worker = workerMap.values().iterator().next();
            return worker;
        }
        return null;
    }

    public static String getWorkerId(Channel channel) {
        String workerId = channel.remoteAddress().toString().replace("/", "");
        return workerId;
    }

    public static Map<String, Worker> sortWorkerByExecutorNum(Map<String, Worker> oriMap) {
        Map<String, Worker> sortedMap = new LinkedHashMap<String, Worker>();
        if (oriMap != null && !oriMap.isEmpty()) {
            List<Map.Entry<String, Worker>> entryList = new ArrayList<Map.Entry<String, Worker>>(oriMap.entrySet());
            Collections.sort(entryList,
                    new Comparator<Map.Entry<String, Worker>>() {
                        public int compare(Map.Entry<String, Worker> entry1,
                                           Map.Entry<String, Worker> entry2) {
                            int value1 = entry1.getValue().getExecutorNum();
                            int value2 = entry2.getValue().getExecutorNum();
                            return value1 - value2;
                        }
                    });
            Iterator<Map.Entry<String, Worker>> iter = entryList.iterator();
            Map.Entry<String, Worker> tmpEntry = null;
            while (iter.hasNext()) {
                tmpEntry = iter.next();
                sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());
            }
        }
        return sortedMap;
    }

}
