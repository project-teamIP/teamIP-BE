INSERT INTO users (is_locked, is_google, is_kakao, clean_point,
                   language, country, login_id, nickname, password,
                   interest, gender, image)
VALUES (false, false, false, 50, 'KOREAN', 'SOUTH_KOREA', 'test01@gmail.com', '유저01', '$2a$10$nRiw2L4edlhC2.kSuy70XONhXgpVeWgXRAfsPp.NiFsBXMxhoOYr2', 'GAME', 'MALE', 'https://heyannyeoung.s3.ap-northeast-2.amazonaws.com/profile2.png'),
       (false, false, false, 50, 'ENGLISH', 'US', 'test02@gmail.com', 'user02', '$2a$10$t.gFil4BXaDsbChhewyUvujK2SuT3I1bIcZIc4B6/1l7MY2X5b3Z.', 'K-POP', 'MALE', 'https://heyannyeoung.s3.ap-northeast-2.amazonaws.com/profile1.png'),
       (false, false, false, 50, 'KOREAN', 'SOUTH_KOREA', 'test03@gmail.com', '유저03', '$2a$10$nRiw2L4edlhC2.kSuy70XONhXgpVeWgXRAfsPp.NiFsBXMxhoOYr2', 'GAME', 'FEMALE', 'https://heyannyeoung.s3.ap-northeast-2.amazonaws.com/profile2.png'),
       (false, false, false, 50, 'ENGLISH', 'UK', 'test04@gmail.com', 'user04', '$2a$10$t.gFil4BXaDsbChhewyUvujK2SuT3I1bIcZIc4B6/1l7MY2X5b3Z.', 'K-POP', 'FEMALE', 'https://heyannyeoung.s3.ap-northeast-2.amazonaws.com/profile3.png'),
       (false, false, false, 50, 'ENGLISH', 'JAPAN', 'test05@gmail.com', 'user05', '$2a$10$t.gFil4BXaDsbChhewyUvujK2SuT3I1bIcZIc4B6/1l7MY2X5b3Z.', 'MOVIE', 'FEMALE', 'https://heyannyeoung.s3.ap-northeast-2.amazonaws.com/profile1.png'),
       (false, false, false, 50, 'KOREAN', 'US', 'test06@gmail.com', '유저06', '$2a$10$t.gFil4BXaDsbChhewyUvujK2SuT3I1bIcZIc4B6/1l7MY2X5b3Z.', 'BOOK', 'MALE', 'https://heyannyeoung.s3.ap-northeast-2.amazonaws.com/profile2.png'),
       (false, false, false, 50, 'KOREAN', 'US', 'test07@gmail.com', '유저07', '$2a$10$t.gFil4BXaDsbChhewyUvujK2SuT3I1bIcZIc4B6/1l7MY2X5b3Z.', 'BOOK', 'FEMALE', 'https://heyannyeoung.s3.ap-northeast-2.amazonaws.com/profile1.png');


INSERT INTO memo (id, created_at, modified_at, title, content, user_id, partner_nickname, partner_image)
VALUES (1, '2023-08-15 00:40:20.275486', '2023-08-15 00:40:20.275486','미국인과대화', '헬로', 1, '유저07', 'https://heyannyeoung.s3.ap-northeast-2.amazonaws.com/partner1.png'),
       (2, '2023-08-14 12:40:20.275486', '2023-08-15 00:40:20.275486','일본인과대화', '스고이데스네', 1, '유저07', 'https://heyannyeoung.s3.ap-northeast-2.amazonaws.com/partner1.png'),
       (3, '2023-08-12 00:11:20.275486', '2023-08-15 00:40:20.275486','중국인과~', '니하오', 1, '유저07', 'https://heyannyeoung.s3.ap-northeast-2.amazonaws.com/partner1.png'),
       (4, '2023-08-11 11:21:20.275486', '2023-08-15 00:40:20.275486','미쿡인', '아메리카버거딜리셔스', 1, '유저07', 'https://heyannyeoung.s3.ap-northeast-2.amazonaws.com/partner1.png'),
       (5, '2023-08-15 00:41:20.275486', '2023-08-15 00:40:20.275486','제목제목', '제목입니다', 1, '유저07', 'https://heyannyeoung.s3.ap-northeast-2.amazonaws.com/partner1.png'),
       (6, '2023-08-10 10:40:20.275486', '2023-08-15 00:40:20.275486','제목6', '내용6', 1, '유저07', 'https://heyannyeoung.s3.ap-northeast-2.amazonaws.com/partner1.png'),
       (7, '2023-08-12 10:38:20.275486', '2023-08-15 00:40:20.275486','일본인과~', '곤니치와', 1, '유저07', 'https://heyannyeoung.s3.ap-northeast-2.amazonaws.com/partner1.png'),
       (8, '2023-08-12 00:20:20.275486', '2023-08-15 00:40:20.275486','언어공부', '좋은표현많이배움', 1, '유저07', 'https://heyannyeoung.s3.ap-northeast-2.amazonaws.com/partner1.png');

INSERT INTO buddy (created_at, receiver_id, sender_id)
VALUES (CURRENT_TIMESTAMP, 2, 1),
        (CURRENT_TIMESTAMP,3, 1),
        (CURRENT_TIMESTAMP,4, 1),
        (CURRENT_TIMESTAMP,5, 1),
        (CURRENT_TIMESTAMP,6, 1);
