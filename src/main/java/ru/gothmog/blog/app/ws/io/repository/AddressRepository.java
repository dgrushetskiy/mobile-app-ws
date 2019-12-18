package ru.gothmog.blog.app.ws.io.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.gothmog.blog.app.ws.io.entity.AddressEntity;
import ru.gothmog.blog.app.ws.io.entity.UserEntity;

import java.util.List;

@Repository
public interface AddressRepository extends CrudRepository<AddressEntity,Long> {

   List<AddressEntity> findAllByUserDetails(UserEntity userEntity);

   AddressEntity findByAddressId(String addressId);

}
