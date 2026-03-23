import '../model/current_recommendation.dart';

/// progression 仓储抽象。
///
/// 该接口属于 `progression/domain`，定义首页需要的推荐训练查询能力。
abstract class ProgressionRepository {
  /// 根据 Program id 查询当前推荐训练。
  Future<CurrentRecommendation?> getCurrentRecommendation(int programId);
}
