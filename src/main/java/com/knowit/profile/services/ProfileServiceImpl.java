package com.knowit.profile.services;

import com.knowit.profile.domain.entities.User;
import com.knowit.profile.domain.models.UpdateUserResponse;
import com.knowit.profile.domain.models.UserProfileResponse;
import com.knowit.profile.domain.models.UserRegistrationModel;
import com.knowit.profile.exceptions.UserDoesNotExistException;
import com.knowit.profile.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.apache.kafka.streams.kstream.KStream;
import java.util.function.Consumer;

@Service
public class ProfileServiceImpl implements ProfileService {

    private static final Logger logger = LoggerFactory.getLogger(ProfileServiceImpl.class);

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    public ProfileServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Bean
    public Consumer<KStream<String, UserRegistrationModel>> onUserRegistered() {
        return input -> input.foreach((emptyString, userProfile) -> {
            if (userProfile.getId() == null) {
                return;
            }
            User user = this.modelMapper.map(userProfile, User.class);
            logger.info(
                    "{} {} is registered successfully!",
                    userProfile.getFirstName(),
                    userProfile.getLastName()
            );
        });
    }

    @Override
    public UserProfileResponse getUserProfile(User user) throws UserDoesNotExistException {
        User userDetails = this.fetchUserById(user.getId());

        return this.modelMapper.map(userDetails, UserProfileResponse.class);
    }

    @Override
    public User fetchUserById(String id) throws UserDoesNotExistException {
        return this.userRepository.findById(id).orElseThrow(() -> new UserDoesNotExistException());
    }

    @Override
    public UserProfileResponse updateUser(
            User user,
            UpdateUserResponse updateUserResponse
    ) throws UserDoesNotExistException {
        User currentUser = this.fetchUserById(user.getId());

        currentUser.setUsername(updateUserResponse.getUsername());
        currentUser.setFirstName(updateUserResponse.getFirstName());
        currentUser.setLastName(updateUserResponse.getLastName());
        currentUser.setEmail(updateUserResponse.getEmail());

        this.userRepository.saveAndFlush(currentUser);
        logger.info("User updated successfully!");

        return this.modelMapper.map(currentUser, UserProfileResponse.class);
    }
}
