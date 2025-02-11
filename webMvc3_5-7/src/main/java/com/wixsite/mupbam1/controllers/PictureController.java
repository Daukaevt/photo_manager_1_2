package com.wixsite.mupbam1.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wixsite.mupbam1.models.Picture;
import com.wixsite.mupbam1.servises.PictureService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/pictures")
@RequiredArgsConstructor
public class PictureController {
    private final PictureService pictureService;

    @GetMapping
    public String getAllPictures(Model model) {
        model.addAttribute("pictures", pictureService.getAllPictures());
        model.addAttribute("pic", new Picture());
        return "index";
    }

    @PostMapping
    public String addPicture(@ModelAttribute Picture picture) {
    	picture.setOwner_key("google");
        pictureService.savePicture(picture);
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
}

