package ru.gothmog.blog.app.ws.io.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.gothmog.blog.app.ws.io.entity.UserEntity;

@Repository
public interface UserRepository extends CrudRepository<UserEntity,Long> {

    UserEntity findByEmail(String email);
}
