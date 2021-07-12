package org.techfrog.fileprocessingworker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class MessageSubscriber implements MessageListener {

    private static final ExecutorService THREAD_POOL = Executors.newFixedThreadPool(10);
    private FileProcessor fileProcessor;

    @Autowired
    public MessageSubscriber(FileProcessor fileProcessor) {
        this.fileProcessor = fileProcessor;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        THREAD_POOL.submit(() -> {
            fileProcessor.processFile(new String(message.getBody()));
        });
    }
}
