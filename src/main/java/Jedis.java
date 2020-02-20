import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.*;

import java.io.IOException;
import java.util.*;

public class Jedis {
    public static void main(String[] args) {
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxTotal(30);
        config.setMaxWaitMillis(2000);
        Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
        jedisClusterNodes.add(new HostAndPort("192.168.56.100", 6379));//Set clusters
//        jedisClusterNodes.add(new HostAndPort("192.168.56.101", 6379));
//        jedisClusterNodes.add(new HostAndPort("192.168.56.102", 6379));
//        jedisClusterNodes.add(new HostAndPort("192.168.56.103", 6379));
//        jedisClusterNodes.add(new HostAndPort("192.168.56.104", 6379));
//        jedisClusterNodes.add(new HostAndPort("192.168.56.105", 6379));
        JedisCluster jc = new JedisCluster(jedisClusterNodes, config);

        //String part
        jc.set("foo", "bar");//Set Key-Value
        jc.set("test", "value");
        jc.set("52", "poolTestValue52");
        jc.set("53", "poolTestvalue53");
        System.out.println(jc.get("foo"));//Show value of key
        System.out.println(jc.get("test"));
        System.out.println(jc.get("52"));
        System.out.println(jc.get("53"));
        jc.setex("twosec", 5, "take time");//Set Key-Value for 5 sec, After 5 sec it`ll be die
        System.out.println(jc.get("twosec"));
        try{
            Thread.sleep(6000);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        System.out.println(jc.get("twosec"));

        //List part
        jc.lpush("list1", "one", "two", "three", "four");//Make list
        jc.rpush("list2", "four", "three", "two", "one");
        System.out.println("list1: "+jc.lrange("list1",0,-1));//show rane of list
        System.out.println("list2: "+jc.lrange("list2", 0, -1));
        System.out.println("list1 lpop: "+jc.lpop("list1"));//show list`s first value of key and delete it
        System.out.println("list2 rpop: "+jc.rpop("list2"));
        System.out.println("list1: "+jc.lrange("list1",0,-1));//show rane of list
        System.out.println("list2: "+jc.lrange("list2", 0, -1));
//        jc.del("list1");
//        jc.del("list2");

        //Set part
        jc.sadd("nickname", "redis");
        jc.sadd("nickname", "study");
        jc.sadd("nickname", "gawx");
        Set<String> nickname = jc.smembers("nickname");
        Iterator iter = nickname.iterator();
        System.out.println("nickname: ");
        while(iter.hasNext()){
            System.out.println(iter.next());
        }

        //Hash part
        jc.hset("user", "name", "redis student");
        jc.hset("user", "job", "kangwon univ student");
        jc.hset("user", "hobby", "coding");

        System.out.println("Hash");
        System.out.println(jc.hget("user","name"));
        System.out.println("user");
        Map<String, String> fields = jc.hgetAll("user");
        System.out.println(fields.get("job"));

        //Sorted Sets
        System.out.println("Sorted Sets");
        jc.zadd("scores", 1234.0, "PlayerOne");
        jc.zadd("scores", 2345.0,"PlayerTwo");
        jc.zadd("scores", 1356.4,"PlayerThree");

        System.out.println(jc.zrangeByScore("scores", 0 , 10000));


        try {
            jc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
