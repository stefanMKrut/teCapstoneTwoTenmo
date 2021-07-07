package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/users")// check this path
@PreAuthorize("isAuthenticated()")
public class UserController {

    private UserDao userDao;

    public UserController(UserDao userDao) {
        this.userDao = userDao;
    }

    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<User> getUsers(){
        return userDao.findAllBasicUserInfo();
    }

    @RequestMapping(path = "/{accountId}")
    public String getUsernameByAccountId(@PathVariable int accountId) {
        return userDao.getUsernameByAccountId(accountId);
    }

}
