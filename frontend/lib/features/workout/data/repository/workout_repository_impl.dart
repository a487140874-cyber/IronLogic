import '../../domain/model/workout_detail.dart';
import '../../domain/model/workout_history_item.dart';
import '../../domain/model/workout_set.dart';
import '../../domain/model/workout_set_input.dart';
import '../../domain/repository/workout_repository.dart';
import '../api/workout_api.dart';
import '../dto/save_workout_sets_request_dto.dart';
import '../dto/workout_detail_dto.dart';
import '../dto/workout_history_item_dto.dart';
import '../dto/workout_set_dto.dart';

/// workout 仓储实现。
///
/// 该类属于 `workout/data/repository`，负责把训练接口返回的 DTO 转为领域模型。
class WorkoutRepositoryImpl implements WorkoutRepository {
  /// 创建仓储实现。
  const WorkoutRepositoryImpl(this._api);

  final WorkoutApi _api;

  @override
  Future<WorkoutDetail> startWorkoutFromTemplate(int templateId) async {
    final WorkoutDetailDto dto = await _api.startWorkoutFromTemplate(templateId);
    return dto.toDomain();
  }

  @override
  Future<WorkoutDetail> getWorkoutDetail(int workoutId) async {
    final WorkoutDetailDto dto = await _api.getWorkoutDetail(workoutId);
    return dto.toDomain();
  }

  @override
  Future<List<WorkoutSet>> saveWorkoutSets(
    int workoutExerciseId,
    List<WorkoutSetInput> sets,
  ) async {
    final SaveWorkoutSetsRequestDto request =
        SaveWorkoutSetsRequestDto.fromInputs(sets);
    final List<WorkoutSetDto> items = await _api.saveWorkoutSets(
      workoutExerciseId,
      request,
    );
    return items.map((WorkoutSetDto item) => item.toDomain()).toList();
  }

  @override
  Future<WorkoutDetail> finishWorkout(int workoutId) async {
    final WorkoutDetailDto dto = await _api.finishWorkout(workoutId);
    return dto.toDomain();
  }

  @override
  Future<List<WorkoutHistoryItem>> listWorkoutHistory() async {
    final List<WorkoutHistoryItemDto> items = await _api.listWorkoutHistory();
    return items.map((WorkoutHistoryItemDto item) => item.toDomain()).toList();
  }
}
