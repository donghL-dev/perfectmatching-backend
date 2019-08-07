package com.matching.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Data
@Table
@NoArgsConstructor
public class Comment implements Serializable {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @ManyToOne(fetch = FetchType.EAGER)
    private User writer;

    @ManyToOne(fetch = FetchType.EAGER)
    private Project project;

    @Column
    private String content;

    @Column
    private LocalDateTime createdDate;

    @Column
    private LocalDateTime modifiedDate;

    @Builder
    public Comment(User writer, Project project, String content, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.writer = writer;
        this.project = project;
        this.content = content;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }
}
