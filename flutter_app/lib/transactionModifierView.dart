import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_app/transactionFetcher.dart';
import 'package:flutter_app/transactionRequest.dart';
import 'package:flutter_app/transactionResponse.dart';
import 'package:intl/intl.dart';

class TransactionModifierView extends StatefulWidget {
  final TransactionResponse transactionResponse;

  TransactionModifierView(this.transactionResponse);

  @override
  _TransactionModifierViewState createState() =>
      _TransactionModifierViewState();
}

class _TransactionModifierViewState extends State<TransactionModifierView> {
  final _form = GlobalKey<FormState>();
  var fetcher = TransactionFetcher();
  DateFormat dateFormat = DateFormat('dd-MM-yy');

  DateTime _selectedDate;
  TextEditingController _textEditingController = TextEditingController();
  Map<String, Object> _formValues = {};

  @override
  void initState() {
    if (widget.transactionResponse != null) {
      _formValues["id"] = widget.transactionResponse.id;
      _formValues["data"] = widget.transactionResponse.data;
      _formValues["description"] = widget.transactionResponse.description;
      _formValues["value"] = widget.transactionResponse.value;
      _textEditingController..text = dateFormat.format(_formValues["data"]);

      if (widget.transactionResponse.type ==
          TransactionType.Despesa.toString().split('.').last) {
        _formValues["type"] = TransactionType.Despesa;
      }
      if (widget.transactionResponse.type ==
          TransactionType.Receita.toString().split('.').last) {
        _formValues["type"] = TransactionType.Receita;
      }
    } else {
      _formValues["type"] = TransactionType.Receita;
    }
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Gerenciar Transação"),
        actions: [
          IconButton(
              icon: Icon(Icons.save),
              onPressed: () async {
                _form.currentState.save();

                var transactionRequest = TransactionRequest(
                  data: _formValues["data"],
                  description: _formValues["description"],
                  value: _formValues["value"],
                  type: _formValues["type"].toString().split('.').last,
                );

                String id = _formValues["id"];
                print("id=$id");
                if (id == null) {
                  await fetcher.create(transactionRequest);
                } else {
                  await fetcher.update(transactionRequest, id);
                }

                Navigator.of(context).pop();
              }),
        ],
      ),
      body: Center(
          child: Padding(
        padding: EdgeInsets.all(15),
        child: Form(
          key: _form,
          child: Column(
            children: <Widget>[
              TextFormField(
                initialValue: _formValues["value"],
                decoration: InputDecoration(labelText: "Valor"),
                onSaved: (value) => _formValues["value"] = value,
                keyboardType: TextInputType.number,
              ),
              TextField(
                decoration: InputDecoration(labelText: "Dia"),
                focusNode: AlwaysDisabledFocusNode(),
                controller: _textEditingController,
                onTap: () => _selectDate(context),
              ),
              TextFormField(
                initialValue: _formValues["description"],
                decoration: InputDecoration(labelText: "Descrição"),
                onSaved: (value) => _formValues["description"] = value,
              ),
              ListTile(
                title: const Text('Receita',
                    style: TextStyle(color: Colors.green)),
                leading: Radio(
                  value: TransactionType.Receita,
                  groupValue: _formValues["type"],
                  onChanged: (value) {
                    setState(() {
                      _formValues["type"] = value;
                    });
                  },
                ),
              ),
              ListTile(
                title:
                    const Text('Despesa', style: TextStyle(color: Colors.red)),
                leading: Radio(
                  value: TransactionType.Despesa,
                  groupValue: _formValues["type"],
                  onChanged: (value) {
                    setState(() {
                      _formValues["type"] = value;
                    });
                  },
                ),
              ),
            ],
          ),
        ),
      )),
    );
  }

  _selectDate(BuildContext context) async {
    DateTime newSelectedDate = await showDatePicker(
      context: context,
      initialDate: _selectedDate != null ? _selectedDate : DateTime.now(),
      firstDate: DateTime.now(),
      lastDate: DateTime(2100),
    );

    if (newSelectedDate != null) {
      _formValues["data"] = newSelectedDate;
      _textEditingController
        ..text = dateFormat.format(newSelectedDate)
        ..selection = TextSelection.fromPosition(TextPosition(
            offset: _textEditingController.text.length,
            affinity: TextAffinity.upstream));
    }
  }
}

class AlwaysDisabledFocusNode extends FocusNode {
  @override
  bool get hasFocus => false;
}
