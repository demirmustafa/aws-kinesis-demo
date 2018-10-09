package io.github.demirmustafa.core.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Builder
@ToString
public class UserEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long userId;
    private String eventType;
    private String detail;
}
