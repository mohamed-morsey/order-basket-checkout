package com.elmenus.order.basket.checkout.service.impl;

import com.elmenus.order.basket.checkout.constants.Messages;
import com.elmenus.order.basket.checkout.dto.UserDto;
import com.elmenus.order.basket.checkout.model.User;
import com.elmenus.order.basket.checkout.repository.UserRepository;
import com.elmenus.order.basket.checkout.service.CrudService;
import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Service for handling CRUD operations of {@link User}
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UsersService implements CrudService<User, UserDto> {
    private static final ModelMapper MODEL_MAPPER = new ModelMapper();

    private final UserRepository userRepository;

    static {
        // id should be ignored when mapping UserDto to User
        MODEL_MAPPER.addMappings(new PropertyMap<UserDto, User>() {
            @Override
            protected void configure() {
                skip(destination.getId());
            }
        });
    }

    /**
     * Fetches all {@link User} entities from database
     *
     * @return List of all {@link User} entities
     */


    public List<User> getAll() {
        List<User> userList = new ArrayList<>();
        Iterator<User> users = userRepository.findAll().iterator();
        users.forEachRemaining(userList::add);
        return userList;
    }

    /**
     * Fetches a specific {@link User} by ID
     *
     * @param id The ID of the {@link User}
     * @return The {@link User} whose ID matches the specified ID, otherwise a {@link EntityNotFoundException} is thrown
     */
    public User get(@NonNull Integer id) {
        Preconditions.checkNotNull(id, Messages.ID_NULL_ERROR);
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> {
                    String errorMessage = String.format(Messages.USER_NOT_FOUND_ERROR, id);
                    log.warn(errorMessage);
                    throw new EntityNotFoundException(errorMessage);
                });
        return existingUser;
    }

    /**
     * Adds a new {@link User} to the system
     *
     * @param userDto The {@link UserDto} to be added
     * @return The ID of the new {@link User}
     */
    public Integer add(@NonNull UserDto userDto) {
        User user = MODEL_MAPPER.map(userDto, User.class);
        User newUser = userRepository.save(user);

        log.info(Messages.USER_CREATED_MESSAGE);

        return newUser.getId();
    }

    /**
     * Updates an existing {@link User} to the system if not exists, otherwise
     *
     * @param id      The ID of the {@link User} to be updated
     * @param userDto The {@link UserDto} to be updated
     */
    public void update(@NonNull Integer id, @NonNull UserDto userDto) {
        Preconditions.checkNotNull(id, Messages.ID_NULL_ERROR);
        Preconditions.checkNotNull(userDto, Messages.USER_NULL_ERROR);

        User user = userRepository.findById(id).orElseThrow(() -> {
            String errorMessage = String.format(Messages.USER_NOT_FOUND_ERROR, id);
            log.warn(errorMessage);
            throw new EntityNotFoundException(errorMessage);
        });

        MODEL_MAPPER.map(userDto, user);
        userRepository.save(user);

        log.info(Messages.USER_UPDATED_MESSAGE);
    }

    /**
     * Deletes a specific {@link User} by ID if exists otherwise a {@link EntityNotFoundException} is thrown
     */
    @Async
    public void delete(@NonNull Integer id) {
        Preconditions.checkNotNull(id, Messages.ID_NULL_ERROR);
        if (!exists(id)) {
            String errorMessage = String.format(Messages.USER_NOT_FOUND_ERROR, id);
            log.warn(errorMessage);
            throw new EntityNotFoundException(errorMessage);
        }

        userRepository.deleteById(id);

        log.info(Messages.USER_DELETED_MESSAGE);
    }

    /**
     * Checks if specific {@link User} with the specified ID exists or not
     *
     * @param id The ID of the {@link User} to be checked
     * @return True if exists, false otherwise
     */
    private boolean exists(Integer id) {
        return userRepository.existsById(id);
    }

}
