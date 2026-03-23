import '../../domain/model/workout_set.dart';

/// WorkoutSet DTO。
///
/// 该类属于 `workout/data`，用于和后端 `WorkoutSetResponse` 对齐。
class WorkoutSetDto {
  /// 创建 DTO。
  const WorkoutSetDto({
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

  /// 从 JSON 构建 DTO。
  factory WorkoutSetDto.fromJson(Map<String, dynamic> json) {
    return WorkoutSetDto(
      id: (json['id'] as num?)?.toInt() ?? 0,
      workoutExerciseId: (json['workoutExerciseId'] as num?)?.toInt() ?? 0,
      setNo: (json['setNo'] as num?)?.toInt() ?? 0,
      weight: (json['weight'] as num?)?.toDouble(),
      reps: (json['reps'] as num?)?.toInt(),
      durationSeconds: (json['durationSeconds'] as num?)?.toInt(),
      restSeconds: (json['restSeconds'] as num?)?.toInt(),
      rpe: (json['rpe'] as num?)?.toDouble(),
      rir: (json['rir'] as num?)?.toInt(),
      isWarmup: json['isWarmup'] as bool?,
      isCompleted: json['isCompleted'] as bool?,
      createdAt: DateTime.tryParse(json['createdAt']?.toString() ?? ''),
      updatedAt: DateTime.tryParse(json['updatedAt']?.toString() ?? ''),
    );
  }

  final int id;
  final int workoutExerciseId;
  final int setNo;
  final double? weight;
  final int? reps;
  final int? durationSeconds;
  final int? restSeconds;
  final double? rpe;
  final int? rir;
  final bool? isWarmup;
  final bool? isCompleted;
  final DateTime? createdAt;
  final DateTime? updatedAt;

  /// 转为领域模型。
  WorkoutSet toDomain() {
    return WorkoutSet(
      id: id,
      workoutExerciseId: workoutExerciseId,
      setNo: setNo,
      weight: weight,
      reps: reps,
      durationSeconds: durationSeconds,
      restSeconds: restSeconds,
      rpe: rpe,
      rir: rir,
      isWarmup: isWarmup,
      isCompleted: isCompleted,
      createdAt: createdAt,
      updatedAt: updatedAt,
    );
  }
}
