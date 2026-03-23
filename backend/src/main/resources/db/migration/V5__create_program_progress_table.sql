-- Creates the program_progress table for progression module v1.
-- This table stores the current recommendation cursor of one user in one Program.
-- It does not replace workout history. Workout history remains in workout_sessions and
-- related tables, while progression only stores lightweight state for the next recommendation.
-- TODO: future rounds may extend this snapshot for block transition and richer rule states.

create table if not exists program_progress (
    id bigserial primary key,
    user_id bigint not null,
    program_id bigint not null,
    current_block_id bigint,
    next_session_template_id bigint,
    last_completed_workout_id bigint,
    sequence_cursor integer not null default 0,
    progress_snapshot_json jsonb,
    updated_at timestamp not null default now(),
    constraint uq_program_progress_user_program unique(user_id, program_id),
    constraint fk_program_progress_program foreign key (program_id) references programs(id),
    constraint fk_program_progress_block foreign key (current_block_id) references program_blocks(id),
    constraint fk_program_progress_next_template foreign key (next_session_template_id) references session_templates(id),
    constraint fk_program_progress_last_workout foreign key (last_completed_workout_id) references workout_sessions(id)
);

create index if not exists idx_program_progress_program_id on program_progress(program_id);
