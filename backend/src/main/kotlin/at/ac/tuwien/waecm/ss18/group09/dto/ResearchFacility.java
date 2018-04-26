package at.ac.tuwien.waecm.ss18.group09.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.Collection;

public class ResearchFacility extends AbstractUser {

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    String rolesStr = "ROLE_RESEARCH";
    return AuthorityUtils.commaSeparatedStringToAuthorityList(rolesStr);
  }

  @Override
  public String getUsername() {
    return getEmail();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
