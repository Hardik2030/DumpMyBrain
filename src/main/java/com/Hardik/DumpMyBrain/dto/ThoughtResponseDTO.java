package com.Hardik.DumpMyBrain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ThoughtResponseDTO {
    private Long id;
    private int clarityScore;
    private List<String> facts;
    private String rawThought;
    private String aiAdvice;
}