import '../dto/current_recommendation_dto.dart';
import '../../domain/model/current_recommendation.dart';
import '../../domain/repository/progression_repository.dart';
import '../api/progression_api.dart';

/// progression 仓储实现。
///
/// 该类属于 `progression/data/repository`，负责把推荐接口的 DTO 转换成首页可使用的领域对象。
class ProgressionRepositoryImpl implements ProgressionRepository {
  /// 创建 progression 仓储实现。
  const ProgressionRepositoryImpl(this._api);

  final ProgressionApi _api;

  @override
  Future<CurrentRecommendation?> getCurrentRecommendation(int programId) async {
    final CurrentRecommendationDto? dto =
        await _api.getCurrentRecommendation(programId);
    return dto?.toDomain();
  }
}
