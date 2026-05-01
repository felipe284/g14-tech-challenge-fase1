package com.fiap_g14.foodlink.api.mapper;

import com.fiap_g14.foodlink.api.domain.UserEntity;
import com.fiap_g14.foodlink.api.dto.PageResponseDTO;
import com.fiap_g14.foodlink.api.dto.UserResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PageResponseMapper {

    public PageResponseDTO toUserPageResponse(Page<UserEntity> page) {
        List<UserResponseDTO> content = page.getContent().stream()
                .map(UserMapper::toDTO)
                .toList();

        return PageResponseDTO.builder()
                .content(content)
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }
}
