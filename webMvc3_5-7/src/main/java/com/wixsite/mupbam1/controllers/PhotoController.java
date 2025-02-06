package com.wixsite.mupbam1.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PhotoController {
	@GetMapping("/")
	public String getHome(Model model) {
		model.addAttribute("message", "Welcome home!");
		return "home_page";
	}
	@GetMapping("/secured")
	public String getSecured(Model model) {
		model.addAttribute("message", "Welcome secured!");
		return "secured_page";
	}

}
