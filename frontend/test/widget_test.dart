import 'package:flutter_test/flutter_test.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

import 'package:frontend/app/app.dart';

void main() {
  testWidgets('app can build', (WidgetTester tester) async {
    await tester.pumpWidget(const ProviderScope(child: IronLogicApp()));

    expect(find.text('当前推荐训练'), findsOneWidget);
  });
}
