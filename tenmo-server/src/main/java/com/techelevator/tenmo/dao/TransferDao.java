package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {

    Transfer send(Transfer transfer);

    List<Transfer> list(String username);

    Transfer getByTransferId(long id);




}
