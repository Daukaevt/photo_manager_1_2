package com.wixsite.mupbam1.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WelcomeController {
	@GetMapping("/")
	public String getWelocePage(Model model) {
		model.addAttribute("background_path", 
				"https://img.freepik.com/free-vector/welcome-pattern-different-languages_23-2147869891.jpg?t=st=1740853335~exp=1740856935~hmac=ba92539897bc24d394b0e36284527d6eee52bc2250b329991cb0b019f6ff0eda&w=740");
		return "welcome";		
	}

}
