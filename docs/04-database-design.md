# 数据库设计

## 数据库
PostgreSQL

## 核心表
- users
- exercises
- programs
- program_blocks
- session_templates
- session_exercise_templates
- progression_rules
- workout_sessions
- workout_exercises
- workout_sets
- program_progress

## 设计原则
1. 模板层与执行层分表
2. 高频筛选字段结构化
3. 扩展参数使用 jsonb
4. 历史训练不可依赖当前模板动态解释

## 1. users

```
create table users (
    id bigserial primary key,
    email varchar(128) unique,
    mobile varchar(32) unique,
    password_hash varchar(255) not null,
    nickname varchar(64) not null,
    gender varchar(16),
    weight_unit varchar(8) not null default 'kg',
    default_rest_seconds integer,
    created_at timestamp not null default now(),
    updated_at timestamp not null default now()
);
```

## 2. exercises

```
create table exercises (
    id bigserial primary key,
    owner_user_id bigint null,
    name varchar(128) not null,
    category varchar(32),
    primary_muscle varchar(64),
    secondary_muscles_json jsonb,
    equipment_type varchar(32),
    movement_pattern varchar(64),
    is_custom boolean not null default false,
    metadata_json jsonb,
    created_at timestamp not null default now(),
    updated_at timestamp not null default now()
);

create index idx_exercises_owner_user_id on exercises(owner_user_id);
create index idx_exercises_name on exercises(name);
```

## 3. programs

```
create table programs (
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

create index idx_programs_user_id on programs(user_id);
create index idx_programs_user_status on programs(user_id, status);
```

## 4. program_blocks

```
create table program_blocks (
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
    unique(program_id, sequence_no)
);

create index idx_program_blocks_program_id on program_blocks(program_id);
```

## 5. session_templates

```
create table session_templates (
    id bigserial primary key,
    block_id bigint not null,
    name varchar(128) not null,
    sequence_no integer not null,
    trigger_mode varchar(32) not null default 'SEQUENCE',
    notes text,
    metadata_json jsonb,
    created_at timestamp not null default now(),
    updated_at timestamp not null default now(),
    unique(block_id, sequence_no)
);

create index idx_session_templates_block_id on session_templates(block_id);
```

## 6. progression_rules

```
create table progression_rules (
    id bigserial primary key,
    user_id bigint null,
    name varchar(128) not null,
    rule_type varchar(32) not null,
    params_json jsonb not null,
    enabled boolean not null default true,
    created_at timestamp not null default now(),
    updated_at timestamp not null default now()
);

create index idx_progression_rules_user_id on progression_rules(user_id);
```

## 7. session_exercise_templates

```
create table session_exercise_templates (
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
    unique(session_template_id, order_no)
);

create index idx_set_templates_session_template_id on session_exercise_templates(session_template_id);
```

## 8. workout_sessions

```
create table workout_sessions (
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
    updated_at timestamp not null default now()
);

create index idx_workout_sessions_user_id on workout_sessions(user_id);
create index idx_workout_sessions_template_id on workout_sessions(source_template_id);
create index idx_workout_sessions_started_at on workout_sessions(started_at);
```

## 9. workout_exercises

```
create table workout_exercises (
    id bigserial primary key,
    workout_session_id bigint not null,
    exercise_id bigint not null,
    source_template_exercise_id bigint,
    actual_order_no integer not null,
    replacement_of_exercise_id bigint,
    notes text,
    created_at timestamp not null default now(),
    updated_at timestamp not null default now(),
    unique(workout_session_id, actual_order_no)
);

create index idx_workout_exercises_session_id on workout_exercises(workout_session_id);
create index idx_workout_exercises_exercise_id on workout_exercises(exercise_id);
```

## 10. workout_sets

```
create table workout_sets (
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
    unique(workout_exercise_id, set_no)
);

create index idx_workout_sets_exercise_id on workout_sets(workout_exercise_id);
```

## 11. program_progress

```
create table program_progress (
    id bigserial primary key,
    user_id bigint not null,
    program_id bigint not null,
    current_block_id bigint,
    next_session_template_id bigint,
    last_completed_workout_id bigint,
    sequence_cursor integer default 0,
    progress_snapshot_json jsonb,
    updated_at timestamp not null default now(),
    unique(user_id, program_id)
);

create index idx_program_progress_program_id on program_progress(program_id);
```