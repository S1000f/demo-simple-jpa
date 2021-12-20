package com.example.demojpa.controller;

import com.example.demojpa.domain.entity.Memos;
import com.example.demojpa.domain.entity.Users;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public class ApiController {

  @PersistenceContext
  private EntityManager em;

  @GetMapping("/user/{idx}")
  public ResponseEntity<?> getUser(@PathVariable(name = "idx") Long idx) {
    long userIdx = idx;

    final Users users = em.find(Users.class, userIdx);

    return ResponseEntity.ok(users);
  }

  @GetMapping("/memo/{idx}")
  public ResponseEntity<?> getMemo(@PathVariable(name = "idx") Long idx) {
    long userIdx = idx;

    final List<Memos> resultList = em.createQuery("select m from Memos m"
        + " join m.users u"
        + " on u.idx =: userIdx", Memos.class)
        .setParameter("userIdx", userIdx)
        .getResultList();

    return ResponseEntity.ok(resultList);
  }

}
