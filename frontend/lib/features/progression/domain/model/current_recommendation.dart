import 'recommended_session_template.dart';

/// 当前推荐训练模型。
///
/// 该类属于 `progression/domain`，表示某个 Program 在当前序列位置上的推荐结果。
class CurrentRecommendation {
  /// 创建当前推荐模型。
  const CurrentRecommendation({
    required this.programId,
    this.currentBlockId,
    this.nextSessionTemplateId,
    this.sequenceCursor,
    this.recommendedSessionTemplate,
  });

  /// Program id。
  final int programId;

  /// 当前 Block id。
  final int? currentBlockId;

  /// 下一个 SessionTemplate id。
  final int? nextSessionTemplateId;

  /// 序列游标。
  final int? sequenceCursor;

  /// 推荐 SessionTemplate。
  final RecommendedSessionTemplate? recommendedSessionTemplate;
}
