import '../api/exercise_api.dart';
import '../dto/create_exercise_request_dto.dart';
import '../dto/exercise_dto.dart';
import '../../domain/model/create_exercise_input.dart';
import '../../domain/model/exercise.dart';
import '../../domain/repository/exercise_repository.dart';

/// Exercise 仓储实现。
///
/// 该类属于 `exercise/data/repository`，负责把 API DTO 转成页面使用的领域模型。
/// 这样页面只依赖稳定的 domain 对象，而不是直接处理后端返回结构。
class ExerciseRepositoryImpl implements ExerciseRepository {
  /// 创建仓储实现。
  const ExerciseRepositoryImpl(this._api);

  final ExerciseApi _api;

  @override
  Future<List<Exercise>> listExercises() async {
    final List<ExerciseDto> items = await _api.listExercises();
    return items.map((ExerciseDto item) => item.toDomain()).toList();
  }

  @override
  Future<Exercise> createExercise(CreateExerciseInput input) async {
    final CreateExerciseRequestDto request =
        CreateExerciseRequestDto.fromInput(input);
    final ExerciseDto dto = await _api.createExercise(request);
    return dto.toDomain();
  }
}
