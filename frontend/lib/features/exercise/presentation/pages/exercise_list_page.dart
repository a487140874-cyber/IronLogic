import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:go_router/go_router.dart';

import '../../../../app/router.dart';
import '../../../../shared/widgets/empty_view.dart';
import '../../../../shared/widgets/error_view.dart';
import '../../../../shared/widgets/ironlogic_scaffold.dart';
import '../../../../shared/widgets/loading_view.dart';
import '../../application/exercise_providers.dart';
import '../../domain/model/exercise.dart';

/// 动作列表页。
///
/// 该页面属于 `exercise/presentation`，负责展示系统动作与自定义动作的混合列表。
/// 本轮先只做浏览和跳转创建，不加入搜索、筛选和分页，确保 MVP 先闭环。
class ExerciseListPage extends ConsumerWidget {
  /// 创建动作列表页。
  const ExerciseListPage({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final AsyncValue<List<Exercise>> exercisesAsync = ref.watch(exerciseListProvider);

    return IronLogicScaffold(
      title: '动作列表',
      actions: <Widget>[
        IconButton(
          tooltip: '刷新',
          onPressed: () => ref.invalidate(exerciseListProvider),
          icon: const Icon(Icons.refresh),
        ),
      ],
      floatingActionButton: FloatingActionButton.extended(
        onPressed: () => context.go(AppRoutes.exerciseCreate),
        icon: const Icon(Icons.add),
        label: const Text('创建动作'),
      ),
      body: exercisesAsync.when(
        loading: () => const LoadingView(message: '正在加载动作列表...'),
        error: (Object error, StackTrace stackTrace) {
          return ErrorView(
            message: error.toString(),
            onRetry: () => ref.invalidate(exerciseListProvider),
          );
        },
        data: (List<Exercise> exercises) {
          if (exercises.isEmpty) {
            return EmptyView(
              message: '当前还没有可用动作，请先创建一个动作。',
              actionLabel: '创建动作',
              onAction: () => context.go(AppRoutes.exerciseCreate),
            );
          }

          return ListView.separated(
            padding: const EdgeInsets.all(16),
            itemCount: exercises.length,
            separatorBuilder: (_, _) => const SizedBox(height: 12),
            itemBuilder: (BuildContext context, int index) {
              final Exercise exercise = exercises[index];
              final bool isCustom = exercise.isCustom ?? false;
              return Card(
                child: ListTile(
                  leading: CircleAvatar(
                    child: Icon(isCustom ? Icons.person : Icons.fitness_center),
                  ),
                  title: Text(exercise.name),
                  subtitle: Text(
                    [
                      exercise.category ?? '未分类',
                      exercise.primaryMuscle ?? '未设置肌群',
                      exercise.equipmentType ?? '未设置器械',
                    ].join(' · '),
                  ),
                  trailing: Text(isCustom ? '自定义' : '系统'),
                ),
              );
            },
          );
        },
      ),
    );
  }
}
