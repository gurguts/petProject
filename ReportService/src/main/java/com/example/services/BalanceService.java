package com.example.services;

import com.example.dto.MovementMoneyDTO;
import com.example.models.TypeMovement;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * This service provides functionality to calculate the total balance based on a list of financial transactions,
 * represented by MovementMoneyDTO objects. The calculation considers both incomes and expenses to determine
 * the net balance.
 */
@Service
public class BalanceService {

    /**
     * This method computes the net balance by processing a list of MovementMoneyDTO objects, each representing
     * a financial transaction. It takes into account both incomes and expenses to calculate the overall balance.
     * <p>
     * Process:
     * - Checks if the provided list is null, returning 0.0 in such cases to handle null inputs safely.
     * - Iterates over the list, filtering out null entries.
     * - For each transaction, it converts the amount to a BigDecimal. It then adjusts the sign based on the
     * transaction type (positive for INCOME, negative for EXPENSE).
     * - Summarizes these values to obtain the total balance.
     *
     * @param moneyDTOList The list of MovementMoneyDTO objects for balance calculation.
     * @return The calculated total balance as a double value.
     */
    public double calculateBalance(List<MovementMoneyDTO> moneyDTOList) {
        if (moneyDTOList == null) {
            return 0.0;
        }
        BigDecimal balance = moneyDTOList.stream()
                .filter(Objects::nonNull)
                .map(dto -> BigDecimal.valueOf(dto.getAmount())
                        .multiply(dto.getType() == TypeMovement.INCOME ? BigDecimal.ONE : BigDecimal.valueOf(-1)))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return balance.doubleValue();
    }
}
