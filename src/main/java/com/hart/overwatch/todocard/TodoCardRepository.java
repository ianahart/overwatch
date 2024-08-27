package com.hart.overwatch.todocard;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoCardRepository extends JpaRepository<TodoCard, Long> {
}
