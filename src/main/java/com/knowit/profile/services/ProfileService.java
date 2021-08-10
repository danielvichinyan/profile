package com.knowit.profile.services;

import com.knowit.profile.domain.entities.User;
import com.knowit.profile.domain.models.UpdateUserModel;
import com.knowit.profile.domain.models.UserGainPointsRequestModel;
import com.knowit.profile.domain.models.UserGainPointsResponseModel;
import com.knowit.profile.domain.models.UserProfileResponseModel;
import com.knowit.profile.exceptions.UserDoesNotExistException;

import java.util.List;

public interface ProfileService {

    UserProfileResponseModel getUserProfile(User user) throws UserDoesNotExistException;

    User fetchByUserId(String id) throws UserDoesNotExistException;

    UserProfileResponseModel updateProfile(User user, UpdateUserModel updateUserModel) throws UserDoesNotExistException;

    UserProfileResponseModel updateUserPoints(User user, UserGainPointsRequestModel model) throws UserDoesNotExistException;

    List<UserProfileResponseModel> getAllUsers();
}