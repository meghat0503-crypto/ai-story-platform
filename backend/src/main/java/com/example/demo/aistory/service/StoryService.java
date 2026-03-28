package com.example.demo.aistory.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.UUID;
@Service
public class StoryService {

    @Value("${openai.api.key}")
    private String apiKey;

    private final WebClient webClient;
public StoryService(WebClient webClient) {
        this.webClient = webClient;  // ✅ injected from config
    }


     public Map<String, String> generateStory(String type) {

    String prompt = "Write a short " + type +
            " story in 4 lines with a moral. End with: Image: <scene>";

    Map response = webClient.post()
            .uri("/responses")
            .header("Authorization", "Bearer " + apiKey)
            .header("Content-Type", "application/json")
            .bodyValue(Map.of(
                    "model", "gpt-4.1-mini",
                    "input", prompt
            ))
            .retrieve()
            .bodyToMono(Map.class)
            .block();

    // ✅ STEP 1: Extract clean text
    List<Map<String, Object>> output =
            (List<Map<String, Object>>) response.get("output");

    Map<String, Object> message = output.get(0);

    List<Map<String, Object>> content =
            (List<Map<String, Object>>) message.get("content");

    String text = (String) content.get(0).get("text");

    System.out.println("EXTRACTED TEXT = " + text);

    // ✅ STEP 2: Split story + image
    String story;
    String imagePrompt = "storybook illustration";

if (text.contains("Image:")) {
    String[] parts = text.split("Image:");
    story = parts[0].trim();

    imagePrompt = parts[1]
            .replaceAll("\\n", "")
            .replaceAll("Moral:.*", "")  // remove any leftover
            .trim();
} else {
    story = text.trim();
}

    // ✅ TEMP image (to confirm UI works)
 

 String imageUrl = generateImage(imagePrompt);

        return Map.of(
                "story", story,
                "imageUrl", imageUrl
        );


    // ✅ TEMP image (to confirm UI works)
  
}


private String generateImage(String prompt) {

    Map response = webClient.post()
            .uri("/images/generations")
            .header("Authorization", "Bearer " + apiKey)
            .header("Content-Type", "application/json")
            .bodyValue(Map.of(
                    "model", "gpt-image-1",
                    "prompt", prompt,
                    "size", "1024x1024"
            ))
            .retrieve()
            .bodyToMono(Map.class)
            .block();

    List<?> data = (List<?>) response.get("data");
    Map<?, ?> imageObj = (Map<?, ?>) data.get(0);

    String base64Image = imageObj.get("b64_json").toString();

    // ✅ Convert base64 → bytes
    byte[] imageBytes = Base64.getDecoder().decode(base64Image);

    // ✅ Save file
    String fileName = "story-" + UUID.randomUUID() + ".png";
    Path path = Path.of("images/" + fileName);

    try {
        Files.createDirectories(path.getParent());
        Files.write(path, imageBytes);
    } catch (Exception e) {
        throw new RuntimeException("Error saving image", e);
    }


    // ✅ Return URL
   
    return "http://localhost:8080/images/" + fileName;
}

}