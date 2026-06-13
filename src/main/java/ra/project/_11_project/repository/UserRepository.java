package ra.project._11_project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.project._11_project.model.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository
        extends JpaRepository<User, Long> {

    // tìm theo username
    Optional<User> findByUsername(
            String username
    );

    // tìm theo email
    Optional<User> findByEmail(
            String email
    );

    // kiểm tra username đã tồn tại chưa
    boolean existsByUsername(
            String username
    );

    // kiểm tra email đã tồn tại chưa
    boolean existsByEmail(
            String email
    );

    // tìm kiếm theo username
    Page<User> findByUsernameContainingIgnoreCase(
            String keyword,
            Pageable pageable
    );
}