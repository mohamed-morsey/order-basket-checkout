package com.elmenus.order.basket.checkout.repository;

import com.elmenus.order.basket.checkout.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
}
