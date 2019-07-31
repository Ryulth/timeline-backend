# timeline-backend

http://timeline.ryulth.com/

이 프로젝트는 2019 NAVER D2 FEST mini 썸머 챌린지에 출품하기 위한 프로젝트입니다. 여러 주제 중, 타임라인 주제를 선정하여 진행되었습니다.

* **Frontend repo** : https://github.com/siosio34/timeline-frontend

### 참여한 사람
* [김형률](https://github.com/Ryulth) : 백엔드 개발
* [조영제](https://github.com/siosio34) : 프론트엔드 개발
* [이소영](https://github.com/devSoyoung) : 프론트엔드 개발

## Development Configuration

> 이 프로젝트는 [Spring boot](https://spring.io/projects/spring-boot)으로 만들어졌습니다.

Repository를 다운 받은 후, 해당 디렉토리에서 아래 명령어를 실행합니다.

```
$ gradle bootrun
or
$ gradlew bootrun
```

App이 `http://localhost:8080`에서 실행됩니다.

## Build Jar

```
$ gradle bootJar
or
$ gradlew bootJar
```

위 명령어를 실행하면 `build/libs` 폴더에 빌드 결과물이 저장됩니다.


## Feature
이 프로젝트는 기본적인 기능을 담은 타임라인 서비스를 구현하였습니다.
* 회원가입, 로그인/로그아웃(jwt token)
* 새 소식(=이벤트) 작성, 삭제, 이미지 첨부
* 타임라인, 내 소식 목록
* 프로필 수정, 프로필 이미지 업로드
* 친구 목록, 추천 친구, 친구 요청 및 거절, 친구 삭제
* 추천 친구, 친구 목록 캐시 처리
* 게시물 페이징 처리
* 엔티티 인덱싱 처리


### 로그인
![image](https://user-images.githubusercontent.com/42922453/62202242-e7c5b800-b3c3-11e9-9333-7004e03240be.png)

### 회원가입
![image](https://user-images.githubusercontent.com/42922453/62202290-ff9d3c00-b3c3-11e9-933c-14bd143fec3b.png)

### 타임라인
![image](https://user-images.githubusercontent.com/42922453/62202350-1774c000-b3c4-11e9-92fe-8425e6396c3d.png)

### 내 프로필
![image](https://user-images.githubusercontent.com/42922453/62202384-29566300-b3c4-11e9-9262-f874e4aaca3d.png)

### 친구 목록
![image](https://user-images.githubusercontent.com/42922453/62202433-3f642380-b3c4-11e9-85ee-bce6252d3cdc.png)

## Used Open source
프로젝트에 사용된 오픈소스 라이브러리는 아래와 같습니다.

* [spring boot](https://spring.io/projects/spring-boot)
* [jackson](https://github.com/FasterXML/jackson-core)
* [lombok](https://projectlombok.org/)
* [spring security](https://spring.io/projects/spring-security)
* [jjwt](https://mvnrepository.com/artifact/io.jsonwebtoken/jjwt)
* [springfox](https://springfox.github.io/springfox)
* [aws-java-sdk-s3](https://mvnrepository.com/artifact/com.amazonaws/aws-java-sdk-s3/1.11.18)
* [spring data redis](https://spring.io/projects/spring-data-redis)
* [lettuce](https://lettuce.io/)
* [mysql](https://www.mysql.com/)
* [nginx](https://nginx.org/en/)
* [ubuntu server](https://ubuntu.com/)
* [redis](https://redis.io/)

## Architecture
![image](https://user-images.githubusercontent.com/32893340/62216684-308d6900-b3e4-11e9-82aa-79e29d033aee.png)

## License
MIT License
