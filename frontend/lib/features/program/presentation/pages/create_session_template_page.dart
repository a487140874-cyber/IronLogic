import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:go_router/go_router.dart';

import '../../../../app/router.dart';
import '../../../../shared/widgets/form_section.dart';
import '../../../../shared/widgets/ironlogic_scaffold.dart';
import '../../application/program_providers.dart';
import '../../domain/model/create_session_template_input.dart';

/// 创建 SessionTemplate 页。
///
/// 该页面属于 `program/presentation`，用于在 Block 下创建训练日模板。
class CreateSessionTemplatePage extends ConsumerStatefulWidget {
  /// 创建页面。
  const CreateSessionTemplatePage({
    required this.blockId,
    super.key,
  });

  /// 所属 Block id。
  final int blockId;

  @override
  ConsumerState<CreateSessionTemplatePage> createState() =>
      _CreateSessionTemplatePageState();
}

class _CreateSessionTemplatePageState
    extends ConsumerState<CreateSessionTemplatePage> {
  static const List<String> _triggerModes = <String>[
    'SEQUENCE',
    'MANUAL',
  ];

  final GlobalKey<FormState> _formKey = GlobalKey<FormState>();
  final TextEditingController _nameController = TextEditingController();
  final TextEditingController _sequenceNoController = TextEditingController(text: '1');
  final TextEditingController _notesController = TextEditingController();

  String _triggerMode = _triggerModes.first;

  @override
  void dispose() {
    _nameController.dispose();
    _sequenceNoController.dispose();
    _notesController.dispose();
    super.dispose();
  }

  /// 提交创建模板请求。
  Future<void> _submit() async {
    if (!_formKey.currentState!.validate()) {
      return;
    }

    final SessionTemplateCreateController controller = ref.read(
      sessionTemplateCreateControllerProvider(widget.blockId).notifier,
    );
    await controller.submit(
      CreateSessionTemplateInput(
        name: _nameController.text.trim(),
        sequenceNo: int.parse(_sequenceNoController.text.trim()),
        triggerMode: _triggerMode,
        notes: _notesController.text.trim().isEmpty
            ? null
            : _notesController.text.trim(),
      ),
    );

    final AsyncValue<void> state = ref.read(
      sessionTemplateCreateControllerProvider(widget.blockId),
    );
    if (!mounted) {
      return;
    }

    state.whenOrNull(
      data: (_) {
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text('SessionTemplate 创建成功')),
        );
        context.go(AppRoutes.blockDetail(widget.blockId));
      },
      error: (Object error, StackTrace stackTrace) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text(error.toString())),
        );
      },
    );
  }

  @override
  Widget build(BuildContext context) {
    final AsyncValue<void> submitState = ref.watch(
      sessionTemplateCreateControllerProvider(widget.blockId),
    );
    final bool isSubmitting = submitState.isLoading;

    return IronLogicScaffold(
      title: '创建 SessionTemplate',
      body: ListView(
        padding: const EdgeInsets.all(16),
        children: <Widget>[
          const Text('本页先支持训练模板基础字段创建，后续可继续补充更细的编排能力。'),
          const SizedBox(height: 16),
          FormSection(
            child: Form(
              key: _formKey,
              child: Column(
                children: <Widget>[
                  TextFormField(
                    controller: _nameController,
                    decoration: const InputDecoration(labelText: '模板名称'),
                    validator: (String? value) {
                      if (value == null || value.trim().isEmpty) {
                        return '请输入模板名称';
                      }
                      return null;
                    },
                  ),
                  const SizedBox(height: 16),
                  TextFormField(
                    controller: _sequenceNoController,
                    keyboardType: TextInputType.number,
                    decoration: const InputDecoration(labelText: '顺序号'),
                    validator: (String? value) =>
                        int.tryParse(value ?? '') == null ? '请输入有效顺序号' : null,
                  ),
                  const SizedBox(height: 16),
                  DropdownButtonFormField<String>(
                    value: _triggerMode,
                    decoration: const InputDecoration(labelText: '触发模式'),
                    items: _triggerModes
                        .map((String value) => DropdownMenuItem<String>(
                              value: value,
                              child: Text(value),
                            ))
                        .toList(),
                    onChanged: (String? value) {
                      if (value != null) {
                        setState(() => _triggerMode = value);
                      }
                    },
                  ),
                  const SizedBox(height: 16),
                  TextFormField(
                    controller: _notesController,
                    decoration: const InputDecoration(labelText: '备注'),
                    maxLines: 3,
                  ),
                  const SizedBox(height: 24),
                  SizedBox(
                    width: double.infinity,
                    child: FilledButton(
                      onPressed: isSubmitting ? null : _submit,
                      child: Text(isSubmitting ? '提交中...' : '提交创建'),
                    ),
                  ),
                ],
              ),
            ),
          ),
        ],
      ),
    );
  }
}
