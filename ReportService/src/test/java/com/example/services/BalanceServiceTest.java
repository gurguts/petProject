package com.example.services;

import com.example.dto.MovementMoneyDTO;
import com.example.models.TypeMovement;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BalanceServiceTest {
    private final BalanceService balanceService = new BalanceService();

    @Test
    public void calculateBalanceNull() {
        assertEquals(0.0, balanceService.calculateBalance(null));
    }

    @Test
    public void calculateBalanceEmpty() {
        assertEquals(0.0, balanceService.calculateBalance(Collections.emptyList()));
    }

    @Test
    public void calculateBalanceOnlyIncome() {
        MovementMoneyDTO income1 = new MovementMoneyDTO();
        income1.setAmount(100.0);
        income1.setType(TypeMovement.INCOME);
        MovementMoneyDTO income2 = new MovementMoneyDTO();
        income2.setAmount(200.0);
        income2.setType(TypeMovement.INCOME);
        List<MovementMoneyDTO> moneyDTOList = Arrays.asList(income1, income2);

        assertEquals(300.0, balanceService.calculateBalance(moneyDTOList));
    }

    @Test
    public void calculateBalanceOnlyOutcome() {
        MovementMoneyDTO outcome1 = new MovementMoneyDTO();
        outcome1.setAmount(50.0);
        outcome1.setType(TypeMovement.EXPENSE);
        MovementMoneyDTO outcome2 = new MovementMoneyDTO();
        outcome2.setAmount(100.0);
        outcome2.setType(TypeMovement.EXPENSE);
        List<MovementMoneyDTO> moneyDTOList = Arrays.asList(outcome1, outcome2);

        assertEquals(-150.0, balanceService.calculateBalance(moneyDTOList));
    }

    @Test
    public void calculateBalanceMixedTypes() {
        MovementMoneyDTO income = new MovementMoneyDTO();
        income.setAmount(200.0);
        income.setType(TypeMovement.INCOME);
        MovementMoneyDTO outcome = new MovementMoneyDTO();
        outcome.setAmount(50.0);
        outcome.setType(TypeMovement.EXPENSE);
        List<MovementMoneyDTO> moneyDTOList = Arrays.asList(income, outcome);

        assertEquals(150.0, balanceService.calculateBalance(moneyDTOList));
    }

    @Test
    public void calculateBalanceDoubleNumbers() {
        MovementMoneyDTO income = new MovementMoneyDTO();
        income.setAmount(255.55);
        income.setType(TypeMovement.INCOME);
        MovementMoneyDTO outcome = new MovementMoneyDTO();
        outcome.setAmount(55.55);
        outcome.setType(TypeMovement.EXPENSE);
        List<MovementMoneyDTO> moneyDTOList = Arrays.asList(income, outcome);

        assertEquals(200.0, balanceService.calculateBalance(moneyDTOList));
    }
}
