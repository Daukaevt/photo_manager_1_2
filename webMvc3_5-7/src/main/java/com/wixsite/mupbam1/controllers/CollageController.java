package com.wixsite.mupbam1.controllers;

import com.wixsite.mupbam1.models.Picture;
import com.wixsite.mupbam1.models.PictureUploadRequest;
import com.wixsite.mupbam1.services.CloudinaryService;
import com.wixsite.mupbam1.services.PictureService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

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
        Page<Picture> picturePage = pictureService.getPicturesPaginated(PageRequest.of(page, size));
        model.addAttribute("photos", picturePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", picturePage.getTotalPages());
        return "collage1";
    }
/*
    // ✅ Загрузка ОДНОГО изображения
    @PostMapping("/upload")
    public String uploadImage(@ModelAttribute PictureUploadRequest request) throws IOException {

        pictureService.uploadSingleImage(request);
        return "redirect:/collage";
    }

    // ✅ Загрузка СПИСКА изображений
    @PostMapping("/uploadBatch")
    public String uploadImagesBatch(@ModelAttribute PictureUploadRequest request) {
        pictureService.uploadMultipleImages(request);
        return "redirect:/collage";
    }
    */
    @PostMapping("/uploadBatch")
    public String uploadImages(@ModelAttribute PictureUploadRequest request) throws IOException {
        List<String> urls = Arrays.stream(request.getUrls().split("\\s+"))
                                  .filter(url -> !url.isEmpty())
                                  .toList();
        
        if (urls.size() > 1) {
            pictureService.uploadMultipleImages(request);
        } else {
            pictureService.uploadSingleImage(request);
        }

        // Получаем количество загруженных фото и вычисляем номер последней страницы
        long totalPictures = pictureService.getTotalPicturesCount();
        int pageSize = 12; // Количество фото на одной странице
        int lastPage = (int) Math.ceil((double) totalPictures / pageSize) - 1;

        return "redirect:/collage?page=" + Math.max(lastPage, 0) + "&size=" + pageSize;
    }




    @GetMapping("/view/{id}")
    public String viewPicture(@PathVariable Long id, 
                              @RequestParam(required = false, defaultValue = "false") boolean editMode, 
                              Model model) {
        Picture picture = pictureService.getPictureById(id);
        model.addAttribute("photo", picture);
        model.addAttribute("editMode", editMode); // Передаём в шаблон
        return "view_photo";
    }

    @PostMapping("/update/{id}")
    public String updatePicture(@PathVariable Long id, @ModelAttribute Picture newPicture) {
        pictureService.updatePicture(id, newPicture);
        return "redirect:/collage/view/" + id; // ✅ Остаёмся на странице просмотра после редактирования
    }

    @PostMapping("/delete/{id}")
    public String deletePicture(@PathVariable Long id) {
        //cloudinaryService.deleteById(id);
    	pictureService.deletePicture(id);
        return "redirect:/collage";
    }
}
