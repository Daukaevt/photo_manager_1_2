package com.wixsite.mupbam1.servises;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    
    // Получение уникального имени пользователя
 	public String getOwner(OAuth2User oauth2User) {
 		String uniqueUser = null;
 		if (googleUser(oauth2User)) {
 			uniqueUser = oauth2User.getAttribute("email");
 		} else if (githubUser(oauth2User)) {
 			uniqueUser = "https://github.com/".concat(oauth2User.getAttribute("login"));
 		} else {
 			return "unknown_owner";
 		}
 		return uniqueUser;
 	}

 	public boolean githubUser(OAuth2User oauth2User) {
 		return oauth2User.getAttribute("id") != null;
 	}

 	public boolean googleUser(OAuth2User oauth2User) {
 		return oauth2User.getAttribute("sub") != null;
 	}
}
