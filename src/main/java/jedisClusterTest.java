import org.junit.Test;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class jedisClusterTest {

    @SuppressWarnings("deprecation")
    @Test
    public void test() {
        Set<HostAndPort> clusterNodes = new HashSet<HostAndPort>();
        clusterNodes.add(new HostAndPort("192.168.56.100", 6379));
        JedisCluster cluster = new JedisCluster(clusterNodes);


        List<HostAndPort> clusterHostAndPortList = new ArrayList<HostAndPort>();
        clusterHostAndPortList.add(new HostAndPort("192.168.56.101", 6379));
        clusterHostAndPortList.add(new HostAndPort("192.168.56.102", 6379));
//        clusterHostAndPortList.add(new HostAndPort("192.168.56.103", 6379));
//        clusterHostAndPortList.add(new HostAndPort("192.168.56.104", 6379));
//        clusterHostAndPortList.add(new HostAndPort("192.168.56.105", 6379));

        HostAndPort nodeInfo1 = clusterHostAndPortList.get(0);
        HostAndPort nodeInfo2 = clusterHostAndPortList.get(1);

        Jedis  node1 = new Jedis(nodeInfo1.getHost(), nodeInfo1.getPort());
        node1.connect();
        node1.flushAll();

        Jedis  node2 = new Jedis(nodeInfo2.getHost(), nodeInfo2.getPort());
        node2.connect();
        node2.flushAll();


        for(int i=0 ; i < 100; i++){
            cluster.set(Integer.toString(i), Integer.toString(i));
        }
        for(int i=0 ; i < 100; i++){
            System.out.println("["+i+"]------- get --------" + cluster.get(Integer.toString(i)));
        }

        // get cluster nodes
        System.out.println("------- cluster nodes --------"+cluster.getClusterNodes().values().size());
    }
}