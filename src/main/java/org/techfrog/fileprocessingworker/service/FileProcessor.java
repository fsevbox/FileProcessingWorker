package org.techfrog.fileprocessingworker.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.techfrog.fileprocessingservice.domain.Task;
import org.techfrog.fileprocessingservice.domain.TaskResult;
import org.techfrog.fileprocessingworker.repository.FileRepository;

import java.io.IOException;
import java.time.Instant;
import java.util.Set;

@Component
public class FileProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileProcessor.class);
    private FileRepository fileRepository;
    private String fileStoragePath;

    @Autowired
    public FileProcessor(FileRepository fileRepository,
                         @Value("${file.storage.path}") String fileStoragePath) {
        this.fileRepository = fileRepository;
        this.fileStoragePath = fileStoragePath;
    }

    public void processFile(String fileId) {
        Task task = fileRepository.findTaskByFileId(fileId);
        updateTaskStatus(task, Task.TaskStatus.PROCESSING);

        try {
            processTask(task);
        } catch (Exception e) {
            LOGGER.error("File processing failed: " + task.getFileId(), e);
            updateTaskStatus(task, Task.TaskStatus.FAILED);
        }
    }

    private void processTask(Task task) throws IOException {
        Set<String> ips = loadAndParseFile(task.getFileId());

        if (!CollectionUtils.isEmpty(ips)) {
            task.setResult(new TaskResult(ips));
            ips.stream()
                    .forEach((ip) -> {
                        double fileScore = getScoreForFile(task.getCreationDate());
                        fileRepository.addDocumentForIP(ip, task.getFileId(), fileScore);
                    });
        }

        updateTaskStatus(task, Task.TaskStatus.FINISHED);
    }

    private double getScoreForFile(String creationDate) {
        Instant instant = Instant.parse(creationDate);
        return instant.toEpochMilli() + Math.random();
    }

    private Set<String> loadAndParseFile(String taskId) throws IOException {
        String loadedFile = FileLoader.loadFile(fileStoragePath + taskId);
        return IPSearcher.searchForIps(loadedFile);
    }

    private void updateTaskStatus(Task task, Task.TaskStatus status) {
        task.setStatus(status);
        fileRepository.updateTask(task);
    }
}
