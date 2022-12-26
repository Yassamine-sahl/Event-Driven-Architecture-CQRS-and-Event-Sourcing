package ma.enset.comptecqrsess.query.repositories;

import ma.enset.comptecqrsess.query.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, String> {
}
