create table doctor
(
    id         varchar(255) not null
        primary key,
    first_name varchar(255),
    last_name  varchar(255),
    phone      varchar(255)
);


create table patient
(
    id         varchar(255) not null
        primary key,
    first_name varchar(255),
    last_name  varchar(255),
    phone      varchar(255)
);


create table appointment
(
    id                  varchar(255) not null
        primary key,
    cancellation_reason varchar(255),
    date_time           timestamp(6),
    status              varchar(255),
    doctor_id           varchar(255)
        constraint fkoeb98n82eph1dx43v3y2bcmsl
            references doctor,
    patient_id          varchar(255)
        constraint fk4apif2ewfyf14077ichee8g06
            references patient
);


