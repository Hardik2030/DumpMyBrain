package com.Hardik.DumpMyBrain.service;

import com.Hardik.DumpMyBrain.entity.ThoughtEntry;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ThoughtParser {

    public static ThoughtEntry parseToEntity(String aiResponse, String originalThought) throws Exception {
        // Regex-based extraction (Keywords ko dhoondhne ke liye)
        String facts = extract(aiResponse, "Facts:");
        String emotion = extract(aiResponse, "Emotion:");
        String bias = extract(aiResponse, "Bias:");
        String urgency = extract(aiResponse, "Urgency:");
        String advice = extract(aiResponse, "Advice:");

        // Fallback: Agar Advice label na mile toh poora response hi advice maan lo
        if (advice.isEmpty()) advice = aiResponse;

        // Bias Count (Safe parsing)
        int biasCount = 0;
        try {
            String cleanBias = bias.replaceAll("[^0-9]", "");
            if (!cleanBias.isEmpty()) {
                // Agar number bahut bada ho (Integer limit se bahar), toh use 0 ya 5 set kardo
                biasCount = (cleanBias.length() > 2) ? 5 : Integer.parseInt(cleanBias);
            }
        } catch (Exception e) { biasCount = 0; }

        List<String> factsList = Arrays.stream(facts.split("[,;]"))
                .map(String::trim).filter(s -> !s.isEmpty()).toList();

        ThoughtEntry entry = new ThoughtEntry();
        entry.setRawThought(originalThought);
        entry.setFacts(factsList);
        entry.setAiAdvice(advice);
        entry.setClarityScore(calculateClarityScore(factsList.size(), biasCount, emotion, urgency));

        return entry;
    }

    private static String extract(String text, String label) {
        // Yeh Pattern keyword ke baad ka text uthata hai jab tak naya keyword na mil jaye
        Pattern pattern = Pattern.compile(label + "\\s*(.*?)(?=;\\s*\\w+:|$|Emotion:|Bias:|Urgency:|Advice:)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(1).trim().replace(";", "");
        }
        return "";
    }

    private static int calculateClarityScore(int facts, int bias, String em, String ur) {
        int score = (facts * 2) - bias;
        if (ur.equalsIgnoreCase("HIGH")) return 1;
        return Math.max(1, Math.min(10, score));
    }
}