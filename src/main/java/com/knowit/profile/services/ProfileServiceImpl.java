package com.knowit.profile.services;

import com.knowit.profile.domain.entities.User;
import com.knowit.profile.domain.models.*;
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

        if (!updateUserModel.getFirstName().equals("")) {
            currentUser.setFirstName(updateUserModel.getFirstName());
        }

        if (!updateUserModel.getLastName().equals("")) {
            currentUser.setLastName(updateUserModel.getLastName());
        }

        if (!updateUserModel.getBornOn().equals("")) {
            currentUser.setBornOn(updateUserModel.getBornOn());
        }

        if (!updateUserModel.getEmail().equals("")) {
            currentUser.setEmail(updateUserModel.getEmail());
        }

//        this.updateAvatar(image, currentUser.getId());

        this.userRepository.saveAndFlush(currentUser);
        logger.info("Changes were updated successfully!");

        return this.modelMapper.map(currentUser, UserProfileResponseModel.class);

    }

    @Override
    public UserProfileResponseModel updateUserPoints(
            User user,
            UserGainPointsRequestModel model
    ) throws UserDoesNotExistException {
        User currentUser = this.fetchByUserId(user.getId());
        System.out.println("Incoming points: " + model.getQuizPoints());
        System.out.println("Old user points: " + currentUser.getQuizPoints());
        currentUser.setQuizPoints(model.getQuizPoints());
        System.out.println("New user points: " + user.getQuizPoints());
        this.userRepository.saveAndFlush(currentUser);
        logger.info("Changes were updated successfully!");

        return this.modelMapper.map(currentUser, UserProfileResponseModel.class);
    }
}