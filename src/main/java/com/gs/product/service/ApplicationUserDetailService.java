//package com.gs.product.service;
//
//import com.gs.product.domain.ApplicationUser;
//import com.gs.product.repository.ApplicationUserRepository;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import static java.util.Collections.emptyList;
//
//@Service
//public class ApplicationUserDetailService implements UserDetailsService {
//    private final ApplicationUserRepository applicationUserRepository;
//
//    public ApplicationUserDetailService(ApplicationUserRepository applicationUserRepository) {
//        this.applicationUserRepository = applicationUserRepository;
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        ApplicationUser applicationUser = applicationUserRepository.findByUsername(username);
//        if (applicationUser == null) {
//            throw new UsernameNotFoundException(username);
//        }
//        return new User(applicationUser.getUsername(), applicationUser.getPassword(), emptyList());
//    }
//}
