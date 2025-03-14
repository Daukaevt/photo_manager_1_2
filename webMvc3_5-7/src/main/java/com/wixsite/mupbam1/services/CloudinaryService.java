package com.wixsite.mupbam1.services;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import io.github.cdimascio.dotenv.Dotenv;
import com.wixsite.mupbam1.models.Picture;
import com.wixsite.mupbam1.repository.PictureRepository;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;
    private final PictureRepository pictureRepository;

    public CloudinaryService(PictureRepository pictureRepository) {
        Dotenv dotenv = Dotenv.load();
        this.cloudinary = new Cloudinary(dotenv.get("CLOUDINARY_URL"));
        this.pictureRepository = pictureRepository;
    }

    // Асинхронная пакетная загрузка изображений
    @Async
    public CompletableFuture<List<String>> uploadImagesAsync(List<String> urls) {
        List<CompletableFuture<String>> uploadFutures = urls.stream()
            .map(url -> CompletableFuture.supplyAsync(() -> {
                try {
                    return uploadImage(url);
                } catch (IOException e) {
                    System.err.println("Ошибка загрузки: " + e.getMessage());
                    return null;
                }
            }))
            .collect(Collectors.toList());

        return CompletableFuture.allOf(uploadFutures.toArray(new CompletableFuture[0]))
            .thenApply(v -> uploadFutures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList()));
    }

    // Загружаем изображение и сразу оптимизируем
    public String uploadImage(String imageUrl) throws IOException {
        Map<?, ?> uploadResult = cloudinary.uploader().upload(imageUrl, ObjectUtils.asMap(
            "use_filename", true,
            "unique_filename", false,
            "overwrite", true,
            "quality", "auto:eco", // Оптимизация качества
            "fetch_format", "auto" // Конвертация в WebP или другой быстрый формат
        ));
        return (String) uploadResult.get("secure_url");
    }

   
}
