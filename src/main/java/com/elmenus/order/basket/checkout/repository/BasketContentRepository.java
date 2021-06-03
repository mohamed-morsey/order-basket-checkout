package com.elmenus.order.basket.checkout.repository;

import com.elmenus.order.basket.checkout.model.BasketContent;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BasketContentRepository extends CrudRepository<BasketContent, Integer> {
    List<BasketContent> findByBasketId(Integer basketId);
}
