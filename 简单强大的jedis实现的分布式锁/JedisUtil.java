package distributedLock;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import java.util.UUID;

/**
 * @program: test
 * @description:
 * @author: yang.wd
 * @version: v1.0
 * @create: 2019-01-24 14:20
 **/
public class JedisUtil {

    private Jedis jedis;

    private String value = UUID.randomUUID().toString();

    public JedisUtil(Jedis j) {
        System.out.println(value);
        this.jedis = j;
    }

    public boolean tryLock() {

        SetParams param = new SetParams();
        param.nx();
        param.px(1000);
        String lock = jedis.set("lock", value, param);
        if (lock != null && "OK".equals(lock)) {
            System.out.println(value + "拿到了锁");
            return true;
        }
        return false;
    }

    public void unLock() {
        jedis.eval("if redis.call('get', KEYS[1])==ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end", 1, "lock", value);
        System.out.println(value + "丢弃了锁");
    }

}
