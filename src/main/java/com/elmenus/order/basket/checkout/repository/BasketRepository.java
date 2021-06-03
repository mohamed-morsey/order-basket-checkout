package com.elmenus.order.basket.checkout.repository;

import com.elmenus.order.basket.checkout.model.Basket;
import org.springframework.data.repository.CrudRepository;

public interface BasketRepository extends CrudRepository<Basket, Integer> {
}
