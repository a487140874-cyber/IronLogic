import '../../domain/model/workout_exercise.dart';
import 'workout_set_dto.dart';

/// WorkoutExercise DTO。
///
/// 该类属于 `workout/data`，用于和后端 `WorkoutExerciseResponse` 对齐。
class WorkoutExerciseDto {
  /// 创建 DTO。
  const WorkoutExerciseDto({
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

  /// 从 JSON 构建 DTO。
  factory WorkoutExerciseDto.fromJson(Map<String, dynamic> json) {
    final List<dynamic> rawSets = json['sets'] as List<dynamic>? ?? <dynamic>[];
    return WorkoutExerciseDto(
      id: (json['id'] as num?)?.toInt() ?? 0,
      workoutSessionId: (json['workoutSessionId'] as num?)?.toInt() ?? 0,
      exerciseId: (json['exerciseId'] as num?)?.toInt() ?? 0,
      sourceTemplateExerciseId:
          (json['sourceTemplateExerciseId'] as num?)?.toInt(),
      actualOrderNo: (json['actualOrderNo'] as num?)?.toInt() ?? 0,
      replacementOfExerciseId:
          (json['replacementOfExerciseId'] as num?)?.toInt(),
      notes: json['notes']?.toString(),
      createdAt: DateTime.tryParse(json['createdAt']?.toString() ?? ''),
      updatedAt: DateTime.tryParse(json['updatedAt']?.toString() ?? ''),
      sets: rawSets
          .map((dynamic item) => WorkoutSetDto.fromJson(item as Map<String, dynamic>))
          .toList(),
    );
  }

  final int id;
  final int workoutSessionId;
  final int exerciseId;
  final int? sourceTemplateExerciseId;
  final int actualOrderNo;
  final int? replacementOfExerciseId;
  final String? notes;
  final DateTime? createdAt;
  final DateTime? updatedAt;
  final List<WorkoutSetDto> sets;

  /// 转为领域模型。
  WorkoutExercise toDomain() {
    return WorkoutExercise(
      id: id,
      workoutSessionId: workoutSessionId,
      exerciseId: exerciseId,
      sourceTemplateExerciseId: sourceTemplateExerciseId,
      actualOrderNo: actualOrderNo,
      replacementOfExerciseId: replacementOfExerciseId,
      notes: notes,
      createdAt: createdAt,
      updatedAt: updatedAt,
      sets: sets.map((WorkoutSetDto item) => item.toDomain()).toList(),
    );
  }
}
