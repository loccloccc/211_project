package ra.project._11_project.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ra.project._11_project.exception.ConflictException;
import ra.project._11_project.exception.ResourceNotFoundException;
import ra.project._11_project.mapper.UserMapper;
import ra.project._11_project.model.dto.request.UserRequest;
import ra.project._11_project.model.dto.response.UserResponse;
import ra.project._11_project.model.entity.RoleEnum;
import ra.project._11_project.model.entity.User;
import ra.project._11_project.repository.UserRepository;
import ra.project._11_project.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    // đăng kí
    @Override
    public UserResponse registerPatient(UserRequest request) {

        if(userRepository.existsByUsername(request.getUsername())){
            throw new ConflictException("Username đã tồn tại");
        }

        User user = User.builder()
                .username(request.getUsername())
                .passwordHash(
                        passwordEncoder.encode(request.getPassword())
                )
                .role(RoleEnum.PATIENT)
                .isActive(true)
                .build();

        return userMapper.toResponse(
                userRepository.save(user)
        );
    }

    // them người dùng
    @Override
    public UserResponse createUser(UserRequest request) {

        User user = User.builder()
                .username(request.getUsername())
                .passwordHash(
                        passwordEncoder.encode(request.getPassword())
                )
                .role(request.getRole())
                .isActive(true)
                .build();

        return userMapper.toResponse(
                userRepository.save(user)
        );
    }

    // sửa
    @Override
    public UserResponse updateUser(Long id, UserRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user"));

        user.setUsername(request.getUsername());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        return userMapper.toResponse(
                userRepository.save(user)
        );
    }

    // xóa
    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user"));
        user.setIsActive(false);
        userRepository.save(user);
    }

    // lấy theo id
    @Override
    public UserResponse findById(Long id) {

        return userMapper.toResponse(
                userRepository.findById(id)
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Không tìm thấy user"
                                )
                        )
        );
    }

    // phân trang
    @Override
    public Page<UserResponse> findAll(
            String keyword,
            int page,
            int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> users;
        if(keyword == null || keyword.isBlank()){
            users = userRepository.findAll(pageable);
        }else{
            users = userRepository.findByUsernameContainingIgnoreCase(keyword, pageable);
        }
        return users.map(userMapper::toResponse);
    }
}