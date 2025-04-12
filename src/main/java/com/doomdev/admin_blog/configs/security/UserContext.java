package com.doomdev.admin_blog.configs.security;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
@Setter
public class UserContext extends User {

    private Long userId;
    private String username;

    public UserContext(String username,
                       String password,
                       Long userId,
                       Collection<? extends GrantedAuthority> authorities
    ) {
        super(username, password, authorities);
        this.username = username;
        this.userId = userId;
    }
}
