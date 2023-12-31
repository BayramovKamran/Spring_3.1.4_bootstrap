package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.kata.spring.boot_security.demo.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findByUsername(String username) {
        if (userRepository.findByUsername(username).isEmpty()) {
            throw new UsernameNotFoundException("Пользователь с таким именем не найден");
        }
        return userRepository.findByUsername(username).get();
    }

    @Override
    public User findUserById(Long id) {
        if (userRepository.findById(id).isEmpty()) {
            throw new UsernameNotFoundException("Пользователь с таким ID не найден");
        }
        return userRepository.findById(id).get();
    }

    @Transactional
    @Override
    public void updateUser(User updateUser, Long id) {

        User user = userRepository.getById(id);
        user.setUsername(updateUser.getUsername());
        user.setLastname(updateUser.getLastname());
        user.setAge(updateUser.getAge());
        user.setEmail(updateUser.getEmail());
        user.setRoles(updateUser.getRoles());
        user.setPassword(passwordEncoder.encode(updateUser.getPassword()));
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void deleteUserById(Long id) {
        if (userRepository.findById(id).isPresent()) {
            userRepository.deleteById(id);
        }
    }
}