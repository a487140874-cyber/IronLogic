import 'package:flutter/material.dart';

/// 通用空状态组件。
///
/// 该组件属于 `shared/widgets`，用于统一列表页和首页在无数据时的展示。
/// MVP 阶段先保证信息清楚，不追求复杂插画或视觉包装。
class EmptyView extends StatelessWidget {
  /// 创建空状态组件。
  const EmptyView({
    required this.message,
    super.key,
    this.actionLabel,
    this.onAction,
  });

  /// 空状态说明文案。
  final String message;

  /// 可选按钮文案。
  final String? actionLabel;

  /// 可选按钮点击事件。
  final VoidCallback? onAction;

  @override
  Widget build(BuildContext context) {
    return Center(
      child: Padding(
        padding: const EdgeInsets.all(24),
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: <Widget>[
            const Icon(Icons.inbox_outlined, size: 48),
            const SizedBox(height: 12),
            Text(message, textAlign: TextAlign.center),
            if (actionLabel != null && onAction != null) ...<Widget>[
              const SizedBox(height: 16),
              FilledButton(
                onPressed: onAction,
                child: Text(actionLabel!),
              ),
            ],
          ],
        ),
      ),
    );
  }
}
