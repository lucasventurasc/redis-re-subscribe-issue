package io.lettuce.redis.resubscribe;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.pubsub.RedisPubSubAdapter;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import io.lettuce.core.pubsub.api.sync.RedisPubSubCommands;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		RedisClient redisClient = RedisClient.create("redis://localhost:6379/0");
		configureNotifyKeySpaceEventsTo_Elx(redisClient);

		StatefulRedisPubSubConnection<String, String> pubSubConnection = redisClient.connectPubSub();
		pubSubConnection.addListener(new RedisSusbcribptionConsolePrinterListener());

		Runnable subscriberThread = () -> {
			try {
				RedisPubSubCommands<String, String> sync = pubSubConnection.sync();
				sync.psubscribe("__key*__:*");
			} catch (Exception e) {
				e.printStackTrace();
			}
		};

		subscriberThread.run();

		while (true) {
			Thread.sleep(1000);
		}
	}

	private static void configureNotifyKeySpaceEventsTo_Elx(RedisClient redisClient) {
		try (StatefulRedisConnection<String, String> connection = redisClient.connect()) {
			RedisCommands<String, String> sync = connection.sync();
			sync.configSet("notify-keyspace-events", "Elx");
		}
	}
}


class RedisSusbcribptionConsolePrinterListener extends RedisPubSubAdapter<String, String> {
	@Override
	public void message(String pattern, String channel, String message) {
		System.out.println(message);
	}
}