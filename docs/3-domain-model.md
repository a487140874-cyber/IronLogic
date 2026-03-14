# 领域模型设计

## 核心实体

### Program
训练计划。

### ProgramBlock
训练阶段，例如增肌期、增力期。

### SessionTemplate
训练日模板，例如 Push Day、Leg Day。

### SessionExerciseTemplate
训练日中的动作模板，包含组数、次数、重量、休息时间等。

### WorkoutSession
一次实际训练记录。

### WorkoutExercise
一次训练中的某个动作。

### WorkoutSet
动作中的单组记录。

### Exercise
动作定义，可为系统动作或用户自定义动作。

### ProgressionRule
渐进规则。

### ProgramProgress
当前训练计划推进状态。

## 核心关系
- 一个 Program 包含多个 ProgramBlock
- 一个 ProgramBlock 包含多个 SessionTemplate
- 一个 SessionTemplate 包含多个 SessionExerciseTemplate
- 一个 WorkoutSession 包含多个 WorkoutExercise
- 一个 WorkoutExercise 包含多个 WorkoutSet
- ProgramProgress 用于记录当前推荐训练状态