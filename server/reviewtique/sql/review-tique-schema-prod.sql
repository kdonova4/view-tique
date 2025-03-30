drop database if exists review_tique;
create database review_tique;
use review_tique;

-- creating tables

create table genre (
    genre_id int primary key auto_increment,
    genre_name varchar(50) unique not null
);

create table platform (
    platform_id int primary key auto_increment,
    platform_name varchar(200) unique not null
);

create table developer (
    developer_id int primary key auto_increment,
    developer_name varchar(200) not null
);

create table app_user (
    app_user_id int primary key auto_increment,
    username varchar(50) not null unique,
    password_hash varchar(2048) not null,
    disabled boolean not null default(0)
);

create table app_role (
    app_role_id int primary key auto_increment,
    role_name varchar(50) not null unique
);

create table app_user_role (
    app_user_id int not null,
    app_role_id int not null,
    constraint pk_app_user_role
        primary key (app_user_id, app_role_id),
    constraint fk_app_user_role_user_id
        foreign key (app_user_id)
        references app_user(app_user_id),
    constraint fk_app_user_role_role_id
        foreign key (app_role_id)
        references app_role(app_role_id)
);

create table game (
    game_id int primary key auto_increment,
    title varchar(250) not null,
    game_description TEXT not null,
    release_date date not null,
    avg_user_score decimal(4, 1) not null default(0.0),
    avg_critic_score decimal(4, 1) not null default(0.0),
    user_review_count int default (0), 
    critic_review_count int default (0), 
    cover varchar(2083),
    developer_id int not null,
    constraint fk_game_developer_id
        foreign key (developer_id)
        references developer(developer_id),
	fulltext (title)
);

create table review (
    review_id int primary key auto_increment,
    score decimal (4, 1) not null,
    review_time timestamp not null,
    review_body varchar(2000) not null,
    likes int not null default(0),
    dislikes int not null default(0),
    app_user_id int not null,
    game_id int not null,
    constraint fk_review_app_user_id
        foreign key (app_user_id)
        references app_user(app_user_id),
    constraint fk_review_game_id
        foreign key (game_id)
        references game(game_id)
);

create table review_reaction (
    review_reaction_id int primary key auto_increment,
    review_id int not null,
    app_user_id int not null,
    reaction_type enum('like', 'dislike') not null,
    constraint fk_review_reaction_review_id
        foreign key (review_id)
        references review(review_id),
    constraint fk_review_reaction_app_user_id
        foreign key (app_user_id)
        references app_user(app_user_id),
	constraint uq_review_reaction unique (review_id, app_user_id)
);

create table game_genre (
    game_id int not null,
    genre_id int not null,
    constraint pk_game_genre
		primary key(game_id, genre_id),
    constraint fk_game_genre_game_id
        foreign key (game_id)
        references game(game_id),
    constraint game_genre_genre_id
        foreign key (genre_id)
        references genre(genre_id)
);

create table game_platform (
    game_id int not null,
    platform_id int not null,
    constraint pk_game_platform
		primary key(game_id, platform_id),
    constraint fk_game_platform_game_id
        foreign key (game_id)
        references game(game_id),
    constraint fk_game_platform_platform_id
        foreign key (platform_id)
        references platform(platform_id)
);

create table wishlist (
    wishlist_id int primary key auto_increment,
    game_id int not null,
    app_user_id int not null,
    constraint fk_wishlist_game_id
        foreign key (game_id)
        references game(game_id),
    constraint fk_wishlist_app_user_id
        foreign key (app_user_id)
        references app_user(app_user_id),
    constraint uq_wishlist unique (game_id, app_user_id)
);

INSERT INTO app_user (username, password_hash, disabled) VALUES
('test_user1', '$2y$10$5XmabI6UghCVaIDJrvgHxeWe.vhe6Htd.QANZJ4RIkPzPHtpirP0y', false),
('admin_user', '$2y$10$5XmabI6UghCVaIDJrvgHxeWe.vhe6Htd.QANZJ4RIkPzPHtpirP0y', false),
('guest_user', '$2y$10$5XmabI6UghCVaIDJrvgHxeWe.vhe6Htd.QANZJ4RIkPzPHtpirP0y', false),
('critic_user', '$2y$10$5XmabI6UghCVaIDJrvgHxeWe.vhe6Htd.QANZJ4RIkPzPHtpirP0y', false),
('disabled_user', '$2y$10$5XmabI6UghCVaIDJrvgHxeWe.vhe6Htd.QANZJ4RIkPzPHtpirP0y', true);

insert into app_role (role_name) values
('USER'),
('CRITIC'),
('ADMIN');

insert into app_user_role values
(1, 1),
(2, 3),
(3, 1),
(4, 2),
(5, 2);



select * from review;
select * from game;
select * from developer;
select * from review_reaction;
select * from game_platform;
select * from platform;
select * from game_genre;
select * from genre;
select * from wishlist;
select * from app_user;
select * from app_user_role;

