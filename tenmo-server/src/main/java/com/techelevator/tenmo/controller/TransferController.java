package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/account/transfer")// check this path
@PreAuthorize("isAuthenticated()")
public class TransferController {

    private TransferDao transferDao;

    public TransferController (TransferDao transferDao) {
        this.transferDao = transferDao;
    }

    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<Transfer> list(Principal principal) {
        return transferDao.list(principal.getName());
    }

    @RequestMapping(path = "", method = RequestMethod.POST)
    public Transfer send(@RequestBody Transfer transfer) {
        return transferDao.send(transfer);
    }

    @RequestMapping(path = "/{id}" , method = RequestMethod.GET )
    public Transfer get(@PathVariable long id) {
        return transferDao.getByTransferId(id);
    }
}
