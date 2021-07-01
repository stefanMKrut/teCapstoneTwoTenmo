package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account/transfer")// check this path
public class TransferController {

    private TransferDao transferDao;

    public TransferController (TransferDao transferDao) {
        this.transferDao = transferDao;
    }

    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<Transfer> list() {
        return transferDao.list();
    }

    @RequestMapping(path = "", method = RequestMethod.POST)
    public Transfer create (@RequestBody Transfer transfer) {
        return transferDao.create(transfer);
    }

    @RequestMapping(path = "/{id}" , method = RequestMethod.GET )
    public Transfer get(@PathVariable int id) {
        return transferDao.getByTransferId(id);
    }
}
