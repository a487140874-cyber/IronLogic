import 'package:flutter/material.dart';

/// 通用错误展示组件。
///
/// 该组件属于 `shared/widgets`，用于把页面级错误展示方式统一起来。
/// 当前只保留“错误信息 + 重试”最小结构，方便后续逐页复用。
class ErrorView extends StatelessWidget {
  /// 创建错误展示组件。
  const ErrorView({
    required this.message,
    super.key,
    this.onRetry,
  });

  /// 错误文案。
  final String message;

  /// 重试回调。
  final VoidCallback? onRetry;

  @override
  Widget build(BuildContext context) {
    return Center(
      child: Padding(
        padding: const EdgeInsets.all(24),
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: <Widget>[
            const Icon(Icons.error_outline, size: 48),
            const SizedBox(height: 12),
            Text(message, textAlign: TextAlign.center),
            if (onRetry != null) ...<Widget>[
              const SizedBox(height: 16),
              OutlinedButton(
                onPressed: onRetry,
                child: const Text('重试'),
              ),
            ],
          ],
        ),
      ),
    );
  }
}
