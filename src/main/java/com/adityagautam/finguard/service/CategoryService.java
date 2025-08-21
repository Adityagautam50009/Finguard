package com.adityagautam.finguard.service;

import com.adityagautam.finguard.dto.CategoryDto;
import com.adityagautam.finguard.model.Category;
import com.adityagautam.finguard.model.Profile;
import com.adityagautam.finguard.repository.CategoryRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final ProfileService profileService;
    private final CategoryRepo categoryRepo;

    // save category
    public CategoryDto saveCategory(CategoryDto categoryDto){
        Profile profile = profileService.getCurrentProfile();
        if(categoryRepo.existsByNameAndProfileId(categoryDto.getName(),profile.getId())){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Category with this name already exists");
        }
        Category newCategory = toEntity(categoryDto,profile);
        newCategory=categoryRepo.save(newCategory);
        return toDto(newCategory);
    }

    private Category toEntity(CategoryDto categoryDto, Profile profile){
        return Category.builder()
                .name(categoryDto.getName())
                .icon(categoryDto.getIcon())
                .type(categoryDto.getType())
                .profile(profile)
                .build();
    }

    private CategoryDto toDto(Category entity){
        return CategoryDto.builder()
                .id(entity.getId())
                .profileId(entity.getProfile()!=null ? entity.getProfile().getId():null)
                .icon(entity.getIcon())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .type(entity.getType())
                .build();
    }
}
