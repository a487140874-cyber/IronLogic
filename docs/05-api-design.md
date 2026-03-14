# API 设计

## 用户
- POST /api/users/register
- POST /api/users/login
- GET /api/users/me

## 动作库
- GET /api/exercises
- POST /api/exercises
- PUT /api/exercises/{id}
- GET /api/exercises/{id}

## 训练计划
- POST /api/programs
- GET /api/programs
- GET /api/programs/{id}
- PUT /api/programs/{id}

## Block
- POST /api/programs/{id}/blocks
- PUT /api/blocks/{id}

## SessionTemplate
- POST /api/blocks/{id}/session-templates
- PUT /api/session-templates/{id}

## Workout
- POST /api/workouts/from-template/{templateId}
- POST /api/workouts/manual
- POST /api/workouts/{id}/finish
- GET /api/workouts/history
- GET /api/workouts/current-recommendation

## Stats
- GET /api/stats/overview
- GET /api/stats/prs