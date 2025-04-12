package com.doomdev.admin_blog.entities;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public record Principal(String clientId, List<String> scopes) implements UserDetails {

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    if (CollectionUtils.isEmpty(scopes)) {
      return Collections.emptyList();
    }
    return scopes.stream().map(SimpleGrantedAuthority::new).toList();
  }

  @Override
  public String getPassword() {
    return null;
  }

  @Override
  public String getUsername() {
    return clientId;
  }

  @Override
  public boolean isAccountNonExpired() {
    return false;
  }

  @Override
  public boolean isAccountNonLocked() {
    return false;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return false;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

}
