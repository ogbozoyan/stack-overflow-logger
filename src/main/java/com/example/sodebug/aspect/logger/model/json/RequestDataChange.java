package com.example.sodebug.aspect.logger.model.json;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
/**
 * The {@code RequestDataChange} class represents the data change in a request.
 *
 * <p>It contains two properties:</p>
 * <ul>
 *     <li>{@link #params}: The list of request parameters</li>
 *     <li>{@link #body}: The body of the request</li>
 * </ul>
 *
 * @author ogbozoyan
 * @date 17.03.2023
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestDataChange implements Serializable {
    private List<Params> params;
    private String body;
}