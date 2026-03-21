package ru.libcrm.view.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.libcrm.model.Transaction;
import ru.libcrm.service.TransactionService;
import ru.libcrm.util.InputReader;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ReturnBooksCommand implements Command {

    private final TransactionService transactionService;
    private final InputReader input;

    @Override
    public void execute() {
        System.out.println("\n------------------------------------");
        System.out.println("           Возврат книги              ");
        System.out.println("---------------------------------------");

        try {
            UUID loanId = input.readUuid("Введите ID выдачи для возврата: ");

            Transaction loan = transactionService.returnBook(loanId);

            System.out.println("\n Книга успешно возвращена!");
            System.out.println("   Номер выдачи: " + loan.getId());
            System.out.println("   Дата возврата: " + loan.getReturnDate().toLocalDate());
            System.out.println("   Статус: " + loan.getStatus());

        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка ввода: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    @Override
    public String getName() {
        return "Вернуть книгу";
    }

    @Override
    public String getDescription() {
        return "Вернуть книгу от читателя";
    }

    @Override
    public String getCategory() {
        return "Операции";
    }
}
