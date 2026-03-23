import 'workout_set.dart';

/// WorkoutExercise 领域模型。
///
/// 该类属于 `workout/domain`，表示一次训练中的一个动作记录。
class WorkoutExercise {
  /// 创建 WorkoutExercise 模型。
  const WorkoutExercise({
    required this.id,
    required this.workoutSessionId,
    required this.exerciseId,
    required this.actualOrderNo,
    required this.sets,
    this.sourceTemplateExerciseId,
    this.replacementOfExerciseId,
    this.notes,
    this.createdAt,
    this.updatedAt,
  });

  /// 动作记录 id。
  final int id;

  /// 所属训练 id。
  final int workoutSessionId;

  /// 引用的 Exercise id。
  final int exerciseId;

  /// 来源模板动作 id。
  final int? sourceTemplateExerciseId;

  /// 实际顺序号。
  final int actualOrderNo;

  /// 替换来源动作 id。
  final int? replacementOfExerciseId;

  /// 备注。
  final String? notes;

  /// 创建时间。
  final DateTime? createdAt;

  /// 更新时间。
  final DateTime? updatedAt;

  /// 组列表。
  final List<WorkoutSet> sets;
}
