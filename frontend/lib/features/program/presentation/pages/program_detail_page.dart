import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:go_router/go_router.dart';

import '../../../../app/router.dart';
import '../../../../core/utils/date_utils.dart';
import '../../../../shared/widgets/error_view.dart';
import '../../../../shared/widgets/ironlogic_scaffold.dart';
import '../../../../shared/widgets/loading_view.dart';
import '../../application/program_providers.dart';
import '../../domain/model/program.dart';
import '../../domain/model/program_block.dart';
import '../widgets/block_list_section.dart';

/// Program 详情页。
///
/// 该页面属于 `program/presentation`，负责展示 Program 基础信息和 Block 列表。
/// 本轮先把“查看 Program -> 创建 Block -> 进入 Block”链路跑通，不提前扩展编辑能力。
class ProgramDetailPage extends ConsumerWidget {
  /// 创建 Program 详情页。
  const ProgramDetailPage({
    required this.programId,
    super.key,
  });

  /// Program id。
  final int programId;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final AsyncValue<Program> programAsync = ref.watch(programDetailProvider(programId));
    final AsyncValue<List<ProgramBlock>> blocksAsync = ref.watch(
      programBlocksProvider(programId),
    );

    return IronLogicScaffold(
      title: 'Program 详情',
      actions: <Widget>[
        IconButton(
          tooltip: '刷新',
          onPressed: () {
            ref.invalidate(programDetailProvider(programId));
            ref.invalidate(programBlocksProvider(programId));
          },
          icon: const Icon(Icons.refresh),
        ),
      ],
      floatingActionButton: FloatingActionButton.extended(
        onPressed: () => context.go(AppRoutes.programBlockCreate(programId)),
        icon: const Icon(Icons.add),
        label: const Text('创建 Block'),
      ),
      body: programAsync.when(
        loading: () => const LoadingView(message: '正在加载 Program 详情...'),
        error: (Object error, StackTrace stackTrace) {
          return ErrorView(
            message: error.toString(),
            onRetry: () {
              ref.invalidate(programDetailProvider(programId));
              ref.invalidate(programBlocksProvider(programId));
            },
          );
        },
        data: (Program program) {
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
                        program.name,
                        style: Theme.of(context).textTheme.titleLarge,
                      ),
                      const SizedBox(height: 8),
                      Text('目标类型：${program.goalType}'),
                      Text('状态：${program.status}'),
                      Text('开始：${AppDateUtils.formatDateLabel(program.startDate)}'),
                      Text('结束：${AppDateUtils.formatDateLabel(program.endDate)}'),
                      const SizedBox(height: 12),
                      Text(program.description?.isNotEmpty == true ? program.description! : '暂无描述'),
                    ],
                  ),
                ),
              ),
              const SizedBox(height: 16),
              Text('Block 列表', style: Theme.of(context).textTheme.titleMedium),
              const SizedBox(height: 12),
              blocksAsync.when(
                loading: () => const LoadingView(message: '正在加载 Block 列表...'),
                error: (Object error, StackTrace stackTrace) {
                  return ErrorView(
                    message: error.toString(),
                    onRetry: () => ref.invalidate(programBlocksProvider(programId)),
                  );
                },
                data: (List<ProgramBlock> blocks) {
                  return BlockListSection(programId: programId, blocks: blocks);
                },
              ),
            ],
          );
        },
      ),
    );
  }
}
