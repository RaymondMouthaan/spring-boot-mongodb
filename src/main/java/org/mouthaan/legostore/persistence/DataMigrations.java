package org.mouthaan.legostore.persistence;

import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;
import org.mouthaan.legostore.model.LegoSet;
import org.mouthaan.legostore.model.PaymentOptions;
import org.mouthaan.legostore.model.PaymentType;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

@ChangeLog(order = "001")
public class DataMigrations {

    @ChangeSet(order = "001", author = "ray", id = "update nb parts")
    public void updateNbOfParts(MongoTemplate mongoTemplate) {
        Criteria priceZeroCriteria = new Criteria().orOperator(
                Criteria.where("nbParts").is(0),
                Criteria.where("nbParts").is(null)
        );

        mongoTemplate.updateMulti(
                new Query(priceZeroCriteria),
                Update.update("nbParts", 122),
                LegoSet.class);

    }

    @ChangeSet(order = "002", author = "ray", id = "insert additional payment")
    public void insertPaymentOption(MongoTemplate mongoTemplate) {
        PaymentOptions bankTransferPayment = new PaymentOptions(PaymentType.BankTransfer, 1);
        mongoTemplate.insert(bankTransferPayment);
    }
}
