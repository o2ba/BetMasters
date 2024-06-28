package controller.transactions;

import com.google.gson.Gson;
import common.exception.NotAuthorizedException;
import common.exception.transactions.InvalidRecipientException;
import common.exception.transactions.NotEnoughBalanceException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import service.app.authRequestService.transactionService.TransactionService;

import java.util.Map;


@RestController
@Api(tags = "Transactions")
public class TransactionsController {

    TransactionService transactionService;

    @Autowired
    public TransactionsController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/transaction/deposit")
    @ApiOperation(value = "Deposit money into the account", tags = "Transactions")
    public ResponseEntity<String> deposit(
            @ApiParam(value = "JWT token", required = true) @RequestParam String jwtToken,
            @ApiParam(value = "Email of the user", required = true) @RequestParam String email,
            @ApiParam(value = "User ID", required = true) @RequestParam int userId,
            @ApiParam(value = "Amount to deposit", required = true) @RequestParam double amount) {

        try {
            transactionService.deposit(jwtToken, email, userId, amount);
        } catch (NotAuthorizedException e) {
            return ResponseEntity.status(401).body("Not authorized");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal server error");
        }

        return ResponseEntity.ok("Deposited " + amount + " into account");
    }

    @PostMapping("/transaction/withdraw")
    @ApiOperation(value = "Withdraw money from the account", tags = "Transactions")
    public ResponseEntity<String> withdraw(
            @ApiParam(value = "JWT token", required = true) @RequestParam String jwtToken,
            @ApiParam(value = "Email of the user", required = true) @RequestParam String email,
            @ApiParam(value = "User ID", required = true) @RequestParam int userId,
            @ApiParam(value = "Amount to withdraw", required = true) @RequestParam double amount) {
        try {
            transactionService.withdraw(jwtToken, email, userId, amount);
        } catch (NotAuthorizedException e) {
            return ResponseEntity.status(401).body("Not authorized");
        } catch (NotEnoughBalanceException e) {
            return ResponseEntity.status(400).body("Not enough balance");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal server error");
        }

        return ResponseEntity.ok("Withdrew " + amount + " from account");
    }

    @PostMapping("/transaction/transfer")
    @ApiOperation(value = "Transfer money from one account to another", tags = "Transactions")
    public ResponseEntity<String> transfer(
            @ApiParam(value = "JWT token", required = true) @RequestParam String jwtToken,
            @ApiParam(value = "Email of the user", required = true) @RequestParam String email,
            @ApiParam(value = "User ID", required = true) @RequestParam int userId,
            @ApiParam(value = "Receiver's ID", required = true) @RequestParam int receiverID,
            @ApiParam(value = "Amount to transfer", required = true) @RequestParam double amount) {

        try {
            transactionService.transfer(jwtToken, email, userId, receiverID, amount);
        } catch (NotAuthorizedException e) {
            return ResponseEntity.status(401).body("Not authorized");
        } catch (NotEnoughBalanceException e) {
            return ResponseEntity.status(400).body("Not enough balance");
        } catch (InvalidRecipientException e) {
            return ResponseEntity.status(400).body("Invalid recipient");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal server error");
        }


        return ResponseEntity.ok("Transferred " + amount + " to account " + receiverID);

    }


    @GetMapping("/transaction/get-balance")
    @ApiOperation(value = "Get the balance of the account", tags = "Transactions")
    public ResponseEntity<String> getBalance(String jwtToken, String email, int userId) {

        double balance;

        try {
            balance = transactionService.getBalance(jwtToken, email, userId);
        } catch (NotAuthorizedException e) {
            return ResponseEntity.status(401).body("Not authorized");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal server error");
        }

        // response { "balance": 100.0 }

        Map<String, Double> balanceMap = Map.of("balance", balance);

        // convert to gson string
        Gson gson = new Gson();
        return ResponseEntity.ok(gson.toJson(balanceMap));
    }

    @GetMapping("/transaction/history")
    @ApiOperation(value = "Get the transaction history of the account", tags = "Transactions")
    public ResponseEntity<String> transactionHistory(String jwtToken, String email, int userId) {
        JSONArray transactions;

        try {
            transactions = transactionService.getTransactions(jwtToken, email, userId);
        } catch (NotAuthorizedException e) {
            return ResponseEntity.status(401).body("Not authorized");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal server error");
        }

        // convert to gson string
        Gson gson = new Gson();
        return ResponseEntity.ok(gson.toJson(transactions));
    }
}