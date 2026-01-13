package csd230.lecture_2_1;

import com.github.javafaker.Commerce;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // use application db (mysql) not default h2 embedded db
@Transactional(propagation = Propagation.NOT_SUPPORTED)// dont rollback so you can see data in the db


class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;

    @Test
    void testSave() {
        Faker faker = new Faker();
        Commerce cm = faker.commerce();
        com.github.javafaker.Number number = faker.number();
        com.github.javafaker.Book fakeBook = faker.book();
        String name=cm.productName();
        String description=cm.material();
        Product newProduct = new Product(name, description, number.randomDouble(2,10,100));

        //testEM.persistAndFlush(b1); the same
        productRepository.save(newProduct);

        Long savedProductID = newProduct.getId();

        Product product = productRepository.findById(savedProductID).orElseThrow();
        // Product book = testEM.find(Product.class, savedProductID);

        assertEquals(savedProductID, product.getId());
    }

    @Test
    void findFirstByName() {
        Product newProduct = new Product("appleone", "this is an apple", 12.34);
        productRepository.save(newProduct);

        Product product = productRepository.findFirstByName("appleone");;
        assertNotNull(product);
    }

    @Test
    void findAllByName() {
        Product newProduct = new Product("appleone", "this is an apple2", 14.34);
        productRepository.save(newProduct);


        Product newProduct2 = new Product("banana", "this is a banana", 22.34);
        productRepository.save(newProduct2);


        Product newProduct3 = new Product("banana", "this is a banana pie", 34.00);
        productRepository.save(newProduct3);

        var products = productRepository.findAllByName("banana");;
        assertEquals(2, products.size());
    }
}

