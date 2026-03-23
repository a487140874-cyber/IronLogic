import '../model/workout_detail.dart';
import '../model/workout_history_item.dart';
import '../model/workout_set.dart';
import '../model/workout_set_input.dart';

/// workout 仓储抽象。
///
/// 该接口属于 `workout/domain`，统一定义训练执行链路所需的数据能力。
abstract class WorkoutRepository {
  /// 从模板开始训练。
  Future<WorkoutDetail> startWorkoutFromTemplate(int templateId);

  /// 查询训练详情。
  Future<WorkoutDetail> getWorkoutDetail(int workoutId);

  /// 覆盖保存某个训练动作下的整组数据。
  Future<List<WorkoutSet>> saveWorkoutSets(
    int workoutExerciseId,
    List<WorkoutSetInput> sets,
  );

  /// 完成训练。
  Future<WorkoutDetail> finishWorkout(int workoutId);

  /// 查询训练历史。
  Future<List<WorkoutHistoryItem>> listWorkoutHistory();
}
