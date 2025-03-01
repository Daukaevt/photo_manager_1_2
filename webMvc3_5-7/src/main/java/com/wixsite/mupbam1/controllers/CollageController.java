package com.wixsite.mupbam1.controllers;

import com.wixsite.mupbam1.models.Picture;
import com.wixsite.mupbam1.services.CloudinaryService;
import com.wixsite.mupbam1.servises.PictureService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/collage")
public class CollageController {
    private final CloudinaryService cloudinaryService;
    private final PictureService pictureService;

    public CollageController(CloudinaryService cloudinaryService, PictureService pictureService) {
        this.cloudinaryService = cloudinaryService;
        this.pictureService = pictureService;
    }

    @GetMapping
    public String getCollage(Model model, 
                             @RequestParam(defaultValue = "0") int page, 
                             @RequestParam(defaultValue = "12") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Picture> picturePage = pictureService.getPicturesPaginated(pageable);

        // Асинхронная обработка изображений
        List<CompletableFuture<Picture>> futurePictures = picturePage.getContent()
            .stream()
            .map(picture -> CompletableFuture.supplyAsync(() -> processPicture(picture)))
            .collect(Collectors.toList());

        // Ожидаем завершения всех загрузок
        List<Picture> processedPictures = futurePictures.stream()
            .map(CompletableFuture::join)
            .collect(Collectors.toList());

        model.addAttribute("photos", processedPictures);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", picturePage.getTotalPages());
        return "collage";
    }

    private Picture processPicture(Picture picture) {
        try {
            String uploadedUrl = cloudinaryService.uploadImage(picture.getUrl());
            return new Picture(null, uploadedUrl, picture.getDescription(), "google");
        } catch (IOException e) {
            System.err.println("Ошибка загрузки изображения: " + e.getMessage());
            return picture; // Возвращаем оригинальный объект, если ошибка
        }
    }
}



/*
import com.wixsite.mupbam1.models.Photo;
import com.wixsite.mupbam1.services.CloudinaryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/collage")
public class CollageController {
    private CloudinaryService cloudinaryService = new CloudinaryService();
    private final List<Photo> processedPhotos = new ArrayList<>();
    private boolean isProcessed = false; // Флаг, чтобы не загружать повторно
    private int COUNT;

    // Предварительный список URL и описаний
    private final List<Photo> initialPhotos = List.of(
        new Photo(1, "https://images.unsplash.com/photo-1506748686214-e9df14d4d9d0", "Морской закат"),
        new Photo(2, "https://images.unsplash.com/photo-1521747116042-5a810fda9664", "Городской стиль"),
        new Photo(3, "https://images.unsplash.com/photo-1517841905240-472988babdf9", "Портрет собаки")
    );

    public CollageController(CloudinaryService cloudinaryService) {
        this.cloudinaryService = cloudinaryService;
    }

    @GetMapping
    public String getCollage(Model model) {
        if (!isProcessed) {
            processInitialPhotos();
            isProcessed = true;
        }
        model.addAttribute("pictures", processedPhotos);
        return "collage";
    }

    private void processInitialPhotos() {
        for (Photo photo : initialPhotos) {
            try {
                String uploadedUrl = cloudinaryService.uploadImage(photo.getUrl());
                processedPhotos.add(new Photo(COUNT++, uploadedUrl, photo.getDescription()));
            } catch (IOException e) {
                System.err.println("Ошибка загрузки изображения: " + e.getMessage());
            }
        }
    }
}
*/
