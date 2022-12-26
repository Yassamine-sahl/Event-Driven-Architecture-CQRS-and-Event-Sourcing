package ma.enset.comptecqrsess.query.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.comptecqrsess.commonapi.enums.OperationType;
import ma.enset.comptecqrsess.commonapi.events.AccountActivatedEvent;
import ma.enset.comptecqrsess.commonapi.events.AccountCreatedEvent;
import ma.enset.comptecqrsess.commonapi.events.AccountCreditedEvent;
import ma.enset.comptecqrsess.commonapi.events.AccountDebitedEvent;
import ma.enset.comptecqrsess.commonapi.queries.GetAccountByIdQuery;
import ma.enset.comptecqrsess.commonapi.queries.GetAllAccountsQuery;
import ma.enset.comptecqrsess.query.entities.Account;
import ma.enset.comptecqrsess.query.entities.Operation;
import ma.enset.comptecqrsess.query.repositories.AccountRepository;
import ma.enset.comptecqrsess.query.repositories.OperationRepository;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

//Ecouter les events
@Service
@AllArgsConstructor
@Slf4j //Permet d'injecter dans votre classe un attribut appel Log
@Transactional
public class AccountServiceHandler {

    private AccountRepository accountRepository;
    private OperationRepository operationRepository;

    @EventHandler
    public void on(AccountCreatedEvent event){
        log.info("*********************");
        log.info("AccountCreatedEvent received");
        Account account = new Account();
        account.setId(event.getId());
        account.setBalance(event.getInitialBalance());
        account.setStatus(event.getStatus());
        account.setCurrency(event.getCurrency());
        accountRepository.save(account);
    }

    @EventHandler
    public void on(AccountActivatedEvent event){
        log.info("*********************");
        log.info("AccountActivatedEvent received");
        Account account= accountRepository.findById(event.getId()).get();
        account.setStatus(event.getStatus());
        accountRepository.save(account);
    }

    @EventHandler
    public void on(AccountDebitedEvent event){
        log.info("*********************");
        log.info("AccountDebitedEvent received");
        Account account= accountRepository.findById(event.getId()).get();
        Operation operation = new Operation();
        operation.setAmount(event.getAmount());
        operation.setDate(new Date()); // a ne pas faire
        operation.setType(OperationType.DEBIT);
        operation.setAccount(account);
        operationRepository.save(operation);
        account.setBalance(account.getBalance()-event.getAmount());
        accountRepository.save(account);
    }

    @EventHandler
    public void on(AccountCreditedEvent event){
        log.info("*********************");
        log.info("AccountCreditedEvent received");
        Account account= accountRepository.findById(event.getId()).get();
        Operation operation = new Operation();
        operation.setAmount(event.getAmount());
        operation.setDate(new Date()); // a ne pas faire
        operation.setType(OperationType.CREDIT);
        operation.setAccount(account);
        operationRepository.save(operation);
        account.setBalance(account.getBalance()+ event.getAmount());
        accountRepository.save(account);
    }

    //Recuperer la liste des comptes
    @QueryHandler
    public List<Account> on(GetAllAccountsQuery query){
        return accountRepository.findAll();
    }

    //Recuperer un compte
    @QueryHandler
    public Account on(GetAccountByIdQuery query){
        return accountRepository.findById(query.getId()).get();
    }
}
