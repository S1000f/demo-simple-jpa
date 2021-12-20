package com.example.demojpa.controller;

import com.example.demojpa.domain.entity.Memos;
import com.example.demojpa.domain.entity.Users;
import com.sun.org.apache.xml.internal.security.algorithms.implementations.SignatureDSA.SHA256;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ApiController {

  @PersistenceContext
  private EntityManager em;

  @Transactional
  @PostMapping("/user")
  public ResponseEntity<?> postUser(@RequestParam String username, @RequestParam String password,
      @RequestParam(required = false) String nickname) throws NoSuchAlgorithmException {
    final MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
    final byte[] digest = sha256.digest(password.getBytes(StandardCharsets.UTF_8));
    final String passwordHash = new String(digest, StandardCharsets.UTF_8);

    final Users users = Users.builder()
        .username(username)
        .password(passwordHash)
        .nickname(nickname)
        .build();

    // insert
    em.persist(users);

    return ResponseEntity.ok().build();
  }

  @Transactional(readOnly = true)
  @GetMapping("/user/{idx}")
  public ResponseEntity<?> getUser(@PathVariable(name = "idx") Long idx) {
    long userIdx = idx;

    // select
    final Users users = em.find(Users.class, userIdx);

    return ResponseEntity.ok(users);
  }

  @Transactional
  @PostMapping("/user/nickname/{idx}")
  public ResponseEntity<?> postNickname(@PathVariable(name = "idx") Long idx, @RequestParam String nickname) {
    boolean isUsingPersistenceContext = true;

    if (isUsingPersistenceContext) {
      final Users users = em.find(Users.class, idx);
      // update
      users.updateNickname(nickname);

    } else {
      // update
      em.createQuery("update Users u"
              + " set u.nickname = :nickname"
              + " where u.idx =: userIdx")
          .setParameter("nickname", nickname)
          .setParameter("userIdx", idx)
          .executeUpdate();
    }

    return ResponseEntity.ok().build();
  }

  @Transactional(readOnly = true)
  @GetMapping("/memo/{idx}")
  public ResponseEntity<?> getMemo(@PathVariable(name = "idx") Long idx) {
    long userIdx = idx;

    // select
    final List<Memos> resultList = em.createQuery("select m from Memos m"
        + " join m.users u"
        + " on u.idx =: userIdx", Memos.class)
        .setParameter("userIdx", userIdx)
        .getResultList();

    return ResponseEntity.ok(resultList);
  }

}
