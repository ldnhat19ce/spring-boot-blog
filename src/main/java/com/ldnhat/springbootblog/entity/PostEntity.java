package com.ldnhat.springbootblog.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "posts", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"title"})
})
public class PostEntity {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "content", nullable = false)
    private String content;

    @OneToMany(mappedBy = "postEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CommentEntity> commentEntities;
}
