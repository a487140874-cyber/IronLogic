import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';

import '../../../../app/router.dart';
import '../../../../shared/widgets/empty_view.dart';
import '../../domain/model/program_block.dart';

/// Block 列表区块。
///
/// 该组件属于 `program/presentation`，负责在 Program 详情页展示 Block 列表。
/// 单独拆出这个组件，是为了让 Program 详情页保留页面编排职责，而列表渲染逻辑保持清晰。
class BlockListSection extends StatelessWidget {
  /// 创建 Block 列表区块。
  const BlockListSection({
    required this.programId,
    required this.blocks,
    super.key,
  });

  /// 所属 Program id。
  final int programId;

  /// Block 列表。
  final List<ProgramBlock> blocks;

  @override
  Widget build(BuildContext context) {
    if (blocks.isEmpty) {
      return EmptyView(
        message: '当前 Program 下还没有 Block。',
        actionLabel: '创建 Block',
        onAction: () => context.go(AppRoutes.programBlockCreate(programId)),
      );
    }

    return Column(
      children: blocks
          .map(
            (ProgramBlock block) => Card(
              child: ListTile(
                title: Text(block.name),
                subtitle: Text(
                  '类型：${block.blockType} · 顺序：${block.sequenceNo}\n'
                  '周期：${block.durationMode ?? '未设置'} ${block.durationValue ?? ''}',
                ),
                isThreeLine: true,
                trailing: const Icon(Icons.chevron_right),
                onTap: () {
                  context.go(
                    AppRoutes.blockDetail(block.id),
                    extra: block,
                  );
                },
              ),
            ),
          )
          .toList(),
    );
  }
}
