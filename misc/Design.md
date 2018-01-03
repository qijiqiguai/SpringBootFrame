1：Encoding is utf8mb4

2: Using Spring-Data-Redis, because SpringSession highly rely on Spring-Data-Redis.

3: Mongo & ElasticSearch and others not use Spring customized solution, because 
    SpringBoot not always using the latest driver of those middle ware. 

### IntelljIDE, Goover Console
org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder encoder = 
    new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder()
encoder.encode("111111")