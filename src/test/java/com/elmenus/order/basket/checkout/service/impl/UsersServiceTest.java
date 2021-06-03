package com.elmenus.order.basket.checkout.service.impl;

import com.elmenus.order.basket.checkout.constants.Messages;
import com.elmenus.order.basket.checkout.dto.UserDto;
import com.elmenus.order.basket.checkout.model.User;
import com.elmenus.order.basket.checkout.repository.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Test class for {@link UsersService}
 */
@RunWith(MockitoJUnitRunner.class)
public class UsersServiceTest {
    // region field values
    private static final int ID = 1;
    private static final String FIRSTNAME = "John";
    private static final String LASTNAME = "Smith";
    private static final String USERNAME = "jsmith";
    private static final String EMAIL = "smith@example.org";
    private static final String MODIFIED_USERNAME = "johnsmith";
    // endregion

    UsersService usersService;

    @Mock
    UserRepository userRepository;

    User user;
    UserDto userDto;

    @Before
    public void setUp() throws Exception {
        usersService = Mockito.spy(new UsersService(userRepository));

        user = new User(ID, FIRSTNAME, LASTNAME, USERNAME, EMAIL);
        userDto = new UserDto(FIRSTNAME, LASTNAME, USERNAME, EMAIL);
    }

    /**
     * Test {@link UsersService#getAll()}
     */
    @Test
    public void testGetAll() {
        // GIVEN
        Mockito.when(userRepository.findAll()).thenReturn(Set.of(user));

        // WHEN
        List<User> users = usersService.getAll();

        // THEN
        Assert.assertNotNull(users);
        Assert.assertFalse(users.isEmpty());
        Assert.assertEquals(1, users.size());
    }

    /**
     * Tests {@link UsersService#get(Integer)}
     */
    @Test
    public void testGet() {
        // GIVEN
        Mockito.when(userRepository.findById(ID)).thenReturn(Optional.of(user));

        // WHEN
        User returnedUser = usersService.get(ID);

        // THEN
        Assert.assertNotNull(returnedUser);
        Assert.assertEquals(user, returnedUser);
    }

    /**
     * Tests {@link UsersService#get(Integer)} but for a NULL ID
     */
    @Test
    public void testGetForNullId() {
        // GIVEN

        // WHEN
        NullPointerException exception = Assert.assertThrows(NullPointerException.class, () -> usersService.get(null));

        // THEN
        Assert.assertNotNull(exception);
        Assert.assertEquals(Messages.ID_NULL_ERROR, exception.getMessage());
    }

    /**
     * Tests {@link UsersService#get(Integer)} but for a a nonexistent {@link User}
     */
    @Test
    public void testGetForNonexistentUser() {
        // GIVEN
        Mockito.when(userRepository.findById(ID)).thenReturn(Optional.empty());

        // WHEN
        EntityNotFoundException exception = Assert.assertThrows(EntityNotFoundException.class, () -> usersService.get(ID));

        // THEN
        Assert.assertNotNull(exception);
        String errorMessage = String.format(Messages.USER_NOT_FOUND_ERROR, ID);
        Assert.assertEquals(errorMessage, exception.getMessage());
    }

    /**
     * Tests {@link UsersService#add(UserDto)}
     */
    @Test
    public void testAdd() {
        // GIVEN
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);

        // WHEN
        Integer id = usersService.add(userDto);

        // THEN
        Assert.assertNotNull(id);
        Assert.assertEquals(ID, id.intValue());
    }

    /**
     * Tests {@link UsersService#update(Integer, UserDto)}
     */
    @Test
    public void testUpdate() {
        // GIVEN
        Mockito.when(userRepository.findById(ID)).thenReturn(Optional.of(user));
        userDto.setUsername(MODIFIED_USERNAME);

        // WHEN
        usersService.update(ID, userDto);

        // THEN
        Mockito.verify(userRepository).save(user);
        Assert.assertEquals(MODIFIED_USERNAME, user.getUsername());
    }

    /**
     * Tests {@link UsersService#update(Integer, UserDto)} but for a NULL ID
     */
    @Test
    public void testUpdateForNullId() {
        // GIVEN

        // WHEN
        NullPointerException exception = Assert.assertThrows(NullPointerException.class, () -> usersService.update(null, userDto));

        // THEN
        Assert.assertNotNull(exception);
        Assert.assertEquals(Messages.ID_NULL_ERROR, exception.getMessage());
    }

    /**
     * Tests {@link UsersService#update(Integer, UserDto)} but for a NULL {@link UserDto}
     */
    @Test
    public void testUpdateForNullUserDto() {
        // GIVEN

        // WHEN
        NullPointerException exception = Assert.assertThrows(NullPointerException.class, () -> usersService.update(ID, null));

        // THEN
        Assert.assertNotNull(exception);
        Assert.assertEquals(Messages.USER_NULL_ERROR, exception.getMessage());
    }

    /**
     * Tests {@link UsersService#update(Integer, UserDto)} but for a a nonexistent {@link User}
     */
    @Test
    public void testUpdateForNonexistentUser() {
        // GIVEN
        Mockito.when(userRepository.findById(ID)).thenReturn(Optional.empty());

        // WHEN
        EntityNotFoundException exception = Assert.assertThrows(EntityNotFoundException.class, () -> usersService.update(ID, userDto));

        // THEN
        Assert.assertNotNull(exception);
        String errorMessage = String.format(Messages.USER_NOT_FOUND_ERROR, ID);
        Assert.assertEquals(errorMessage, exception.getMessage());
    }

    /**
     * Tests {@link UsersService#delete(Integer)}
     */
    @Test
    public void testDelete() {
        // GIVEN
        Mockito.when(userRepository.existsById(ID)).thenReturn(true);

        // WHEN
        usersService.delete(ID);

        // THEN
        Mockito.verify(userRepository).deleteById(ID);
    }

    /**
     * Tests Tests {@link UsersService#delete(Integer)} but for a NULL ID
     */
    @Test
    public void testDeleteForNullId() {
        // GIVEN

        // WHEN
        NullPointerException exception = Assert.assertThrows(NullPointerException.class, () -> usersService.delete(null));

        // THEN
        Assert.assertNotNull(exception);
        Assert.assertEquals(Messages.ID_NULL_ERROR, exception.getMessage());
    }

    /**
     * Tests Tests {@link UsersService#delete(Integer)} but for a a nonexistent {@link User}
     */
    @Test
    public void testDeleteForNonexistentUserF() {
        // GIVEN
        Mockito.when(userRepository.existsById(ID)).thenReturn(false);

        // WHEN
        EntityNotFoundException exception = Assert.assertThrows(EntityNotFoundException.class, () -> usersService.delete(ID));

        // THEN
        Assert.assertNotNull(exception);
        String errorMessage = String.format(Messages.USER_NOT_FOUND_ERROR, ID);
        Assert.assertEquals(errorMessage, exception.getMessage());
    }
}