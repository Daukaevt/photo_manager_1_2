package com.wixsite.mupbam1.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wixsite.mupbam1.services.CloudinaryService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CollageController {

    @Autowired
    private CloudinaryService cloudinaryService;

    private final List<String> imageUrls = new ArrayList<>();
    
    {
    	imageUrls.add("https://iili.io/2mqfOV1.jpg");
    }
    
    @GetMapping("/")
    public String welcomePage(Model model) {
        model.addAttribute("background_path", 
        		"https://img.freepik.com/free-vector/photo-album-cartoon-illustration-with-human-hand-holding-pencil-writing-explanation-photograph-scrapbook-page_1284-28262.jpg?t=st=1740765764~exp=1740769364~hmac=e4d3e9fe52bf1f0ede45fbca6e678cae0ba32a4f3d11c1c1de301544fed7a949&w=826");
        return "welcome";  // Отображаем приветственную страницу с миниатюрами
    }

    @GetMapping("/collage")
    public String showForm(Model model) {
        model.addAttribute("imageUrls", imageUrls);
        return "collage";  // Отображаем главную страницу с миниатюрами
    }

    @PostMapping("/upload")
    public String uploadImage(@RequestParam("imageUrl") String imageUrl, Model model) {
        try {
            String uploadedUrl = cloudinaryService.uploadImage(imageUrl);
            imageUrls.add(uploadedUrl);
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("error", "Ошибка при загрузке изображения");
        }
        model.addAttribute("imageUrls", imageUrls);
        return "collage";
    }
}
