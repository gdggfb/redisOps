# redisOps
类似于RedisTemplate的一个东西，基于springboot平台封装的jedis原生api

## idea
公司采用了spring cloud搭建分布式web服务器，spring cloud有很多的配套封装，比如：[spring-data-redis](https://projects.spring.io/spring-data-redis/),这个模型idea非常新颖神奇，本来打算用一用，但是发现其很难在应用中自由切换redis db，于是我封装了一个类似的模型，可以在应用中自由切换db。</br>
**最重要的是，RedisOps通过proxy在切面动态的向pool获取、释放connect，使用者不用频繁的jedis.close()**</br> 
jedis 原生api还是非常好用的 

## code
启用redisOps
```java
@EnableRedisOps
@SpringBootApplication
public class App {
	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}
}
```

使用redisOps
```java
@Autowired
private RedisOps redisOps;

public void fun(){
redisOps.select(4);
redisOps.set("key", "value", "NX", "PX", 1000 * 60);

redisOps.select(6);
redisOps.zadd("key", 0, "value");
}
```

## tips
[RedisAutoConfig](https://github.com/gdggfb/redisOps/blob/master/src/main/java/cc/xkxk/common/RedisAutoConfig.java) 还需要改进，目前还没有读取所以配置参数！
