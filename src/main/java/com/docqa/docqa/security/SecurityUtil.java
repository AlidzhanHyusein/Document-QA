package com.docqa.docqa.security;

import com.docqa.docqa.entity.User;
import com.docqa.docqa.exception.FileProcessingException;
import com.docqa.docqa.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {

    private final UserRepository userRepository;

    public SecurityUtil(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new FileProcessingException("User not authenticated");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            return userRepository.findByUsername(username)
                    .orElseThrow(() -> new FileProcessingException("User not found"));
        }

        throw new FileProcessingException("Invalid authentication");
    }

    public String getCurrentUsername() {
        return getCurrentUser().getUsername();
    }
}