package controller.user.transaction;

import common.exception.UnhandledErrorException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import service.app.user.activity.transact.TransactionService;
import service.app.user.activity.transact.TransactionType;
import service.app.user.activity.transact.exception.InvalidTransactionException;
import service.app.user.activity.transact.exception.InvalidUserException;

@RestController
@Api(tags = "Transactions")
public class TransactionController {

    TransactionService transactionService;


    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/user/deposit")
    public ResponseEntity<String> deposit(
            @ApiParam(value = "Amount to deposit", required = true) @RequestParam double amount,
            @ApiParam(value = "User ID", required = true) @RequestParam int userId,
            @ApiParam(value = "JWT token" , required = true) @RequestParam String jwtToken,
            @ApiParam(value = "email", required = true) @RequestParam String email
    ) throws UnhandledErrorException, InvalidTransactionException, InvalidUserException {

        try {
            int t_id = transactionService.addMoney(userId, amount, TransactionType.DEPOSIT);
        } catch (InvalidUserException e) {
            throw e;
        }


        return ResponseEntity.ok("Deposit successful");
    }
}
