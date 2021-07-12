package org.techfrog.fileprocessingservice.domain;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task implements Serializable {

    public enum TaskStatus {
        PENDING,
        PROCESSING,
        FAILED,
        FINISHED
    }

    private String taskId;
    private String creationDate;
    private String fileId;
    private TaskStatus status;
    private TaskResult result;

}
