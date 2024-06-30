package controller.user.transaction;

import com.google.gson.GsonBuilder;
import common.exception.UnhandledErrorException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import service.app.user.activity.transact.TransactionService;
import service.app.user.activity.transact.TransactionType;
import service.app.user.activity.transact.exception.InvalidTransactionException;
import service.app.user.activity.transact.exception.InvalidUserException;
import service.app.user.activity.transact.exception.NotEnoughBalanceException;

import java.util.HashMap;
import java.util.Map;

@RestController
@Api(tags = "Transactions")
public class TransactionController {

    TransactionService transactionService;

    Logger logger = LoggerFactory.getLogger(TransactionController.class);


    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/transaction/deposit")
    public ResponseEntity<String> deposit(
            @ApiParam(value = "Amount to deposit", required = true) @RequestParam double amount,
            @ApiParam(value = "User ID", required = true) @RequestParam int userId,
            @ApiParam(value = "JWT token" , required = true) @RequestParam String jwtToken,
            @ApiParam(value = "email", required = true) @RequestParam String email
    ) throws UnhandledErrorException, InvalidTransactionException, InvalidUserException {


        int t_id = transactionService.addMoney(userId, amount, TransactionType.DEPOSIT);

        Map<String, Object> response = new HashMap<>();
        response.put("transaction_id", t_id);
        response.put("message", "Deposit successful");
        response.put("deposit_amount", amount);

        try {
            response.put("balance", transactionService.getBalance(userId));
        } catch (Exception e) {
            logger.error("Error retrieving balance", e);
            response.put("balance", "Error retrieving balance");
        }

        GsonBuilder gsonBuilder = new GsonBuilder();
        String json = gsonBuilder.create().toJson(response);


        return ResponseEntity.ok(json);
    }


    @PostMapping("/transaction/withdraw")
    public ResponseEntity<String> withdraw(
            @ApiParam(value = "Amount to withdraw", required = true) @RequestParam double amount,
            @ApiParam(value = "User ID", required = true) @RequestParam int userId,
            @ApiParam(value = "JWT token" , required = true) @RequestParam String jwtToken,
            @ApiParam(value = "email", required = true) @RequestParam String email
    ) throws UnhandledErrorException, InvalidTransactionException, InvalidUserException, NotEnoughBalanceException {

        int t_id = transactionService.withdrawMoney(userId, amount, TransactionType.WITHDRAWAL);

        Map<String, Object> response = new HashMap<>();
        response.put("transaction_id", t_id);
        response.put("message", "Withdrawal successful");
        response.put("withdrawal_amount", amount);

        try {
            response.put("balance", transactionService.getBalance(userId));
        } catch (Exception e) {
            logger.error("Error retrieving balance", e);
            response.put("balance", "Error retrieving balance");
        }

        GsonBuilder gsonBuilder = new GsonBuilder();
        String json = gsonBuilder.create().toJson(response);

        return ResponseEntity.ok(json);
    }

    @GetMapping("/transaction/get-balance")
    public ResponseEntity<String> getBalance(
            @ApiParam(value = "User ID", required = true) @RequestParam int userId,
            @ApiParam(value = "JWT token" , required = true) @RequestParam String jwtToken,
            @ApiParam(value = "email", required = true) @RequestParam String email
    ) throws UnhandledErrorException, InvalidUserException {

        Map<String, Object> response = new HashMap<>();

        response.put("balance", transactionService.getBalance(userId));

        GsonBuilder gsonBuilder = new GsonBuilder();
        String json = gsonBuilder.create().toJson(response);

        return ResponseEntity.ok(json);
    }

    @GetMapping("/transaction/history")
    public ResponseEntity<String> getHistory(
            @ApiParam(value = "User ID", required = true) @RequestParam int userId,
            @ApiParam(value = "JWT token" , required = true) @RequestParam String jwtToken,
            @ApiParam(value = "email", required = true) @RequestParam String email
    ) throws UnhandledErrorException {

        GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();
        String json = gsonBuilder.create().toJson(transactionService.getTransactions(userId));

        return ResponseEntity.ok(json);
    }

}
