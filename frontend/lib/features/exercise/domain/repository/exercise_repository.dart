import '../model/create_exercise_input.dart';
import '../model/exercise.dart';

/// Exercise 仓储抽象。
///
/// 该接口属于 `exercise/domain`，用于定义页面真正关心的数据能力。
/// repository 抽象存在的意义是隔离数据来源，避免页面直接耦合 Dio 与 JSON 结构。
abstract class ExerciseRepository {
  /// 查询当前用户可见的动作列表。
  Future<List<Exercise>> listExercises();

  /// 创建一个自定义动作。
  Future<Exercise> createExercise(CreateExerciseInput input);
}
