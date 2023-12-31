package com.zero.triptalk.alert.repository;

import com.zero.triptalk.alert.entity.Alert;
import com.zero.triptalk.planner.entity.Planner;
import com.zero.triptalk.user.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlertRepository extends JpaRepository<Alert, Long> {


    Page<Alert> findByNicknameOrderByAlertDtDesc(String user, Pageable pageable);

    Long countByNicknameAndUserCheckYnFalse(String nickname);
}
