BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "users" (
	"ID"	INTEGER,
	"name"	VARCHAR,
	"username"	VARCHAR,
	"password"	VARCHAR,
	"role"	VARCHAR,
	PRIMARY KEY("ID")
);
CREATE TABLE IF NOT EXISTS "attendance" (
	"id"	INTEGER,
	"employee_id"	INTEGER,
	"name"	INTEGER,
	"date"	DATE,
	"time_in"	TIME,
	"time_out"	TIME,
	"overtime"	INTEGER,
	"note"	TEXT,
	"work_type"	VARCHAR,
	PRIMARY KEY("id" AUTOINCREMENT)
);
CREATE TABLE IF NOT EXISTS "deductions" (
	"id"	INTEGER,
	"SSS"	INTEGER,
	"PAG_IBIG"	INTEGER,
	"PHILHEALTH"	INTEGER,
	"advanced"	INTEGER,
	PRIMARY KEY("id")
);
CREATE TABLE IF NOT EXISTS "payroll" (
	"id"	INTEGER,
	"employee_id"	INTEGER,
	"name"	VARCHAR,
	"Department"	VARCHAR,
	"workType"	VARCHAR,
	"grossPay"	INTEGER,
	"ratePerDay"	DECIMAL,
	"daysWorked"	DECIMAL,
	"overtimeHours"	DECIMAL,
	"bonus"	DECIMAL,
	"totalDeduction"	DECIMAL,
	"netPay"	DECIMAL,
	"created_at"	DATE,
	"updated_at"	DATE,
	PRIMARY KEY("id")
);
CREATE TABLE IF NOT EXISTS "department" (
	"department_id"	INTEGER,
	"department_name"	VARCHAR,
	"department_description"	TEXT,
	PRIMARY KEY("department_id")
);
CREATE TABLE IF NOT EXISTS "types" (
	"id"	INTEGER,
	"department_id"	INTEGER,
	"work_type"	VARCHAR,
	"abbreviation"	TEXT,
	"wage"	INTEGER,
	FOREIGN KEY("department_id") REFERENCES "department"("department_id"),
	PRIMARY KEY("id")
);
CREATE TABLE IF NOT EXISTS "employees" (
	"id"	INTEGER NOT NULL CHECK(typeof("id") = 'integer'),
	"employee_id"	VARCHAR,
	"first_name"	VARCHAR,
	"middle_name"	VARCHAR,
	"last_name"	VARCHAR,
	"address"	VARCHAR,
	"department_id"	INTEGER,
	"work_type_id"	VARCHAR,
	"rate"	INTEGER,
	"grossPay"	INTEGER,
	"netPay"	BLOB,
	"gender"	VARCHAR,
	"tel_number"	VARCHAR,
	"email"	VARCHAR,
	"profile_image"	BLOB,
	"date_hired"	DATE,
	"DOB"	DATE,
	FOREIGN KEY("work_type_id") REFERENCES "types"("id"),
	FOREIGN KEY("department_id") REFERENCES "department"("department_id"),
	PRIMARY KEY("id" AUTOINCREMENT)
);
INSERT INTO "users" VALUES (1,'USER','user','user','Employee');
INSERT INTO "users" VALUES (2,'ADMIN','admin','admin','ADMIN');
INSERT INTO "deductions" VALUES (1,570,100,500,NULL);
INSERT INTO "department" VALUES (1,'Welding Department','Welders Department');
INSERT INTO "department" VALUES (2,'Human Resources Department','HR Team');
INSERT INTO "types" VALUES (1,1,'Shielded Metal Arc Welding','SMAW',500);
INSERT INTO "types" VALUES (2,1,'Gas Tungsten Arc Welding','GTAW',900);
INSERT INTO "types" VALUES (3,1,'Flux-cored Arc Welding','FCAW',900);
INSERT INTO "types" VALUES (4,1,'Gas Metal Arc Welding ','GMAW',1000);
INSERT INTO "employees" VALUES (1,'DEV001','Gaudenz',NULL,'Padullon','hellow',1,'2',1000,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
COMMIT;
