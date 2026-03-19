-- Creates the program definition tables used by the Program module MVP.
-- These tables only describe plan templates. They do not record executed workouts.
-- Unique constraints are scoped to the parent object because order matters locally:
--   - program_blocks.sequence_no is unique within one program
--   - session_templates.sequence_no is unique within one block
--   - session_exercise_templates.order_no is unique within one session template
-- progression_rule_id stays nullable and unconstrained for now because progression module is
-- intentionally postponed to a later round.

create table if not exists programs (
    id bigserial primary key,
    user_id bigint not null,
    name varchar(128) not null,
    goal_type varchar(32) not null,
    status varchar(32) not null,
    description text,
    start_date date,
    end_date date,
    created_at timestamp not null default now(),
    updated_at timestamp not null default now()
);

create index if not exists idx_programs_user_id on programs(user_id);
create index if not exists idx_programs_user_status on programs(user_id, status);

create table if not exists program_blocks (
    id bigserial primary key,
    program_id bigint not null,
    name varchar(128) not null,
    block_type varchar(32) not null,
    sequence_no integer not null,
    duration_mode varchar(32) not null,
    duration_value integer,
    deload_enabled boolean not null default false,
    metadata_json jsonb,
    created_at timestamp not null default now(),
    updated_at timestamp not null default now(),
    constraint uq_program_blocks_program_sequence unique(program_id, sequence_no),
    constraint fk_program_blocks_program foreign key (program_id) references programs(id)
);

create index if not exists idx_program_blocks_program_id on program_blocks(program_id);

create table if not exists session_templates (
    id bigserial primary key,
    block_id bigint not null,
    name varchar(128) not null,
    sequence_no integer not null,
    trigger_mode varchar(32) not null default 'SEQUENCE',
    notes text,
    metadata_json jsonb,
    created_at timestamp not null default now(),
    updated_at timestamp not null default now(),
    constraint uq_session_templates_block_sequence unique(block_id, sequence_no),
    constraint fk_session_templates_block foreign key (block_id) references program_blocks(id)
);

create index if not exists idx_session_templates_block_id on session_templates(block_id);

create table if not exists session_exercise_templates (
    id bigserial primary key,
    session_template_id bigint not null,
    exercise_id bigint not null,
    order_no integer not null,
    target_sets integer not null,
    target_reps integer,
    target_weight numeric(10,2),
    target_weight_unit varchar(8) default 'kg',
    rest_seconds integer,
    intensity_mode varchar(32) default 'WEIGHT',
    progression_rule_id bigint,
    prescription_json jsonb,
    created_at timestamp not null default now(),
    updated_at timestamp not null default now(),
    constraint uq_session_exercise_templates_order unique(session_template_id, order_no),
    constraint fk_session_exercise_templates_template foreign key (session_template_id) references session_templates(id),
    constraint fk_session_exercise_templates_exercise foreign key (exercise_id) references exercises(id)
);

create index if not exists idx_set_templates_session_template_id
    on session_exercise_templates(session_template_id);
