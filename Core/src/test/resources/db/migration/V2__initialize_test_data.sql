INSERT INTO doctor (id, first_name, last_name, phone) VALUES ('1', 'Ahmed', 'Hamed', '01234564654');
INSERT INTO doctor (id, first_name, last_name, phone) VALUES ('2', 'Mohamed', 'Kamal', '0105465464');
INSERT INTO doctor (id, first_name, last_name, phone) VALUES ('3', 'Mostafa', 'Hamed', '01146469466');
INSERT INTO doctor (id, first_name, last_name, phone) VALUES ('4', 'Khalil', 'Yousry', '010005646546');

INSERT INTO patient (id, first_name, last_name, phone) VALUES ('1', 'Ahmed', 'Kamal', '01234566455');
INSERT INTO patient (id, first_name, last_name, phone) VALUES ('2', 'Ahmed', 'Attef', '01000021222');
INSERT INTO patient (id, first_name, last_name, phone) VALUES ('3', 'Ibrahim', 'Baiomy', '01145878888');
INSERT INTO patient (id, first_name, last_name, phone) VALUES ('4', 'Fekry', 'Esmat', '010101010100');

INSERT INTO appointment (id, date_time, doctor_id, patient_id, status) VALUES ('1', '2023-02-28T05:05:09.000000', '1', '1', 'ACTIVE');
INSERT INTO appointment (id, date_time, doctor_id, patient_id, status) VALUES ('2', '2023-02-27T05:05:33.000000', '2', '2', 'ACTIVE');
INSERT INTO appointment (id, date_time, doctor_id, patient_id, status) VALUES ('3', '2023-02-26T05:05:45.000000', '3', '3', 'ACTIVE');
