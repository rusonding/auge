package test;

import java.util.*;

/**
 * Created by lixun on 2017/6/24.
 */
public class Test {

    private int num;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public static Map<String, String> sortMapByValue(Map<String, String> oriMap) {
        Map<String, String> sortedMap = new LinkedHashMap<String, String>();
        if (oriMap != null && !oriMap.isEmpty()) {
            List<Map.Entry<String, String>> entryList = new ArrayList<Map.Entry<String, String>>(oriMap.entrySet());
            Collections.sort(entryList,
                    new Comparator<Map.Entry<String, String>>() {
                        public int compare(Map.Entry<String, String> entry1,
                                           Map.Entry<String, String> entry2) {
                            int value1 = 0, value2 = 0;
                            try {
                                value1 = Integer.parseInt(entry1.getValue());
                                value2 = Integer.parseInt(entry2.getValue());
                            } catch (NumberFormatException e) {
                                value1 = 0;
                                value2 = 0;
                            }
                            return value2 - value1;
                        }
                    });
            Iterator<Map.Entry<String, String>> iter = entryList.iterator();
            Map.Entry<String, String> tmpEntry = null;
            while (iter.hasNext()) {
                tmpEntry = iter.next();
                sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());
            }
        }
        return sortedMap;
    }


    public static Map<String, Test> sortMapByValue2(Map<String, Test> oriMap) {
        Map<String, Test> sortedMap = new LinkedHashMap<String, Test>();
        if (oriMap != null && !oriMap.isEmpty()) {
            List<Map.Entry<String, Test>> entryList = new ArrayList<Map.Entry<String, Test>>(oriMap.entrySet());
            Collections.sort(entryList,
                    new Comparator<Map.Entry<String, Test>>() {
                        public int compare(Map.Entry<String, Test> entry1,
                                           Map.Entry<String, Test> entry2) {
                            int value1 = 0, value2 = 0;
                            try {
                                value1 = entry1.getValue().getNum();
                                value2 = entry2.getValue().getNum();
                            } catch (NumberFormatException e) {
                                value1 = 0;
                                value2 = 0;
                            }
                            return value1 - value2;
                        }
                    });
            Iterator<Map.Entry<String, Test>> iter = entryList.iterator();
            Map.Entry<String, Test> tmpEntry = null;
            while (iter.hasNext()) {
                tmpEntry = iter.next();
                sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());
            }
        }
        return sortedMap;
    }


    public static void main(String[] args) {
        Map<String,Test> map = new HashMap<String, Test>();
        Test t = new Test();
        t.setNum(2);
        map.put("a", t);
        t = new Test();
        t.setNum(1);
        map.put("c", t);
        t = new Test();
        t.setNum(11);
        map.put("b", t);
        t = new Test();
        t.setNum(3);
        map.put("e", t);
        t = new Test();
        t.setNum(4);
        map.put("g", t);
//        t = new Test();
//        t.setNum(2);
        map.put("af", t);
        System.out.println(map);
        Map<String, Test> stringStringMap = sortMapByValue2(map);
        System.out.println(stringStringMap);
        System.out.println(stringStringMap.values().iterator().next());
    }

    @Override
    public String toString() {
        return "Test{" +
                "num=" + num +
                '}';
    }
}
