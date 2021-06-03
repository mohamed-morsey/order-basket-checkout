package com.elmenus.order.basket.checkout.model;

import com.elmenus.order.basket.checkout.constants.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class BasketContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @ManyToOne
    @JoinColumn(name="basketId", referencedColumnName=Constants.ID_FIELD)
    Basket basket;

    @ManyToOne
    @JoinColumn(name="itemId", referencedColumnName=Constants.ID_FIELD)
    Item item;

    private float quantity;
}
