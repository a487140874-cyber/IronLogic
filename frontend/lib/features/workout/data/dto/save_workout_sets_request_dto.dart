import '../../domain/model/workout_set_input.dart';

/// 保存 Workout sets 的请求 DTO。
///
/// 该类属于 `workout/data`，用于映射后端“整列表覆盖保存”接口。
class SaveWorkoutSetsRequestDto {
  /// 创建请求 DTO。
  const SaveWorkoutSetsRequestDto({
    required this.sets,
  });

  /// 从领域输入构建请求 DTO。
  factory SaveWorkoutSetsRequestDto.fromInputs(List<WorkoutSetInput> inputs) {
    return SaveWorkoutSetsRequestDto(
      sets: inputs
          .map(
            (WorkoutSetInput input) => <String, dynamic>{
              'setNo': input.setNo,
              'weight': input.weight,
              'reps': input.reps,
              'durationSeconds': input.durationSeconds,
              'restSeconds': input.restSeconds,
              'rpe': input.rpe,
              'rir': input.rir,
              'isWarmup': input.isWarmup,
              'isCompleted': input.isCompleted,
            },
          )
          .toList(),
    );
  }

  /// 组列表 JSON。
  final List<Map<String, dynamic>> sets;

  /// 转为 JSON。
  Map<String, dynamic> toJson() {
    return <String, dynamic>{'sets': sets};
  }
}
