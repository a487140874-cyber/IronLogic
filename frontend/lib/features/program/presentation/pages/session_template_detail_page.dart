import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:go_router/go_router.dart';

import '../../../../app/router.dart';
import '../../../../shared/widgets/error_view.dart';
import '../../../../shared/widgets/ironlogic_scaffold.dart';
import '../../../../shared/widgets/loading_view.dart';
import '../../../exercise/application/exercise_providers.dart';
import '../../../exercise/domain/model/exercise.dart';
import '../../../workout/application/workout_providers.dart';
import '../../application/program_providers.dart';
import '../../domain/model/session_exercise_template.dart';
import '../../domain/model/session_template.dart';
import '../widgets/session_exercise_template_list_section.dart';

/// SessionTemplate 详情页。
///
/// 该页面属于 `program/presentation`，负责展示模板基础信息、模板动作列表，并提供开始训练入口。
/// 当前后端没有模板详情单独接口，因此页面依赖路由传入的模板信息；若缺失，则回退为最小展示。
class SessionTemplateDetailPage extends ConsumerWidget {
  /// 创建页面。
  const SessionTemplateDetailPage({
    required this.templateId,
    this.template,
    super.key,
  });

  /// 模板 id。
  final int templateId;

  /// 可选模板对象。
  final SessionTemplate? template;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final AsyncValue<List<SessionExerciseTemplate>> exercisesAsync = ref.watch(
      sessionTemplateExercisesProvider(templateId),
    );
    final AsyncValue<List<Exercise>> catalogAsync = ref.watch(exerciseListProvider);
    final AsyncValue<void> startState = ref.watch(
      startWorkoutControllerProvider(templateId),
    );

    return IronLogicScaffold(
      title: 'SessionTemplate 详情',
      actions: <Widget>[
        IconButton(
          tooltip: '刷新',
          onPressed: () {
            ref.invalidate(sessionTemplateExercisesProvider(templateId));
            ref.invalidate(exerciseListProvider);
          },
          icon: const Icon(Icons.refresh),
        ),
      ],
      floatingActionButton: FloatingActionButton.extended(
        onPressed: () => context.go(AppRoutes.sessionTemplateExerciseCreate(templateId)),
        icon: const Icon(Icons.add),
        label: const Text('添加模板动作'),
      ),
      body: ListView(
        padding: const EdgeInsets.all(16),
        children: <Widget>[
          Card(
            child: Padding(
              padding: const EdgeInsets.all(16),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: <Widget>[
                  Text(
                    template?.name ?? 'SessionTemplate #$templateId',
                    style: Theme.of(context).textTheme.titleLarge,
                  ),
                  const SizedBox(height: 8),
                  Text('模板 ID：$templateId'),
                  Text('顺序：${template?.sequenceNo ?? '未提供'}'),
                  Text('触发模式：${template?.triggerMode ?? '未提供'}'),
                  const SizedBox(height: 12),
                  SizedBox(
                    width: double.infinity,
                    child: FilledButton.icon(
                      onPressed: startState.isLoading
                          ? null
                          : () => _startWorkout(context, ref),
                      icon: const Icon(Icons.play_arrow),
                      label: Text(startState.isLoading ? '启动中...' : '从模板开始训练'),
                    ),
                  ),
                ],
              ),
            ),
          ),
          const SizedBox(height: 16),
          Text('模板动作列表', style: Theme.of(context).textTheme.titleMedium),
          const SizedBox(height: 12),
          catalogAsync.when(
            loading: () => const LoadingView(message: '正在加载动作目录...'),
            error: (Object error, StackTrace stackTrace) => ErrorView(
              message: error.toString(),
              onRetry: () => ref.invalidate(exerciseListProvider),
            ),
            data: (List<Exercise> catalog) {
              final Map<int, String> exerciseNames = <int, String>{
                for (final Exercise item in catalog) item.id: item.name,
              };
              return exercisesAsync.when(
                loading: () => const LoadingView(message: '正在加载模板动作列表...'),
                error: (Object error, StackTrace stackTrace) => ErrorView(
                  message: error.toString(),
                  onRetry: () => ref.invalidate(
                    sessionTemplateExercisesProvider(templateId),
                  ),
                ),
                data: (List<SessionExerciseTemplate> items) {
                  return SessionExerciseTemplateListSection(
                    templateId: templateId,
                    items: items,
                    exerciseNameOf: (int exerciseId) =>
                        exerciseNames[exerciseId] ?? 'Exercise #$exerciseId',
                  );
                },
              );
            },
          ),
        ],
      ),
    );
  }

  /// 从模板开始训练并跳转到训练详情页。
  Future<void> _startWorkout(BuildContext context, WidgetRef ref) async {
    final StartWorkoutController controller = ref.read(
      startWorkoutControllerProvider(templateId).notifier,
    );
    try {
      final detail = await controller.start();
      if (!context.mounted) {
        return;
      }
      context.go(AppRoutes.workoutDetail(detail.id));
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
