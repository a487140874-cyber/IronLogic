import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';

import '../../../../app/router.dart';
import '../../../../shared/widgets/empty_view.dart';
import '../../domain/model/session_exercise_template.dart';

/// SessionExerciseTemplate 列表区块。
///
/// 该组件属于 `program/presentation`，负责在 SessionTemplate 详情页展示模板动作列表。
class SessionExerciseTemplateListSection extends StatelessWidget {
  /// 创建模板动作列表区块。
  const SessionExerciseTemplateListSection({
    required this.templateId,
    required this.items,
    required this.exerciseNameOf,
    super.key,
  });

  /// 所属模板 id。
  final int templateId;

  /// 模板动作列表。
  final List<SessionExerciseTemplate> items;

  /// 根据 exerciseId 获取动作名称。
  final String Function(int exerciseId) exerciseNameOf;

  @override
  Widget build(BuildContext context) {
    if (items.isEmpty) {
      return EmptyView(
        message: '当前暂无训练模板动作。',
        actionLabel: '添加模板动作',
        onAction: () => context.go(AppRoutes.sessionTemplateExerciseCreate(templateId)),
      );
    }

    return Column(
      children: items
          .map(
            (SessionExerciseTemplate item) => Card(
              child: ListTile(
                title: Text(exerciseNameOf(item.exerciseId)),
                subtitle: Text(
                  '顺序：${item.orderNo} · 组数：${item.targetSets} · 次数：${item.targetReps ?? '-'}\n'
                  '重量：${item.targetWeight ?? '-'} ${item.targetWeightUnit ?? ''} · '
                  '休息：${item.restSeconds ?? '-'} 秒',
                ),
                isThreeLine: true,
              ),
            ),
          )
          .toList(),
    );
  }
}
