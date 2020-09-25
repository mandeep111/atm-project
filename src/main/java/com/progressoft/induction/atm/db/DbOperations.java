package com.progressoft.induction.atm.db;

import com.progressoft.induction.atm.Banknote;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DbOperations {
    Connection connection = null;
    Statement statement = null;
    ResultSet resultSet = null;

    public boolean checkAccountNumber(String accountNumber) {
        connection = DbConnection.get_connection();
        try {
            String sql = "SELECT * FROM user_account WHERE account_number = "+ accountNumber;
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean doesAccountHasSufficientBalance(String accountNumber, BigDecimal amount) {
        connection = DbConnection.get_connection();
        try {
            String sql = "SELECT * FROM user_account WHERE account_number = "+ accountNumber;
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                BigDecimal balance = resultSet.getBigDecimal("account_balance");
//                System.out.println(balance);
                if (balance.doubleValue() >= amount.doubleValue()) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    public void updateAccount(String accountNumber, BigDecimal amount) {
        connection = DbConnection.get_connection();
        try {
            String sql = "UPDATE `atm_db`.`user_account` SET `account_balance` = "+amount+" WHERE account_number = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, accountNumber);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public BigDecimal accountBalance(String accountNumber) {
        connection = DbConnection.get_connection();
        BigDecimal balance = BigDecimal.valueOf(0);
        try {
            String sql = "SELECT * FROM user_account WHERE account_number = "+ accountNumber;
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                balance = resultSet.getBigDecimal("account_balance");
                return balance;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return balance;
    }



    public boolean atmBalance(BigDecimal amount) {
        ArrayList count = (ArrayList) notesCount();
        int fifty = (int) count.get(0);
        int twenty = (int) count.get(1);
        int ten = (int) count.get(2);
        int five = (int) count.get(3);
        int total_cash = fifty * 50 + twenty * 20 + ten * 10 + five * 5;
        return total_cash >= amount.doubleValue();
    }

    public void updateAtmBalance(BigDecimal number, String bank_note) {
        connection = DbConnection.get_connection();
        try {
            String sql = "UPDATE `atm_db`.`atm` SET `number` = "+number+" WHERE bank_note = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, bank_note);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Integer> notesCount() {
        connection = DbConnection.get_connection();
        List<Integer> count = new ArrayList<>();
        try {
            String sql = "SELECT number FROM `atm_db`.`atm`";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            while(resultSet.next()) {
                count.add(resultSet.getBigDecimal("number").intValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

//    public boolean AtmBalance(BigDecimal amount) {
//        connection = DbConnection.get_connection();
//        double atmTotalBalance = 0.0;
//        try {
//            String sql = "SELECT * FROM atm ";
//            statement = connection.createStatement();
//            ResultSet resultSet = statement.executeQuery(sql);
//            while (resultSet.next()) {
//                switch (resultSet.getString("bank_note")) {
//                    case "Fifty":
//                        BigDecimal count = resultSet.getBigDecimal("number");
//                        atmTotalBalance += Banknote.FIFTY_JOD.getValue().doubleValue() * count.doubleValue();
//                        break;
//                    case "Twenty":
//                        BigDecimal count1 = resultSet.getBigDecimal("number");
//                        atmTotalBalance += Banknote.TWENTY_JOD.getValue().doubleValue() * count1.doubleValue();
//                        break;
//                    case "Ten":
//                        BigDecimal count2 = resultSet.getBigDecimal("number");
//                        atmTotalBalance += Banknote.TEN_JOD.getValue().doubleValue() * count2.doubleValue();
//                        break;
//                    case "Five":
//                        BigDecimal count3 = resultSet.getBigDecimal("number");
//                        atmTotalBalance += Banknote.FIVE_JOD.getValue().doubleValue() * count3.doubleValue();
//                        break;
//                    default:
//                        break;
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return atmTotalBalance >= amount.doubleValue();
//    }

}
