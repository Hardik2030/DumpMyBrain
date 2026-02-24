package com.Hardik.DumpMyBrain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "thoughts")
@Getter
@Setter
@NoArgsConstructor
public class ThoughtEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String rawThought;

    @ElementCollection
    private List<String> facts;

    private String detectedBias;
    private int clarityScore;
    private LocalDateTime createdAt;

    @Column(columnDefinition = "TEXT")
    private String aiAdvice;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}