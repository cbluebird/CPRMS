package team.sugarsmile.cprms.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.io.InputStream;
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
        jedis.auth(properties.getProperty("password"));
        jedis.select(Integer.parseInt(properties.getProperty("db")));
        return jedis;
    }

    public static void close(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }
}

