package ecommerce.ecommerce.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ecommerce.ecommerce.config.MessageStrings;
import ecommerce.ecommerce.dto.users.SignInDto;
import ecommerce.ecommerce.dto.users.SignInResponseDto;
import ecommerce.ecommerce.dto.users.SignUpResponseDto;
import ecommerce.ecommerce.dto.users.SignupDto;
import ecommerce.ecommerce.exceptions.AuthenticationFailException;
import ecommerce.ecommerce.exceptions.CustomException;
import ecommerce.ecommerce.model.AuthenticationToken;
import ecommerce.ecommerce.model.User;
import ecommerce.ecommerce.repository.UserRepository;
import jakarta.xml.bind.DatatypeConverter;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    AuthenticationService authenticationService;
    Logger logger = LoggerFactory.getLogger(UserService.class);

    public SignUpResponseDto signUp(SignupDto signupDto) throws CustomException {
        // Check to see if the current email address has already been registered.
        if (Objects.nonNull(userRepository.findByEmail(signupDto.getEmail()))) {
            // If the email address has been registered then throw an exception.
            throw new CustomException("User already exists");
        }
        // first encrypt the password
        String encryptedPassword = signupDto.getPassword();
        try {
            encryptedPassword = hashPassword(signupDto.getPassword());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            logger.error("hashing password failed {}", e.getMessage());
        }

        User user = new User(signupDto.getFirstName(), signupDto.getLastName(), signupDto.getEmail(),
                encryptedPassword);
        try {
            // save the User
            userRepository.save(user);
            final AuthenticationToken authenticationToken = new AuthenticationToken(user);
            authenticationService.saveConfirmation(authenticationToken);
            // success in creating
            return new SignUpResponseDto("success", "user created successfully");
        } catch (Exception e) {
            // handle signup error
            throw new CustomException(e.getMessage());
        }
    }

    String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());
        byte[] digest = md.digest();
        String myHash = DatatypeConverter
                .printHexBinary(digest).toUpperCase();
        return myHash;
    }

    public SignInResponseDto signIn(SignInDto signInDto) throws AuthenticationFailException, CustomException {
        // first find User by email
        User user = userRepository.findByEmail(signInDto.getEmail());
        if(!Objects.nonNull(user)){
            throw new AuthenticationFailException("user not present");
        }
        try {
            // check if password is right
            if (!user.getPassword().equals(hashPassword(signInDto.getPassword()))){
                // passwords do not match
                throw  new AuthenticationFailException(MessageStrings.WRONG_PASSWORD);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            logger.error("hashing password failed {}", e.getMessage());
            throw new CustomException(e.getMessage());
        }

        AuthenticationToken token = authenticationService.getToken(user);

        if(!Objects.nonNull(token)) {
            // token not present
            throw new CustomException(MessageStrings.AUTH_TOEKN_NOT_PRESENT);
        }
        return new SignInResponseDto ("success", token.getToken());
    }
}
