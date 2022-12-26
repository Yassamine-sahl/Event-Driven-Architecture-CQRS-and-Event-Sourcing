package ma.enset.comptecqrsess.query.repositories;

import ma.enset.comptecqrsess.query.entities.Account;
import ma.enset.comptecqrsess.query.entities.Operation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperationRepository extends JpaRepository<Operation, Long> {
}
