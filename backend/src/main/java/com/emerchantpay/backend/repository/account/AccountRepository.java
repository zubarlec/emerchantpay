package com.emerchantpay.backend.repository.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.emerchantpay.backend.domain.account.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
	Account findByEmailAllIgnoreCase(String email);
}
