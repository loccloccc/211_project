package ra.project._11_project.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ra.project._11_project.exception.BadRequestException;
import ra.project._11_project.exception.ConflictException;
import ra.project._11_project.exception.ResourceNotFoundException;
import ra.project._11_project.mapper.UserMapper;
import ra.project._11_project.model.dto.request.ChangePasswordRequest;
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

    // Đăng ký bệnh nhân
    @Override
    public UserResponse registerPatient(
            UserRequest request
    ) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ConflictException("Username đã tồn tại");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("Email đã tồn tại");
        }

        if (request.getUsername().contains(" ")) {
            throw new BadRequestException("Username không được để dấu cách");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .passwordHash(
                        passwordEncoder.encode(
                                request.getPassword()
                        )
                )
                .role(RoleEnum.PATIENT)
                .isActive(true)
                .build();

        return userMapper.toResponse(
                userRepository.save(user)
        );
    }

    // Admin tạo user
    @Override
    public UserResponse createUser(
            UserRequest request
    ) {

        if (userRepository.existsByUsername(
                request.getUsername()
        )) {
            throw new ConflictException("Username đã tồn tại");
        }

        if (userRepository.existsByEmail(
                request.getEmail()
        )) {
            throw new ConflictException("Email đã tồn tại");
        }

        if (request.getUsername().contains(" ")) {
            throw new BadRequestException("Username không được để dấu cách");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .passwordHash(
                        passwordEncoder.encode(
                                request.getPassword()
                        )
                )
                .role(request.getRole())
                .isActive(true)
                .build();

        return userMapper.toResponse(
                userRepository.save(user)
        );
    }

    // Cập nhật user
    @Override
    public UserResponse updateUser(
            Long id,
            UserRequest request
    ) {

        System.out.println("========== UPDATE USER ==========");
        System.out.println("REQUEST = " + request);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user"));

        User existedUser = userRepository
                .findByUsername(request.getUsername())
                .orElse(null);

        if (existedUser != null && !existedUser.getId().equals(id)) {
            throw new ConflictException("Username đã tồn tại");
        }

        User existedEmail = userRepository
                .findByEmail(request.getEmail())
                .orElse(null);

        if (existedEmail != null && !existedEmail.getId().equals(id)) {
            throw new ConflictException("Email đã tồn tại");
        }

        if (request.getUsername().contains(" ")) {
            throw new BadRequestException(
                    "Username không được để dấu cách"
            );
        }

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());

        user.setPasswordHash(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );

        user.setRole(request.getRole());

        user.setIsActive(request.getIsActive());

        System.out.println("IS_ACTIVE = " + user.getIsActive());

        return userMapper.toResponse(
                userRepository.save(user)
        );
    }

    // Xóa mềm
    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user"));
        user.setIsActive(false);
        userRepository.save(user);
    }

    // Tìm theo ID
    @Override
    public UserResponse findById(Long id) {

        return userMapper.toResponse(userRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user"))
        );
    }

    // Phân trang + tìm kiếm
    @Override
    public Page<UserResponse> findAll(
            String keyword,
            int page,
            int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> users;
        if (keyword == null || keyword.isBlank()) {
            users = userRepository.findAll(pageable);
        } else {
            users = userRepository.findByUsernameContainingIgnoreCase(keyword, pageable);
        }

        return users.map(userMapper::toResponse);
    }

    // Đổi mật khẩu
    @Override
    public void changePassword(
            ChangePasswordRequest request
    ) {

        String username = SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

        User user = userRepository.findByUsername(username)
                        .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));

        if (!passwordEncoder.matches(
                request.getOldPassword(),
                user.getPasswordHash()
        )) {
            throw new BadRequestException("Mật khẩu cũ không chính xác");
        }
        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }
}