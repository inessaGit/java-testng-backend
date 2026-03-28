package com.qa.utils;

import com.qa.models.Post;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;
import java.util.UUID;

/**
 * Factory class for generating randomised test data.
 *
 * All methods are static — this class is not meant to be instantiated.
 *
 * <p>Example usage:
 * <pre>
 *   Post post = DataFactory.randomPost();
 *   String email = DataFactory.randomEmail();
 *   String text  = DataFactory.randomString(10);
 *   int    id    = DataFactory.randomInt(100);
 * </pre>
 */
public final class DataFactory {

    private static final Logger log = LogManager.getLogger(DataFactory.class);
    private static final Random RANDOM = new Random();

    private static final String ALPHA_NUMERIC = "abcdefghijklmnopqrstuvwxyz0123456789";

    private DataFactory() {
        // Utility class — prevent instantiation
    }

    /**
     * Creates a {@link Post} with randomly generated title, body, and userId.
     *
     * @return a new Post with random data (id is null — server-assigned)
     */
    public static Post randomPost() {
        Post post = Post.builder()
                .userId(randomInt(10) + 1)   // userId 1–10
                .title(randomString(15))
                .body(randomString(50))
                .build();
        log.debug("Generated random Post: {}", post);
        return post;
    }

    /**
     * Creates a random email address in the form {@code test_<uuid>@example.com}.
     *
     * @return a unique email string
     */
    public static String randomEmail() {
        String email = "test_" + UUID.randomUUID().toString().replace("-", "").substring(0, 8) + "@example.com";
        log.debug("Generated random email: {}", email);
        return email;
    }

    /**
     * Creates a random alphanumeric string of the given length.
     *
     * @param length the desired string length (must be &gt; 0)
     * @return a random alphanumeric string
     */
    public static String randomString(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Length must be greater than 0, was: " + length);
        }
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(ALPHA_NUMERIC.charAt(RANDOM.nextInt(ALPHA_NUMERIC.length())));
        }
        return sb.toString();
    }

    /**
     * Returns a random non-negative integer in range [0, max).
     *
     * @param max the upper bound (exclusive), must be &gt; 0
     * @return a random int from 0 (inclusive) to max (exclusive)
     */
    public static int randomInt(int max) {
        if (max <= 0) {
            throw new IllegalArgumentException("max must be greater than 0, was: " + max);
        }
        return RANDOM.nextInt(max);
    }
}
