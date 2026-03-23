import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:go_router/go_router.dart';

import '../../../../app/router.dart';
import '../../../../core/utils/date_utils.dart';
import '../../../../shared/widgets/empty_view.dart';
import '../../../../shared/widgets/error_view.dart';
import '../../../../shared/widgets/ironlogic_scaffold.dart';
import '../../../../shared/widgets/loading_view.dart';
import '../../application/program_providers.dart';
import '../../domain/model/program.dart';

/// Program 列表页。
///
/// 该页面属于 `program/presentation`，负责展示当前用户的训练计划列表。
/// 当前列表只显示本轮创建和推荐页需要的核心信息，不扩展详情和编辑能力。
class ProgramListPage extends ConsumerWidget {
  /// 创建 Program 列表页。
  const ProgramListPage({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final AsyncValue<List<Program>> programsAsync = ref.watch(programListProvider);

    return IronLogicScaffold(
      title: 'Program 列表',
      actions: <Widget>[
        IconButton(
          tooltip: '刷新',
          onPressed: () => ref.invalidate(programListProvider),
          icon: const Icon(Icons.refresh),
        ),
      ],
      floatingActionButton: FloatingActionButton.extended(
        onPressed: () => context.go(AppRoutes.programCreate),
        icon: const Icon(Icons.add),
        label: const Text('创建 Program'),
      ),
      body: programsAsync.when(
        loading: () => const LoadingView(message: '正在加载 Program 列表...'),
        error: (Object error, StackTrace stackTrace) {
          return ErrorView(
            message: error.toString(),
            onRetry: () => ref.invalidate(programListProvider),
          );
        },
        data: (List<Program> programs) {
          if (programs.isEmpty) {
            return EmptyView(
              message: '当前还没有 Program，请先创建一个训练计划。',
              actionLabel: '创建 Program',
              onAction: () => context.go(AppRoutes.programCreate),
            );
          }

          return ListView.separated(
            padding: const EdgeInsets.all(16),
            itemCount: programs.length,
            separatorBuilder: (_, _) => const SizedBox(height: 12),
            itemBuilder: (BuildContext context, int index) {
              final Program program = programs[index];
              return Card(
                child: ListTile(
                  title: Text(program.name),
                  subtitle: Text(
                    '目标：${program.goalType} · 状态：${program.status}\n'
                    '开始：${AppDateUtils.formatDateLabel(program.startDate)} · '
                    '结束：${AppDateUtils.formatDateLabel(program.endDate)}',
                  ),
                  isThreeLine: true,
                ),
              );
            },
          );
        },
      ),
    );
  }
}
