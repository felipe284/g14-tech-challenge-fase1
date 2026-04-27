package com.fiap_g14.foodlink.api.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PageResponseDTO {
    private List<UserResponseDTO> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
}

