package at.ac.tuwien.waecm.ss18.group09.dto;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.Objects;

@Document(collection = "researchFacility")
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
