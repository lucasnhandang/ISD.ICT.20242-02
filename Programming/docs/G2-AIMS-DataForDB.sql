-- Insert roles
INSERT INTO role (name) VALUES ('PRODUCT_MANAGER');
INSERT INTO role (name) VALUES ('ADMIN');

-- Insert users
INSERT INTO users (name, password, email, phonenumber) VALUES 
('Dang Van Nhan', '$2a$10$UNqb33Qv1vHyuW6qMUuxJuOZmA/zgh8oyc493HInbvuE65vLV2lPm', 'nhandang@gmail.com', '0901234567'),
('Admin', '$2a$10$UNqb33Qv1vHyuW6qMUuxJuOZmA/zgh8oyc493HInbvuE65vLV2lPm', 'admin1@gmail.com', '0912345678'),
('Admin PM', '$2a$10$UNqb33Qv1vHyuW6qMUuxJuOZmA/zgh8oyc493HInbvuE65vLV2lPm', 'admin2@gmail.com', '0923456789');

-- Insert user_role_map
INSERT INTO user_role_map (user_id, role_id) VALUES 
(1, 1), -- is Product Manager
(2, 2), -- is Admin
(3, 1), -- is Product Manager
(3, 2); -- is also Admin

-- Insert products (CD)
INSERT INTO product (title, value, currentprice, category, barcode, description, quantity, entrydate, dimension, weight, rushordersupported, imageurl) VALUES
('Greatest Hits - The Beatles', 200000, 180000, 'CD', 'CD001', 'A collection of The Beatles greatest hits.', 50, '2025-01-10', '12x12x0.5 cm', 0.1, TRUE, 'http://example.com/cd/beatles.jpg'),
('Thriller - Michael Jackson', 220000, 200000, 'CD', 'CD002', 'Iconic album by Michael Jackson.', 30, '2025-01-15', '12x12x0.5 cm', 0.1, TRUE, 'http://example.com/cd/thriller.jpg'),
('Back to Black - Amy Winehouse', 210000, 190000, 'CD', 'CD003', 'Soulful album by Amy Winehouse.', 40, '2025-02-01', '12x12x0.5 cm', 0.1, FALSE, 'http://example.com/cd/amy.jpg'),
('Abbey Road - The Beatles', 200000, 180000, 'CD', 'CD004', 'Classic album by The Beatles.', 25, '2025-02-10', '12x12x0.5 cm', 0.1, TRUE, 'http://example.com/cd/abbey.jpg'),
('Dark Side of the Moon - Pink Floyd', 230000, 210000, 'CD', 'CD005', 'Legendary album by Pink Floyd.', 35, '2025-03-01', '12x12x0.5 cm', 0.1, TRUE, 'http://example.com/cd/pinkfloyd.jpg'),
('Nevermind - Nirvana', 190000, 170000, 'CD', 'CD006', 'Grunge masterpiece by Nirvana.', 45, '2025-03-15', '12x12x0.5 cm', 0.1, FALSE, 'http://example.com/cd/nirvana.jpg'),
('Rumours - Fleetwood Mac', 200000, 180000, 'CD', 'CD007', 'Timeless album by Fleetwood Mac.', 20, '2025-04-01', '12x12x0.5 cm', 0.1, TRUE, 'http://example.com/cd/rumours.jpg'),
('Born to Run - Bruce Springsteen', 210000, 190000, 'CD', 'CD008', 'Classic rock by Bruce Springsteen.', 30, '2025-04-10', '12x12x0.5 cm', 0.1, TRUE, 'http://example.com/cd/springsteen.jpg'),
('21 - Adele', 220000, 200000, 'CD', 'CD009', 'Popular album by Adele.', 50, '2025-05-01', '12x12x0.5 cm', 0.1, FALSE, 'http://example.com/cd/adele.jpg'),
('Legend - Bob Marley', 200000, 180000, 'CD', 'CD010', 'Reggae classics by Bob Marley.', 40, '2025-05-15', '12x12x0.5 cm', 0.1, TRUE, 'http://example.com/cd/marley.jpg');

INSERT INTO cd (id, artists, recordlabel, tracklist, genre, releasedate) VALUES
(1, 'The Beatles', 'Apple Records', 'Come Together, Something, Here Comes the Sun', 'Rock', '1969-09-26'),
(2, 'Michael Jackson', 'Epic Records', 'Billie Jean, Beat It, Thriller', 'Pop', '1982-11-30'),
(3, 'Amy Winehouse', 'Island Records', 'Rehab, Back to Black, You Know I''m No Good', 'Soul', '2006-10-27'),
(4, 'The Beatles', 'Apple Records', 'Something, Octopus''s Garden, Here Comes the Sun', 'Rock', '1969-09-26'),
(5, 'Pink Floyd', 'Harvest Records', 'Time, Money, Us and Them', 'Progressive Rock', '1973-03-01'),
(6, 'Nirvana', 'DGC Records', 'Smells Like Teen Spirit, Come As You Are', 'Grunge', '1991-09-24'),
(7, 'Fleetwood Mac', 'Warner Bros', 'Dreams, Go Your Own Way, The Chain', 'Rock', '1977-02-04'),
(8, 'Bruce Springsteen', 'Columbia Records', 'Born to Run, Thunder Road', 'Rock', '1975-08-25'),
(9, 'Adele', 'XL Recordings', 'Rolling in the Deep, Someone Like You', 'Pop', '2011-01-24'),
(10, 'Bob Marley', 'Island Records', 'Is This Love, Jamming, Three Little Birds', 'Reggae', '1984-05-08');

-- Insert products (DVD)
INSERT INTO product (title, value, currentprice, category, barcode, description, quantity, entrydate, dimension, weight, rushordersupported, imageurl) VALUES
('Inception', 250000, 230000, 'DVD', 'DVD001', 'A sci-fi thriller by Christopher Nolan.', 20, '2025-01-20', '13x19x1.5 cm', 0.2, TRUE, 'http://example.com/dvd/inception.jpg'),
('The Godfather', 270000, 250000, 'DVD', 'DVD002', 'Classic mafia film by Francis Ford Coppola.', 15, '2025-01-25', '13x19x1.5 cm', 0.2, TRUE, 'http://example.com/dvd/godfather.jpg'),
('The Matrix', 240000, 220000, 'DVD', 'DVD003', 'Sci-fi action film.', 25, '2025-02-05', '13x19x1.5 cm', 0.2, FALSE, 'http://example.com/dvd/matrix.jpg'),
('Pulp Fiction', 230000, 210000, 'DVD', 'DVD004', 'Quentin Tarantino''s masterpiece.', 30, '2025-02-15', '13x19x1.5 cm', 0.2, TRUE, 'http://example.com/dvd/pulpfiction.jpg'),
('Star Wars: A New Hope', 260000, 240000, 'DVD', 'DVD005', 'The original Star Wars film.', 20, '2025-03-05', '13x19x1.5 cm', 0.2, TRUE, 'http://example.com/dvd/starwars.jpg'),
('The Shawshank Redemption', 250000, 230000, 'DVD', 'DVD006', 'A story of hope and friendship.', 25, '2025-03-10', '13x19x1.5 cm', 0.2, FALSE, 'http://example.com/dvd/shawshank.jpg'),
('Forrest Gump', 240000, 220000, 'DVD', 'DVD007', 'A heartwarming tale of life.', 30, '2025-04-05', '13x19x1.5 cm', 0.2, TRUE, 'http://example.com/dvd/forrest.jpg'),
('The Dark Knight', 270000, 250000, 'DVD', 'DVD008', 'Batman faces the Joker.', 15, '2025-04-15', '13x19x1.5 cm', 0.2, TRUE, 'http://example.com/dvd/darkknight.jpg'),
('Titanic', 260000, 240000, 'DVD', 'DVD009', 'Epic romance and tragedy.', 20, '2025-05-05', '13x19x1.5 cm', 0.2, FALSE, 'http://example.com/dvd/titanic.jpg'),
('Gladiator', 250000, 230000, 'DVD', 'DVD010', 'A Roman general seeks revenge.', 25, '2025-05-10', '13x19x1.5 cm', 0.2, TRUE, 'http://example.com/dvd/gladiator.jpg');

INSERT INTO dvd (id, disctype, director, runtime, studio, language, subtitles, releasedate, genre) VALUES
(11, 'DVD', 'Christopher Nolan', 148, 'Warner Bros', 'English', 'English, Vietnamese', '2010-07-16', 'Sci-Fi'),
(12, 'DVD', 'Francis Ford Coppola', 175, 'Paramount', 'English', 'English, Vietnamese', '1972-03-24', 'Crime'),
(13, 'DVD', 'Wachowski Brothers', 136, 'Warner Bros', 'English', 'English, Vietnamese', '1999-03-31', 'Sci-Fi'),
(14, 'DVD', 'Quentin Tarantino', 154, 'Miramax', 'English', 'English, Vietnamese', '1994-10-14', 'Crime'),
(15, 'DVD', 'George Lucas', 121, 'Lucasfilm', 'English', 'English, Vietnamese', '1977-05-25', 'Sci-Fi'),
(16, 'DVD', 'Frank Darabont', 142, 'Columbia Pictures', 'English', 'English, Vietnamese', '1994-09-23', 'Drama'),
(17, 'DVD', 'Robert Zemeckis', 142, 'Paramount', 'English', 'English, Vietnamese', '1994-07-06', 'Drama'),
(18, 'DVD', 'Christopher Nolan', 152, 'Warner Bros', 'English', 'English, Vietnamese', '2008-07-18', 'Action'),
(19, 'DVD', 'James Cameron', 194, '20th Century Fox', 'English', 'English, Vietnamese', '1997-12-19', 'Romance'),
(20, 'DVD', 'Ridley Scott', 155, 'DreamWorks', 'English', 'English, Vietnamese', '2000-05-05', 'Action');

-- Insert products (LP)
INSERT INTO product (title, value, currentprice, category, barcode, description, quantity, entrydate, dimension, weight, rushordersupported, imageurl) VALUES
('Bohemian Rhapsody - Queen', 350000, 320000, 'LP', 'LP001', 'Queen''s iconic album on vinyl.', 15, '2025-01-30', '31x31x0.5 cm', 0.4, TRUE, 'http://example.com/lp/queen.jpg'),
('Led Zeppelin IV - Led Zeppelin', 360000, 330000, 'LP', 'LP002', 'Classic rock vinyl by Led Zeppelin.', 10, '2025-02-20', '31x31x0.5 cm', 0.4, TRUE, 'http://example.com/lp/zeppelin.jpg'),
('Hotel California - Eagles', 340000, 310000, 'LP', 'LP003', 'Eagles'' legendary album on vinyl.', 20, '2025-03-20', '31x31x0.5 cm', 0.4, FALSE, 'http://example.com/lp/eagles.jpg'),
('Sgt. Pepper''s - The Beatles', 350000, 320000, 'LP', 'LP004', 'The Beatles'' masterpiece on vinyl.', 15, '2025-04-01', '31x31x0.5 cm', 0.4, TRUE, 'http://example.com/lp/sgtpepper.jpg'),
('The Wall - Pink Floyd', 370000, 340000, 'LP', 'LP005', 'Pink Floyd''s epic rock opera.', 10, '2025-04-20', '31x31x0.5 cm', 0.4, TRUE, 'http://example.com/lp/thewall.jpg'),
('Kind of Blue - Miles Davis', 330000, 300000, 'LP', 'LP006', 'Jazz masterpiece on vinyl.', 20, '2025-05-01', '31x31x0.5 cm', 0.4, FALSE, 'http://example.com/lp/milesdavis.jpg'),
('Pet Sounds - The Beach Boys', 340000, 310000, 'LP', 'LP007', 'The Beach Boys'' classic on vinyl.', 15, '2025-05-10', '31x31x0.5 cm', 0.4, TRUE, 'http://example.com/lp/petsounds.jpg'),
('Exile on Main St. - The Rolling Stones', 350000, 320000, 'LP', 'LP008', 'Rolling Stones'' double album.', 10, '2025-05-20', '31x31x0.5 cm', 0.4, TRUE, 'http://example.com/lp/stones.jpg'),
('Purple Rain - Prince', 360000, 330000, 'LP', 'LP009', 'Prince''s iconic album on vinyl.', 15, '2025-06-01', '31x31x0.5 cm', 0.4, FALSE, 'http://example.com/lp/purple.jpg'),
('OK Computer - Radiohead', 340000, 310000, 'LP', 'LP010', 'Radiohead''s groundbreaking album.', 20, '2025-06-05', '31x31x0.5 cm', 0.4, TRUE, 'http://example.com/lp/radiohead.jpg');

INSERT INTO lp (id, artists, recordlabel, tracklist, genre, releasedate) VALUES
(21, 'Queen', 'EMI', 'Bohemian Rhapsody, Killer Queen', 'Rock', '1975-11-21'),
(22, 'Led Zeppelin', 'Atlantic', 'Stairway to Heaven, Black Dog', 'Rock', '1971-11-08'),
(23, 'Eagles', 'Asylum', 'Hotel California, New Kid in Town', 'Rock', '1976-12-08'),
(24, 'The Beatles', 'Apple Records', 'Sgt. Pepper''s, Lucy in the Sky', 'Rock', '1967-05-26'),
(25, 'Pink Floyd', 'Harvest', 'Another Brick in the Wall, Comfortably Numb', 'Rock', '1979-11-30'),
(26, 'Miles Davis', 'Columbia', 'So What, Blue in Green', 'Jazz', '1959-08-17'),
(27, 'The Beach Boys', 'Capitol', 'God Only Knows, Wouldn''t It Be Nice', 'Pop', '1966-05-16'),
(28, 'The Rolling Stones', 'Rolling Stones', 'Tumbling Dice, Sweet Virginia', 'Rock', '1972-05-12'),
(29, 'Prince', 'Warner Bros', 'Purple Rain, Let''s Go Crazy', 'Pop', '1984-06-25'),
(30, 'Radiohead', 'Parlophone', 'Paranoid Android, Karma Police', 'Alternative', '1997-05-21');

-- Insert products (Book)
INSERT INTO product (title, value, currentprice, category, barcode, description, quantity, entrydate, dimension, weight, rushordersupported, imageurl) VALUES
('To Kill a Mockingbird', 150000, 130000, 'Book', 'BOOK001', 'A novel about justice and morality.', 50, '2025-01-10', '13x20x2 cm', 0.3, TRUE, 'http://example.com/book/mockingbird.jpg'),
('1984', 140000, 120000, 'Book', 'BOOK002', 'George Orwell''s dystopian classic.', 40, '2025-01-15', '13x20x2 cm', 0.3, TRUE, 'http://example.com/book/1984.jpg'),
('Pride and Prejudice', 130000, 110000, 'Book', 'BOOK003', 'Jane Austen''s romantic novel.', 45, '2025-02-01', '13x20x2 cm', 0.3, FALSE, 'http://example.com/book/pride.jpg'),
('The Great Gatsby', 120000, 100000, 'Book', 'BOOK004', 'F. Scott Fitzgerald''s American classic.', 35, '2025-02-10', '13x20x2 cm', 0.3, TRUE, 'http://example.com/book/gatsby.jpg'),
('The Catcher in the Rye', 130000, 110000, 'Book', 'BOOK005', 'J.D. Salinger''s coming-of-age novel.', 30, '2025-03-01', '13x20x2 cm', 0.3, TRUE, 'http://example.com/book/catcher.jpg'),
('Lord of the Rings', 200000, 180000, 'Book', 'BOOK006', 'J.R.R. Tolkien''s epic fantasy.', 25, '2025-03-15', '13x20x3 cm', 0.5, FALSE, 'http://example.com/book/lotr.jpg'),
('Harry Potter and the Philosopher''s Stone', 160000, 140000, 'Book', 'BOOK007', 'J.K. Rowling''s magical adventure.', 50, '2025-04-01', '13x20x2 cm', 0.3, TRUE, 'http://example.com/book/harry.jpg'),
('The Hobbit', 150000, 130000, 'Book', 'BOOK008', 'J.R.R. Tolkien''s fantasy classic.', 40, '2025-04-10', '13x20x2 cm', 0.3, TRUE, 'http://example.com/book/hobbit.jpg'),
('Animal Farm', 120000, 100000, 'Book', 'BOOK009', 'George Orwell''s political satire.', 35, '2025-05-01', '13x20x2 cm', 0.3, FALSE, 'http://example.com/book/animalfarm.jpg'),
('Brave New World', 140000, 120000, 'Book', 'BOOK010', 'Aldous Huxley''s dystopian novel.', 30, '2025-05-15', '13x20x2 cm', 0.3, TRUE, 'http://example.com/book/brave.jpg');

INSERT INTO book (id, authors, covertype, publisher, publicationdate, numpages, language, genre) VALUES
(31, 'Harper Lee', 'Paperback', 'Grand Central', '1960-07-11', 281, 'English', 'Fiction'),
(32, 'George Orwell', 'Paperback', 'Signet Classics', '1949-06-08', 328, 'English', 'Dystopian'),
(33, 'Jane Austen', 'Hardcover', 'Penguin Classics', '1813-01-28', 432, 'English', 'Romance'),
(34, 'F. Scott Fitzgerald', 'Paperback', 'Scribner', '1925-04-10', 180, 'English', 'Fiction'),
(35, 'J.D. Salinger', 'Paperback', 'Little, Brown', '1951-07-16', 277, 'English', 'Fiction'),
(36, 'J.R.R. Tolkien', 'Hardcover', 'Houghton Mifflin', '1954-07-29', 1178, 'English', 'Fantasy'),
(37, 'J.K. Rowling', 'Paperback', 'Bloomsbury', '1997-06-26', 223, 'English', 'Fantasy'),
(38, 'J.R.R. Tolkien', 'Paperback', 'Houghton Mifflin', '1937-09-21', 310, 'English', 'Fantasy'),
(39, 'George Orwell', 'Paperback', 'Signet Classics', '1945-08-17', 112, 'English', 'Satire'),
(40, 'Aldous Huxley', 'Paperback', 'Harper Perennial', '1932-09-01', 268, 'English', 'Dystopian');