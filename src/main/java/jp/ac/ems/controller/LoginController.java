package jp.ac.ems.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {
	
	@GetMapping(path="login")
	String login() {
		return "login";
	}

	@GetMapping(path="top")
	String top() {
		return "top";
	}
}
