package com.example.demojpa.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Users {

  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  private Long idx;

  @Column(nullable = false)
  private String username;

  @Column(nullable = false)
  private String password;

  private String nickname;

}
