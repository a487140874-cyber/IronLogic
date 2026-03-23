import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:go_router/go_router.dart';

import '../../../../app/router.dart';
import '../../../../shared/widgets/empty_view.dart';
import '../../../../shared/widgets/error_view.dart';
import '../../../../shared/widgets/ironlogic_scaffold.dart';
import '../../../../shared/widgets/loading_view.dart';
import '../../../exercise/application/exercise_providers.dart';
import '../../../exercise/domain/model/exercise.dart';
import '../../application/workout_providers.dart';
import '../../domain/model/workout_detail.dart';
import '../../domain/model/workout_exercise.dart';
import '../widgets/workout_exercise_card.dart';

/// 训练详情页。
///
/// 该页面属于 `workout/presentation`，负责展示训练基本信息、动作列表、组数据编辑入口，以及完成训练按钮。
class WorkoutDetailPage extends ConsumerWidget {
  /// 创建训练详情页。
  const WorkoutDetailPage({
    required this.workoutId,
    super.key,
  });

  /// 训练 id。
  final int workoutId;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final AsyncValue<WorkoutDetail> detailAsync = ref.watch(
      workoutDetailProvider(workoutId),
    );
    final AsyncValue<List<Exercise>> catalogAsync = ref.watch(exerciseListProvider);
    final AsyncValue<void> finishState = ref.watch(
      finishWorkoutControllerProvider(workoutId),
    );

    return IronLogicScaffold(
      title: '训练详情',
      actions: <Widget>[
        IconButton(
          tooltip: '刷新',
          onPressed: () {
            ref.invalidate(workoutDetailProvider(workoutId));
            ref.invalidate(exerciseListProvider);
          },
          icon: const Icon(Icons.refresh),
        ),
      ],
      body: detailAsync.when(
        loading: () => const LoadingView(message: '正在加载训练详情...'),
        error: (Object error, StackTrace stackTrace) {
          return ErrorView(
            message: error.toString(),
            onRetry: () => ref.invalidate(workoutDetailProvider(workoutId)),
          );
        },
        data: (WorkoutDetail detail) {
          return catalogAsync.when(
            loading: () => const LoadingView(message: '正在加载动作目录...'),
            error: (Object error, StackTrace stackTrace) {
              return ErrorView(
                message: error.toString(),
                onRetry: () => ref.invalidate(exerciseListProvider),
              );
            },
            data: (List<Exercise> catalog) {
              final Map<int, String> exerciseNames = <int, String>{
                for (final Exercise item in catalog) item.id: item.name,
              };
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
                            '训练 #${detail.id}',
                            style: Theme.of(context).textTheme.titleLarge,
                          ),
                          const SizedBox(height: 8),
                          Text('来源：${detail.sourceType}'),
                          Text('状态：${detail.status}'),
                          Text('开始时间：${detail.startedAt ?? '未开始'}'),
                          Text('结束时间：${detail.endedAt ?? '未结束'}'),
                          const SizedBox(height: 12),
                          SizedBox(
                            width: double.infinity,
                            child: FilledButton(
                              onPressed: finishState.isLoading
                                  ? null
                                  : () => _finishWorkout(context, ref),
                              child: Text(finishState.isLoading ? '提交中...' : '完成训练'),
                            ),
                          ),
                        ],
                      ),
                    ),
                  ),
                  const SizedBox(height: 16),
                  Text('训练动作', style: Theme.of(context).textTheme.titleMedium),
                  const SizedBox(height: 12),
                  if (detail.exercises.isEmpty)
                    const EmptyView(message: '当前训练下还没有动作。')
                  else
                    ...detail.exercises.map(
                      (WorkoutExercise exercise) => Padding(
                        padding: const EdgeInsets.only(bottom: 12),
                        child: WorkoutExerciseCard(
                          exercise: exercise,
                          exerciseName:
                              exerciseNames[exercise.exerciseId] ??
                              'Exercise #${exercise.exerciseId}',
                          onSaved: () => ref.invalidate(workoutDetailProvider(workoutId)),
                        ),
                      ),
                    ),
                ],
              );
            },
          );
        },
      ),
    );
  }

  /// 完成训练并跳转到历史页。
  ///
  /// 这里选择跳转到历史页，是因为完成训练后用户更自然地会想确认记录已经落库，
  /// 同时也能从历史页回看刚完成的训练。
  Future<void> _finishWorkout(BuildContext context, WidgetRef ref) async {
    final FinishWorkoutController controller = ref.read(
      finishWorkoutControllerProvider(workoutId).notifier,
    );
    try {
      await controller.finish();
      if (!context.mounted) {
        return;
      }
      context.go(AppRoutes.workoutHistory);
    } catch (error) {
      if (!context.mounted) {
        return;
      }
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text(error.toString())),
      );
    }
  }
}
