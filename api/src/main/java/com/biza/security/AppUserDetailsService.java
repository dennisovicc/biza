package com.biza.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import com.biza.repo.UtilizadorRepo;

import java.util.List;

@Service
public class AppUserDetailsService implements UserDetailsService {

    private final UtilizadorRepo repo;

    public AppUserDetailsService(UtilizadorRepo repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var u = repo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilizador não encontrado"));

        if (!u.isActivo()) throw new UsernameNotFoundException("Utilizador inactivo");

        return new User(
                u.getUsername(),
                u.getPasswordHash(),
                List.of(new SimpleGrantedAuthority("ROLE_" + u.getRole().name()))
        );
    }
}
