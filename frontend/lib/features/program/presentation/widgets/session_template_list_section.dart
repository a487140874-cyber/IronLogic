import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';

import '../../../../app/router.dart';
import '../../../../shared/widgets/empty_view.dart';
import '../../domain/model/session_template.dart';

/// SessionTemplate 列表区块。
///
/// 该组件属于 `program/presentation`，用于 Block 详情页展示模板列表。
class SessionTemplateListSection extends StatelessWidget {
  /// 创建 SessionTemplate 列表区块。
  const SessionTemplateListSection({
    required this.blockId,
    required this.templates,
    super.key,
  });

  /// 所属 Block id。
  final int blockId;

  /// 模板列表。
  final List<SessionTemplate> templates;

  @override
  Widget build(BuildContext context) {
    if (templates.isEmpty) {
      return EmptyView(
        message: '当前 Block 下还没有 SessionTemplate。',
        actionLabel: '创建 SessionTemplate',
        onAction: () => context.go(AppRoutes.sessionTemplateCreate(blockId)),
      );
    }

    return Column(
      children: templates
          .map(
            (SessionTemplate template) => Card(
              child: ListTile(
                title: Text(template.name),
                subtitle: Text(
                  '顺序：${template.sequenceNo} · 触发：${template.triggerMode ?? '未设置'}\n'
                  '${template.notes?.isNotEmpty == true ? template.notes : '暂无备注'}',
                ),
                isThreeLine: true,
                trailing: const Icon(Icons.chevron_right),
                onTap: () {
                  context.go(
                    AppRoutes.sessionTemplateDetail(template.id),
                    extra: template,
                  );
                },
              ),
            ),
          )
          .toList(),
    );
  }
}
