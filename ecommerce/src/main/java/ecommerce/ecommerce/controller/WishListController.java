package ecommerce.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ecommerce.ecommerce.common.ApiResponse;
import ecommerce.ecommerce.dto.ProductDto;
import ecommerce.ecommerce.exceptions.AuthenticationFailException;
import ecommerce.ecommerce.model.Product;
import ecommerce.ecommerce.model.User;
import ecommerce.ecommerce.model.WishList;
import ecommerce.ecommerce.repository.ProductRepository;
import ecommerce.ecommerce.service.AuthenticationService;
import ecommerce.ecommerce.service.WishListService;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/wishlist")
@RestController
public class WishListController {
    @Autowired
    WishListService wishListService;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    private ProductRepository productRepository;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addWishList(@RequestBody ProductDto productDto, @RequestParam("token") String token) throws AuthenticationFailException {
        authenticationService.authenticate(token);

        User user = authenticationService.getUser(token);

        Product product = productRepository.getReferenceById(productDto.getId());

        WishList wishList = new WishList(user, product);
        wishListService.createWishList(wishList);

        return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Added to wishlist"), HttpStatus.CREATED);
    }

    @GetMapping("/{token}")
    public ResponseEntity<List<ProductDto>> getWishList(@PathVariable("token") String token) throws AuthenticationFailException {
        authenticationService.authenticate(token);
        User user = authenticationService.getUser(token);

        List<WishList> wishLists = wishListService.readWishList(user);

        List<ProductDto> products = new ArrayList<>();
        for (WishList wishList : wishLists) {
            products.add(new ProductDto(wishList.getProduct()));
        }

        return new ResponseEntity<>(products, HttpStatus.OK);
    }
}
