
## HasMap 无序
```java
Map<String, String> hashMap = new HashMap<String, String>();

hashMap.put("name1", "josan1");
hashMap.put("name2", "josan2");
hashMap.put("name3", "josan3");

Set<Entry<String, String>> set = hashMap.entrySet();
Iterator<Entry<String, String>> iterator = set.iterator();

while(iterator.hasNext()) {
		Entry entry = iterator.next();
		String key = (String) entry.getKey();
		String value = (String) entry.getValue();
		System.out.println("key:" + key + ",value:" + value);
}

key:name3,value:josan3
key:name2,value:josan2
key:name1,value:josan1


我们是按照xxx1、xxx2、xxx3的顺序插入的，但是输出结果并不是按照顺序的。
```

## LinkedHashMap应用场景
```java
Map<String, String> linkedHashMap = new LinkedHashMap<>();
linkedHashMap.put("name1", "josan1");
linkedHashMap.put("name2", "josan2");
linkedHashMap.put("name3", "josan3");
Set<Entry<String, String>> set = linkedHashMap.entrySet();
Iterator<Entry<String, String>> iterator = set.iterator();
while(iterator.hasNext()) {
		Entry entry = iterator.next();
		String key = (String) entry.getKey();
		String value = (String) entry.getValue();
		System.out.println("key:" + key + ",value:" + value);
}

key:name1,value:josan1
key:name2,value:josan2
key:name3,value:josan3
```

## TreeMap的用法,主要是排序
TreeMap中默认的排序为升序，如果要改变其排序可以自己写一个Comparator
```java
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;


public class Compare {
    public static void main(String[] args) {
        TreeMap<String,Integer> map = new TreeMap<String,Integer>(new xbComparator());
        map.put("key_1", 1);
        map.put("key_2", 2);
        map.put("key_3", 3);   
        Set<String> keys = map.keySet();
        Iterator<String> iter = keys.iterator();
        while(iter.hasNext())
        {
                String key = iter.next();
                System.out.println(" "+key+":"+map.get(key));
        }
    }
}

class xbComparator implements Comparator{

    public int compare(Object o1,Object o2)
    {
        String i1=(String)o1;
        String i2=(String)o2;
        return -i1.compareTo(i2);
    }
}

key_3:3
key_2:2
key_1:1
```
