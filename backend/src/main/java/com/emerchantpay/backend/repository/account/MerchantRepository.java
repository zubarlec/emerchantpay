package com.emerchantpay.backend.repository.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.emerchantpay.backend.domain.account.Merchant;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Long> {
}
