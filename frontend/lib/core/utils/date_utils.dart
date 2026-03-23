/// 日期格式化工具。
///
/// 该类属于 `core/utils`，用于承载本轮 MVP 足够用的日期显示与提交格式化逻辑。
/// 当前避免引入额外日期库，先用简单实现保证链路清晰。
abstract final class AppDateUtils {
  /// 把 [DateTime] 转成 `yyyy-MM-dd` 字符串，用于表单提交。
  static String toDateInput(DateTime value) {
    final String year = value.year.toString().padLeft(4, '0');
    final String month = value.month.toString().padLeft(2, '0');
    final String day = value.day.toString().padLeft(2, '0');
    return '$year-$month-$day';
  }

  /// 把 ISO 日期字符串转换成更适合页面展示的文本。
  static String formatDateLabel(String? value) {
    if (value == null || value.isEmpty) {
      return '未设置';
    }

    final DateTime? parsed = DateTime.tryParse(value);
    if (parsed == null) {
      return value;
    }

    return toDateInput(parsed);
  }
}
