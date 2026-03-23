import '../../domain/model/recommended_exercise.dart';

/// 推荐动作 DTO。
///
/// 该类属于 `progression/data`，负责解析 `RecommendedExerciseResponse`。
class RecommendedExerciseDto {
  /// 创建推荐动作 DTO。
  const RecommendedExerciseDto({
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

  /// 从 JSON 构建 DTO。
  factory RecommendedExerciseDto.fromJson(Map<String, dynamic> json) {
    return RecommendedExerciseDto(
      id: (json['id'] as num?)?.toInt() ?? 0,
      exerciseId: (json['exerciseId'] as num?)?.toInt() ?? 0,
      exerciseName: json['exerciseName']?.toString() ?? '',
      orderNo: (json['orderNo'] as num?)?.toInt(),
      targetSets: (json['targetSets'] as num?)?.toInt(),
      targetReps: (json['targetReps'] as num?)?.toInt(),
      targetWeight: (json['targetWeight'] as num?)?.toDouble(),
      targetWeightUnit: json['targetWeightUnit']?.toString(),
      restSeconds: (json['restSeconds'] as num?)?.toInt(),
      intensityMode: json['intensityMode']?.toString(),
      prescriptionJson: json['prescriptionJson']?.toString(),
    );
  }

  final int id;
  final int exerciseId;
  final String exerciseName;
  final int? orderNo;
  final int? targetSets;
  final int? targetReps;
  final double? targetWeight;
  final String? targetWeightUnit;
  final int? restSeconds;
  final String? intensityMode;
  final String? prescriptionJson;

  /// 转为领域模型。
  RecommendedExercise toDomain() {
    return RecommendedExercise(
      id: id,
      exerciseId: exerciseId,
      exerciseName: exerciseName,
      orderNo: orderNo,
      targetSets: targetSets,
      targetReps: targetReps,
      targetWeight: targetWeight,
      targetWeightUnit: targetWeightUnit,
      restSeconds: restSeconds,
      intensityMode: intensityMode,
      prescriptionJson: prescriptionJson,
    );
  }
}
