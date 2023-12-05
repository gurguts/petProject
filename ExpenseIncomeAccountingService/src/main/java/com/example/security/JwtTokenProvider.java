package com.example.security;

import com.example.exception.JwtAuthenticationException;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * Component responsible for JWT (JSON Web Token) operations in the application.
 * <p>
 * This class provides functionalities for creating, validating, and parsing JWT tokens used
 * in the application's authentication and authorization processes.
 */
@Component
public class JwtTokenProvider {

    /**
     * This field stores the secret key value, which is used as part of the algorithm to sign JWT tokens.
     */
    @Value("${jwt.secret}")
    private String secretKey;

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
        String username = getUsername(token);
        List<SimpleGrantedAuthority> authorities = getRoles(token);

        return new UsernamePasswordAuthenticationToken(username, "", authorities);
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
     * This private method decodes a JWT token to extract the role claims. It then converts these roles
     * into SimpleGrantedAuthority objects, which are used by Spring Security for authorization purposes.
     * <p>
     * Steps:
     * - Decodes the JWT secret key and creates a SecretKey object for JWT parsing.
     * - Parses the JWT token using the Jwts parser to extract the claims.
     * - Retrieves the role from the claims and creates a SimpleGrantedAuthority object for each role.
     *
     * @param token The JWT token from which the roles are to be extracted.
     * @return A list of SimpleGrantedAuthority objects representing the user's roles.
     */
    private List<SimpleGrantedAuthority> getRoles(String token) {
        byte[] decodedKey = Base64.getDecoder().decode(secretKey);
        SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "HmacSHA256");

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(originalKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        String role = claims.get("role", String.class);
        authorities.add(new SimpleGrantedAuthority(role));

        return authorities;
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

    /**
     * This method decodes a JWT token to extract the subject, which in this context, is the user's login.
     * It uses the JwtParser from the Jwts library to parse the token, with the secret key decoded from Base64
     * format to validate the token's signature.
     * <p>
     * Process:
     * - Decodes the secret key used for signing the JWT token.
     * - Parses the token using the JwtParser to obtain the claims.
     * - Extracts the subject (login) from the claims.
     *
     * @param token The JWT token from which the login is to be extracted.
     * @return The login (subject) extracted from the JWT token.
     */
    public String getLoginFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Base64.getDecoder().decode(secretKey))
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }
}