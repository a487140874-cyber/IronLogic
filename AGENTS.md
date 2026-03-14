# IronLogic Agent Rules

## Project Context

IronLogic is a professional workout tracking app focused on:

- Progressive overload
- Periodized training
- Template-based training + manual training
- Sequence-based progression instead of calendar-based scheduling
- Future extensibility for stats, diet, and adaptive training

## Core Domain Naming

Use these names consistently:

- Program
- ProgramBlock
- SessionTemplate
- SessionExerciseTemplate
- WorkoutSession
- WorkoutExercise
- WorkoutSet
- ProgressionRule
- ProgramProgress
- Exercise

Do not invent alternative names unless explicitly asked.

## Core Architecture Rules

1. Keep template models and workout record models strictly separated.
2. Progression logic must live in the `progression` module.
3. The project uses a modular monolith architecture.
4. Backend is Spring Boot + MyBatis-Plus + PostgreSQL.
5. Frontend is Flutter with feature-first structure.
6. Keep controller thin, application service orchestrates, domain holds core business rules.
7. Do not introduce microservices, message queues, Redis, or event bus in MVP unless explicitly requested.

## Product Rules

1. Sequence progression is preferred over calendar-based training scheduling.
2. The system supports both:
   - template-based workout
   - manual workout
3. Progression rules generate recommendations, not forced results.
4. MVP must stay small. Do not add:
   - diet module
   - social features
   - AI coach chat
   - wearable integrations
   unless explicitly requested.

## Engineering Rules

1. Write code module by module.
2. Do not modify unrelated files.
3. Prefer small, reviewable changes.
4. When generating code:
   - create DTOs
   - create entity / persistence objects
   - create service interfaces and implementations
   - create controllers
   - create mapper / repository
   - create Flyway migration when database changes
5. When unsure, align with docs in `/docs`.

## Source of Truth

Always follow these files first:

1. `docs/02-architecture.md`
2. `docs/03-domain-model.md`
3. `docs/04-database-design.md`
4. `docs/05-api-design.md`
5. `docs/06-mvp-roadmap.md`

If code conflicts with docs, prefer docs unless explicitly instructed otherwise.