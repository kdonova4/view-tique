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
        references developer(developer_id),
	fulltext(title)
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

insert into genre (genre_name) values
('Action'),
('Adventure'),
('RPG'),
('Strategy'),
('Sports'),
('Shooter'),
('Story Rich'),
('Multiplayer'),
('Singleplayer'),
('Stealth'),
('Simulation');

insert into platform (platform_name) values
('PC'),
('PlayStation 5'),
('PlayStation 4'),
('Xbox Series X'),
('Xbox One'),
('Xbox 360'),
('Xbox'),
('Nintendo Switch'),
('PC VR'),
('Playstation 3'),
('Mobile');

insert into developer (developer_name) values
('Bungie'),
('Dice'),
('Infinity Ward'),
('CD Project Red'),
('Eidos Montreal'),
('Arkane'),
('Looking Glass Studios');

INSERT INTO app_user (username, password_hash, disabled) VALUES
('test_user1', '$2y$10$5XmabI6UghCVaIDJrvgHxeWe.vhe6Htd.QANZJ4RIkPzPHtpirP0y', false),
('admin_user', '$2y$10$5XmabI6UghCVaIDJrvgHxeWe.vhe6Htd.QANZJ4RIkPzPHtpirP0y', false),
('guest_user', '$2y$10$5XmabI6UghCVaIDJrvgHxeWe.vhe6Htd.QANZJ4RIkPzPHtpirP0y', false),
('power_user', '$2y$10$5XmabI6UghCVaIDJrvgHxeWe.vhe6Htd.QANZJ4RIkPzPHtpirP0y', false),
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

insert into game (title, game_description, release_date, avg_user_score, avg_critic_score, user_review_count, critic_review_count, developer_id)
values
('Deus Ex: Mankind Divided', 'Now an experienced covert operative, Adam Jensen is forced to operate in a world that has grown to despise his kind. Armed with a new arsenal of state-of-the-art weapons and augmentations, he must choose the right approach, along with who to trust, in order to unravel a vast worldwide conspiracy. ',
 '2024-08-23', 8.5, 7.9, 150, 30, 5),
('Halo 2', 'Following the destruction of Halo, humankind experiences a short-lived victory. Eager for revenge, the Covenant launches a surprise attack on Earth, but they find themselves ill-prepared to defeat the UNSC’s home fleet and are forced to flee into slipspace. When the Master Chief pursues his overzealous enemies, they discover yet another Halo ring, uncovering long-buried secrets, including an unlikely ally, that will dramatically alter the course of the Human-Covenant Conflict forever.',
 '2004-11-09', 9.0, 8.2, 120, 25, 1),
('Witcher 3', 'You are Geralt of Rivia, mercenary monster slayer. Before you stands a war-torn, monster-infested continent you can explore at will. Your current contract? Tracking down Ciri — the Child of Prophecy, a living weapon that can alter the shape of the world. ',
 '2015-05-18', 7.5, 6.8, 100, 20, 4),
('Dishonored', 'Dishonored is an immersive first-person action game that casts you as a supernatural assassin driven by revenge. With Dishonored’s flexible combat system, creatively eliminate your targets as you combine the supernatural abilities, weapons and unusual gadgets at your disposal. ',
 '2012-10-09', 8.0, 7.5, 110, 22, 6),
('Battlefield 4', 'Embrace unrivaled destruction in Battlefield 4™. Revel in the glorious chaos of all-out war packed with rewarding, tactical challenges in an interactive environment. ',
 '2013-10-29', 8.7, 8.0, 130, 28, 2),
('Battlefield 3', 'Enjoy total freedom to fight the way you want. Explore 29 massive multiplayer maps and use loads of vehicles, weapons, and gadgets to help you turn up the heat. Every second of battle gets you closer to unlocking tons of extras and moving up in the Ranks. So get in the action.',
  '2011-10-28', 8.7, 8.0, 130, 28, 2);

INSERT INTO review (score, review_time, review_body, likes, dislikes, app_user_id, game_id) VALUES
(8.5, '2025-01-23 14:35:00', 'Great game with immersive story and excellent gameplay mechanics.', 15, 2, 4, 1),
(7.0, '2025-01-22 18:20:00', 'Fun game, but the graphics felt outdated.', 10, 5, 1, 4),
(9.2, '2025-01-21 20:00:00', 'Absolutely loved it! The soundtrack and visuals are stunning.', 25, 1, 1, 3),
(6.8, '2025-01-20 11:00:00', 'Decent, but the gameplay felt repetitive after a while.', 8, 3, 1, 2),
(4.5, '2025-01-19 09:15:00', 'Poor experience due to constant bugs and crashes.', 3, 20, 1, 5),
(8.0, '2025-01-18 16:45:00', 'Solid multiplayer mode, but single-player lacks depth.', 18, 4, 1, 6),
(7.5, '2025-01-17 12:30:00', 'Good overall, but a bit overpriced for the content.', 12, 6, 1, 1);

INSERT INTO review_reaction (review_id, app_user_id, reaction_type) VALUES
(1, 1, 'like'),
(2, 1, 'dislike'),
(3, 1, 'like'),
(4, 1, 'like'),
(5, 1, 'dislike'),
(6, 1, 'dislike'),
(7, 1, 'like'),
(1, 4, 'dislike'),
(2, 4, 'like'),
(3, 4, 'dislike'),
(4, 4, 'like'),
(5, 4, 'like'),
(6, 4, 'dislike'),
(7, 4, 'dislike');

insert into game_genre (game_id, genre_id) values
(1, 1),
(1, 2),
(1, 3),
(2, 1),
(2, 6),
(3, 2),
(3, 3),
(4, 9),
(4, 10),
(5, 6),
(5, 8),
(6, 6),
(6, 8);

insert into game_platform (game_id, platform_id) values
(1, 1),
(1, 2),
(1, 3),
(1, 4),
(1, 5),
(2, 1),
(2, 4),
(2, 5),
(2, 6),
(2, 7),
(3, 1),
(3, 2),
(3, 3),
(3, 4),
(3, 5),
(3, 8),
(4, 1),
(4, 2),
(4, 3),
(4, 4),
(4, 5),
(4, 6),
(5, 1),
(5, 2),
(5, 3),
(5, 4),
(5, 5),
(5, 6),
(6, 1),
(6, 6),
(6, 10);

insert into wishlist (game_id, app_user_id) values
(1, 1),
(4, 1),
(5, 2),
(6, 2),
(1, 3),
(2, 3),
(4, 3),
(5, 3),
(2, 4),
(1, 5),
(2, 5),
(3, 5);
