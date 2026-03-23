import 'package:flutter_riverpod/flutter_riverpod.dart';

import '../../program/application/program_providers.dart';
import '../../program/domain/model/program.dart';
import '../data/api/progression_api.dart';
import '../data/repository/progression_repository_impl.dart';
import '../domain/model/current_recommendation.dart';
import '../domain/repository/progression_repository.dart';

/// progression 仓储 Provider。
final progressionRepositoryProvider = Provider<ProgressionRepository>((ref) {
  return ProgressionRepositoryImpl(ref.watch(progressionApiProvider));
});

/// 首页推荐页的组合状态。
///
/// 首页需要先拿 Program 列表，再根据选中的 Program 查询推荐训练。
/// 因此这里单独定义一个状态对象，把“当前选中的 Program”和“推荐结果”一起交给页面。
class HomeRecommendationState {
  /// 创建首页推荐状态。
  const HomeRecommendationState({
    this.selectedProgram,
    this.recommendation,
  });

  final Program? selectedProgram;
  final CurrentRecommendation? recommendation;

  /// 是否没有任何 Program。
  bool get hasNoProgram => selectedProgram == null;

  /// 是否没有推荐模板。
  bool get hasNoRecommendation => recommendation?.recommendedSessionTemplate == null;
}

/// 首页推荐数据 Provider。
///
/// 这里在 application 层串起 Program 与 progression 两个模块，
/// 因为“选哪个 Program 去请求推荐”属于页面编排逻辑，而不是纯领域规则。
final homeRecommendationProvider =
    FutureProvider<HomeRecommendationState>((ref) async {
  final List<Program> programs = await ref.watch(programListProvider.future);
  if (programs.isEmpty) {
    return const HomeRecommendationState();
  }

  final Program selectedProgram = programs.firstWhere(
    (Program program) => program.status.toUpperCase() == 'ACTIVE',
    orElse: () => programs.first,
  );

  final CurrentRecommendation? recommendation = await ref
      .watch(progressionRepositoryProvider)
      .getCurrentRecommendation(selectedProgram.id);

  return HomeRecommendationState(
    selectedProgram: selectedProgram,
    recommendation: recommendation,
  );
});
