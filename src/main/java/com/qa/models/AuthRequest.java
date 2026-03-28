package com.qa.models;

import lombok.Builder;
import lombok.Data;

/**
 * POJO for authentication request payloads (login / register).
 *
 * <p>Example JSON:
 * <pre>
 * {
 *   "email": "eve.holt@reqres.in",
 *   "password": "cityslicka"
 * }
 * </pre>
 */
@Data
@Builder
public class AuthRequest {

    /** The user's email address. */
    private String email;

    /** The user's password. */
    private String password;
}
