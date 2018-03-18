package at.ac.tuwien.waecm.ss18.group09.auth

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority

/**
 * <h4>About this class</h4>
 *
 * <p>Description</p>
 *
 * @author Michael Sober
 * @version 1.0.0
 * @since 1.0.0
 */
class JwtAuthenticationToken : AbstractAuthenticationToken {

    constructor(principal: String, credentials: String) : super(null) {
        super.setAuthenticated(true)
        isAuthenticated = false
    }

    constructor(
        principal: String,
        credentials: String,
        authorities: Collection<out GrantedAuthority>
    ) : super(authorities) {
        super.setAuthenticated(true)
    }

    override fun getPrincipal(): Any {
        return principal
    }

    override fun getCredentials(): Any {
        return credentials
    }

    @Throws(IllegalArgumentException::class)
    override fun setAuthenticated(isAuthenticated: Boolean) {
        if (isAuthenticated) {
            throw IllegalArgumentException(
                "Cannot set this token to trusted - use constructor which takes a " +
                        "GrantedAuthority list instead"
            )
        }

        super.setAuthenticated(false)
    }
}