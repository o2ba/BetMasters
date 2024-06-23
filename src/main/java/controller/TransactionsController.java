package controller;

import common.exception.NotAuthorizedException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import service.app.authRequestService.transactionService.TransactionService;

@Controller
public class TransactionsController {

    TransactionService transactionService;

    @Autowired
    public TransactionsController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/deposit")
    @ApiOperation(value = "Deposit money into the account")
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

    @PostMapping("/withdraw")
    @ApiOperation(value = "Withdraw money from the account")
    public void withdraw(
            @ApiParam(value = "JWT token", required = true) @RequestParam String jwtToken,
            @ApiParam(value = "Email of the user", required = true) @RequestParam String email,
            @ApiParam(value = "User ID", required = true) @RequestParam int userId,
            @ApiParam(value = "Amount to withdraw", required = true) @RequestParam double amount) {

    }

    @PostMapping("/transfer")
    @ApiOperation(value = "Transfer money from one account to another")
    public void transfer(
            @ApiParam(value = "JWT token", required = true) @RequestParam String jwtToken,
            @ApiParam(value = "Email of the user", required = true) @RequestParam String email,
            @ApiParam(value = "User ID", required = true) @RequestParam int userId,
            @ApiParam(value = "Receiver's ID", required = true) @RequestParam int receiverID,
            @ApiParam(value = "Amount to transfer", required = true) @RequestParam double amount) {
    }

    /*
    @GetMapping("/getBalance")
    @ApiOperation(value = "Get the balance of the account")
    public double getBalance(String jwtToken, String email, int userId) {
        return transactionService.getBalance(jwtToken, email, userId);
    }

    @GetMapping("/transactionHistory")
    @ApiOperation(value = "Get the transaction history of the account")
    public void transactionHistory(String jwtToken, String email, int userId) {
        transactionService.transactionHistory(jwtToken, email, userId);
    }
    */
}
