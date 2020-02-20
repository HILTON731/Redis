import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPipeline;

public class MovieLens21MPipeline {
    ShardedJedis jedis;

    public MovieLens21MPipeline() {
        JedisShardInfo si = new JedisShardInfo("192.168.56.100", 6379, 5000);
        List<JedisShardInfo> list = new ArrayList<JedisShardInfo>();
        list.add(si);
        list.add(new JedisShardInfo("192.168.56.101", 6379, 5000));
        list.add(new JedisShardInfo("192.168.56.102", 6379, 5000));

        jedis = new ShardedJedis(list);
    }

    public void push() {
        ShardedJedisPipeline pipeline = jedis.pipelined();
        Scanner s;

        try {
            s = new Scanner(new File("C:/Users/tony9/IdeaProjects/redis/src/main/java/ratings.csv"));

            StringBuilder key = new StringBuilder();
            String s1 = s.nextLine(); // Skip first line
            while (s.hasNextLine()) {
                s1 = s.nextLine();
                String[] spl = s1.split(",");
                // key="u:"+spl[0]+":m";
                key.append("u:").append(spl[0]).append(":m");
                pipeline.sadd(key.toString(), spl[1]);
                key.setLength(0);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("bye" + e.getMessage());
        }

        pipeline.sync();
    }

    public static void main(String[] args) {
        MovieLens21MPipeline obj = new MovieLens21MPipeline();
        long startTime = System.currentTimeMillis();
        obj.push();
        long endTime = System.currentTimeMillis();
        double d = 0.0;
        d = (double) (endTime - startTime);
        System.out.println("Throughput: " + d);
    }
}
