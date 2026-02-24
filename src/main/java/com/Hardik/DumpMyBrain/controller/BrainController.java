package com.Hardik.DumpMyBrain.controller;

import com.Hardik.DumpMyBrain.dto.ThoughtRequestDTO;
import com.Hardik.DumpMyBrain.dto.ThoughtResponseDTO;
import com.Hardik.DumpMyBrain.service.ThoughtAnalysis;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/thoughts")
public class BrainController {

    private final ThoughtAnalysis thoughtService;

    // Constructor Injection
    public BrainController(ThoughtAnalysis thoughtService) {
        this.thoughtService = thoughtService;
    }

    /**
     *  POST: User se messy thought lega, AI se analyze karwayega aur save karega.
     */
    @PostMapping("/analyze")
    public ResponseEntity<ThoughtResponseDTO> analyze(@RequestBody ThoughtRequestDTO request) {
        // Service ko request bheji aur ResponseDTO wapas liya
        ThoughtResponseDTO response = thoughtService.analyzeAndSave(request);
        return ResponseEntity.ok(response);
    }

    /**
     *  GET: Database se saari history dikhayega (DTO format mein).
     */
    @GetMapping("/history")
    public ResponseEntity<List<ThoughtResponseDTO>> getHistory() {
        return ResponseEntity.ok(thoughtService.getAllSavedThoughts());
    }

    /**
     * GET: Kisi ek specific thought ko ID se fetch karne ke liye.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ThoughtResponseDTO> getById(@PathVariable Long id) {
        // Iska method humne Service mein banaya hai
        // Agar nahi banaya toh bas normal GET call ki tarah return kar do
        return ResponseEntity.ok(thoughtService.getThoughtByIdAsDto(id));
    }
}