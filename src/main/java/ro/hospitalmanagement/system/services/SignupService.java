package ro.hospitalmanagement.system.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.hospitalmanagement.system.entities.UserEntity;
import ro.hospitalmanagement.system.repositories.UserRepository;
import ro.hospitalmanagement.system.requests.SignupRequest;

@Service
public class SignupService {
    @Autowired
    UserRepository userRepository;
    public UserEntity add(SignupRequest signupRequest){
        UserEntity userEntity = new UserEntity();
        userEntity.setFirstName(signupRequest.getFirstName());
        userEntity.setLastName(signupRequest.getLastName());
        userEntity.setAge(signupRequest.getAge());
        userEntity.setUsername(signupRequest.getUsername());
        UserEntity user = userRepository.save(userEntity);
        return user;
    }
}