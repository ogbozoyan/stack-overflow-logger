package com.example.sodebug.web.controller;

import com.example.sodebug.aspect.ToLogger;
import com.example.sodebug.aspect.logger.model.json.ActionDomainEnum;
import com.example.sodebug.aspect.logger.model.json.ActionEnum;
import com.example.sodebug.aspect.logger.model.json.HttpMethodEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author ogbozoyan
 * @date 01.07.2023
 */
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/example")
@RequiredArgsConstructor
public class ExampleController {

    @ResponseStatus(HttpStatus.OK)
    @GetMapping()
    @ToLogger(action = ActionEnum.READ, actionDomain = ActionDomainEnum.CRUD, httpMethod = HttpMethodEnum.GET)
    public ResponseEntity<Object> getOne() {
        return ResponseEntity.ok("Hello World!");
    }
}
