package repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import entity.User;

public interface UserRepository extends JpaRepository<User, String> {

	@Query("SELECT u FROM User u WHERE u.username = :username")
	User findByUsername(@Param("username") String username);

//	引数に渡されたユーザ名をDBから探して、存在した数を返します。(2つ存在することはないので、1か0)
	int countByUsername(String username); // 追加部分
}