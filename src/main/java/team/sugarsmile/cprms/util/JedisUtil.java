package team.sugarsmile.cprms.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Transaction;
import team.sugarsmile.cprms.exception.ErrorCode;
import team.sugarsmile.cprms.exception.SystemException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

/**
 * @author XiMo
 */

public final class JedisUtil {
    private static JedisPool jedisPool;
    private static Properties properties;

    static {
        properties = new Properties();
        InputStream inputStream = JedisUtil.class.getClassLoader().getResourceAsStream("jedis.properties");

        try {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(Integer.parseInt(properties.getProperty("maxTotal")));
        poolConfig.setMaxIdle(Integer.parseInt(properties.getProperty("maxIdle")));

        jedisPool = new JedisPool(poolConfig, properties.getProperty("host"), Integer.parseInt(properties.getProperty("port")));
    }

    public static Jedis getJedis() {
        Jedis jedis = jedisPool.getResource();
        String password = properties.getProperty("password");
        if (!Objects.equals(password, "")) {
            jedis.auth(properties.getProperty("password"));
        }
        jedis.select(Integer.parseInt(properties.getProperty("db")));
        return jedis;
    }

    public static void close(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }

    public static Integer getLoginKey(String username) {
        Jedis redis = null;
        try {
            redis = JedisUtil.getJedis();
            String loginKey = username + "_login_key";

            // 使用Redis事务来保证操作的原子性
            redis.watch(loginKey);
            String currentValue = redis.get(loginKey);

            if (currentValue != null) {
                return Integer.parseInt(currentValue);
            }
        } catch (Exception e) {
            if (redis != null) {
                redis.unwatch();
            }
            throw new SystemException(ErrorCode.REDIS_ERROR.getCode(), e.getMessage(), e);
        } finally {
            close(redis);
        }
        return 0;
    }

    public static void incrementLoginKey(String username) {
        Jedis redis = null;
        try {
            redis = JedisUtil.getJedis();
            String loginKey = username + "_login_key";

            // 使用Redis事务来保证操作的原子性
            redis.watch(loginKey);
            String currentValue = redis.get(loginKey);

            Transaction transaction = redis.multi();
            if (currentValue != null) {
                transaction.incr(loginKey);
            } else {
                transaction.set(loginKey, "1");
            }
            transaction.expire(loginKey, 1800);// 设置过期时间为30分钟
            // 提交事务
            transaction.exec();
        } catch (Exception e) {
            if (redis != null) {
                redis.unwatch();
            }
            throw new SystemException(ErrorCode.REDIS_ERROR.getCode(), e.getMessage(), e);
        } finally {
            close(redis);
        }
    }

    public static void delLoginKey(String username) {
        Jedis redis = null;
        try {
            redis = JedisUtil.getJedis();
            String loginKey = username + "_login_key";

            // 使用Redis事务来保证操作的原子性
            redis.watch(loginKey);
            String currentValue = redis.get(loginKey);

            Transaction transaction = redis.multi();
            if (currentValue != null) {
                transaction.del(loginKey);
            }
            // 提交事务
            transaction.exec();
        } catch (Exception e) {
            if (redis != null) {
                redis.unwatch();
            }
            throw new SystemException(ErrorCode.REDIS_ERROR.getCode(), e.getMessage(), e);
        } finally {
            close(redis);
        }
    }
}

