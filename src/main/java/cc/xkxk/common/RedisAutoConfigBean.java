package cc.xkxk.common;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisAutoConfigBean implements MethodInterceptor, FactoryBean<Object>, InitializingBean {
	// private final static Logger logger =
	// LoggerFactory.getLogger(RedisAutoConfigBean.class);
	private JedisPool jedisPool;
	private Object proxy;
	private ThreadLocal<Integer> thread = new ThreadLocal<>();

	public RedisAutoConfigBean(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}

	@Override
	public Object getObject() throws Exception {
		return proxy;
	}

	@Override
	public Class<?> getObjectType() {
		return RedisOps.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		if ("select".equals(invocation.getMethod().getName())) {
			thread.set((Integer) invocation.getArguments()[0]);
			return "1";
		}
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.select((thread.get() == null) ? 0 : thread.get());
			Object obj = invocation.getMethod().invoke(jedis, invocation.getArguments());
			return obj;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		ProxyFactory pf = new ProxyFactory(new RedisOps());
		pf.setProxyTargetClass(true);
		pf.addAdvice(this);
		proxy = pf.getProxy();
	}
}
