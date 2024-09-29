package ps.com.viajeros.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ps.com.viajeros.dtos.common.ErrorApi;
import ps.com.viajeros.entities.ChoferEntity;
import ps.com.viajeros.services.ChoferService;

import java.time.LocalDateTime;
import java.util.List;
@RestController
@RequestMapping("/api/chofer")
public class ChoferController {

    @Autowired
    ChoferService choferService;

    @GetMapping
    public ResponseEntity<Object> getAllChofer() {
        try {
            List<ChoferEntity> brands = choferService.getAllChofer();
            return ResponseEntity.ok(brands);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorApi(LocalDateTime.now().toString(), HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error obtaining brands", ex.getMessage()));
        }
    }
}

