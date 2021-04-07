package com.knowit.profile.services;

import com.knowit.profile.domain.entities.User;
import com.knowit.profile.domain.models.UpdateUserResponse;
import com.knowit.profile.domain.models.UserProfileResponse;
import com.knowit.profile.exceptions.UserDoesNotExistException;

public interface ProfileService {

    UserProfileResponse getUserProfile(User user) throws UserDoesNotExistException;

    User fetchUserById(String id) throws UserDoesNotExistException;

    UserProfileResponse updateUser(User user, UpdateUserResponse updateUserResponse) throws UserDoesNotExistException;
}
