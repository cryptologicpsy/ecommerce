package org.example.webapptest.user;






import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.example.webapptest.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.AllArgsConstructor;


@Controller
@AllArgsConstructor
public class UserController {

	private final UserService userService;

	private final ConfirmationTokenService confirmationTokenService;

	@GetMapping("/sign-in")
	String signIn() {

		return "sign-in";
	}

	@GetMapping("/sign-up")
	String signUpPage(User user) {

		return "sign-up";
	}

	@PostMapping("/sign-up")
	String signUp(User user) {

		userService.signUpUser(user);

		return "redirect:/sign-in";
	}

	@GetMapping("/sign-up/confirm")
	String confirmMail(@RequestParam("token") String token) {

		Optional<ConfirmationToken> optionalConfirmationToken = confirmationTokenService.findConfirmationTokenByToken(token);

		optionalConfirmationToken.ifPresent(userService::confirmUser);

		return "redirect:/sign-in";
	}
	
	
	 
	}
