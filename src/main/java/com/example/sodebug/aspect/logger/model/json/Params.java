package com.example.sodebug.aspect.logger.model.json;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * The {@code Params} class represents a key-value pair for request parameters.
 *
 * <p>It provides two properties:</p>
 * <ul>
 *     <li>{@link #key}: The key of the parameter</li>
 *     <li>{@link #value}: The value of the parameter as an array of strings</li>
 * </ul>
 *
 * @author ogbozoyan
 * @date 17.03.2023
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Params implements Serializable {
    private String key;
    private String[] value;
}
