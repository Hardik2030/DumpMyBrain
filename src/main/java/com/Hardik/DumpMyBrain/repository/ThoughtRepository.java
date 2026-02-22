package com.Hardik.DumpMyBrain.repository;

import com.Hardik.DumpMyBrain.entity.ThoughtEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThoughtRepository extends JpaRepository<ThoughtEntry, Long> {
    // save(), findById(), aur delete() due to jpa repository
}