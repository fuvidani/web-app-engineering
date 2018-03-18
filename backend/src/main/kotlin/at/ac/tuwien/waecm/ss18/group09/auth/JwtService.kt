package at.ac.tuwien.waecm.ss18.group09.auth

import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.User
import java.util.*
import javax.crypto.spec.SecretKeySpec
import javax.xml.bind.DatatypeConverter

/**
 * <h4>About this class</h4>
 *
 * <p>Description</p>
 *
 * @author Michael Sober
 * @version 1.0.0
 * @since 1.0.0
 */
class JwtService : IJwtService {

    private val CLAIM_ROLES = "roles"

    @Value("\${spring.security.token.secret}")
    private val tokenSecret: String? = null

    @Value("\${spring.security.token.expiration}")
    private val tokenExpiration: Long = 0

    @Throws(JwtServiceException::class)
    override fun generateJwt(user: User): String {

        val nowMillis = System.currentTimeMillis()
        val nowDate = Date(nowMillis)
        val expirationDate = Date(nowMillis + tokenExpiration)

        val signatureAlgorithm = SignatureAlgorithm.HS256
        val tokenSecretBytes = DatatypeConverter.parseBase64Binary(tokenSecret)
        val tokenKey = SecretKeySpec(tokenSecretBytes, signatureAlgorithm.jcaName)

        val jwtBuilder = Jwts.builder()
                .setClaims(generatePrivateClaims(user))
                .setSubject(user.username)
                .setIssuedAt(nowDate)
                .setExpiration(expirationDate)
                .signWith(signatureAlgorithm, tokenKey)

        return jwtBuilder.compact()

    }

    /**
     * Generates the private claims with the given details of the user
     * @param user the details which are used to created the private claims
     * @return the private claims with the user information
     */
    private fun generatePrivateClaims(user: User): Map<String, Any> {

        val claims = HashMap<String, Any>()

        val stringBuilder = StringBuilder()
        var separator = ""

        for (authority in user.authorities) {
            stringBuilder.append(separator)
            stringBuilder.append(authority.authority)
            separator = ","
        }

        claims[CLAIM_ROLES] = stringBuilder.toString()

        return claims

    }

    @Throws(JwtServiceException::class)
    override fun parseJwt(token: String): User {

        val claims = getClaims(token)

        val authorities = claims[CLAIM_ROLES] as String
        val grantedAuthorityList = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities)

        return User(claims.subject, "[PROTECTED]", grantedAuthorityList)

    }

    /**
     * Gets the claims from the token.
     * @param token the token from which the claims should be extracted.
     * @return the claims of the given token.
     * @throws JwtServiceException this exception is thrown if an error occurs while extracting the claims of the token.
     */
    @Throws(JwtServiceException::class)
    private fun getClaims(token: String): Claims {

        try {
            return Jwts.parser()
                    .setSigningKey(tokenSecret)
                    .parseClaimsJws(token).body
        } catch (e: JwtException) {
            throw JwtServiceException("Error while parsing the claims of the jwt: " + e.message)
        }

    }

}