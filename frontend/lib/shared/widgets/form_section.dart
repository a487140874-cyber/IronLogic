import 'package:flutter/material.dart';

/// 通用表单容器。
///
/// 该组件属于 `shared/widgets`，用于让创建页的布局保持一致。
/// 本轮只做基础表单承载，不加入复杂校验提示样式与动画。
class FormSection extends StatelessWidget {
  /// 创建通用表单容器。
  const FormSection({
    required this.child,
    super.key,
  });

  /// 表单主体内容。
  final Widget child;

  @override
  Widget build(BuildContext context) {
    return Card(
      child: Padding(
        padding: const EdgeInsets.all(16),
        child: child,
      ),
    );
  }
}
