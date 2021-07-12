package org.techfrog.fileprocessingworker.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;
import org.techfrog.fileprocessingservice.domain.Task;


@Repository
public class FileRepository {

    private static final String HASH_FILES_KEY = "files:";
    private static final String SORTED_FILES_BY_IP = "filesByIP:";

    private ValueOperations<String, Task> valueOperations;
    private ZSetOperations<String, String> sortedSetOperations;

    @Autowired
    public FileRepository(RedisTemplate redisTemplate) {
        this.valueOperations = redisTemplate.opsForValue();
        this.sortedSetOperations = redisTemplate.opsForZSet();
    }

    public Task findTaskByFileId(String id) {
        return valueOperations.get(HASH_FILES_KEY + id);
    }

    public void updateTask(Task task) {
        valueOperations.set(HASH_FILES_KEY + task.getFileId(), task);
    }

    public void addDocumentForIP(String ip, String fileId, double score) {
        sortedSetOperations.add(SORTED_FILES_BY_IP + ip, fileId, score);
    }

}
