package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import com.techelevator.tenmo.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {
    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Transfer create(Transfer transfer) {
        String sql = "INSERT INTO transfers (transfer_type_id, transfer_status_id, account_from, account_to, amount)" +
                " VALUES (?, ?, ?, ?, ?) RETURNING transfer_id;";
        int newId = jdbcTemplate.queryForObject(sql, int.class, transfer.getTransferTypeId(), transfer.getTransferStatusId(),
                transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());

        return getByTransferId(newId);

    }

    @Override
    public List<Transfer> list() {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT * FROM transfers;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while(results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            transfers.add(transfer);
        }
        return transfers;


    }

    @Override
    public Transfer getByTransferId(int id) {
        String sql = "SELECT * FROM users WHERE transfer_id = ?;";
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
