CREATE TABLE sqlite_sequence(name,seq);
CREATE TABLE IF NOT EXISTS "users" (
	"ID"	INTEGER,
	"name"	VARCHAR,
	PRIMARY KEY("ID")
);
CREATE TABLE IF NOT EXISTS "student" (
	"id"	INTEGER,
	"name"	VARCHAR,
	"age"	INTEGER,
	"email"	VARCHAR,
	PRIMARY KEY("id")
);
