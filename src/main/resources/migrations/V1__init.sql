create table public.audience
(
    id            uuid not null
        primary key,
    audience_name varchar(255)
        unique
);

alter table public.audience
    owner to postgres;

create table public.faculty
(
    id           uuid not null
        primary key,
    faculty_name varchar(255)
        unique
);

alter table public.faculty
    owner to postgres;

create table public.student_group
(
    faculty_id   uuid
        constraint fk7ufc9we1dv281aqjoe965od7h
            references public.faculty,
    id           uuid not null
        primary key,
    group_number varchar(255)
        unique
);

alter table public.student_group
    owner to postgres;

create table public.subject
(
    id           uuid not null
        primary key,
    subject_name varchar(255)
        unique
);

alter table public.subject
    owner to postgres;

create table public.timeslot
(
    end_date        time(6),
    start_date      time(6),
    timeslot_number integer,
    id              uuid not null
        primary key
);

alter table public.timeslot
    owner to postgres;

create table public.users
(
    faculty_id uuid
        constraint fk8qkbqvhy7o4vqv850rirhn5hg
            references public.faculty,
    group_id   uuid
        constraint fk8kjeshbtmk3val3lk2ptdwqg6
            references public.student_group,
    id         uuid         not null
        primary key,
    email      varchar(255) not null
        unique,
    full_name  varchar(255) not null,
    password   varchar(255) not null,
    roles      varchar(255)[]
);

alter table public.users
    owner to postgres;

create table public.booking_lesson
(
    date                date,
    participation_count integer,
    audience_id         uuid
        constraint fk6n82bm073imatyak04ri5h6v3
            references public.audience,
    id                  uuid not null
        primary key,
    user_id             uuid
        constraint fk9s5nq9irhxjdfyp2q5x8uuna8
            references public.users,
    title               varchar(255)
);

alter table public.booking_lesson
    owner to postgres;

create table public.booked_timeslots
(
    booking_lesson_id uuid not null
        constraint fkl272ka2t68mc69jorlto08q8
            references public.booking_lesson,
    timeslot_id       uuid not null
        constraint fkmtns4gie75t0enhfiqn9f1oep
            references public.timeslot,
    primary key (booking_lesson_id, timeslot_id)
);

alter table public.booked_timeslots
    owner to postgres;

create table public.lesson
(
    date        date,
    audience_id uuid
        constraint fkahftq2onsop9dm0ipux4qb316
            references public.audience,
    id          uuid not null
        primary key,
    subject_id  uuid
        constraint fk7ydr23s8y9j6lip5qrngoymx4
            references public.subject,
    timeslot_id uuid
        constraint fk8p6phq0t6amg4gttl8veyphy
            references public.timeslot,
    user_id     uuid
        constraint fk9oykd6q5y63diioopksi065o7
            references public.users,
    lesson_type varchar(255)
        constraint lesson_lesson_type_check
            check ((lesson_type)::text = ANY
                   ((ARRAY ['LECTURE'::character varying, 'PRACTICE'::character varying, 'LABORATORY'::character varying])::text[])),
    constraint ukosmiy5q03oxi9osnn2sv2stu9
        unique (date, timeslot_id, audience_id)
);

alter table public.lesson
    owner to postgres;

create table public.groups_on_lesson
(
    group_id  uuid not null
        constraint fk2bw8dnkk75hi5l5jc5ulrbi9e
            references public.lesson,
    lesson_id uuid not null
        constraint fk43x3k7maafx11k9ghmb5wp5uy
            references public.student_group,
    primary key (group_id, lesson_id)
);

alter table public.groups_on_lesson
    owner to postgres;

create table public.token
(
    expired    boolean,
    revoked    boolean,
    id         uuid not null
        primary key,
    user_id    uuid
        constraint fkj8rfw4x0wjjyibfqq566j4qng
            references public.users,
    token      varchar(255),
    token_type varchar(255)
        constraint token_token_type_check
            check ((token_type)::text = 'BEARER'::text)
);

alter table public.token
    owner to postgres;

