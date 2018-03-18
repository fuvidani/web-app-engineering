package at.ac.tuwien.waecm.ss18.group09.auth

import org.springframework.security.core.userdetails.User

/**
 * <h4>About this class</h4>
 *
 * <p>Description</p>
 *
 * @author Michael Sober
 * @version 1.0.0
 * @since 1.0.0
 */
interface IJwtService {

  /**
   * This method generates a signed JWT for the given UserDetails.
   * @param user the details from the user which are stored in the payload of the JWT.
   * @return the generated JWT, with the UserDetails in the payload.
   * @throws JwtServiceException this exception is thrown if an error occurs while generating the JWT.
   */
  @Throws(JwtServiceException::class)
  fun generateJwt(user: User): String

  /**
   * This method parses the signed JWT and extracts the information from the payload to construct the UserDetails.
   * @param token the token which should get parsed.
   * @return the constructed user from the token payload.
   * @throws JwtServiceException this exception is thrown if an error occurs while parsing the JWT.
   */
  @Throws(JwtServiceException::class)
  fun parseJwt(token: String): User

}