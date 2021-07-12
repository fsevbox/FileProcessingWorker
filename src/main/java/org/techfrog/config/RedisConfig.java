package org.techfrog.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.techfrog.fileprocessingworker.service.FileProcessor;
import org.techfrog.fileprocessingworker.service.MessageSubscriber;

import java.time.Duration;

@Configuration
public class RedisConfig {

    @Bean
    public RedisConnectionFactory redisConnectionFactory(RedisProps redisProps) {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(redisProps.getHostname());
        config.setPort(redisProps.getPort());
        config.setDatabase(redisProps.getDatabase());
        config.setPassword(RedisPassword.of(redisProps.getPassword()));

        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
                .commandTimeout(Duration.ofMillis(redisProps.getTimeout()))
                .build();

        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(config, clientConfig);
        return lettuceConnectionFactory;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(
            RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);

        template.setKeySerializer(new StringRedisSerializer());
        GenericJackson2JsonRedisSerializer jacksonSerializer = new GenericJackson2JsonRedisSerializer();
        template.setValueSerializer(jacksonSerializer);
        template.setHashKeySerializer(jacksonSerializer);
        template.setHashValueSerializer(jacksonSerializer);
        return template;
    }

    @Bean
    public ChannelTopic queue(@Value("${pubsub.tasks.queue}") String queueName) {
        return new ChannelTopic(queueName);
    }

    @Bean
    public MessageListenerAdapter messageListener(FileProcessor fileProcessor) {
        return new MessageListenerAdapter(new MessageSubscriber(fileProcessor));
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(
            RedisConnectionFactory redisConnectionFactory,
            MessageListenerAdapter messageListener,
            ChannelTopic queue) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        container.addMessageListener(messageListener, queue);
        return container;
    }

}
