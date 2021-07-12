package org.techfrog.fileprocessingservice.domain;

import lombok.*;

import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskResult implements Serializable {

    private Set<String> ips;
}
