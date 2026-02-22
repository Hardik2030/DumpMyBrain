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

    // User ka original messy thought yahan save hoga
    @Column(columnDefinition = "TEXT")
    private String rawThought;

    // AI jo facts nikalega, wo is list mein jayenge
    // Postgres mein iske liye ek separate table banegi piche se
    @ElementCollection
    private List<String> facts;

    // AI jo cognitive bias detect karega (e.g., Overthinking, All-or-nothing)
    private String detectedBias;

    // Dimaag kitna saaf hai (1-10)
    private int clarityScore;

    // Kis waqt dimaag dump kiya tha
    private LocalDateTime createdAt;

    // Data save hone se pehle automatic timestamp lagane ke liye
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}