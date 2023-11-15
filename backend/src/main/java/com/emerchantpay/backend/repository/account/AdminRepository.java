package com.emerchantpay.backend.repository.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.emerchantpay.backend.domain.account.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
}
