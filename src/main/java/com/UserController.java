/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com;

import com.model.User;
import com.dao.UserDao;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * A class to test interactions with the MySQL database using the UserDao class.
 *
 * @author netgloo
 */
@Controller
public class UserController {

    // ------------------------
    // PUBLIC METHODS
    // ------------------------
    /**
     * /create --> Create a new user and save it in the database.
     *
     * @param email User's email
     * @param name User's name
     * @return A string describing if the user is succesfully created or not.
     */
    @RequestMapping("/create")
    @ResponseBody
    public String create(String email, String name, String password) {
        User user = null;
        try {
            user = userDao.findByEmail(email);
            String userId = String.valueOf(user.getId());
            return "User Already exists <a href=\"/signup\">Register a new account</a>";
        } catch (Exception ex) {
            try {
                user = new User(email, name, password);
                userDao.save(user);
            } catch (Exception x) {
                return "Error creating the user: " + x.toString();
            }
        }

        return "User succesfully created! (id = " + user.getId() + ")<a href=\"/login\">Login</a>";
    }
    @GetMapping("/signup")
    public String signUp(Model model) throws IOException{
        return "signup";
    }
    @GetMapping("/login")
    public String signin(Model model) throws IOException{
        return "login";
    }
    /**
     * /delete --> Delete the user having the passed id.
     *
     * @param id The id of the user to delete
     * @return A string describing if the user is succesfully deleted or not.
     */
    @RequestMapping("/delete")
    @ResponseBody
    public String delete(long id) {
        try {
            User user = new User(id);
            userDao.delete(user);
        } catch (Exception ex) {
            return "Error deleting the user: " + ex.toString();
        }
        return "User succesfully deleted!";
    }

    /**
     * /get-by-email --> Return the id for the user having the passed email.
     *
     * @param email The email to search in the database.
     * @return The user id or a message error if the user is not found.
     */
    @RequestMapping("/get-by-email")
    @ResponseBody
    public String getByEmail(String email) {
        String userId;
        try {
            User user = userDao.findByEmail(email);
            userId = String.valueOf(user.getId());
        } catch (Exception ex) {
            return "User not found";
        }
        return "The user id is: " + userId;
    }

    /**
     * /update --> Update the email and the name for the user in the database
     * having the passed id.
     *
     * @param id The id for the user to update.
     * @param email The new email.
     * @param name The new name.
     * @return A string describing if the user is succesfully updated or not.
     */
    @RequestMapping("/update")
    @ResponseBody
    public String updateUser(long id, String email, String name, String password) {
        try {
            User user = userDao.findOne(id);
            user.setEmail(email);
            user.setName(name);
            user.setPassword(password);
            userDao.save(user);
        } catch (Exception ex) {
            return "Error updating the user: " + ex.toString();
        }
        return "User succesfully updated!";
    }
    
    

    // ------------------------
    // PRIVATE FIELDS
    // ------------------------
    @Autowired
    private UserDao userDao;

} // class UserController
