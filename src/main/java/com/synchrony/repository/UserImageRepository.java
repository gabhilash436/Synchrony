package com.synchrony.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.synchrony.entity.UserImage;

@Repository
public interface UserImageRepository extends JpaRepository<UserImage,Long> {

	public UserImage findByUserId(Long userId);
}
