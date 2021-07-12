package org.techfrog.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisProps {

    private final String hostname;
    private final int port;
    private final int database;
    private final String password;
    private final long timeout;

    public RedisProps(
            @Value("${redis.hostname}") String hostname,
            @Value("${redis.port}") int port,
            @Value("${redis.database}") int database,
            @Value("${redis.password}") String password,
            @Value("${redis.timeout}") long timeout) {
        this.hostname = hostname;
        this.port = port;
        this.database = database;
        this.password = password;
        this.timeout = timeout;
    }

    public String getHostname() {
        return hostname;
    }

    public int getPort() {
        return port;
    }

    public int getDatabase() {
        return database;
    }

    public String getPassword() {
        return password;
    }

    public long getTimeout() {
        return timeout;
    }
}
