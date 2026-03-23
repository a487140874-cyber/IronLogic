import '../../domain/model/current_recommendation.dart';
import 'recommended_session_template_dto.dart';

/// 当前推荐训练 DTO。
///
/// 该类属于 `progression/data`，负责解析 `CurrentRecommendationResponse`。
class CurrentRecommendationDto {
  /// 创建当前推荐 DTO。
  const CurrentRecommendationDto({
    required this.programId,
    this.currentBlockId,
    this.nextSessionTemplateId,
    this.sequenceCursor,
    this.recommendedSessionTemplate,
  });

  /// 从 JSON 构建 DTO。
  factory CurrentRecommendationDto.fromJson(Map<String, dynamic> json) {
    final Object? template = json['recommendedSessionTemplate'];
    return CurrentRecommendationDto(
      programId: (json['programId'] as num?)?.toInt() ?? 0,
      currentBlockId: (json['currentBlockId'] as num?)?.toInt(),
      nextSessionTemplateId: (json['nextSessionTemplateId'] as num?)?.toInt(),
      sequenceCursor: (json['sequenceCursor'] as num?)?.toInt(),
      recommendedSessionTemplate: template is Map<String, dynamic>
          ? RecommendedSessionTemplateDto.fromJson(template)
          : null,
    );
  }

  final int programId;
  final int? currentBlockId;
  final int? nextSessionTemplateId;
  final int? sequenceCursor;
  final RecommendedSessionTemplateDto? recommendedSessionTemplate;

  /// 转为领域模型。
  CurrentRecommendation toDomain() {
    return CurrentRecommendation(
      programId: programId,
      currentBlockId: currentBlockId,
      nextSessionTemplateId: nextSessionTemplateId,
      sequenceCursor: sequenceCursor,
      recommendedSessionTemplate: recommendedSessionTemplate?.toDomain(),
    );
  }
}
