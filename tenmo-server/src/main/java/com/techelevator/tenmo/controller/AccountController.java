package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.model.Account;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;

/**
 * Controller to view account information.
 */

@RestController
@RequestMapping("/account")
@PreAuthorize("isAuthenticated()")
public class AccountController {

    private AccountDao accountDao;

    public AccountController(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @RequestMapping(path = "", method = RequestMethod.GET)
    public Account getAccount(Principal principal){
        return accountDao.getAccount(principal.getName());
    }

    @RequestMapping(path = "/balance", method = RequestMethod.GET)
    public BigDecimal getBalance(Principal principal){
        return accountDao.getBalance(principal.getName());
    }

    @RequestMapping(path = "/send/{userId}", method = RequestMethod.PUT)
    public boolean updateBalance(@Valid @RequestBody BigDecimal amount, @PathVariable int userId) {
        return accountDao.updateBalance(amount, userId);
    }

}
