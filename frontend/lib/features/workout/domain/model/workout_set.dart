/// WorkoutSet 领域模型。
///
/// 该类属于 `workout/domain`，表示某个 WorkoutExercise 下的一组记录。
/// 它和模板动作中的目标组不同，这里承载的是实际训练填写的数据。
class WorkoutSet {
  /// 创建 WorkoutSet 模型。
  const WorkoutSet({
    required this.id,
    required this.workoutExerciseId,
    required this.setNo,
    this.weight,
    this.reps,
    this.durationSeconds,
    this.restSeconds,
    this.rpe,
    this.rir,
    this.isWarmup,
    this.isCompleted,
    this.createdAt,
    this.updatedAt,
  });

  /// 组 id。
  final int id;

  /// 所属 WorkoutExercise id。
  final int workoutExerciseId;

  /// 组号。
  final int setNo;

  /// 重量。
  final double? weight;

  /// 次数。
  final int? reps;

  /// 持续秒数。
  final int? durationSeconds;

  /// 休息秒数。
  final int? restSeconds;

  /// RPE。
  final double? rpe;

  /// RIR。
  final int? rir;

  /// 是否热身组。
  final bool? isWarmup;

  /// 是否完成。
  final bool? isCompleted;

  /// 创建时间。
  final DateTime? createdAt;

  /// 更新时间。
  final DateTime? updatedAt;
}
