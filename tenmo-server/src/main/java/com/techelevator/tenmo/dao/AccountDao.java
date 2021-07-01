package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;

public interface AccountDao {

    Account getAccount(String username);

    BigDecimal getBalance(String username);

    boolean updateBalance(BigDecimal amount, int userId);

}
