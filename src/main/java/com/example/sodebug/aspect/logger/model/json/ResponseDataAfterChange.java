package com.example.sodebug.aspect.logger.model.json;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * The {@code ResponseDataAfterChange} class represents the response data after a change operation.
 *
 * <p>It contains one property:</p>
 * <ul>
 *     <li>{@link #body}: The body of the response</li>
 * </ul>
 *
 * <p>This class is typically used for DELETE, SAVE, and UPDATE methods.</p>
 *
 * @author ogbozoyan
 * @date 17.03.2023
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDataAfterChange implements Serializable {
    private String body;
}
