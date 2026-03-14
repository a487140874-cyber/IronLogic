-- Creates the exercises catalog table used by the Exercise module MVP.
-- owner_user_id is nullable by design:
--   null   -> system exercise shared by every user
--   value  -> custom exercise owned by one specific user
-- is_custom is stored explicitly so future queries can distinguish user-created records
-- without inferring only from owner_user_id.
-- secondary_muscles_json and metadata_json stay as jsonb to keep MVP schema small while
-- still allowing structured extension later.
create table if not exists exercises (
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

create index if not exists idx_exercises_owner_user_id on exercises(owner_user_id);
create index if not exists idx_exercises_name on exercises(name);
