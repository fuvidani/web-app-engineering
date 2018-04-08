package at.ac.tuwien.waecm.ss18.group09.dto;

import java.util.Objects;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.userdetails.UserDetails;

@Document(collection = "abstract_user")
public abstract class AbstractUser implements UserDetails {

  @Id private String id;

  @Indexed(unique = true)
  @Email
  @NotBlank
  private String email;

  @NotBlank private String password;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @Override
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public boolean equals(Object o) {
      if (this == o) {
          return true;
      }
      if (o == null || getClass() != o.getClass()) {
          return false;
      }
    AbstractUser that = (AbstractUser) o;
    return Objects.equals(id, that.id)
        && Objects.equals(email, that.email)
        && Objects.equals(password, that.password);
  }

  @Override
  public int hashCode() {

    return Objects.hash(id, email, password);
  }

  @Override
  public String toString() {
    return "AbstractUser{"
        + "id='"
        + id
        + '\''
        + ", email='"
        + email
        + '\''
        + ", password='"
        + password
        + '\''
        + '}';
  }
}
