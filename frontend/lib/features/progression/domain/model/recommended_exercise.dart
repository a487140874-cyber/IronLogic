/// 推荐动作模型。
///
/// 该类属于 `progression/domain`，表示当前推荐 SessionTemplate 中的单个推荐动作。
class RecommendedExercise {
  /// 创建推荐动作模型。
  const RecommendedExercise({
    required this.id,
    required this.exerciseId,
    required this.exerciseName,
    this.orderNo,
    this.targetSets,
    this.targetReps,
    this.targetWeight,
    this.targetWeightUnit,
    this.restSeconds,
    this.intensityMode,
    this.prescriptionJson,
  });

  /// 模板动作 id。
  final int id;

  /// Exercise id。
  final int exerciseId;

  /// 动作名称。
  final String exerciseName;

  /// 顺序号。
  final int? orderNo;

  /// 目标组数。
  final int? targetSets;

  /// 目标次数。
  final int? targetReps;

  /// 目标重量。
  final double? targetWeight;

  /// 重量单位。
  final String? targetWeightUnit;

  /// 休息秒数。
  final int? restSeconds;

  /// 强度模式。
  final String? intensityMode;

  /// 预留处方 JSON。
  final String? prescriptionJson;
}
