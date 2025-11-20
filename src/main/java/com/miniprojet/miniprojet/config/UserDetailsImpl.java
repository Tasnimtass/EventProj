package com.miniprojet.miniprojet.config;

import com.miniprojet.miniprojet.entity.Utilisateur;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UserDetailsImpl implements UserDetails {

    private final String email;
    private final Integer id;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;

    // Constructeur de UserDetailsImpl qui prend un Utilisateur
    public UserDetailsImpl(Utilisateur user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.password = user.getMdp();
        // Récupération des rôles et transformation en Authorities
        this.authorities = user.getRoles().stream()//grandauthority interfac qui presente les role
                .map(role -> (GrantedAuthority) () -> "ROLE_" + role.getName().name()) // ()-> c'et une expression lambda pour appleelr la methode de interface grandauthorities(getAuthority())
                .collect(Collectors.toList());
    }// spring yfhm list<grandauthorities> hak alleh makkhthinech set<role>

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Renvoyer true ou une logique basée sur l'état de l'utilisateur
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // À ajuster selon la logique de verrouillage de compte
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // À ajuster selon la logique de validation des identifiants
    }

    @Override
    public boolean isEnabled() {
        return true; // À ajuster selon la logique de statut de l'utilisateur
    }

    public Integer getId() {
        return this.id;
    }
}
