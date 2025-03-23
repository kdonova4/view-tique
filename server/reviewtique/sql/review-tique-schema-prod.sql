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
    cover varchar(2083),
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
('Bungie'), -- 1
('Bethesda'), -- 2
('Dice'), -- 3
('Infinity Ward'), -- 4
('CD Projekt Red'), -- 5
('Eidos Montreal'), -- 6
('Arkane'), -- 7
('Looking Glass Studios'), -- 8
('Naughty Dog'), -- 9
('Treyarch'), -- 10
('FromSoftware'), -- 11
('Playground Games'), -- 12
('Santa Monica Studio'), -- 13
('Insomniac Games'), -- 14
('Respawn Entertainment'), -- 15
('Psyonix'),-- 16
('Rockstar'), -- 17
('Mojang Studios'), -- 18
('Epic Games'), -- 19
('Guerrilla Games'), -- 20
('Blizzard'), -- 21
('Bioware'), -- 22
('Irrational Games'), -- 23
('Ubisoft'), -- 24
('Konami'), -- 25
('Capcom'); -- 26


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

insert into game (title, game_description, release_date, avg_user_score, avg_critic_score, user_review_count, critic_review_count, developer_id)
values
('Deus Ex: Mankind Divided', 'Now an experienced covert operative, Adam Jensen is forced to operate in a world that has grown to despise his kind. Armed with a new arsenal of state-of-the-art weapons and augmentations, he must choose the right approach, along with who to trust, in order to unravel a vast worldwide conspiracy. ',
 '2016-08-23', 8.5, 7.9, 150, 30, 6),
('Halo 2', 'Following the destruction of Halo, humankind experiences a short-lived victory. Eager for revenge, the Covenant launches a surprise attack on Earth, but they find themselves ill-prepared to defeat the UNSC’s home fleet and are forced to flee into slipspace. When the Master Chief pursues his overzealous enemies, they discover yet another Halo ring, uncovering long-buried secrets, including an unlikely ally, that will dramatically alter the course of the Human-Covenant Conflict forever.',
 '2004-11-09', 9.0, 8.2, 10, 25, 1),
('Witcher 3', 'You are Geralt of Rivia, mercenary monster slayer. Before you stands a war-torn, monster-infested continent you can explore at will. Your current contract? Tracking down Ciri — the Child of Prophecy, a living weapon that can alter the shape of the world. ',
 '2015-05-18', 4.5, 6.8, 10, 20, 5),
('Dishonored', 'Dishonored is an immersive first-person action game that casts you as a supernatural assassin driven by revenge. With Dishonored’s flexible combat system, creatively eliminate your targets as you combine the supernatural abilities, weapons and unusual gadgets at your disposal. ',
 '2012-10-09', 8.0, 7.5, 110, 22, 7),
('Battlefield 4', 'Embrace unrivaled destruction in Battlefield 4™. Revel in the glorious chaos of all-out war packed with rewarding, tactical challenges in an interactive environment. ',
 '2013-10-29', 8.7, 8.0, 130, 28, 3),
('Battlefield 3', 'Enjoy total freedom to fight the way you want. Explore 29 massive multiplayer maps and use loads of vehicles, weapons, and gadgets to help you turn up the heat. Every second of battle gets you closer to unlocking tons of extras and moving up in the Ranks. So get in the action.',
  '2011-10-28', 8.7, 8.0, 130, 28, 3),
('The Elder Scrolls V: Skyrim', 'The Elder Scrolls V: Skyrim is an open-world RPG set in the fantasy world of Tamriel, where you play as the Dragonborn tasked with saving the world from an ancient dragon. Featuring exploration, questing, and combat, Skyrim offers a rich and immersive world to explore.', '2011-11-11', 9.5, 9.3, 15, 10, 2),
('The Last of Us', 'A post-apocalyptic action-adventure game where players follow Joel and Ellie as they navigate through dangerous environments filled with hostile humans and infected creatures. The Last of Us is known for its emotionally charged storytelling and immersive world.', '2013-06-14', 9.8, 9.5, 1000, 200, 9),
('Grand Theft Auto V', 'Grand Theft Auto V takes place in the fictional state of San Andreas, following three protagonists as they engage in heists, explore the open world, and deal with personal conflicts. Known for its detailed world, dynamic gameplay, and extensive multiplayer mode.', '2013-09-17', 9.5, 9.4, 1500, 300, 17),
('Red Dead Redemption 2', 'Set in the American Wild West, Red Dead Redemption 2 follows Arthur Morgan and the Van der Linde gang. Players experience a massive open world with realistic ecosystems, complex characters, and an emotionally driven story of loyalty and survival.', '2018-10-26', 9.6, 9.8, 1000, 400, 17),
('Minecraft', 'Minecraft is a sandbox game that allows players to explore, build, and survive in a blocky world. With various gameplay modes and an emphasis on creativity, Minecraft has become a global phenomenon, inspiring players to build entire worlds.', '2011-11-18', 9.3, 8.7, 15000, 500, 18),
('Fortnite', 'Fortnite is a free-to-play battle royale game where 100 players fight to be the last one standing. Players can gather resources, build structures, and engage in fast-paced combat. Known for its dynamic events and seasonal updates.', '2017-09-26', 8.3, 8.0, 20000, 600, 19),
('Horizon Zero Dawn', 'Horizon Zero Dawn is an open-world action RPG set in a post-apocalyptic world where robotic creatures roam the earth. Players control Aloy, a young hunter, as she uncovers the mystery behind the fall of humanity.', '2017-02-28', 9.0, 8.8, 900, 150, 20),
('Overwatch', 'Overwatch is a team-based first-person shooter featuring unique heroes with different abilities. Players engage in strategic, objective-based combat to outplay the opposing team. The game has become a competitive esports phenomenon.', '2016-05-24', 8.6, 8.9, 10000, 400, 21),
('Mass Effect 2', 'Mass Effect 2 is a sci-fi action RPG where players control Commander Shepard in a fight against an imminent galactic threat. Players form relationships, make difficult choices, and explore different planets in this narrative-driven space adventure.', '2010-01-26', 9.4, 9.2, 800, 200, 22),
('The Witcher 2: Assassins of Kings', 'The Witcher 2 is an action RPG where players control Geralt of Rivia in a politically charged fantasy world. The game focuses on combat, story choices, and moral ambiguity as Geralt uncovers a conspiracy and faces powerful enemies.', '2011-05-17', 8.9, 8.7, 600, 150, 5),
('Bioshock Infinite', 'Bioshock Infinite is a first-person shooter set in the floating city of Columbia. Players control Booker DeWitt as he tries to rescue a mysterious woman named Elizabeth while uncovering the city’s dark secrets.', '2013-03-26', 9.1, 9.2, 500, 250, 23),
('Assassin’s Creed IV: Black Flag', 'Assassin’s Creed IV: Black Flag is an open-world action game set in the golden age of piracy. Players control Edward Kenway, a pirate who gets caught up in the war between Assassins and Templars, while exploring the high seas and hunting for treasure.', '2013-10-29', 8.5, 8.9, 800, 300, 24),
('Metal Gear Solid V: The Phantom Pain', 'Metal Gear Solid V: The Phantom Pain is an open-world stealth action game where players control Venom Snake, a mercenary seeking revenge after a devastating attack on his base. The game features deep stealth mechanics, open-world exploration, and a gripping story.', '2015-09-01', 9.2, 9.5, 700, 400, 25),
('Resident Evil 2 (Remake)', 'Resident Evil 2 (Remake) is a survival horror game that reimagines the 1998 classic. Players control Leon S. Kennedy and Claire Redfield as they attempt to survive the zombie outbreak in Raccoon City, with over-the-shoulder combat and modern graphics.', '2019-01-25', 9.0, 9.2, 800, 350, 26),
('Sekiro: Shadows Die Twice', 'Sekiro: Shadows Die Twice is an action-adventure game set in a re-imagined 16th-century Japan. Players control a shinobi named Wolf as he embarks on a journey to rescue his kidnapped lord and take revenge on his enemies. Known for its challenging combat and deep narrative.', '2019-03-22', 9.3, 9.5, 1000, 500, 11),
('Fallout 4', 'Fallout 4 is an open-world RPG set in a post-apocalyptic Boston. Players control the Sole Survivor as they explore the wasteland, build settlements, and navigate a world filled with factions, monsters, and difficult moral choices.', '2015-11-10', 8.7, 8.8, 900, 400, 2),
('God of War (2018)', 'God of War (2018) follows Kratos, the Greek god, as he embarks on a journey with his son Atreus through Norse mythology. The game features a deep narrative, innovative combat, and a massive open world to explore.', '2018-04-20', 9.5, 9.4, 1200, 500, 13),
('Uncharted 4: A Thief’s End', 'Uncharted 4: A Thief’s End follows Nathan Drake as he comes out of retirement for one last adventure. The game blends platforming, exploration, and combat in a narrative-driven action-adventure experience.', '2016-05-10', 9.3, 9.5, 1100, 350, 9),
('Dark Souls III', 'Dark Souls III is an action RPG known for its punishing difficulty and intricate world design. Players control the Ashen One as they explore a dark, ruined world filled with terrifying enemies and challenging boss fights.', '2016-04-12', 8.9, 9.0, 1000, 450, 11),
('Rocket League', 'Rocket League is a unique blend of soccer and vehicular combat, where players control rocket-powered cars to score goals on various arenas. The game features fast-paced multiplayer matches and has become a staple of competitive gaming.', '2015-07-07', 8.5, 8.2, 7000, 300, 16),
('Destiny', 'Destiny is an online-only multiplayer first-person shooter where players assume the role of a Guardian to protect the Last City of humanity.', '2014-09-09', 8.0, 8.5, 5000, 400, 1),
('Destiny 2', 'Destiny 2 expands on the original with new story content, gameplay modes, and expanded worlds.', '2017-09-06', 8.2, 8.5, 10000, 500, 1),
('Battlefield 1', 'A World War I themed shooter that introduces new mechanics, environments, and historical battles.', '2016-10-21', 8.9, 8.3, 7000, 320, 3),
('Call of Duty: Modern Warfare', 'A reimagining of the classic first-person shooter that introduces tactical gameplay and a gripping story.', '2019-10-25', 8.7, 9.0, 20000, 500, 4),
('Call of Duty: Modern Warfare 2', 'The second installment in the Modern Warfare series, continuing the story and introducing new multiplayer features.', '2009-11-10', 9.0, 8.8, 25000, 700, 4),
('Cyberpunk 2077', 'A dystopian open-world RPG set in the futuristic Night City, where players control V and engage in high-tech adventures.', '2020-12-10', 7.5, 8.0, 10000, 500, 5),
('Dishonored 2', 'A first-person stealth game where players control either Emily Kaldwin or Corvo Attano to take back the throne.', '2016-11-11', 8.5, 8.8, 6000, 200, 7),
('Prey', 'A first-person sci-fi game where players fight alien creatures on a space station, with a mix of exploration and combat.', '2017-05-05', 8.3, 8.1, 7000, 300, 7),
('System Shock 2', 'A survival horror RPG set in space, where players must combat mutants and rogue AIs to escape the station.', '1999-08-11', 9.2, 9.1, 4000, 150, 8),
('Thief', 'A stealth-based game where players control Garrett, a master thief, navigating a dystopian city to complete dangerous heists.', '1998-10-22', 8.5, 8.3, 5000, 200, 8),
('Apex Legends', 'A free-to-play battle royale game set in the Titanfall universe, featuring unique heroes with different abilities.', '2019-02-04', 8.7, 8.4, 15000, 500, 15),
('Star Wars Jedi: Fallen Order', 'A third-person action game set in the Star Wars universe where players control Cal Kestis, a young Jedi.', '2019-11-15', 9.0, 9.1, 18000, 600, 15);

INSERT INTO review (score, review_time, review_body, likes, dislikes, app_user_id, game_id) VALUES
(8.5, '2025-01-23 14:35:00', 'Great game with immersive story and excellent gameplay mechanics.', 15, 2, 4, 1),
(7.0, '2025-01-22 18:20:00', 'Fun game, but the graphics felt outdated.', 10, 5, 1, 4),
(9.2, '2025-01-21 20:00:00', 'Absolutely loved it! The soundtrack and visuals are stunning.', 25, 1, 2, 3),
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

