package org.mouthaan.legostore;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mouthaan.legostore.model.*;
import org.mouthaan.legostore.persistence.LegoSetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

@RunWith(SpringRunner.class)
@DataMongoTest
public class LegoStoreDatabaseTests {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private LegoSetRepository legoSetRepository;

    @Before
    public void before() {
        this.legoSetRepository.deleteAll();

        /*
        Lego Sets for testing
         */
        LegoSet milleniumFalcon = new LegoSet(
                "Millenium Falcon",
                "Star Wars",
                LegoSetDifficulty.HARD,
                new DeliveryInfo(LocalDate.now().plusDays(1), 30, true),
                Arrays.asList(
                        new ProductReview("Dan", 7),
                        new ProductReview("Anna", 10),
                        new ProductReview("John", 8)
                ),
                new PaymentOptions(PaymentType.CreditCard, 0));

        LegoSet skyPolice = new LegoSet(
                "Sky Police Air Base",
                "City",
                LegoSetDifficulty.MEDIUM,
                new DeliveryInfo(LocalDate.now().plusDays(3), 50, true),
                Arrays.asList(
                        new ProductReview("Dan", 5),
                        new ProductReview("Andrew", 8)
                ),
                new PaymentOptions(PaymentType.CreditCard, 0));

        this.legoSetRepository.insert(milleniumFalcon);
        this.legoSetRepository.insert(skyPolice);
    }

    @Test
    public void findAllByGreatReviews_should_return_products_that_have_a_review_with_a_rating_of_ten() {
        List<LegoSet> allByGreatReviews = (List<LegoSet>) this.legoSetRepository.findAllByGreatReviews();

        assertEquals(1, allByGreatReviews.size());
        assertEquals("Millenium Falcon", allByGreatReviews.get(0).getName());
    }

    @Test
    public void findAllInStock_should_return_products_that_are_instock() {
        /* Given */
        // Clean up
        this.legoSetRepository.deleteAll();

        // Two products, one in stock, one not in stock
        LegoSet mcLarenSenna = new LegoSet(
                "McLaren Senna",
                "Speed Champions",
                LegoSetDifficulty.EASY,
                new DeliveryInfo(LocalDate.now().plusDays(7), 70, false),
                Arrays.asList(
                        new ProductReview("Bogdan", 9),
                        new ProductReview("Christa", 9)
                ),
                new PaymentOptions(PaymentType.CreditCard, 0));

        LegoSet mindstormsEve = new LegoSet(
                "MINDSTORMS EV3",
                "Mindstorms",
                LegoSetDifficulty.HARD,
                new DeliveryInfo(LocalDate.now().plusDays(10), 100, true),
                Arrays.asList(
                        new ProductReview("Cosmin", 10),
                        new ProductReview("Jane", 9),
                        new ProductReview("James", 10)
                ),
                new PaymentOptions(PaymentType.CreditCard, 0));

        this.legoSetRepository.insert(mcLarenSenna);
        this.legoSetRepository.insert(mindstormsEve);

        /* When */
        List<LegoSet> result = (List<LegoSet>) this.legoSetRepository.findAllByInStock();

        /* Then */
        assertEquals(1, result.size());
        assertEquals("MINDSTORMS EV3", result.get(0).getName());
    }
}
