package ecommerce.ecommerce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ecommerce.ecommerce.model.User;
import ecommerce.ecommerce.model.WishList;
import ecommerce.ecommerce.repository.WishListRepository;

@Service
public class WishListService {
    @Autowired
    private WishListRepository wishListRepository;

    public void createWishList(WishList wishList) {
        wishListRepository.save(wishList);
    }

    public List<WishList> readWishList(User user) {
        return wishListRepository.findAllByUserOrderByCreatedDateDesc(user);
    }

    public void updateWishList(WishList wishList) {
        wishListRepository.save(wishList);
    }
}
