package com.wixsite.mupbam1.servises;

import java.util.List;

import org.springframework.stereotype.Service;

import com.wixsite.mupbam1.models.Picture;
import com.wixsite.mupbam1.repository.PictureRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PictureService {
    private final PictureRepository pictureRepository;

    public List<Picture> getAllPictures() {
        return pictureRepository.findAll();
    }

    public Picture getPictureById(Long id) {
        return pictureRepository.findById(id).orElseThrow(() -> new RuntimeException("Picture not found"));
    }

    public Picture savePicture(Picture picture) {
        return pictureRepository.save(picture);
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
}
