package ecommerce.ecommerce.service;
import ecommerce.ecommerce.dto.ProductDto;
import ecommerce.ecommerce.model.Category;
import ecommerce.ecommerce.model.Product;
import ecommerce.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import ecommerce.ecommerce.exceptions.ProductNotExistException;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public void addProduct(ProductDto productDto, Category category) {
        Product product = getProductFromDto(productDto, category);
        productRepository.save(product);
    }

    public static Product getProductFromDto(ProductDto productDto, Category category) {
        Product product = new Product();
        product.setCategory(category);
        product.setDescription(productDto.getDescription());
        product.setImageURL(productDto.getImageURL());
        product.setPrice(productDto.getPrice());
        product.setName(productDto.getName());
        return product;
    }

    public void updateProduct(int productID,ProductDto productDto, Category category) {
        Product product = getProductFromDto(productDto, category);
        // Set id as we did not do so in previous step
        product.setId(productID);
        productRepository.save(product);

    }

    public Product getProductById(Integer productId) throws ProductNotExistException {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (!optionalProduct.isPresent())
            throw new ProductNotExistException("Product id is invalid " + productId);
        return optionalProduct.get();
    }
}
