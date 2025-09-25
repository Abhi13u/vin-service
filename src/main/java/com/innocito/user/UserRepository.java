package com.innocito.user;

import com.innocito.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  User findByUid(String uid);

  User findByEmail(String email);
}
