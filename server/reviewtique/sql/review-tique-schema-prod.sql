drop database if exists review_tique;
create database review_tique;
use review_tique;

-- creating tables

create table genre (
    genre_id int primary key auto_increment,
    genre_name varchar(50) not null
);

create table platform (
    platform_id int primary key auto_increment,
    platform_name varchar(50) not null
);

create table developer (
    developer_id int primary key auto_increment,
    developer_name varchar(50) not null
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
    title varchar(150) not null,
    game_description varchar(1000) not null,
    release_date date not null,
    avg_user_score decimal(4, 1) not null default(0.0),
    avg_critic_score decimal(4, 1) not null default(0.0),
    user_review_count int default (0), 
    critic_review_count int default (0), 
    developer_id int not null,
    constraint fk_game_developer_id
        foreign key (developer_id)
        references developer(developer_id)
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

insert into developer (developer_name) values ('Developer A');
insert into developer (developer_name) values ('Developer B');
insert into developer (developer_name) values ('Developer C');

insert into game (title, game_description, release_date, avg_user_score, avg_critic_score, user_review_count, critic_review_count, developer_id) 
values 
('Game 1', 'Description of Game 1', '2024-01-01', 8.5, 7.9, 150, 30, 1),
('Game 2', 'Description of Game 2', '2024-02-01', 9.0, 8.2, 120, 25, 2),
('Game 3', 'Description of Game 3', '2024-03-01', 7.5, 6.8, 100, 20, 3),
('Game 4', 'Description of Game 4', '2024-04-01', 8.0, 7.5, 110, 22, 1),
('Game 5', 'Description of Game 5', '2024-05-01', 8.7, 8.0, 130, 28, 2);

insert into genre (genre_name) values 
('Action'),
('Adventure'),
('RPG'),
('Strategy'),
('Sports'),
('Simulation');

insert into platform (platform_name) values 
('PC'),
('PlayStation 5'),
('Xbox Series X'),
('Nintendo Switch'),
('PC VR'),
('Mobile');

insert into game_genre (game_id, genre_id) values 
(1, 1), -- Game 1 - Action
(1, 2), -- Game 1 - Adventure
(2, 3), -- Game 2 - RPG
(2, 4), -- Game 2 - Strategy
(3, 5), -- Game 3 - Sports
(3, 6), -- Game 3 - Simulation
(4, 1), -- Game 4 - Action
(5, 2); -- Game 5 - Adventure

insert into game_platform (game_id, platform_id) values 
(1, 1), -- Game 1 - PC
(1, 2), -- Game 1 - PlayStation 5
(2, 3), -- Game 2 - Xbox Series X
(2, 4), -- Game 2 - Nintendo Switch
(3, 1), -- Game 3 - PC
(4, 5), -- Game 4 - PC VR
(5, 6); -- Game 5 - Mobile
