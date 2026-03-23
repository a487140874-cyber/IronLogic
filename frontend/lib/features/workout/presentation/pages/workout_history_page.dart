import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:go_router/go_router.dart';

import '../../../../app/router.dart';
import '../../../../shared/widgets/empty_view.dart';
import '../../../../shared/widgets/error_view.dart';
import '../../../../shared/widgets/ironlogic_scaffold.dart';
import '../../../../shared/widgets/loading_view.dart';
import '../../application/workout_providers.dart';
import '../../domain/model/workout_history_item.dart';

/// 训练历史页。
///
/// 该页面属于 `workout/presentation`，用于展示当前用户的历史训练列表。
/// 本轮先做简单列表，不加入筛选、分组和统计。
class WorkoutHistoryPage extends ConsumerWidget {
  /// 创建训练历史页。
  const WorkoutHistoryPage({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final AsyncValue<List<WorkoutHistoryItem>> historyAsync = ref.watch(
      workoutHistoryProvider,
    );

    return IronLogicScaffold(
      title: '训练历史',
      actions: <Widget>[
        IconButton(
          tooltip: '刷新',
          onPressed: () => ref.invalidate(workoutHistoryProvider),
          icon: const Icon(Icons.refresh),
        ),
      ],
      body: historyAsync.when(
        loading: () => const LoadingView(message: '正在加载训练历史...'),
        error: (Object error, StackTrace stackTrace) {
          return ErrorView(
            message: error.toString(),
            onRetry: () => ref.invalidate(workoutHistoryProvider),
          );
        },
        data: (List<WorkoutHistoryItem> items) {
          if (items.isEmpty) {
            return const EmptyView(message: '当前还没有训练历史记录。');
          }

          return ListView.separated(
            padding: const EdgeInsets.all(16),
            itemCount: items.length,
            separatorBuilder: (_, _) => const SizedBox(height: 12),
            itemBuilder: (BuildContext context, int index) {
              final WorkoutHistoryItem item = items[index];
              return Card(
                child: ListTile(
                  title: Text('训练 #${item.id}'),
                  subtitle: Text(
                    '来源：${item.sourceType} · 状态：${item.status}\n'
                    '开始：${item.startedAt ?? '未开始'}\n'
                    '结束：${item.endedAt ?? '未结束'}\n'
                    '备注：${item.notes?.isNotEmpty == true ? item.notes : '暂无'}',
                  ),
                  isThreeLine: true,
                  trailing: const Icon(Icons.chevron_right),
                  onTap: () => context.go(AppRoutes.workoutDetail(item.id)),
                ),
              );
            },
          );
        },
      ),
    );
  }
}
