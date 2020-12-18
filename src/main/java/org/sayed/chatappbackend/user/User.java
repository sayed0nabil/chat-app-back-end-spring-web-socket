package org.sayed.chatappbackend.user;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User implements UserDetails {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    private String username;

    @JsonIgnore
    private String password;

    @JsonIgnore
    private boolean enabled;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @JsonIgnore
    private Set<Role> authorities = new HashSet<>();

    public User(){
        this.enabled = true;
    }
    public User(String username, String password) {
        Set<Role> authorityList = new HashSet<>();
        authorityList.add(new Role("USER"));

        this.username = username;
        this.password = password;
        this.authorities = authorityList;
        this.enabled = true;
    }

    public Long getId() {
        return id;
    }

    public Collection<? extends GrantedAuthority> getAuthorities(){
        List<GrantedAuthority> authorityList = new ArrayList<>();
        authorities.forEach((role -> authorityList.add(new SimpleGrantedAuthority(role.getName()))));
        return authorityList;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

}