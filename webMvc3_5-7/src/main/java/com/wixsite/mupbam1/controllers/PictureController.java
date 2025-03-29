package com.wixsite.mupbam1.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.wixsite.mupbam1.models.Picture;
import com.wixsite.mupbam1.services.PictureService;

import lombok.RequiredArgsConstructor;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/pictures")
@RequiredArgsConstructor
public class PictureController {
    private final PictureService pictureService;

    @GetMapping
    public String getAllPictures(@RequestParam(defaultValue = "0") int page, 
                                 @RequestParam(defaultValue = "12") int size, 
                                 Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Picture> picturePage = pictureService.getPicturesPaginated(null, pageable);
        model.addAttribute("pic", new Picture());
        model.addAttribute("pictures", picturePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", picturePage.getTotalPages());

        return "index";
    }

    @PostMapping
    public String addPicture(@RequestParam("urls") String urls,
                             @RequestParam("description") String description,
                             Authentication authentication) {
        String ownerKey = userName(authentication);
        List<String> urlList = Arrays.asList(urls.trim().split("\\s+"));
        
        List<Picture> pictures = urlList.stream().map(url -> {
            Picture picture = new Picture();
            picture.setUrl(url);
            picture.setDescription(description);
            picture.setOwner_key(ownerKey);
            return picture;
        }).toList();
        
        pictureService.saveAllPictures(pictures);
        
        return "redirect:/pictures";
    }

    @GetMapping("/{id}")
    public String getPicture(@PathVariable Long id, Model model) {
        model.addAttribute("picture", pictureService.getPictureById(id));
        return "picture";
    }

    @PostMapping("/{id}/edit")
    public String updatePicture(@PathVariable Long id, @ModelAttribute Picture picture) {
        pictureService.updatePicture(id, picture);
        return "redirect:/pictures/" + id;
    }

    @PostMapping("/{id}/delete")
    public String deletePicture(@PathVariable Long id) {
        pictureService.deletePicture(id);
        return "redirect:/pictures";
    }
    
    public String userName(Authentication authentication) {
        String uniqueUserName = null;
        if (authentication.getPrincipal() instanceof OAuth2User oauth2User) {
            uniqueUserName = pictureService.getOwner(oauth2User);
        }
        return uniqueUserName;
    }
}

