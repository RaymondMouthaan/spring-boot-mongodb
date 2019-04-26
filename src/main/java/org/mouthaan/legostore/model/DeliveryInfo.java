package org.mouthaan.legostore.model;

import java.time.LocalDate;

public class DeliveryInfo {
    private LocalDate deliveryDate;
    private int deliveryFee;
    private boolean inStock;

    public DeliveryInfo(LocalDate deliveryDate, int deliveryFee, boolean inStock) {
        this.deliveryDate = deliveryDate;
        this.deliveryFee = deliveryFee;
        this.inStock = inStock;
    }

    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    public int getDeliveryFee() {
        return deliveryFee;
    }

    public boolean isInStock() {
        return inStock;
    }
}
