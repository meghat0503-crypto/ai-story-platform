package com.example.demo.aistory.controller;

import com.example.demo.aistory.service.StoryService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class StoryController {

    private final StoryService storyService;

    public StoryController(StoryService storyService) {
        this.storyService = storyService;
    }

    @GetMapping("/story")
    public Map<String, String> getStory(@RequestParam(defaultValue = "panchatantra") String type) {
        return storyService.generateStory(type);
    }
}
