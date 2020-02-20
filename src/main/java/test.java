import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class test {

    public static void main(String[] args) {
        // TODO Auto-generated method stub

        String host = "192.168.56.100";
        int port = 6379;
        int timeout = 3000;
        int db = 2;
        String AUTH = "redis";

        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();

        JedisPool pool = new JedisPool(jedisPoolConfig, host, port, timeout, null, db);

        Jedis jedis = pool.getResource();

        // list 형태로 입력
        jedis.lpush("id", "choi");
        jedis.lpush("id", "keach");
        jedis.lpush("id", "hoo");

        // 첫번 째 요소를 제거하고 꺼냄
        System.out.println(jedis.lpop("id"));
        // 마지막 요소를 제거하고 꺼냄
        System.out.println(jedis.rpop("id"));

        // hash 형태로 입력
        Map<String, String> score = new HashMap<>();
        score.put("kim", "220");
        score.put("park", "240");

        jedis.hmset("score", score);

        Map<String, String> getMap = jedis.hgetAll("score");

        Set<String> key = getMap.keySet();

        Iterator<String> keyIter = key.iterator();

        while (keyIter.hasNext()) {
            System.out.println(getMap.get(keyIter.next()));
        }

        // Set 형태로 입력
        jedis.sadd("user", "user00");
        jedis.sadd("user", "user01");
        jedis.sadd("user", "user02");

        Set<String> user = jedis.smembers("user");

        Iterator<String> iter = user.iterator();

        while (iter.hasNext()) {
            System.out.println(iter.next());
        }


        // Sorted Set 형태로 입력
        jedis.zadd("sortedUser", 1000, "user00");
        jedis.zadd("sortedUser", 1048, "user01");
        jedis.zadd("sortedUser", 1024, "user02");


        if (jedis != null) {
            jedis.close();
        }
        pool.close();

    }

}

