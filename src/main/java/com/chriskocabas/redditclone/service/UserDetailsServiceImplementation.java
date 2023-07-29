package com.chriskocabas.redditclone.service;

import com.chriskocabas.redditclone.model.User;
import com.chriskocabas.redditclone.repository.IUserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

import static java.util.Collections.singletonList;

@Service
@AllArgsConstructor
public class UserDetailsServiceImplementation  {

    private final IUserRepository userRepository;


//    @Override
//    @Transactional
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Optional<User> userOptional = userRepository.findByUsername(username);
//        User user = userOptional.orElseThrow(()->
//                new UsernameNotFoundException("User "+ username+ " could not be found."));
//
//        return  new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),user.isEnabled(),
//                true,true,true,getAuthorities("USER"));
//    }
//
//    private Collection<? extends GrantedAuthority> getAuthorities(String role){
//        return singletonList(new SimpleGrantedAuthority(role));
//    }
}
