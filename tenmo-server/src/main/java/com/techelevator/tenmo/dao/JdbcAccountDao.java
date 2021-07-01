package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class JdbcAccountDao implements AccountDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean updateBalance(BigDecimal amount, int userId) {
        String sql = "UPDATE accounts a SET balance = balance + ? " +
                "WHERE a.user_id = ?;";
        return jdbcTemplate.update(sql, amount, userId) == 1;
    }

    @Override
    public Account getAccount(String username) throws UsernameNotFoundException {
        Account account = null;
        String sql = "SELECT account_id, user_id, balance FROM accounts a " +
                "JOIN users u USING(user_id) " +
                "WHERE a.user_id = (SELECT user_id FROM users WHERE username = ?);";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, username);
        if (result.next()) {
            account = mapRowToAccount(result);
        } else {
            throw new UsernameNotFoundException("User " + username + " not found.");
        }
        return account;
    }

    @Override
    public Account getAccountFromUserId(int userId) throws UsernameNotFoundException {
        Account account = null;
        String sql = "SELECT account_id, user_id, balance FROM accounts WHERE user_id = ?;";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, userId);
        if (result.next()) {
            account = mapRowToAccount(result);
        } else {
            throw new UsernameNotFoundException("User " + userId + " not found.");
        }
        return account;
    }

    @Override
    public BigDecimal getBalance(String username) throws UsernameNotFoundException {
        Account account = getAccount(username);
        BigDecimal balance = null;
        if (account != null) {
            balance = account.getBalance();
        } else {
            throw new UsernameNotFoundException("User " + username + " not found.");
        }
        return balance;
    }

    private Account mapRowToAccount(SqlRowSet rs) {
        Account account = new Account();
        account.setUserId(rs.getLong("user_id"));
        account.setAccountId(rs.getLong("account_id"));
        account.setBalance(rs.getBigDecimal("balance"));
        return account;
    }


}
