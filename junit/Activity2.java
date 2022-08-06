import activityPrograms.BankAccount;
import activityPrograms.NotEnoughFundsException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Activity2 {

    @Test
    void notEnoughFunds() {
        BankAccount account = new BankAccount(9);
        Assertions.assertThrows(NotEnoughFundsException.class, () -> account.withdraw(10),
                "Balance must be greater than withdrawal Amount");
    }

    @Test
    void enoughFunds() {
        BankAccount account = new BankAccount(100);
        assertDoesNotThrow(() -> account.withdraw(100));
    }
}