package com.knowit.profile.services;

import com.knowit.profile.domain.entities.User;
import com.knowit.profile.domain.models.RegisterUserModel;
import com.knowit.profile.domain.models.UpdateUserModel;
import com.knowit.profile.domain.models.UserProfileResponseModel;
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
    public Consumer<KStream<String, RegisterUserModel>> onUserRegistered() {
        return input -> input.foreach((emptyString, inputProfileModel) -> {
            if (inputProfileModel.getId() == null) {
                return;
            }
            User user = this.modelMapper.map(inputProfileModel, User.class);
            logger.info(
                    "{} {} is registered successfully in Database!",
                    inputProfileModel.getFirstName(),
                    inputProfileModel.getLastName()
            );
            this.userRepository.saveAndFlush(user);
        });
    }

    @Override
    public UserProfileResponseModel getUserProfile(User user) throws UserDoesNotExistException {
        User userInfo = this.fetchByUserId(user.getId());
        return this.modelMapper.map(userInfo, UserProfileResponseModel.class);
    }

    @Override
    public User fetchByUserId(String id) throws UserDoesNotExistException {
        return this.userRepository.findById(id).orElseThrow(() -> new UserDoesNotExistException());
    }

    @Override
    public UserProfileResponseModel updateProfile(
            User user,
            UpdateUserModel updateUserModel
    ) throws UserDoesNotExistException {
        User currentUser = this.fetchByUserId(user.getId());

        currentUser.setFirstName(updateUserModel.getFirstName());
        currentUser.setLastName(updateUserModel.getLastName());
        currentUser.setBornOn(updateUserModel.getBornOn());

        this.userRepository.saveAndFlush(currentUser);
        logger.info("Changes were updated successfully!");
        return this.modelMapper.map(currentUser, UserProfileResponseModel.class);
    }
}
