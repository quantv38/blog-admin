package com.doomdev.admin_blog.entities;

import com.doomdev.admin_blog.contants.enums.ImageType;
import com.doomdev.admin_blog.contants.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "images")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "post_id", nullable = false)
    private Long postId;

    @Size(max = 255)
    @NotNull
    @ColumnDefault("''")
    @Column(name = "link", nullable = false)
    private String link;

    @NotNull
    @Column(name = "status", nullable = false)
    private Status status;

    @NotNull
    @Column(name = "type", nullable = false)
    private ImageType type;
}