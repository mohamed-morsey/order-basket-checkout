package com.elmenus.order.basket.checkout.repository;

import com.elmenus.order.basket.checkout.model.Item;
import org.springframework.data.repository.CrudRepository;

public interface ItemRepository extends CrudRepository<Item, Integer> {
}
