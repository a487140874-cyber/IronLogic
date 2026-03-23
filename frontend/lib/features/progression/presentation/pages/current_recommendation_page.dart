import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:go_router/go_router.dart';

import '../../../../app/router.dart';
import '../../../../shared/widgets/empty_view.dart';
import '../../../../shared/widgets/error_view.dart';
import '../../../../shared/widgets/ironlogic_scaffold.dart';
import '../../../../shared/widgets/loading_view.dart';
import '../../../program/application/program_providers.dart';
import '../../application/progression_providers.dart';
import '../../domain/model/recommended_exercise.dart';

/// 当前推荐训练页。
///
/// 该页面属于 `progression/presentation`，是本轮首页主入口。
/// 页面先取 Program 列表，再获取当前推荐训练。这样拆分是因为后端推荐接口需要显式的 Program id。
class CurrentRecommendationPage extends ConsumerWidget {
  /// 创建当前推荐训练页。
  const CurrentRecommendationPage({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final AsyncValue<HomeRecommendationState> recommendationAsync =
        ref.watch(homeRecommendationProvider);

    return IronLogicScaffold(
      title: '当前推荐训练',
      actions: <Widget>[
        IconButton(
          tooltip: '刷新',
          onPressed: () {
            ref.invalidate(programListProvider);
            ref.invalidate(homeRecommendationProvider);
          },
          icon: const Icon(Icons.refresh),
        ),
      ],
      body: recommendationAsync.when(
        loading: () => const LoadingView(message: '正在加载当前推荐训练...'),
        error: (Object error, StackTrace stackTrace) {
          return ErrorView(
            message: error.toString(),
            onRetry: () {
              ref.invalidate(programListProvider);
              ref.invalidate(homeRecommendationProvider);
            },
          );
        },
        data: (HomeRecommendationState state) {
          if (state.hasNoProgram) {
            return EmptyView(
              message: '当前还没有 Program，无法生成推荐训练。',
              actionLabel: '去创建 Program',
              onAction: () => context.go(AppRoutes.programCreate),
            );
          }

          if (state.hasNoRecommendation) {
            return const EmptyView(
              message: '当前 Program 还没有可展示的推荐训练。',
            );
          }

          final recommendation = state.recommendation!;
          final sessionTemplate = recommendation.recommendedSessionTemplate!;

          return ListView(
            padding: const EdgeInsets.all(16),
            children: <Widget>[
              Card(
                child: Padding(
                  padding: const EdgeInsets.all(16),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: <Widget>[
                      Text(
                        state.selectedProgram!.name,
                        style: Theme.of(context).textTheme.titleLarge,
                      ),
                      const SizedBox(height: 8),
                      Text('目标类型：${state.selectedProgram!.goalType}'),
                      Text('状态：${state.selectedProgram!.status}'),
                      const SizedBox(height: 12),
                      Text(
                        '当前推荐模板：${sessionTemplate.name}',
                        style: Theme.of(context).textTheme.titleMedium,
                      ),
                      const SizedBox(height: 4),
                      Text('序列游标：${recommendation.sequenceCursor ?? 0}'),
                    ],
                  ),
                ),
              ),
              const SizedBox(height: 16),
              Text(
                '推荐动作',
                style: Theme.of(context).textTheme.titleMedium,
              ),
              const SizedBox(height: 12),
              ...sessionTemplate.exercises.map(
                (RecommendedExercise exercise) => Card(
                  child: ListTile(
                    title: Text(exercise.exerciseName),
                    subtitle: Text(
                      '组数：${exercise.targetSets ?? '-'} · 次数：${exercise.targetReps ?? '-'} · '
                      '休息：${exercise.restSeconds ?? '-'} 秒',
                    ),
                  ),
                ),
              ),
              if (sessionTemplate.exercises.isEmpty)
                const Padding(
                  padding: EdgeInsets.only(top: 8),
                  child: Text('当前推荐模板下没有动作。'),
                ),
            ],
          );
        },
      ),
    );
  }
}
