package com.progressoft.induction.atm;

import com.progressoft.induction.atm.db.DbOperations;

import java.math.BigDecimal;

public class BankingSystemIMPL implements BankingSystem {
    private DbOperations operations;


    @Override
    public BigDecimal getAccountBalance(String accountNumber) {
        operations = new DbOperations();
        BigDecimal accountBalance = operations.accountBalance(accountNumber);
      return accountBalance;
    }

    @Override
    public void debitAccount(String accountNumber, BigDecimal amount) {
        BigDecimal accountBalance = getAccountBalance(accountNumber);
        double updatedBalance = accountBalance.doubleValue() - amount.doubleValue();
        operations.updateAccount(accountNumber, BigDecimal.valueOf(updatedBalance));
    }
}
