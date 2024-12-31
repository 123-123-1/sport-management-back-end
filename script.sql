create table if not exists chat
(
    chat_id       int auto_increment
        primary key,
    chat_name     varchar(50)                                          null,
    type          enum ('groupChat', 'friendChat') default 'groupChat' null,
    creation_time datetime                                             null,
    photo         varchar(100)                                         null
);

create table if not exists `group`
(
    group_id    int auto_increment
        primary key,
    description text         null,
    name        varchar(100) null,
    photo       varchar(100) null,
    chat_id     int          null,
    constraint Group_chat_chatID_fk
        foreign key (chat_id) references chat (chat_id)
);

create table if not exists user
(
    user_id           int auto_increment
        primary key,
    user_name         varchar(50)  null,
    password          varchar(255) null,
    phone             varchar(20)  null,
    real_name         varchar(100) null,
    registration_date datetime     null,
    photo             varchar(100) null
);

create table if not exists chat_member
(
    chat_member_id int auto_increment
        primary key,
    chat_id        int null,
    user_id        int null,
    constraint chat_member_pk_2
        unique (user_id, chat_id),
    constraint chat_member_chat_id_fk
        foreign key (chat_id) references chat (chat_id),
    constraint chat_member_user_id_fk
        foreign key (user_id) references user (user_id)
);

create table if not exists friend_application
(
    friend_application_id int auto_increment
        primary key,
    apply_info            varchar(100)                                               null,
    state                 enum ('accepted', 'rejected', 'waiting') default 'waiting' null,
    operation_time        datetime                                                   null,
    expiration_time       datetime                                                   null,
    applicant_id          int                                                        null,
    reviewer_id           int                                                        null,
    constraint friendApplication_user_id_fk
        foreign key (applicant_id) references user (user_id),
    constraint friendApplication_user_id_fk_2
        foreign key (reviewer_id) references user (user_id)
);

create table if not exists group_application
(
    group_application_id int auto_increment
        primary key,
    type                 enum ('invited', 'inviting')             default 'invited' not null,
    apply_info           varchar(100)                                               null,
    state                enum ('accepted', 'rejected', 'waiting') default 'waiting' not null,
    operation_time       datetime                                                   null,
    expiration_time      datetime                                                   null,
    applicant_id         int                                                        not null,
    group_id             int                                                        not null,
    reviewer_id          int                                                        null,
    constraint groupApplication_group_id_fk
        foreign key (group_id) references `group` (group_id),
    constraint groupApplication_user_id_fk
        foreign key (applicant_id) references user (user_id),
    constraint group_application_user_user_id_fk
        foreign key (reviewer_id) references user (user_id)
);

create table if not exists group_member
(
    group_member_id int auto_increment
        primary key,
    user_id         int                                        null,
    group_id        int                                        null,
    role            enum ('member', 'leader') default 'member' null,
    constraint groupMember_group_id_fk
        foreign key (group_id) references `group` (group_id),
    constraint groupMember_user_id_fk
        foreign key (user_id) references user (user_id)
);

create table if not exists group_record
(
    group_record_id int auto_increment
        primary key,
    operator_id     int         null,
    target_id       int         null,
    group_id        int         null,
    time            datetime    null,
    operate_type    varchar(10) null,
    constraint group_record_group_id_fk
        foreign key (group_id) references `group` (group_id),
    constraint group_record_groupmember_id_fk
        foreign key (operator_id) references group_member (group_member_id),
    constraint group_record_groupmember_id_fk_2
        foreign key (target_id) references group_member (group_member_id)
);

create table if not exists message
(
    message_id int auto_increment
        primary key,
    time       datetime     null,
    content    varchar(255) null,
    chat_id    int          null,
    user_id    int          null,
    constraint message_chat_id_fk
        foreign key (chat_id) references chat (chat_id),
    constraint message_user_id_fk
        foreign key (user_id) references user (user_id)
);

create table if not exists message_validation
(
    message_validation_id int auto_increment
        primary key,
    phone                 varchar(20) null,
    message_code          varchar(10) null,
    expiration_time       datetime    null,
    user_id               int         null,
    constraint messageVaildation_user_id_fk
        foreign key (user_id) references user (user_id)
);

create table if not exists notification
(
    notification_id int auto_increment
        primary key,
    type            enum ('system', 'reservation', 'friend', 'deleted') default 'system' null,
    title           varchar(20)                                                          null,
    content         varchar(255)                                                         null,
    timestamp       datetime                                                             null,
    state           enum ('read', 'unread', 'mark', 'deleted')          default 'unread' null,
    user_id         int                                                                  null,
    group_id        int                                                                  null,
    constraint notification_group_id_fk
        foreign key (group_id) references `group` (group_id),
    constraint notification_user_id_fk
        foreign key (user_id) references user (user_id)
);

create table if not exists venue
(
    venue_id       int auto_increment
        primary key,
    name           varchar(20)             null,
    image          varchar(100)            null,
    description    text                    null,
    location       varchar(100)            null,
    venue_state    enum ('open', 'closed') null,
    contact_number varchar(20)             null
);

create table if not exists court
(
    court_id    int auto_increment
        primary key,
    location    varchar(100)                             null,
    capacity    int                                      null,
    court_state enum ('open', 'closed') default 'closed' null,
    venie_id    int                                      null,
    court_name  varchar(50)                              null,
    type        varchar(20)                              null,
    constraint Court_venuemanager_id_fk
        foreign key (venie_id) references venue (venue_id)
);

create table if not exists timeslot
(
    timeslot_id int auto_increment
        primary key,
    start_time  datetime null,
    end_time    datetime null,
    venue_id    int      null,
    constraint timeSlot_venuemanager_id_fk
        foreign key (venue_id) references venue (venue_id)
);

create table if not exists court_availability
(
    availability_id int auto_increment
        primary key,
    timeslot_id     int                                                                null,
    court_id        int                                                                null,
    state           enum ('reservable', 'matching', 'full', 'closed') default 'closed' null,
    price           decimal(10, 2)                                                     null,
    constraint court_availability_court_court_id_fk
        foreign key (court_id) references court (court_id),
    constraint court_availability_timeslot_timeslot_id_fk
        foreign key (timeslot_id) references timeslot (timeslot_id)
);

create table if not exists reservation
(
    reservation_id int auto_increment
        primary key,
    type           enum ('individual', 'group', 'match')                            default 'individual' null,
    state          enum ('reserved', 'signed', 'matching', 'cancelled', 'violated') default 'reserved'   null,
    timeslot_id    int                                                                                   null,
    constraint reservation_timeslot_id_fk
        foreign key (timeslot_id) references timeslot (timeslot_id)
);

create table if not exists group_reservation
(
    group_reservation_id int auto_increment
        primary key,
    group_id             int not null,
    reservation_id       int not null,
    constraint groupreservation_group_id_fk
        foreign key (group_id) references `group` (group_id),
    constraint groupreservation_reservation_id_fk
        foreign key (reservation_id) references reservation (reservation_id)
);

create table if not exists match_reservation
(
    match_reservation_id int auto_increment
        primary key,
    expiration_time      datetime null,
    reserved_count       int      null,
    constraint MatchReservation_reservation_id_fk
        foreign key (match_reservation_id) references reservation (reservation_id)
);

create table if not exists reservation_record
(
    reservation_record_id int auto_increment
        primary key,
    state                 enum ('reserved', 'signed', 'matching', 'cancelled', 'violated') default 'reserved' null,
    time                  datetime                                                                            null,
    user_id               int                                                                                 null,
    reservation_id        int                                                                                 null,
    constraint reservation_record_reservation_id_fk
        foreign key (reservation_id) references reservation (reservation_id),
    constraint reservation_record_user_id_fk
        foreign key (user_id) references user (user_id)
);

create table if not exists user_reservation
(
    user_id             int null,
    user_reservation_id int auto_increment
        primary key,
    reservation_id      int not null,
    constraint userreservation_reservation_id_fk
        foreign key (reservation_id) references reservation (reservation_id),
    constraint userreservation_user_id_fk
        foreign key (user_id) references user (user_id)
);

create table if not exists venue_comment
(
    venue_comment_id int auto_increment
        primary key,
    content          varchar(255) null,
    time             datetime     null,
    user_id          int          not null,
    venue_id         int          not null,
    score            float        null,
    constraint venueComment_user_id_fk
        foreign key (user_id) references user (user_id),
    constraint venueComment_venuemanager_id_fk
        foreign key (venue_id) references venue (venue_id)
);

create table if not exists violation
(
    violation_id    int auto_increment
        primary key,
    state           enum ('normal', 'locked') default 'normal' null,
    violation_count int                                        null,
    unlock_time     datetime                                   null,
    user_id         int                                        null,
    constraint violation_user_id_fk
        foreign key (user_id) references user (user_id)
);


