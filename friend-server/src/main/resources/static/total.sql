create table chat_message
(
    id          bigint auto_increment
        primary key,
    sender_id   int      not null,
    receiver_id int      not null,
    content     text     not null,
    timestamp   datetime not null,
    constraint chat_message_ibfk_1
        foreign key (sender_id) references user (user_id),
    constraint chat_message_ibfk_2
        foreign key (receiver_id) references user (user_id)
);

create index receiver_id
    on chat_message (receiver_id);

create index sender_id
    on chat_message (sender_id);

create table friend_relation
(
    id        bigint auto_increment
        primary key,
    user_id   int         not null comment '用户 ID',
    friend_id int         not null comment '好友 ID',
    status    varchar(50) not null comment '好友关系状态（如：pending, accepted）'
)
    comment '好友关系表';

create index idx_friend_id
    on friend_relation (friend_id);

create index idx_user_id
    on friend_relation (user_id);

create table user
(
    user_id       int auto_increment
        primary key,
    user_name     varchar(255) not null,
    user_password varchar(255) not null,
    user_email    varchar(255) null,
    user_phone    varchar(255) null,
    user_code     varchar(255) null
);

