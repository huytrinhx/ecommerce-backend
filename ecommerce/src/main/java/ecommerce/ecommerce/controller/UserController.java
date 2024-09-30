package ecommerce.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ecommerce.ecommerce.dto.users.SignInDto;
import ecommerce.ecommerce.dto.users.SignInResponseDto;
import ecommerce.ecommerce.dto.users.SignUpResponseDto;
import ecommerce.ecommerce.dto.users.SignupDto;
import ecommerce.ecommerce.exceptions.AuthenticationFailException;
import ecommerce.ecommerce.exceptions.CustomException;
import ecommerce.ecommerce.service.UserService;

@RequestMapping("user")
@RestController
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/signup")
    public SignUpResponseDto Signup(@RequestBody SignupDto signupDto) throws CustomException {
        return userService.signUp(signupDto);
    }

    @PostMapping("/signIn")
    public SignInResponseDto Signup(@RequestBody SignInDto signInDto) throws AuthenticationFailException, CustomException {
        return userService.signIn(signInDto);
    }
}
