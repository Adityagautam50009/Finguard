package com.adityagautam.finguard.service;

import com.adityagautam.finguard.dto.ProfileDto;
import com.adityagautam.finguard.model.Profile;
import com.adityagautam.finguard.repository.ProfileRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepo profileRepo;
    private final EmailService emailService;


    public ProfileDto registerProfile(ProfileDto profileDto){
        Profile newProfile = toEntity(profileDto);
        newProfile.setActivationToken(UUID.randomUUID().toString());
        newProfile=profileRepo.save(newProfile);
        //send activation email
        String activationLink = "http://localhost:8080/api/v1.0/activate?token="+newProfile.getActivationToken();
        String subject = "Activate your Finguard Account";
        String body = "Click on the following link to activate you account: "+activationLink;
        emailService.sendEmail(newProfile.getEmail(), subject, body);
        return toDto(newProfile);
    }


    public Profile toEntity(ProfileDto profileDto){
        return Profile.builder()
                .id(profileDto.getId())
                .fullName(profileDto.getFullName())
                .email(profileDto.getEmail())
                .password(profileDto.getPassword())
                .profileImageUrl(profileDto.getProfileImageUrl())
                .createdAt(profileDto.getCreateAt())
                .updatedAt(profileDto.getUpdatedAt())
                .build();
    }


    public ProfileDto toDto(Profile profile){
        return ProfileDto.builder()
                .id(profile.getId())
                .fullName(profile.getFullName())
                .email(profile.getEmail())
                .profileImageUrl(profile.getProfileImageUrl())
                .createAt(profile.getCreatedAt())
                .updatedAt(profile.getUpdatedAt())
                .build();
    }

    public boolean activateProfile(String activationToken){
        return profileRepo.findByActivationToken(activationToken)
                .map(profile -> {
                    profile.setIsActive(true);
                    profileRepo.save(profile);
                    return true;
                }).orElse(false);
    }
}
