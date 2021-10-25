package com.stegnerd.config

class Config (
  val isDevelopment: Boolean = false,
  val jdbcUrl: String,
  val dbUser: String,
  val dbPassword: String,
  val jwtSecret: String,
  val jwtIssuer: String,
  val jwtAudience: String,
  val jwtRealm: String
)