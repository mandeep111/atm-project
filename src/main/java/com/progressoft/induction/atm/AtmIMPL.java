package com.progressoft.induction.atm;

import com.progressoft.induction.atm.db.DbOperations;
import com.progressoft.induction.atm.exceptions.AccountNotFoundException;
import com.progressoft.induction.atm.exceptions.InsufficientFundsException;
import com.progressoft.induction.atm.exceptions.NotEnoughMoneyInATMException;

import java.math.BigDecimal;
import java.util.*;

public class AtmIMPL implements ATM{

    private DbOperations operations;

    @Override
    public List<Banknote> withdraw(String accountNumber, BigDecimal amount) {
        BankingSystemIMPL bankingSystemIMPL = new BankingSystemIMPL();
        operations = new DbOperations();
        List<Banknote> notes;
        if(operations.checkAccountNumber(accountNumber)) {
            if(operations.doesAccountHasSufficientBalance(accountNumber, amount)) {
               if(operations.atmBalance(amount)) {
                   bankingSystemIMPL.debitAccount(accountNumber, amount);
                   notes = calculateAmount(amount);
                   return notes;
               } else throw new NotEnoughMoneyInATMException();
            } else throw new InsufficientFundsException();
        } else throw new AccountNotFoundException();
    }

    List<Banknote> calculateAmount(BigDecimal amount) {
        ArrayList count = (ArrayList) operations.notesCount();
        int fifty = (int) count.get(0);
        int twenty = (int) count.get(1);
        int ten = (int) count.get(2);
        int five = (int) count.get(3);

        int[] note_count = {0,0,0,0};
        int total_cash = fifty * 50 + twenty * 20 + ten * 10 + five * 5;

        List<Banknote> notes = new ArrayList<>();
        if (total_cash >=0) {
            int amount_to_withdraw = amount.intValue();

            if (amount_to_withdraw % 5 ==0) {
                if (amount_to_withdraw < total_cash) {
                    while (amount_to_withdraw != 0) {
                        if(amount_to_withdraw >= 50 && fifty !=0) {
                            notes.add(Banknote.FIFTY_JOD);
                            fifty -= 1;
                            note_count[0]++;
                            amount_to_withdraw -= 50;
                        }
                        if(amount_to_withdraw >= 20 && twenty !=0) {
                            notes.add(Banknote.TWENTY_JOD);
                            twenty -= 1;
                            note_count[1]++;
                            amount_to_withdraw -= 20;
                        }
                        if(amount_to_withdraw >= 10 && ten !=0) {
                            notes.add(Banknote.TEN_JOD);
                            ten -= 1;
                            note_count[2]++;
                            amount_to_withdraw -= 10;
                        }
                        if(amount_to_withdraw >= 5 && five !=0) {
                            notes.add(Banknote.FIVE_JOD);
                            five -= 1;
                            note_count[3]++;
                            amount_to_withdraw -= 5;
                        }
                    }
                } else{
                    System.out.println("Not enough money");
                }


                System.out.println("Fifty = "+ note_count[0]+ " Twenty = "+ note_count[1]+ " Ten = "+ note_count[2]+ " Five = "+ note_count[3]);
//                System.out.println("provided cash "+ notes);
            } else {
                System.out.println("Amount should be divisible by 5");
            }
            operations.updateAtmBalance(BigDecimal.valueOf(fifty),"Fifty");
            operations.updateAtmBalance(BigDecimal.valueOf(twenty),"Twenty");
            operations.updateAtmBalance(BigDecimal.valueOf(ten),"Ten");
            operations.updateAtmBalance(BigDecimal.valueOf(five),"Five");
        }
        return notes;
    }

}
