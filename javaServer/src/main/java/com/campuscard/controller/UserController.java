package com.campuscard.controller;

import com.campuscard.entity.User;
import com.campuscard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;
import java.util.Map;
import java.util.HashMap;
import javax.annotation.PostConstruct;

@RestController
@RequestMapping("/api")
@CrossOrigin  // 添加跨域支持
public class UserController {
    
    @Autowired
    private UserRepository userRepository;
    
    // 初始化管理员账号
    @PostConstruct
    public void init() {
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setId(UUID.randomUUID().toString());
            admin.setUsername("admin");
            admin.setPassword("123456");
            admin.setRole("admin");
            admin.setAvatar("https://i.pravatar.cc/150?img=1");
            userRepository.save(admin);
        }
    }
    
    private Map<String, Object> createUserResponse(User user) {
        Map<String, Object> response = new HashMap<>();
        response.put("id", user.getId());
        response.put("username", user.getUsername());
        response.put("role", user.getRole());
        response.put("avatar", user.getAvatar());
        return response;
    }
    
    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", message);
        return response;
    }
    
    // 用户登录
    @GetMapping("/users")
    public ResponseEntity<Map<String, Object>> login(
            @RequestParam String username,
            @RequestParam String password) {
        try {
            return userRepository.findByUsername(username)
                    .filter(user -> user.getPassword().equals(password))
                    .map(user -> {
                        Map<String, Object> response = new HashMap<>();
                        response.put("id", user.getId());
                        response.put("username", user.getUsername());
                        response.put("role", user.getRole());
                        response.put("avatar", user.getAvatar());
                        return ResponseEntity.ok(response);
                    })
                    .orElseGet(() -> {
                        Map<String, Object> error = new HashMap<>();
                        error.put("message", "用户名或密码错误");
                        return ResponseEntity.status(401).body(error);
                    });
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("message", "登录失败：" + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }
    
    // 用户注册
    @PostMapping("/users")
    public ResponseEntity<Map<String, Object>> register(@RequestBody User user) {
        try {
            // 检查用户名是否已存在
            if (userRepository.existsByUsername(user.getUsername())) {
                Map<String, Object> error = new HashMap<>();
                error.put("message", "用户名已存在");
                return ResponseEntity.badRequest().body(error);
            }
            
            // 设置用户ID
            user.setId(UUID.randomUUID().toString());
            
            // 设置默认角色
            if (user.getRole() == null) {
                user.setRole("user");
            }
            
            // 如果没有头像，生成随机头像
            if (user.getAvatar() == null) {
                user.setAvatar("https://i.pravatar.cc/150?img=" + 
                    (int)(Math.random() * 70));
            }
            
            // 保存用户
            User savedUser = userRepository.save(user);
            
            // 构造响应
            Map<String, Object> response = new HashMap<>();
            response.put("id", savedUser.getId());
            response.put("username", savedUser.getUsername());
            response.put("role", savedUser.getRole());
            response.put("avatar", savedUser.getAvatar());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("message", "注册失败：" + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    // 更新用户信息
    @PutMapping("/users/{id}")
    public ResponseEntity<Map<String, Object>> updateUser(
            @PathVariable String id,
            @RequestBody User user) {
        return userRepository.findById(id)
                .map(existing -> {
                    if (user.getPassword() != null && !user.getPassword().trim().isEmpty()) {
                        existing.setPassword(user.getPassword());
                    }
                    if (user.getAvatar() != null) {
                        existing.setAvatar(user.getAvatar());
                    }
                    User updated = userRepository.save(existing);
                    return ResponseEntity.ok(createUserResponse(updated));
                })
                .orElse(ResponseEntity.badRequest()
                    .body(createErrorResponse("用户不存在")));
    }
} 