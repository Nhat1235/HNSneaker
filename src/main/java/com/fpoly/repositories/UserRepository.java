package com.fpoly.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.stereotype.Repository;

import com.fpoly.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	
	@EntityGraph(value = "UserComplete", type=EntityGraphType.FETCH)
	User findByUsername(String username);
			
	User findByEmail(String email);
	
	@Query(value="select * from user inner join user_role on user.id = user_role.user_id where role_id=2",nativeQuery = true)
	List<User> findByRolel();
}
