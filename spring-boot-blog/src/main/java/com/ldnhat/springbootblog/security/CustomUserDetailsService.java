package com.ldnhat.springbootblog.security;

import com.ldnhat.springbootblog.entity.RoleEntity;
import com.ldnhat.springbootblog.entity.UserEntity;
import com.ldnhat.springbootblog.repository.UserRepository;
import com.ldnhat.springbootblog.utils.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException(AppConstants.USERNAME_OR_EMAIL_NOT_FOUND
                                +usernameOrEmail));
        return new User(userEntity.getEmail(), userEntity.getPassword(),
                mapRolesToAuthorities(userEntity.getRoleEntities()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Set<RoleEntity> roleEntities){
        return roleEntities.stream().map(roleEntity -> new SimpleGrantedAuthority(roleEntity.getName()))
        .collect(Collectors.toList());
    }
}
