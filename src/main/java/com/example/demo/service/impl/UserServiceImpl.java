package com.example.demo.service.impl;

import cn.hutool.core.util.StrUtil;
import com.example.demo.component.MailComponent;
import com.example.demo.domain.ERR;
import com.example.demo.domain.Recode;
import com.example.demo.domain.entity.User;
import com.example.demo.domain.query.UserQuery;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author honghui 2021/07/07
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final MailComponent mailComponent;

  public UserServiceImpl(UserRepository userRepository,
                         MailComponent mailComponent) {
    this.userRepository = userRepository;
    this.mailComponent = mailComponent;
  }


  @Override
  public User createUser(User user) {
    if (StrUtil.isEmptyIfStr(user.getUsername())) {
      throw ERR.of(Recode.MISSING_PARAM);
    }
    String content = "<table border=\"1\">\n" +
        "    <tr>\n" +
        "        <td>账号</td>\n" +
        "        <td>密码</td>\n" +
        "    </tr>\n" +
        "    <tr>\n" +
        "        <td>" + user.getUsername() + "</td>\n" +
        "        <td>" + user.getPassword() + "</td>\n" +
        "    </tr>\n" +
        "</table>";
    mailComponent.sendAsync(MailComponent.MailSending.html(
        "新增账户",
        content));
    return userRepository.save(user);
  }

  @Override
  public Page<User> findUserList(Pageable pageable) {
    return userRepository.findAll(pageable);
  }

  @Override
  public List<User> findUserList() {
    return userRepository.findAll();
  }

  @Override
  public Optional<User> findUser(Long id) {
    return userRepository.findById(id);
  }

  @Override
  public List<User> findUserQuery(String username, String password) {
    return userRepository.findAll(UserQuery.builder()
        .username(username)
        .password(password).build().toSpec());
  }

  @Override
  public void updateUsername(Long id, String username) {
    userRepository.updateUsername(id, username);
  }
}
