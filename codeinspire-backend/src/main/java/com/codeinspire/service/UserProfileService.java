package com.codeinspire.service;

import com.codeinspire.dto.ProfileInitRequest;
import com.codeinspire.entity.UserProfile;
import com.codeinspire.repository.UserProfileRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final ObjectMapper objectMapper;

    public UserProfile initProfile(Long userId, ProfileInitRequest request) {
        UserProfile existingProfile = userProfileRepository.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<UserProfile>()
                        .eq(UserProfile::getUserId, userId)
        );

        if (existingProfile != null) {
            throw new RuntimeException("用户画像已初始化");
        }

        UserProfile profile = new UserProfile();
        profile.setUserId(userId);
        profile.setSchoolLevel(request.getSchoolLevel());
        profile.setEducationLevel(request.getEducationLevel());
        profile.setMajor(request.getMajor());
        profile.setGrade(request.getGrade());
        profile.setMajorDirection(request.getMajorDirection());
        profile.setTargetPosition(request.getTargetPosition());
        profile.setTargetCityLevel(request.getTargetCityLevel());
        profile.setUrgencyLevel(request.getUrgencyLevel());
        profile.setWeeklyAvailableHours(request.getWeeklyAvailableHours());

        if (request.getSkills() != null && !request.getSkills().isEmpty()) {
            try {
                profile.setSkills(objectMapper.writeValueAsString(request.getSkills()));
            } catch (JsonProcessingException e) {
                profile.setSkills("[]");
            }
        }

        userProfileRepository.insert(profile);
        return profile;
    }

    public UserProfile getProfileByUserId(Long userId) {
        return userProfileRepository.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<UserProfile>()
                        .eq(UserProfile::getUserId, userId)
        );
    }

    public UserProfile updateProfile(Long userId, ProfileInitRequest request) {
        UserProfile profile = getProfileByUserId(userId);
        if (profile == null) {
            throw new RuntimeException("用户画像不存在，请先初始化");
        }

        if (request.getSchoolLevel() != null) profile.setSchoolLevel(request.getSchoolLevel());
        if (request.getEducationLevel() != null) profile.setEducationLevel(request.getEducationLevel());
        if (request.getMajor() != null) profile.setMajor(request.getMajor());
        if (request.getGrade() != null) profile.setGrade(request.getGrade());
        if (request.getMajorDirection() != null) profile.setMajorDirection(request.getMajorDirection());
        if (request.getTargetPosition() != null) profile.setTargetPosition(request.getTargetPosition());
        if (request.getTargetCityLevel() != null) profile.setTargetCityLevel(request.getTargetCityLevel());
        if (request.getUrgencyLevel() != null) profile.setUrgencyLevel(request.getUrgencyLevel());
        if (request.getWeeklyAvailableHours() != null) profile.setWeeklyAvailableHours(request.getWeeklyAvailableHours());

        if (request.getSkills() != null) {
            try {
                profile.setSkills(objectMapper.writeValueAsString(request.getSkills()));
            } catch (JsonProcessingException e) {
                // ignore
            }
        }

        userProfileRepository.updateById(profile);
        return profile;
    }
}
