package com.wixsite.mupbam1.services;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wixsite.mupbam1.models.Picture;
import com.wixsite.mupbam1.models.PictureUploadRequest;
import com.wixsite.mupbam1.repository.PictureRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PictureService {
    private final PictureRepository pictureRepository;
    private final CloudinaryService cloudinaryService;

    // ✅ Удалили пустой конструктор — Spring сам создаст экземпляр с нужными зависимостями

    public List<Picture> getAllPictures() {
        return pictureRepository.findAll();
    }
    
    public Page<Picture> getPicturesPaginated(Pageable pageable) {
        return pictureRepository.findAll(pageable);
    }

    public Picture getPictureById(Long id) {
        return pictureRepository.findById(id).orElseThrow(() -> new RuntimeException("Picture not found"));
    }

    public Picture savePicture(Picture picture) {
        return pictureRepository.save(picture);
    }
    
    @Transactional
    public void saveAllPictures(List<Picture> pictures) {
        pictureRepository.saveAll(pictures);
    }

    public Picture updatePicture(Long id, Picture newPicture) {
        Picture picture = getPictureById(id);
        picture.setUrl(newPicture.getUrl());
        picture.setDescription(newPicture.getDescription());
        return pictureRepository.save(picture);
    }

    public void deletePicture(Long id) {
        pictureRepository.deleteById(id);
    }

    // ✅ Обработка одного изображения
    public void uploadSingleImage(PictureUploadRequest request) throws IOException {
        String uploadedUrl = cloudinaryService.uploadImage(request.getUrls());
        Picture picture = new Picture(null, uploadedUrl, request.getDescription(), "user");
        pictureRepository.save(picture);
    }

    // ✅ Обработка списка изображений
    public void uploadMultipleImages(PictureUploadRequest request) {
        List<Picture> uploadedPictures = Arrays.stream(request.getUrls().split("\\s+")) // Исправлено: убираем лишние пробелы
        	    .map(url -> {
        	        try {
        	            System.out.println("Загрузка: " + url); // Логирование
        	            String uploadedUrl = cloudinaryService.uploadImage(url);
        	            return new Picture(null, uploadedUrl, request.getDescription(), "user");
        	        } catch (IOException e) {
        	            System.err.println("Ошибка загрузки: " + url + " -> " + e.getMessage());
        	            return null;
        	        }
        	    })
        	    .filter(Objects::nonNull)
        	    .collect(Collectors.toList());


        pictureRepository.saveAll(uploadedPictures);
    }

    // ✅ Получение уникального имени пользователя
    public String getOwner(OAuth2User oauth2User) {
        if (googleUser(oauth2User)) {
            return oauth2User.getAttribute("email");
        } else if (githubUser(oauth2User)) {
            return "https://github.com/".concat(oauth2User.getAttribute("login"));
        }
        return "unknown_owner";
    }

    private boolean githubUser(OAuth2User oauth2User) {
        return oauth2User.getAttribute("id") != null;
    }

    private boolean googleUser(OAuth2User oauth2User) {
        return oauth2User.getAttribute("sub") != null;
    }
}
