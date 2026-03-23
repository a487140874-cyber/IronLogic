/// WorkoutSet 保存输入模型。
///
/// 该类属于 `workout/domain`，用于训练详情页把可编辑表单数据提交给仓储层。
class WorkoutSetInput {
  /// 创建组输入对象。
  const WorkoutSetInput({
    required this.setNo,
    this.weight,
    this.reps,
    this.durationSeconds,
    this.restSeconds,
    this.rpe,
    this.rir,
    this.isWarmup,
    this.isCompleted,
  });

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
}
