package com.jpmc.midascore.controller;


import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Balance;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/balance")
public class ApiController {

  @Autowired
  private UserRepository userRepository;

  @GetMapping
  private Balance getBalance(@RequestParam long userId) {
    UserRecord user = userRepository.findById(userId).orElse(null);

    if(user == null){
      return new Balance(0);
    }else{
      return new Balance(user.getBalance());
    }
  }
}
