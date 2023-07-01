package com.example.sodebug.web.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * A data transfer object representing the response of an abstract operation.
 * It contains the content, total number of elements, and total number of pages.
 *
 * @author ogbozoyan
 * @date 13.02.2023
 */
@Data
public class AbstractResponseDTO implements Serializable {
    private Object content;
    private Long totalElements;
    private Integer totalPages;

    /**
     * Constructs an AbstractResponseDTO object with the specified content, total number of elements,
     * and total number of pages.
     *
     * @param content       The content of the response.
     * @param totalElements The total number of elements in the response.
     * @param totalPages    The total number of pages in the response.
     */
    public AbstractResponseDTO(Object content, Long totalElements, Integer totalPages) {
        this.content = content;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
    }
}
