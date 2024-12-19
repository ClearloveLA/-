package com.campuscard.repository;

import com.campuscard.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    // 可以添加自定义查询方法
} 