package ecommerce.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ecommerce.ecommerce.model.AuthenticationToken;
import ecommerce.ecommerce.model.User;

public interface TokenRepository extends JpaRepository<AuthenticationToken, Integer> {
    AuthenticationToken findTokenByUser(User user);
    AuthenticationToken findTokenByToken(String token);
}

