package com.elmenus.order.basket.checkout.model;

import com.elmenus.order.basket.checkout.constants.Messages;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = Messages.NAME_BLANK_ERROR)
    private String name;

    @Min(value = 0, message = Messages.PRICE_NEGATIVE_ERROR)
    private float price;

    @Min(value = 0, message = Messages.QUANTITY_NEGATIVE_ERROR)
    private float quantity;
}
