package ecommerce.ecommerce.service;

import ecommerce.ecommerce.dto.cart.AddToCartDto;
import ecommerce.ecommerce.dto.cart.CartDto;
import ecommerce.ecommerce.dto.cart.CartItemDto;
import ecommerce.ecommerce.exceptions.CartItemNotExistException;
import ecommerce.ecommerce.model.Cart;
import ecommerce.ecommerce.model.Product;
import ecommerce.ecommerce.model.User;
import ecommerce.ecommerce.repository.CartRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

@Service
public class CartService {
    @Autowired
    CartRepository cartRepository;

    public void addToCart(AddToCartDto addToCartDto, Product product, User user) {
        Cart cart = new Cart(product, addToCartDto.getQuantity(), user);
        cartRepository.save(cart);
    }

    public CartDto listCartItems(User user) {
        // first get all the cart items for user
        List<Cart> cartList = cartRepository.findAllByUserOrderByCreatedDateDesc(user);

        // convert cart to cartItemDto
        List<CartItemDto> cartItems = new ArrayList<>();
        for (Cart cart : cartList) {
            CartItemDto cartItemDto = new CartItemDto(cart);
            cartItems.add(cartItemDto);
        }

        // calculate the total price
        double totalCost = 0;
        for (CartItemDto cartItemDto : cartItems) {
            totalCost += cartItemDto.getProduct().getPrice() * cartItemDto.getQuantity();
        }

        // return cart DTO
        return new CartDto(cartItems, totalCost);
    }
    
        public void deleteCartItem(int cartItemId, User user) throws CartItemNotExistException {

        // first check if cartItemId is valid else throw an CartItemNotExistException
        Optional<Cart> optionalCart = cartRepository.findById(cartItemId);
        if (!Objects.nonNull(optionalCart)) {
            throw new CartItemNotExistException("Cart item does not exist.");
        }
        Cart cart = optionalCart.get();
        // next check if the cartItem belongs to the user else throw CartItemNotExistException exception
        if (cart.getUser() != user) {
            throw new CartItemNotExistException("Cart item does not match with user.");
        }
        // delete the cart item
        cartRepository.deleteById(cartItemId);
    }


}
