package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {

    Transfer create(Transfer transfer);

    List<Transfer> list();

    Transfer getByTransferId(int id);




}
