package org.mouthaan.legostore.api;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.mouthaan.legostore.model.LegoSet;
import org.mouthaan.legostore.model.LegoSetDifficulty;
import org.mouthaan.legostore.model.QLegoSet;
import org.mouthaan.legostore.persistence.LegoSetRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("legostore/api")
public class LegoStoreController {
    private LegoSetRepository legoSetRepository;

    public LegoStoreController(LegoSetRepository legoSetRepository) {
        this.legoSetRepository = legoSetRepository;
    }

    @PostMapping
    public void insert(@RequestBody LegoSet legoSet) {
        legoSetRepository.insert(legoSet);
    }

    @PutMapping
    public void update(@RequestBody LegoSet legoSet) {
        this.legoSetRepository.save(legoSet);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        this.legoSetRepository.deleteById(id);
    }

    @GetMapping("/all")
    public Collection<LegoSet> all() {
        Sort sortByThemeAsc = Sort.by("theme").ascending();
        return this.legoSetRepository.findAll(sortByThemeAsc);
    }

    @GetMapping("/{id}")
    public LegoSet byId(@PathVariable String id) {
        return this.legoSetRepository.findById(id).orElse(null);
    }

    @GetMapping("/byTheme/{theme}")
    public Collection<LegoSet> byTheme(@PathVariable String theme) {
        Sort sortByThemeAsc = Sort.by("theme").ascending();
        return this.legoSetRepository.findAllByThemeContains(theme, sortByThemeAsc);
    }

    @GetMapping("/hardThatStartWithM")
    public Collection<LegoSet> hardThatStartWithM() {
        return this.legoSetRepository.findAllByDifficultyAndNameStartsWith(LegoSetDifficulty.HARD, "M");
    }

    @GetMapping("/byDeliveryFeeLessThen/{price}")
    public Collection<LegoSet> byDeliveryFeeLessThan(@PathVariable int price) {
        return this.legoSetRepository.findAllByDeliveryPriceLessThan(price);
    }

    @GetMapping("/byGreatReviews")
    private Collection<LegoSet> byGreatReviews() {
        return this.legoSetRepository.findAllByGreatReviews();
    }

    @GetMapping("/notStarWars")
    public Collection<LegoSet> notStarWars() {
        return this.legoSetRepository.findAllByThemeNotContains("Star Wars");
    }

    @GetMapping("/inStock")
    public Collection<LegoSet> inStock() {
        return this.legoSetRepository.findAllByInStock();
    }

    @GetMapping("/bestBuys")
    public Collection<LegoSet> bestBuys() {

        // build query
        QLegoSet query = new QLegoSet("query");
        BooleanExpression inStockFilter = query.deliveryInfo.inStock.isTrue();
        Predicate smallDeliveryFeeFilter = query.deliveryInfo.deliveryFee.lt(50);
        Predicate hasGreatReviewFilter = query.reviews.any().rating.eq(10);

        Predicate bestBuysFilter = inStockFilter
                .and(smallDeliveryFeeFilter)
                .and(hasGreatReviewFilter);

        // pass the query to findAll()
        return (Collection<LegoSet>) this.legoSetRepository.findAll(bestBuysFilter);
    }

    @GetMapping("fullTextSearch/{text}")
    public Collection<LegoSet> fullTextSearch(@PathVariable String text) {
        TextCriteria textCriteria = TextCriteria.forDefaultLanguage().matching(text);
        return this.legoSetRepository.findAllBy(textCriteria);
    }

    @GetMapping("notOfInterest")
    public Collection<LegoSet> notOfInterest() {
        // build query
        QLegoSet query = new QLegoSet("query");
        BooleanExpression inNotStockFilter = query.deliveryInfo.inStock.isFalse();
        Predicate ratingZero = query.reviews.isEmpty();

        Predicate notOfInterestFilter = inNotStockFilter
                .or(ratingZero);

        return (Collection<LegoSet>) this.legoSetRepository.findAll(notOfInterestFilter);
    }

    @GetMapping("/byPaymentOption/{id}")
    public Collection<LegoSet> byPaymentOption(@PathVariable String id) {
        return this.legoSetRepository.findByPaymentOptionId(id);
    }
}
