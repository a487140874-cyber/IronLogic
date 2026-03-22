-- Creates the workout execution tables used by the Workout module MVP.
-- These tables store actual training results and must stay separate from template tables.
-- source_type differentiates TEMPLATE and MANUAL because recommendation/history logic later
-- must know whether a workout was planned or free-form.
-- workout_sets are stored independently from templates because execution results must remain
-- stable even if the underlying template changes in the future.

create table if not exists workout_sessions (
    id bigserial primary key,
    user_id bigint not null,
    source_type varchar(32) not null,
    source_program_id bigint,
    source_block_id bigint,
    source_template_id bigint,
    status varchar(32) not null,
    started_at timestamp not null,
    ended_at timestamp,
    notes text,
    created_at timestamp not null default now(),
    updated_at timestamp not null default now(),
    constraint chk_workout_sessions_source_type check (source_type in ('TEMPLATE', 'MANUAL')),
    constraint chk_workout_sessions_status check (status in ('IN_PROGRESS', 'COMPLETED')),
    constraint fk_workout_sessions_program foreign key (source_program_id) references programs(id),
    constraint fk_workout_sessions_block foreign key (source_block_id) references program_blocks(id),
    constraint fk_workout_sessions_template foreign key (source_template_id) references session_templates(id)
);

create index if not exists idx_workout_sessions_user_id on workout_sessions(user_id);
create index if not exists idx_workout_sessions_template_id on workout_sessions(source_template_id);
create index if not exists idx_workout_sessions_started_at on workout_sessions(started_at);

create table if not exists workout_exercises (
    id bigserial primary key,
    workout_session_id bigint not null,
    exercise_id bigint not null,
    source_template_exercise_id bigint,
    actual_order_no integer not null,
    replacement_of_exercise_id bigint,
    notes text,
    created_at timestamp not null default now(),
    updated_at timestamp not null default now(),
    constraint uq_workout_exercises_session_order unique(workout_session_id, actual_order_no),
    constraint fk_workout_exercises_session foreign key (workout_session_id) references workout_sessions(id),
    constraint fk_workout_exercises_exercise foreign key (exercise_id) references exercises(id),
    constraint fk_workout_exercises_template_exercise foreign key (source_template_exercise_id) references session_exercise_templates(id)
);

create index if not exists idx_workout_exercises_session_id on workout_exercises(workout_session_id);
create index if not exists idx_workout_exercises_exercise_id on workout_exercises(exercise_id);

create table if not exists workout_sets (
    id bigserial primary key,
    workout_exercise_id bigint not null,
    set_no integer not null,
    weight numeric(10,2),
    reps integer,
    duration_seconds integer,
    rest_seconds integer,
    rpe numeric(4,1),
    rir integer,
    is_warmup boolean not null default false,
    is_completed boolean not null default true,
    created_at timestamp not null default now(),
    updated_at timestamp not null default now(),
    constraint uq_workout_sets_exercise_set_no unique(workout_exercise_id, set_no),
    constraint fk_workout_sets_exercise foreign key (workout_exercise_id) references workout_exercises(id)
);

create index if not exists idx_workout_sets_exercise_id on workout_sets(workout_exercise_id);
