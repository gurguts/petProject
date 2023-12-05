package com.example.security;

import com.example.exception.JwtAuthenticationException;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.spec.SecretKeySpec;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * Component responsible for JWT (JSON Web Token) operations in the application.
 * <p>
 * This class provides functionalities for creating, validating, and parsing JWT tokens used
 * in the application's authentication and authorization processes.
 */
@Component
public class JwtTokenProvider {
    /**
     * A Spring Security service used for retrieving user details.
     */
    private final UserDetailsService userDetailsService;
    /**
     * This field stores the secret key value, which is used as part of the algorithm to sign JWT tokens.
     */
    @Value("${jwt.secret}")
    private String secretKey;
    /**
     * This field defines the duration for which a JWT token remains valid after its creation.
     */
    @Value("${jwt.expiration}")
    private long validityMilliseconds;

    public JwtTokenProvider(@Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * Initializes the JwtTokenProvider after its construction.
     * <p>
     * This method is annotated with @PostConstruct, indicating that it should be executed after
     * the bean's properties have been initialized. Its primary role is to prepare the secret key
     * used for signing JWT tokens. It processes the key to ensure it is suitable for use with
     * the HMAC-SHA256 signing algorithm.
     * <p>
     * The method performs the following operations:
     * - It uses SHA-256 hashing to hash the original secret key.
     * - It then creates a SecretKeySpec using the hashed key, specifying HMAC-SHA256 as the algorithm.
     * - Finally, the hashed key is encoded using Base64 and reassigned to the secretKey field.
     *
     * @throws NoSuchAlgorithmException If the specified algorithm (SHA-256) is not available.
     */
    @PostConstruct
    protected void init() throws NoSuchAlgorithmException {
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        byte[] hash = sha256.digest(secretKey.getBytes());
        SecretKeySpec secretKeySpec = new SecretKeySpec(hash, "HmacSHA256");
        secretKey = Base64.getEncoder().encodeToString(secretKeySpec.getEncoded());
    }

    /**
     * This method generates a JWT token, which is used for authenticating and authorizing users in the application.
     * The token encapsulates user-specific claims, including the user's login and role. These claims are then
     * used during the user's session to verify their identity and permissions.
     * <p>
     * The process of token creation involves the following steps:
     * - Setting the subject of the token to the user's login.
     * - Adding the user's role as an additional claim.
     * - Setting the issuance time and the expiration time based on the current time and the configured token validity.
     * - Signing the token with the HMAC-SHA256 algorithm using the processed secret key.
     *
     * @param login The login identifier of the user for whom the token is being created.
     * @param role  The role of the user, which will be included in the token claims.
     * @return A string representation of the JWT token.
     */
    public String createToken(String login, String role) {
        Claims claims = Jwts.claims().setSubject(login);
        claims.put("role", role);
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityMilliseconds * 1000);

        byte[] decodedKey = Base64.getDecoder().decode(secretKey);
        SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "HmacSHA256");

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(originalKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * This method checks if the provided JWT token is valid and has not expired.
     * <p>
     * The method performs the following operations:
     * - Decodes the secret key and prepares it for verification.
     * - Uses the JJWT library's parser to parse the token with the decoded secret key.
     * - Checks if the token's expiration time is after the current date and time.
     * <p>
     * If the token is valid and not expired, the method returns true. If the token is invalid, expired,
     * or if any other issue occurs during the parsing and validation process, a JwtAuthenticationException
     * is thrown with an UNAUTHORIZED status, indicating an authentication failure.
     *
     * @param token The JWT token to be validated.
     * @return A boolean indicating whether the token is valid and not expired.
     * @throws JwtAuthenticationException if the token is expired, invalid, or if any other
     *                                    validation issue occurs.
     */
    public boolean validateToken(String token) {
        try {
            byte[] decodedKey = Base64.getDecoder().decode(secretKey);
            SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "HmacSHA256");
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(originalKey)
                    .build()
                    .parseClaimsJws(token);
            return !claimsJws.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtAuthenticationException("JWT token is expired or invalid", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * This method is used to extract the user's details from a given JWT token and create an
     * <p>
     * The method follows these steps:
     * - Extracts the username from the JWT token.
     * - Loads the UserDetails associated with the extracted username using UserDetailsService.
     * - Constructs a UsernamePasswordAuthenticationToken with the UserDetails and its authorities.
     * This token is used by Spring Security to represent the current authenticated user.
     * <p>
     * The constructed Authentication object contains the principal, credentials (which are empty
     * in this case as they are not necessary after authentication), and authorities (roles and
     * permissions) of the user. This object is then used to establish the user's identity and
     * authorization information in the security context of the application.
     *
     * @param token The JWT token from which the user's authentication information is to be extracted.
     * @return An Authentication object representing the user's identity and authorities.
     */
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    /**
     * This method decodes a given JWT token to retrieve the subject, which in the context
     * of this application, is the username of the user.
     * <p>
     * The process involves:
     * - Decoding the Base64-encoded secret key to its original byte array format.
     * - Creating a SecretKeySpec with the decoded key for the HMAC-SHA256 signing algorithm.
     * - Parsing the JWT token with the specified signing key to validate its signature and extract claims.
     * - Retrieving the 'subject' field from the token's claims, which represents the username.
     *
     * @param token The JWT token from which the username is to be extracted.
     * @return The username extracted from the token.
     */
    public String getUsername(String token) {
        byte[] decodedKey = Base64.getDecoder().decode(secretKey);
        SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "HmacSHA256");

        return Jwts.parserBuilder().
                setSigningKey(originalKey)
                .build().parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * This method scans through the cookies present in the HttpServletRequest and looks for a
     * cookie named "authToken". This cookie is expected to contain the JWT token used for
     * authentication. If the "authToken" cookie is found, its value (the JWT token) is returned.
     * <p>
     * If the request does not contain the "authToken" cookie, or if there are no cookies at all,
     * the method returns null, indicating that no JWT token is present in the request.
     * <p>
     * This approach is commonly used in applications where JWT tokens are stored in cookies for
     * managing authentication and session information in a web context.
     *
     * @param request The HttpServletRequest from which the JWT token is to be extracted.
     * @return The JWT token as a String if it's found in the request cookies, or null if not found.
     */
    public String resolveToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("authToken")) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
