package com.wixsite.mupbam1.controllers;

import com.wixsite.mupbam1.models.Picture;
import com.wixsite.mupbam1.models.PictureUploadRequest;
import com.wixsite.mupbam1.services.CloudinaryService;
import com.wixsite.mupbam1.services.PictureService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/collage")
public class CollageController {
    private static final Logger logger = LoggerFactory.getLogger(CollageController.class);
    private final CloudinaryService cloudinaryService;
    private final PictureService pictureService;

    public CollageController(CloudinaryService cloudinaryService, PictureService pictureService) {
        this.cloudinaryService = cloudinaryService;
        this.pictureService = pictureService;
    }

    @GetMapping
    public String getCollage(Model model, Authentication authentication,
                             @RequestParam(defaultValue = "0") int page, 
                             @RequestParam(defaultValue = "12") int size) {
        String ownerKey = userName(authentication);
        Page<Picture> picturePage = pictureService.getPicturesPaginated(ownerKey, PageRequest.of(page, size));
        model.addAttribute("photos", picturePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", picturePage.getTotalPages());

        return "collage1";
    }
    
    public String userName(Authentication authentication) {
        String uniqueUserName = null;
        if (authentication.getPrincipal() instanceof OAuth2User oauth2User) {
            uniqueUserName = pictureService.getOwner(oauth2User);
        }
        return uniqueUserName;
    }

    @PostMapping("/uploadBatch")
    public String uploadImages(Authentication authentication,
    		@ModelAttribute PictureUploadRequest request) {
    	String ownerKey = userName(authentication);
        List<String> urls = request.getUrls() != null 
            ? Arrays.stream(request.getUrls().trim().split("\\s+"))
                    .filter(url -> !url.isEmpty())
                    .toList()
            : List.of();

        try {
            if (urls.size() > 1) {
                pictureService.uploadMultipleImages(request, ownerKey);
            } else {
                pictureService.uploadSingleImage(request, ownerKey);
            }
        } catch (IOException e) {
            logger.error("Ошибка загрузки изображения: {}", e.getMessage());
            return "redirect:/collage?error=uploadFailed";
        }

        long totalPictures = pictureService.getTotalPicturesCount();
        int pageSize = 12;
        int lastPage = Math.max(0, (int) Math.ceil((double) totalPictures / pageSize) - 1);

        return "redirect:/collage?page=" + lastPage + "&size=" + pageSize;
    }

    @GetMapping("/view/{id}")
    public String viewPicture(@PathVariable Long id, 
                              @RequestParam(required = false, defaultValue = "false") boolean editMode,
                              @RequestParam(required = false, defaultValue = "0") int page,
                              Model model) {
        Picture picture = pictureService.getPictureById(id);

        long totalPictures = pictureService.getTotalPicturesCount();
        int pageSize = 12;
        int totalPages = (int) Math.ceil((double) totalPictures / pageSize);

        model.addAttribute("photo", picture);
        model.addAttribute("editMode", editMode);
        model.addAttribute("page", page);
        model.addAttribute("totalPages", totalPages);

        return "view_photo1";
    }

    @PostMapping("/update/{id}")
    public String updatePicture(@PathVariable Long id, 
                                @RequestParam String url,
                                @RequestParam String description,
                                @RequestParam(required = false, defaultValue = "0") int page) {
        Picture updatedPicture = new Picture();
        updatedPicture.setId(id);
        updatedPicture.setUrl(url);
        updatedPicture.setDescription(description);
        pictureService.updatePicture(id, updatedPicture);

        return "redirect:/collage?page=" + page + "&size=12";
    }

    @PostMapping("/delete/{id}")
    public String deletePicture(@PathVariable Long id,
                                @RequestParam(required = false) Integer page) {
        logger.info("Удаление изображения ID: {}", id);
        pictureService.deletePicture(id);
        return "redirect:/collage?page=" + (page != null ? page : 0) + "&size=12"; 
    }
}
