import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:go_router/go_router.dart';

import '../../../../app/router.dart';
import '../../../../shared/widgets/error_view.dart';
import '../../../../shared/widgets/ironlogic_scaffold.dart';
import '../../../../shared/widgets/loading_view.dart';
import '../../application/program_providers.dart';
import '../../domain/model/program_block.dart';
import '../../domain/model/session_template.dart';
import '../widgets/session_template_list_section.dart';

/// Block 详情页。
///
/// 该页面属于 `program/presentation`，展示 Block 基础信息与其下的 SessionTemplate 列表。
/// 当前后端没有单独的 Block 详情接口，因此页面优先依赖路由传入的 Block 信息；若没有，则回退为最小展示。
class BlockDetailPage extends ConsumerWidget {
  /// 创建 Block 详情页。
  const BlockDetailPage({
    required this.blockId,
    this.block,
    super.key,
  });

  /// Block id。
  final int blockId;

  /// 可选的 Block 对象。
  final ProgramBlock? block;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final AsyncValue<List<SessionTemplate>> templatesAsync = ref.watch(
      sessionTemplatesProvider(blockId),
    );

    return IronLogicScaffold(
      title: 'Block 详情',
      actions: <Widget>[
        IconButton(
          tooltip: '刷新',
          onPressed: () => ref.invalidate(sessionTemplatesProvider(blockId)),
          icon: const Icon(Icons.refresh),
        ),
      ],
      floatingActionButton: FloatingActionButton.extended(
        onPressed: () => context.go(AppRoutes.sessionTemplateCreate(blockId)),
        icon: const Icon(Icons.add),
        label: const Text('创建 SessionTemplate'),
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
                    block?.name ?? 'Block #$blockId',
                    style: Theme.of(context).textTheme.titleLarge,
                  ),
                  const SizedBox(height: 8),
                  Text('Block ID：$blockId'),
                  Text('类型：${block?.blockType ?? '未提供'}'),
                  Text('顺序：${block?.sequenceNo ?? '未提供'}'),
                  Text('持续方式：${block?.durationMode ?? '未提供'}'),
                ],
              ),
            ),
          ),
          const SizedBox(height: 16),
          Text('SessionTemplate 列表', style: Theme.of(context).textTheme.titleMedium),
          const SizedBox(height: 12),
          templatesAsync.when(
            loading: () => const LoadingView(message: '正在加载模板列表...'),
            error: (Object error, StackTrace stackTrace) {
              return ErrorView(
                message: error.toString(),
                onRetry: () => ref.invalidate(sessionTemplatesProvider(blockId)),
              );
            },
            data: (List<SessionTemplate> templates) {
              return SessionTemplateListSection(
                blockId: blockId,
                templates: templates,
              );
            },
          ),
        ],
      ),
    );
  }
}
