package com.Hardik.DumpMyBrain.service;

import com.Hardik.DumpMyBrain.dto.ThoughtRequestDTO;
import com.Hardik.DumpMyBrain.dto.ThoughtResponseDTO;
import com.Hardik.DumpMyBrain.entity.ThoughtEntry;
import com.Hardik.DumpMyBrain.repository.ThoughtRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ThoughtAnalysis {
    private final ChatClient chatClient;
    private final ThoughtRepository repository;

    public ThoughtAnalysis(ChatClient.Builder builder, ThoughtRepository repository) {
        this.chatClient = builder.build();
        this.repository = repository;
    }

    public ThoughtResponseDTO analyzeAndSave(ThoughtRequestDTO request) {
        String input = request.getMessyThought();

        String aiResponse = chatClient.prompt()
                .system("You are a wise older brother and a supportive psychologist. " +
                        "Analyze the user's thought. " +
                        "CRITICAL: Match the user's tone exactly in the 'Advice' section. " +
                        "If the user is casual/bro, you be the 'wise brother'. " +
                        "If the user is serious, you be the 'professional psychologist'. " +
                        "If the user uses Hinglish, you MUST use Hinglish in the advice. " +
                        "Keep the Advice extremely short (max 2 sentences). " +
                        "Format response EXACTLY as follows (Do not change labels): " +
                        "Facts: [actionable items separated by comma]; " +
                        "Emotion: [CALM, STRESSED, or INTENSE]; " +
                        "Bias: [number of distortions]; " +
                        "Urgency: [NONE, WATCH, or HIGH]; " +
                        "Advice: [Your short, adaptive, brotherly advice]")
                .user(input)
                .call()
                .content();
        ThoughtEntry entry;
        try {
            entry = ThoughtParser.parseToEntity(aiResponse, input);
        } catch (Exception e) {
            entry = createFallback(input, e.getMessage());
        }

        ThoughtEntry saved = repository.save(entry);

        return new ThoughtResponseDTO(
                saved.getId(),
                saved.getClarityScore(),
                saved.getFacts(),
                saved.getRawThought(),
                saved.getAiAdvice()
        );
    }

    public List<ThoughtResponseDTO> getAllSavedThoughts() {
        return repository.findAll().stream()
                .map(saved -> new ThoughtResponseDTO(
                        saved.getId(),
                        saved.getClarityScore(),
                        saved.getFacts(),
                        saved.getRawThought(),
                        saved.getAiAdvice()
                ))
                .collect(Collectors.toList());
    }

    public ThoughtResponseDTO getThoughtByIdAsDto(Long id) {
        ThoughtEntry saved = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Thought not found with ID: " + id));

        return new ThoughtResponseDTO(
                saved.getId(),
                saved.getClarityScore(),
                saved.getFacts(),
                saved.getRawThought(),
                saved.getAiAdvice()
        );
    }

    private ThoughtEntry createFallback(String thought, String error) {
        System.err.println("❌ SERVICE ERROR: " + error);
        ThoughtEntry fallback = new ThoughtEntry();
        fallback.setRawThought(thought);
        fallback.setClarityScore(5);
        fallback.setFacts(List.of("Error: " + error));
        fallback.setAiAdvice("Bhai thoda issue aa gaya hai, par tension mat lo!");
        return fallback;
    }
}