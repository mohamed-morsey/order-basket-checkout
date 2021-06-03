package com.elmenus.order.basket.checkout.service;

import org.springframework.lang.NonNull;

import java.util.List;

/**
 * Provides support for the basic CRUD operations of type T
 *
 * @param <T> The actual type of the entity
 * @param <D> The DTO associated with that type
 */
public interface CrudService<T, D> {
    List<T> getAll();

    T get(@NonNull Integer id);

    Integer add(@NonNull D dtoObject);

    void update(@NonNull Integer id, @NonNull D dtoObject);

    void delete(@NonNull Integer id);
}
