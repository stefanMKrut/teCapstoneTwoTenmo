package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import com.techelevator.tenmo.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {
    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Transfer send(Transfer transfer) {
        String sql = "BEGIN TRANSACTION; " +
                "UPDATE accounts SET balance = balance + ? " +
                "WHERE account_id = ?; " +
                "UPDATE accounts SET balance = balance + ? " +
                "WHERE account_id = ?; " +
                "INSERT INTO transfers (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "VALUES (?, ?, ?, ?, ?); " +
                "COMMIT;";
        String sql2 = "SELECT currval('seq_transfer_id');";
        jdbcTemplate.update(sql, transfer.getAmount(),
                transfer.getAccountTo(), transfer.getAmount() * -1, transfer.getAccountFrom(), transfer.getTransferTypeId(), transfer.getTransferStatusId(),
                transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());
        // It is possible that this will return the wrong transaction_id, if the transaction fails then it will return the
        // last id that was successfully created. Additionally, if two users were to create a transaction at the exact same
        // time, then one user may actually be sent the transfer_id of the other user's transfer.
        // Walt said it was good enough for the project though :)
        Long newId = jdbcTemplate.queryForObject(sql2, Long.class);
        if (newId == null) {
            return null;
        } else {
            return getByTransferId(newId);
        }
    }

    @Override
    public List<Transfer> list(String username) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount FROM transfers " +
                "WHERE account_from = (SELECT account_id FROM accounts WHERE user_id = (SELECT user_id FROM users WHERE username = ?)) " +
                "OR account_to = (SELECT account_id FROM accounts WHERE user_id = (SELECT user_id FROM users WHERE username = ?));";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, username, username);
        while(results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            transfers.add(transfer);
        }
        return transfers;
    }

    @Override
    public Transfer getByTransferId(long id) {
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount " +
                "FROM transfers WHERE transfer_id = ?;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, id);
        if (rowSet.next()){
            return mapRowToTransfer(rowSet);
        }
        return null; //throw exception here??
    }


    private Transfer mapRowToTransfer(SqlRowSet rs) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(rs.getInt("transfer_id"));
        transfer.setTransferTypeId(rs.getInt("transfer_type_id"));
        transfer.setTransferStatusId(rs.getInt("transfer_status_id"));
        transfer.setAccountFrom(rs.getInt("account_from"));
        transfer.setAccountTo(rs.getInt("account_to"));
        transfer.setAmount(rs.getDouble("amount"));
        return transfer;
}

}
