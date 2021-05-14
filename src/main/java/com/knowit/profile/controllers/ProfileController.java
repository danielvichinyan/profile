package com.knowit.profile.controllers;

import com.knowit.profile.domain.entities.User;
import com.knowit.profile.domain.models.UpdateUserModel;
import com.knowit.profile.domain.models.UserProfileResponseModel;
import com.knowit.profile.exceptions.UserDoesNotExistException;
import com.knowit.profile.services.ProfileService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/me")
    public UserProfileResponseModel getUserProfile(
            @AuthenticationPrincipal User user
    ) throws UserDoesNotExistException {
        return this.profileService.getUserProfile(user);
    }

    @PutMapping("/me")
    public UserProfileResponseModel updateUserProfile(
            @AuthenticationPrincipal User user,
            @RequestBody UpdateUserModel updateUserModel
    ) throws UserDoesNotExistException {
        return this.profileService.updateProfile(user, updateUserModel);
    }
}
