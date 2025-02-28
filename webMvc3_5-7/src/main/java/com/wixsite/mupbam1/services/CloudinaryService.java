package com.wixsite.mupbam1.services;


import org.springframework.stereotype.Service;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import io.github.cdimascio.dotenv.Dotenv;
import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService() {
        // Загружаем переменные окружения из .env файла
        Dotenv dotenv = Dotenv.load();
        this.cloudinary = new Cloudinary(dotenv.get("CLOUDINARY_URL"));
    }

    public String uploadImage(String imageUrl) throws IOException {
        Map<String, Object> params = ObjectUtils.asMap(
            "use_filename", true,
            "unique_filename", false,
            "overwrite", true
        );

        // Загружаем изображение и получаем результат
        Map<?, ?> uploadResult = cloudinary.uploader().upload(imageUrl, params);

        // Возвращаем URL загруженного изображения
        return (String) uploadResult.get("secure_url");
    }
}
