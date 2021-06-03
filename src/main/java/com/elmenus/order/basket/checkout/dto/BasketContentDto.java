package com.elmenus.order.basket.checkout.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BasketContentDto {
    private int basketId;
    private int itemId;
    private float quantity;
}
