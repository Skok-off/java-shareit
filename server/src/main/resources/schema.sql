CREATE TABLE IF NOT EXISTS users (id int NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY
                                 ,name varchar(200) NOT NULL DEFAULT ''
                                 ,email varchar(200) NOT NULL UNIQUE);

CREATE TABLE IF NOT EXISTS requests (id int NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY
                                    ,description varchar(2000) NOT NULL DEFAULT ''
                                    ,requestor_id int NOT NULL REFERENCES users(id) ON DELETE CASCADE
                                    ,created timestamp without time zone NOT NULL);

CREATE TABLE IF NOT EXISTS items (id int NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY
                                 ,name varchar(200) NOT NULL DEFAULT ''
                                 ,description varchar(2000) NOT NULL DEFAULT ''
                                 ,owner_id int NOT NULL REFERENCES users(id) ON DELETE CASCADE
                                 ,is_available boolean NOT NULL DEFAULT false
                                 ,request_id int NULL REFERENCES requests(id));

CREATE TABLE IF NOT EXISTS bookings (id int NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY
                                    ,start_date timestamp without time zone NOT NULL
                                    ,end_date timestamp without time zone NOT NULL
                                    ,item_id int NOT NULL REFERENCES items(id) ON DELETE CASCADE
                                    ,booker_id int NOT NULL REFERENCES users(id) ON DELETE CASCADE
                                    ,status varchar(20) NOT NULL DEFAULT 'WAITING');

CREATE TABLE IF NOT EXISTS comments (id int NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY
                                    ,text varchar(4000) NOT NULL DEFAULT ''
                                    ,item_id int NOT NULL REFERENCES items(id) ON DELETE CASCADE
                                    ,author_id int NOT NULL REFERENCES users(id) ON DELETE CASCADE
                                    ,created timestamp without time zone NOT NULL);