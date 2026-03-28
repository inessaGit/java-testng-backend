package com.qa.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * POJO representing a post resource from the JSONPlaceholder API.
 *
 * <p>Example JSON:
 * <pre>
 * {
 *   "userId": 1,
 *   "id": 1,
 *   "title": "sunt aut facere repellat provident occaecati",
 *   "body": "quia et suscipit..."
 * }
 * </pre>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Post {

    /** The ID of the user who authored this post. */
    private Integer userId;

    /** The unique identifier for this post (assigned by the server). */
    private Integer id;

    /** The title of the post. */
    private String title;

    /** The body/content of the post. */
    private String body;
}
